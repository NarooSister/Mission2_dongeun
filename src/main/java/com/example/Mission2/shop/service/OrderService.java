package com.example.Mission2.shop.service;

import com.example.Mission2.auth.AuthenticationFacade;
import com.example.Mission2.auth.entity.UserEntity;
import com.example.Mission2.auth.repo.UserRepository;
import com.example.Mission2.shop.dto.OrderDto;
import com.example.Mission2.shop.dto.PaymentCancelDto;
import com.example.Mission2.shop.dto.PaymentConfirmDto;
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
import java.util.LinkedHashMap;
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
    private final TossHttpService tossService;

    //CREATE
    //구매자가 구매 요청하기
    public OrderDto createOrder(Long shopId, Long goodsId, OrderDto dto) {
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
        if (goods.getStock() <= 0) {
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
    public OrderDto acceptOrder(Long shopId, Long goodsId, Long orderId, Boolean accept, OrderDto dto) {
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
        if (!user.getUsername().equals(goods.getShop().getOwner().getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //구매 요청
        if (accept) {
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
    public void cancelOrder(Long orderId) {
        //주문 가져오기
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //구매자 정보 가져오기
        String username = authFacade.getUserEntity().getUsername();
        UserEntity customer = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //구매자 확인
        if (!customer.getUsername().equals(order.getCustomer().getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "주문을 요청한 유저만 취소가 가능합니다.");
        }

        //수락되기 전에만 취소 가능
        if (!order.getStatus().equals(OrderStatus.ACCEPT)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //구매 요청 취소
        order.setStatus(OrderStatus.CANCEL);
        orderRepository.save(order);
    }




    //TossPayment로 구매 요청시 바로 결제
    public Object confirmPayment(PaymentConfirmDto dto) {
        // HTTP 요청이 보내진다.
        Object tossPaymentObj = tossService.confirmPayment(dto);
        log.info(tossPaymentObj.toString());

        // TODO 사용자가 결제한 물품 + 결제 정보에 대한 내용을 DB에 저장한다.

        // 1. 결제한 물품 정보를 응답 Body에서 찾는다 (orderName)
        String orderName = ((LinkedHashMap<String, Object>) tossPaymentObj)
                .get("orderName").toString();

        // 2. orderName에서 goodsId 회수하고, 그에 해당하는 Goods 엔티티를 조회한다.
        Long goodsId = Long.parseLong(orderName.split("-")[0]);
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

        //구매자 가져오기
        String username = authFacade.getUserEntity().getUsername();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //shop 가져오기
        Shop shop = shopRepository.findById(goods.getShop().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 3. Goods 엔티티를 바탕으로 Order 만들자.
        return OrderDto.fromEntity(orderRepository.save(Order.builder()
                .amount(dto.getAmount())
                .goods(goods)
                .shop(shop)
                .customer(user)
                .createdAt(LocalDateTime.now())
                .tossPaymentKey(dto.getPaymentKey())
                .tossOrderId(dto.getOrderId())
                .status(OrderStatus.COMPLETE)
                .build()));
    }
//
//    // readTossPayment
//    public Object readTossPayment(Long id) {
//        // 1. id를 가지고 주문정보를 조회한다.
//        Order order = orderRepository.findById(id)
//                .orElseThrow(() ->
//                        new ResponseStatusException(HttpStatus.NOT_FOUND));
//        // 2. 주문정보에 포함된 결제 정보키(paymentKey)를 바탕으로
//        // Toss에 요청을 보내 결제 정보를 받는다.
//        Object response = tossService.getPayment(order.getTossPaymentKey());
//        log.info(response.toString());
//        // 3. 해당 결제 정보를 반환한다.
//        return response;
//    }
//    //DELETE
//    //toss 합치기
//    //구매자가 구매 요청 취소
//    @Transactional
//    public Object cancelOrder(Long orderId, PaymentCancelDto dto) {
//        //주문 가져오기
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//
//        //구매자 정보 가져오기
//        String username = authFacade.getUserEntity().getUsername();
//        UserEntity customer = userRepository.findByUsername(username)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//
//        //구매자 확인
//        if (!customer.getUsername().equals(order.getCustomer().getUsername())) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "주문을 요청한 유저만 취소가 가능합니다.");
//        }
//
//        //수락되기 전에만 취소 가능
//        if (!order.getStatus().equals(OrderStatus.ACCEPT)) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
//        }
//
//        //구매 요청 취소
//        order.setStatus(OrderStatus.CANCEL);
//        orderRepository.save(order);
//
//        // 3. 취소후 결과를 응답한다.
//        return tossService.cancelPayment(order.getTossPaymentKey(), dto);
//    }




}
