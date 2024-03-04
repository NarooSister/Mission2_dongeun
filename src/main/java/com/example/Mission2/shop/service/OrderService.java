package com.example.Mission2.shop.service;

import com.example.Mission2.auth.AuthenticationFacade;
import com.example.Mission2.auth.entity.UserEntity;
import com.example.Mission2.auth.repo.UserRepository;
import com.example.Mission2.shop.dto.OrderDto;
import com.example.Mission2.shop.entity.Goods;
import com.example.Mission2.shop.entity.Order;
import com.example.Mission2.shop.entity.OrderStatus;
import com.example.Mission2.shop.entity.Shop;
import com.example.Mission2.shop.repository.GoodsRepository;
import com.example.Mission2.shop.repository.OrderRepository;
import com.example.Mission2.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final GoodsRepository goodsRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final AuthenticationFacade authFacade;
    private final ShopRepository shopRepository;

    //CREATE
    //구매자가 구매 요청하기
    public OrderDto createOrder(Long shopId, Long goodsId, OrderDto dto){
        //상품 가져오기
        Goods goods = goodsRepository.findByIdAndShopId(goodsId, shopId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //shop 가져오기
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //구매자 가져오기
        String username = authFacade.getUserEntity().getUsername();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //재고 확인
        if(goods.getStock() <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "재고가 없습니다.");
        }

        if (user.getUsername().equals(goods.getShop().getOwner().getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "자신이 등록한 물건을 구입할 수 없습니다.");
        }

        //주문 생성
        Order order = Order.builder()
                .amount(dto.getAmount())
                .status(OrderStatus.WAITING)
                .createdAt(LocalDateTime.now())
                .goods(goods)
                .shop(shop)
                .customer(user)
                .build();

        return OrderDto.fromEntity(orderRepository.save(order));
    }


    //READ
    //쇼핑몰 주인이 구매 요청 확인 하기
    public List<OrderDto> readOrderAll(Long shopId, Long goodsId) {
        return orderRepository.findAllByShopIdAndGoodsIdAndStatus(shopId, goodsId, OrderStatus.WAITING)
                .stream()
                .map(OrderDto::fromEntity)
                .collect(Collectors.toList());
    }


    //UPDATE
    //쇼핑몰 주인이 구매 요청을 수락 또는 거절
    @Transactional
    public OrderDto acceptOrder(Long shopId, Long goodsId, Long orderId, Boolean accept, OrderDto dto){
        //order 가져오기
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //상품 가져오기
        Goods goods = goodsRepository.findByIdAndShopId(goodsId, shopId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //쇼핑몰 주인 정보 가져오기
        String username = authFacade.getUserEntity().getUsername();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //주인 확인
        if(!user.getUsername().equals(goods.getShop().getOwner().getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //구매 요청
        if(accept){
            //재고 상태 변경
            Integer stock = goods.getStock() - order.getAmount();
            goods.setStock(stock);
            goodsRepository.save(goods);
            //Order 상태 변경
            order.setStatus(OrderStatus.ACCEPT);

        } else {
            //구매 요청 거절 이유 추가
            order.setRejectReason(dto.getRejectReason());
            //Order 상태 변경
            order.setStatus(OrderStatus.REJECT);

        }
        return OrderDto.fromEntity(orderRepository.save(order));
    }

    //DELETE
    //구매자가 구매 요청 취소
    @Transactional
    public void cancelOrder(Long shopId, Long goodsId, Long orderId){
        //주문 가져오기
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //상품 가져오기
        Goods goods = goodsRepository.findByIdAndShopId(goodsId, shopId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //구매자 정보 가져오기
        String username = authFacade.getUserEntity().getUsername();
        UserEntity customer = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //구매자 확인
        if(!customer.getUsername().equals(order.getCustomer().getUsername())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "주문을 요청한 유저만 취소가 가능합니다.");
        }

        //수락되기 전에만 취소 가능
        if(!order.getStatus().equals(OrderStatus.ACCEPT)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //구매 요청 취소
        order.setStatus(OrderStatus.CANCEL);
        orderRepository.save(order);
    }

}
