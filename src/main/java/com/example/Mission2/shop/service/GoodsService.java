package com.example.Mission2.shop.service;

import com.example.Mission2.FileFacade;
import com.example.Mission2.ImageCategory;
import com.example.Mission2.auth.AuthenticationFacade;
import com.example.Mission2.shop.dto.GoodsDto;
import com.example.Mission2.shop.entity.Goods;
import com.example.Mission2.shop.entity.Shop;
import com.example.Mission2.auth.entity.UserEntity;
import com.example.Mission2.shop.repository.GoodsRepository;
import com.example.Mission2.shop.repository.ShopRepository;
import com.example.Mission2.auth.repo.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class GoodsService {
    private final UserRepository userRepository;
    private final GoodsRepository goodsRepository;
    private final AuthenticationFacade authFacade;
    private final ShopRepository shopRepository;
    private final FileFacade fileFacade;

    //CREATE
    //쇼핑몰 주인이 상품을 등록
    public GoodsDto createGoods(GoodsDto dto, MultipartFile goodsImage) {
        //유저 불러오기
        UserEntity user = authFacade.getUserEntity();

        //shop 정보 가져오기
        Optional<Shop> optionalShop = shopRepository.findByOwnerId(user.getId());
        if (optionalShop.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Shop shop = optionalShop.get();

        //goodsImage 생성
        String requestPath = (String) fileFacade.uploadImage(ImageCategory.GOODS, goodsImage);

        //상품 정보 저장
        Goods goods = Goods.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .imageUrl(requestPath)
                .shop(shop)
                .build();

        return GoodsDto.fromEntity(goodsRepository.save(goods));
    }

    //READ ALL
    //쇼핑몰 상품을 검색할 수 있다.
    //이름, 가격 범위를 기준으로 상품을 검색한다.
    public List<GoodsDto> readGoodsAll(Integer condition, String name, Integer min, Integer max){
        List<Goods> goodsList = new ArrayList<>();
        // 1: 이름순 조회, 2: 가격 범위 기준

        // 이름순 조회의 경우 name 입력
        if(condition == 1){
            goodsList = goodsRepository.findAllByName(name);
        }
        // 가격 범위 기준 조회 : min - 최소 가격, max - 최대 가격
        if(condition == 2){
            goodsList = goodsRepository.findAllByPriceBetween(min, max);
        }

        //저장
        return goodsList.stream()
                .map(GoodsDto::fromEntity)
                .collect(Collectors.toList());
    }

    //UPDATE
    //쇼핑몰 주인이 등록한 상품을 수정
    public GoodsDto updateGoods(Long id, GoodsDto dto, MultipartFile goodsImage){

        //유저 불러오기
        UserEntity user = authFacade.getUserEntity();

        //상품 가져오기
        Optional<Goods> optionalGoods = goodsRepository.findById(id);
        if(optionalGoods.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Goods goods = optionalGoods.get();

        //shop 정보 가져오기
        Optional<Shop> optionalShop = shopRepository.findByOwnerId(user.getId());
        if (optionalShop.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Shop shop = optionalShop.get();

        //쇼핑몰 주인 확인
        if(!goods.getShop().getId().equals(shop.getId()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        //이미지 생성
        String requestPath = (String) fileFacade.uploadImage(ImageCategory.GOODS, goodsImage);

        //정보 수정
        goods.setName(dto.getName());
        goods.setDescription(dto.getDescription());
        goods.setPrice(dto.getPrice());
        goods.setStock(dto.getStock());
        goods.setImageUrl(requestPath);

        return GoodsDto.fromEntity(goodsRepository.save(goods));
    }

    //DELETE
    //쇼핑몰 주인이 등록한 상품을 삭제
    public void deleteGoods(Long id){
        UserEntity user = authFacade.getUserEntity();

        //상품 가져오기
        Optional<Goods> optionalGoods = goodsRepository.findById(id);
        if(optionalGoods.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Goods goods = optionalGoods.get();

        //shop 정보 가져오기
        Optional<Shop> optionalShop = shopRepository.findByOwnerId(user.getId());
        if (optionalShop.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Shop shop = optionalShop.get();

        //쇼핑몰 주인 확인
        if(!goods.getShop().getId().equals(shop.getId()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        //삭제
        goodsRepository.deleteById(id);
    }
}
