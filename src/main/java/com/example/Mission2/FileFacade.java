package com.example.Mission2;

import com.example.Mission2.auth.AuthenticationFacade;
import com.example.Mission2.auth.entity.UserEntity;
import com.example.Mission2.usedItem.entity.UsedItem;
import jdk.jfr.Frequency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileFacade {
    private final AuthenticationFacade facade;

    public Object uploadImage(ImageCategory imageCategory, MultipartFile image){


        String imageDir = "";
        String imageName = "";

        switch (imageCategory){
            case USER -> {
                imageDir = "media/profile/";
                imageName = "profile_";
                break;
            }
            case USED_ITEM -> {
                imageDir = "media/itemImage/";
                imageName = "itemImage_";
                break;
            }
            case GOODS -> {
                imageDir = "media/goodsImage/";
                imageName = "goodsImage_";
                break;
            }
        }

        log.info(imageDir);

        // 주어진 Path를 기준으로, 없는 모든 디렉토리를 생성하는 메서드
        try {
            Files.createDirectories(Path.of(imageDir));
        } catch (IOException e) {
            // 폴더를 만드는데 실패하면 기록을하고 사용자에게 알림
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //filename에 날짜 추가
        // 특수문자 : 사용이 불가능해서 형식 바꿈
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        String fileName = imageName + formattedDateTime;

        // 실제 파일 이름을 경로와 확장자를 포함하여 만들기 ("profile_{username}.{png}")
        String originalFilename = image.getOriginalFilename();
        String[] fileNameSplit = originalFilename.split("\\.");
        String extension = fileNameSplit[fileNameSplit.length - 1];

        String profileFilename = fileName +"." + extension;
        log.info(profileFilename);

        //경로
        String profilePath = imageDir + profileFilename;
        log.info(profilePath);

        // 실제로 해당 위치에 파일을 저장
        try {
            image.transferTo(Path.of(profilePath));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // entity 에 위치를 저장
        // http://localhost:8080/static/profile.{확장자}
        String requestPath = "";
        switch (imageCategory){
            case USER -> {
                requestPath = String.format("/static/profile/%s", profileFilename);
                log.info(requestPath);
                UserEntity user = facade.getUserEntity();
                user.setImageUrl(requestPath);
               return user;
            }
            case USED_ITEM -> {
                return requestPath = String.format("/static/itemImage/%s", profileFilename);
            }
            case GOODS -> {
                return requestPath = String.format("/static/goodsImage/%s", profileFilename);
            }
        }
            return "";
    }
}
