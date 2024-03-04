package com.example.Mission2.usedItem.service;

import com.example.Mission2.auth.AuthenticationFacade;
import com.example.Mission2.usedItem.dto.ItemDto;
import com.example.Mission2.usedItem.entity.ItemStatus;
import com.example.Mission2.usedItem.entity.UsedItem;
import com.example.Mission2.auth.entity.UserEntity;
import com.example.Mission2.usedItem.repo.ItemRepository;
import com.example.Mission2.auth.repo.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final AuthenticationFacade facade;

    //CREATE
    //물품 등록
    public ItemDto createItem(ItemDto dto) {
        //사용자 정보 가지고 오기
        String username = facade.getUserEntity().getUsername();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        UsedItem item = UsedItem.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .status(ItemStatus.ON_SALE)
                .user(user)
                .build();
        log.info("Item: {}", item);
        return ItemDto.fromEntity(itemRepository.save(item));

    }

    public List<ItemDto> readItemAll() {
//        List<ItemDto> itemList = new ArrayList<>();
//        for(UsedItem entity : itemRepository.findAll()){
//            itemList.add(ItemDto.fromEntity(entity));
//        }
//        return itemList;
        //stream 으로
        return itemRepository.findAll().stream()
                .map(ItemDto::fromEntity)
                .toList();
    }

    public ItemDto readItem(Long id) {

//        Optional<UsedItem> optionalItem = itemRepository.findById(id);
//        if(optionalItem.isEmpty()){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        }
//        UsedItem item = optionalItem.get();
//        return ItemDto.fromEntity(item);

        return itemRepository.findById(id)
                .map(ItemDto::fromEntity)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    //UPDATE - 판매자만 가능
    public ItemDto updateItem(Long id, ItemDto dto) {
        //id로 정보 찾기
        Optional<UsedItem> optionalItem = itemRepository.findById(id);
        if (optionalItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        UsedItem target = optionalItem.get();
        if (!target.getUser().getUsername().equals(facade.getUserEntity().getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "판매자만 수정할 수 있습니다.");
        }

        target.setTitle(dto.getTitle());
        target.setDescription(dto.getDescription());
        target.setPrice(dto.getPrice());

        return ItemDto.fromEntity(itemRepository.save(target));
    }

    public void deleteItem(Long id){
        Optional<UsedItem> optionalItem = itemRepository.findById(id);
        if (optionalItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        UsedItem target = optionalItem.get();
        if (!target.getUser().getUsername().equals(facade.getUserEntity().getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "판매자만 삭제할 수 있습니다.");
        }

        itemRepository.deleteById(id);
    }
}
