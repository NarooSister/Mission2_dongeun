# 쇼핑몰 구축 미션 프로젝트
요구사항 정의서 기반으로 쇼핑몰을 구축하는 미션 프로젝트

기간: 2024-02-22 ~ 2024-03-04

## 목표
1. 기본 과제 꼼꼼하게 구현하기
2. README 자세히 작성하기
3. 수업시간에 배운 Toss payments, NCP MAPS 구현하기

## 프로젝트 개요
일반 사용자는 중고거래가 가능하며, 사업자는 인터넷 쇼핑몰을 운영할 수 있게 해주는
쇼핑몰 사이트를 만들어보자.

요구사항의 내용은 프론트엔드 없이, 백엔드만 개발한다. 별도의 프론트엔드 클라이언트가 존재한다고 생각하고 서버를 만들고, Postman으로 테스트를 한다. 단, CORS는 지금은 고려하지 않는다.
<details>
<summary>과제 요구사항</summary>
<div markdown="1">

## 기능 설명



### 1. 기본 과제
- 사용자 인증 및 권한 처리
- 중고거래 중개하기
- 쇼핑몰 운영하기

### 2. 추가 과제 (최소 1개 구현)

- 결제 시스템 (Toss Payments)
- 사용자 위치기반 기능 (NCP MAPS)
- 알림 기능 추가하기 - E-mail (Jakarta Mail)
- 알림 기능 추가하기 (NCP SENS)
- 사업자 자동 로그인 방지 (NCP Capcha)

## 기본 과제 요구사항


### 1. 사용자 인증 및 권한 처리


- 요청을 보낸 사용자가 누구인지 구분할 수 있는 인증 체계가 갖춰져야 한다.
    - JWT 기반의 토큰 인증 방식이 권장된다.
    - 사용자는 별도의 클라이언트를 통해 아이디와 비밀번호를 전달한다.
    - 로그인 URL로 아이디와 비밀번호가 전달되면, 해당 내용의 정당성을 확인하여 JWT를 발급하여 클라이언트에게 반환한다.
    - 클라이언트는 이후 이 JWT를 Bearer Authentication 방식으로 제시해야 한다.
- 사용자는 회원가입이 가능하다.
    - 아이디, 비밀번호를 제공하여 회원가입이 가능하다.
    - 서비스를 이용하려면 닉네임, 이름, 연령대, 이메일, 전화번호 정보를 추가해야 한다.
    - 사용자의 프로필 이미지가 업로드 가능하다.
- 사용자의 권한이 관리되어야 한다.
    - 네 종류의 사용자가 있다. (비활성 사용자, 일반 사용자, 사업자 사용자, 관리자)
    - 최초의 회원가입시 비활성 사용자로 가입된다.
    - 비활성 사용자가 서비스를 위한 필수 정보를 추가하면 일반 사용자로 자동으로 전환된다.
    - 일반 사용자는 자신의 사업자 등록번호(가정)을 전달해 사업자 사용자로 전환신청을 할 수 있다.
        - 사업자 등록번호는 실제 형식과 일치할 필요 없다.
    - 관리자는 사업자 사용자 전환 신청 목록을 확인할 수 있다.
    - 관리자는 사업자 사용자 전환 신청을 수락 또는 거절할 수 있다.
    - 관리자는 서비스와 상관없이 고정된 사용자이다.
        - 다른 회원가입 과정을 통해 만들어진 사용자는 관리자가 될 수 없다.

### 2. 중고거래 중개하기



- 물품 등록
    - 일반 사용자는 중고 거래를 목적으로 물품에 대한 정보를 등록할 수 있다.
        - 제목, 설명, 대표 이미지, 최소 가격이 필요하다.
            - 대표 이미지는 반드시 함께 등록될 필요는 없다.
            - 다른 항목은 필수이다.
            - 최초로 물품이 등록될 때, 중고 물품의 상태는 **판매중** 상태가 된다.
    - 등록된 물품 정보는 비활성 사용자를 제외 누구든지 열람할 수 있다.
        - 사용자의 상세 정보는 공개되지 않는다.
    - 등록된 물품 정보는 작성자가 수정, 삭제가 가능하다.
- 구매 제안
    - **물품을 등록한 사용자**와 **비활성 사용자** 제외, 등록된 물품에 대하여 구매 제안을 등록할 수 있다.
    - 등록된 구매 제안은 **물품을 등록한 사용자**와 **제안을 등록한 사용자**만 조회가 가능하다.
        - **제안을 등록한 사용자**는 자신의 제안만 확인이 가능하다.
        - **물품을 등록한 사용자**는 모든 제안이 확인 가능하다.
    - **물품을 등록한 사용자**는 ****등록된 구매 제안을 수락 또는 거절할 수 있다.
        - 이때 구매 제안의 상태는 **수락** 또는 **거절**이 된다.
    - **제안을 등록한 사용자**는 자신이 등록한 제안이 수락 상태일 경우, 구매 확정을 할 수 있다.
        - 이때 구매 제안의 상태는 **확정** 상태가 된다.
        - 구매 제안이 확정될 경우, 대상 물품의 상태는 **판매 완료**가 된다.
        - 구매 제안이 확정될 경우, 확정되지 않은 다른 구매 제안의 상태는 모두 **거절**이 된다.

### 3. 쇼핑몰 운영하기



- 쇼핑몰 개설
    - 일반 사용자가 사업자 사용자로 전환될 때 **준비중** 상태의 쇼핑몰이 추가된다. 사업자 사용자는 이 쇼핑몰의 주인이 된다.
    - 쇼핑몰에는 이름, 소개, 분류의 정보를 가지고 있으며, 주인은 자유롭게 수정이 가능하다.
        - 분류의 종류는 서비스 제작자에 의해 미리 정해진다. (최소 5)
    - 쇼핑몰의 이름, 소개, 분류가 전부 작성된 상태라면 쇼핑몰을 개설 신청을 할 수 있다.
    - 관리자는 개설 신청된 쇼핑몰의 목록을 확인할 수 있으며, 정보를 확인후 허가 또는 불허 할 수 있다.
        - 불허 할 경우 그 이유를 함께 작성해야 한다.
        - 불허된 이유를 쇼핑몰의 주인이 확인할 수 있어야 한다.
    - 개설이 허가된 쇼핑몰을 **오픈** 상태가 된다.
    - 쇼핑몰 주인은 사유를 작성하여 쇼핑몰 폐쇄 요청을 할 수 있다.
        - 관리자는 쇼핑몰 폐쇄 요청을 확인 후 수락할 수 있다.

- 쇼핑몰 관리
    - 쇼핑몰 주인은 쇼핑몰에 상품을 등록할 수 있다.
        - 필수적인 정보는 상품 이름, 상품 이미지, 상품 설명, 상품 가격, 상품 재고가 있다.
    - 쇼핑몰 주인은 등록한 상품을 수정할 수 있다.
    - 쇼핑몰 주인은 등록한 상품을 삭제할 수 있다.

- 쇼핑몰 조회
    - 비활성 사용자를 제외한 사용자는 쇼핑몰을 조회할 수 있다.
        - 조건 없이 조회할 경우, 가장 최근에 거래가 있었던 쇼핑몰 순서로 조회된다.
        - 이름, 쇼핑몰 분류를 조건으로 쇼핑몰을 검색할 수 있다.

- 쇼핑몰 상품 검색
    - 비활성 사용자를 제외한 사용자는 쇼핑몰의 상품을 검색할 수 있다.
        - 이름, 가격 범위를 기준으로 상품을 검색할 수 있다.
        - 조회되는 상품이 등록된 쇼핑몰에 대한 정보가 함께 제공되어야 한다.

- 쇼핑몰 상품 구매
    - 비활성 사용자를 제외한 사용자는 쇼핑몰의 상품을 구매할 수 있다.
        - 상품과 구매 수량을 기준으로 구매 요청을 할 수 있다.
        - 구매 요청 후 사용자는 구매에 필요한 금액을 전달한다고 가정한다.
        - 주인이 전달된 금액을 확인하면 구매 요청을 수락할 수 있다.
        - 구매 요청이 수락되면, 상품 재고가 자동으로 갱신된다. 이후엔 구매 취소가 불가능하다.
        - 구매 요청이 수락되기 전에는 구매 요청을 취소할 수 있다.




## 추가 과제 요구사항



### 1. 결제 시스템 (Toss Payments)



- 사용자가 서비스 사용중 두가지 상황에서 결제를 진행하도록 서비스를 수정한다.

- 쇼핑몰 상품 구매
    - 사용자가 상품의 구매 요청을 하는 시점에, 결제를 진행한다.
    - 결제가 이뤄지면 자동으로 재고가 갱신된다.
    - 주인은 구매 요청에 대하여 구매 요청을 수락할 수 있다. 이후엔 구매 취소가 불가능하다.
    - 정당한 사유가 있으면 구매 요청을 거절할 수 있다. 사유는 관리자가 확인 가능하다.
    - 구매 요청이 수락되기 전에는 사용자가 구매 요청을 취소할 수 있다.
    - 구매 요청이 취소될 경우 사용자는 구매에 결제된 금액을 환불받는다.

### 2. 사용자 위치기반 기능 (NCP Maps)



사용자 위치 기반 서비스를 추가한다.

- 중고 물품 구매 확정시
- 두 사용자는 거래를 진행할 위치를 제안할 수 있다.
    - 서로의 제안을 확인하고, 어느쪽 사용자든 상대방의 제안을 수락할 수 있다.
    - 수락하게 되면 각 사용자는 거래 위치까지 이동할 수 있는 방법에 대한 정보를 제공받을 수 있다.
- 오프라인 구매
    - 쇼핑몰에 오프라인 상점 위치를 등록할 수 있다.
    - 쇼핑몰의 상품을 구매할때 방문을 선택할 수 있다.
    - 사용자는 자신의 위치로부터 쇼핑몰까지 이동할 수 있는 방법에 대한 정보를 제공받을 수 있다.

### 3. 알림 기능 추가하기 - E-mail (Jakarta Mail)



서비스의 특정 지점의 사용자에게 이메일로 서비스의 상태 변화를 알려준다.

이 기능 또는 SMS 기능은 둘 중 하나만 적용한다. [Jakarta Mail 패키지](https://docs.spring.io/spring-framework/reference/integration/email.html)를 이용한다.

- 회원가입 시 사용자 이메일 인증
    - 비활성 사용자가 이메일 주소를 기입할 때, 이메일 인증을 할 수 있도록 한다.
        - 비활성 사용자가 계정 활성화를 위해 필수 정보를 입력한다.
        - 본래의 요구사항대로 계정이 바로 활성화 되는 대신, 사용자의 이메일로
          이메일이 전송된다.
        - 전송된 이메일의 링크를 접속하면 사용자 계정이 활성화 된다.
        - 단, 이메일의 링크를 10분 이내에 클릭해야 한다. 시간이 지날 경우 다시 이메일 인증을
          요청할 수 있다.

- 중고 거래 시 구매 제안 알림
    - 사용자가 등록한 물품에 구매 제안이 등록될 경우 간략한 정보가 이메일로 전송된다.
        - 대상 물품, 제안 가격 등
    - 사용자가 등록한 구매 제안이 수락되면, 구매 제안을 한 사용자에게 정보가 이메일로
      전송된다.
    - 사용자가 등록한 물품의 거래가 종료되면, 확정되지 않은 사용자에게 정보가 이메일로
      전송된다.

- 상품 판매 확정 알림
    - 쇼핑몰의 상품에 대하여 구매요청을 한 사용자는, 주인이 구매를 확정하는 시점에 정보가
      이메일로 전송된다.

### 4. 알림 기능 추가하기 - SMS (NCP SENS)



서비스의 특정 지점에 사용자에게 문자로 서비스의 상태 변화를 알려준다. 이 기능 또는 E-mail 기능은 둘 중 하나만 적용한다. [NCP SENS](https://www.ncloud.com/product/applicationService/sens) 서비스를 활용한다.

- 회원가입시 사용자 핸드폰 인증
    - 비활성 사용자가 핸드폰 번호를 기입할때, 핸드폰 인증을 할 수 있도록 한다.
        - 비활성 사용자가 계정 활성화를 위해 필수 정보를 입력한다.
        - 본래의 요구사항데로 계정이 바로 활성화 되는 대신, 사용자의 핸드폰으로 문자 메시지를 통해 인증번호가 전송된다.
        - 사용자는 특정 UI를 통해 전달된 인증번호를 전달한다. 인증번호가 보내진 인증번호와 일치하면 사용자의 계정이 활성화된다.
        - 단 인증 번호를 3분 이내에 전달해야 한다. 시간이 지날경우 다시 번호 인증을 요청할 수 있다.

- 중고 거래시 구매 제안 알림
    - 사용자가 등록한 물품에 구매 제안이 등록될 경우 간략한 정보가 문자로 전송된다.
        - 대상 물품, 제안 가격 등
    - 사용자가 등록한 구매 제안이 수락되면, 구매 제안을 한 사용자에게 정보가 문자로 전송된다.
    - 사용자가 등록한 물품의 거래가 종료되면, 확정되지 않은 사용자에게 정보가 문자로 전송된다.

- 상품 판매 확정 알림
    - 쇼핑몰의 상품에 대하여 구매요청을 한 사용자는, 주인이 구매를 확정하는 시점에 정보가 문자로 전송된다.

### 5. 사업자 자동 로그인 방지 (NCP Capcha)



사업자 사용자 또는 관리자의 자동 로그인을 방지한다.

- 로그인 시도
    - 사업자 또는 관리자가 로그인을 시도하면, 캡차 사진을 볼 수 있는 링크로 이동시킨다.
    - 캡차에 나타나는 글씨를 정상적으로 입력하면, 로그인을 진행한다.
    - 사용자가 로그인을 위해 아이디 비밀번호를 제공한 시점과 캡차가 제공되는 시점이 다르다.
        - 상태를 구분하기 위한 대책을 마련해야 한다.

</div>
</details>


## 기능 구현

Postman으로 테스트

- [API Download](src%2Fmain%2Fresources%2Fstatic%2Ffile%2FShop.postman_collection.json)

### 사용자 인증 및 권한 처리
<details>
<summary>User</summary>
<div>

### 1. 회원가입


<details>
<summary>Postman</summary>
<div>

![users_register.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fusers_register.png)

</div>
</details>

### 2. 로그인

- 로그인 후 jwt 토큰 값 저장
<details>
<summary>Postman</summary>
<div>

![users_login.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fusers_login.png)
</div>
</details>



### 3. 마이 페이지


<details>
<summary>Postman</summary>
<div markdown="1">

![users_my-page.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fusers_my-page.png)
</div>
</details>

### 4. 임시 사용자 -> 일반 사용자 전환


<details>
<summary>Postman</summary>
<div markdown="1">

- Auth에서 token 값 입력

![users_update_token.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fusers_update_token.png)


- params에 username 보내기

![users_update.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fusers_update.png)

- DB에 유저 정보와 ROLE_USER 업데이트 된 것 확인

![users_update_success.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fusers_update_success.png)

</div>
</details>



### 5. 프로필 이미지 등록

<details>
<summary>Postman</summary>
<div>

- token 값 입력 후 이미지 파일 등록
- 이미지 url 저장된 것 확인

![users_image.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fusers_image.png)

- 프로필 폴더에 username으로 프로필 이미지 저장된 것 확인

![users_image_save.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fusers_image_save.png)

</div>
</details>


### 6. 사업자 사용자로 전환 신청


<details>
<summary>Postman</summary>
<div markdown="1">


- 사업자 번호 등록

![users_businessNum.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fusers_businessNum.png)

- DB에 등록 확인

![users_businessNum_save.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fusers_businessNum_save.png)

</div>
</details>

</div>
</details>

<details>
<summary>Admin</summary>
<div>

### 1. 일반 사용자 -> 사업자 사용자 전환 신청 목록 열람

<details>
<summary>Postman</summary>
<div markdown="1">



![admin_business_requestList.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fadmin_business_requestList.png)

</div>
</details>

### 2. 신청 목록을 보고 승인, 반려

<details>
<summary>Postman</summary>
<div markdown="1">


- 승인 : id와 true 전달

![admin_business_true.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fadmin_business_true.png)

- 반려 : id와 false 전달

![admin_business_false.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fadmin_business_false.png)

- 로그 확인

![admin_business_log.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fadmin_business_log.png)

</div>
</details>

</div>
</details>


### 중고 거래 중개하기

<details>
<summary>UsedItem</summary>
<div>

### 1. CREATE - 물품 정보 등록

<details>
<summary>Postman</summary>
<div>

- 물품 생성

![items_create.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fitems_create.png)

- DB에서 확인

![items_create_db.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fitems_create_db.png)

- ImageUrl 포함하여 생성

![items_create_image.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fitems_create_image.png)

</div>
</details>

### 2. REAL ALL - 물품 목록 열람

<details>
<summary>Postman</summary>
<div>

![items_readAll.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fitems_readAll.png)

</div>
</details>

### 3. READ ONE - 물품 상세 정보 열람

<details>
<summary>Postman</summary>
<div>

![items_readOne.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fitems_readOne.png)

</div>
</details>


### 4. UPDATE - 물품 정보 수정


<details>
<summary>Postman</summary>
<div>

![items_update_image.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fitems_update_image.png)

</div>
</details>

### 5. DELETE - 등록된 물품 삭제


<details>
<summary>Postman</summary>
<div>

- 판매자만 삭제할 수 있음

![items_delete.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fitems_delete.png)


- DB에서 지워진 것 확인

![items_delete_DB.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fitems_delete_DB.png)

</div>
</details>

</div>
</details>


<details>
<summary>Offer</summary>
<div>

### 1. CREATE - 구매 제안 등록

<details>
<summary>Postman</summary>
<div>

- 없는 itemId를 입력했을 떄 오류

![offers_create_error.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Foffers_create_error.png)

- 구매 제안 등록

![offers_create.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Foffers_create.png)


</div>
</details>

### 2. READ ALL - 판매자: 모든 구매 제안 열람

<details>
<summary>Postman</summary>
<div>

![offers_readAll.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Foffers_readAll.png)

</div>
</details>

### 3. READ ONE - 구매자: 제안 상세 열람

<details>
<summary>Postman</summary>
<div>

- 제안 상세 열람

![offers_readOne.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Foffers_readOne.png)

- 구매자는 자신의 제안만 열람이 가능함.

![offers_readOne_unAuth.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Foffers_readOne_unAuth.png)

</div>
</details>

### 4. UPDATE - 판매자: 구매 제안 수락 또는 거절

<details>
<summary>Postman</summary>
<div>

- 판매자가 구매 제안을 수락

![offers_accept.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Foffers_accept.png)

- DB에 ACCEPT로 저장된 것 확인

![offers_accept_DB.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Foffers_accept_DB.png)



</div>
</details>

### 5. UPDATE - 구매자: 구매 확정을 결정

<details>
<summary>Postman</summary>
<div>

- 구매자가 구매 확정, 상태 CONFIRMED로 변경됨

![offers_confirm.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Foffers_confirm.png)

- DB에 저장 확인

![offers_confirm_DB.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Foffers_confirm_DB.png)

</div>
</details>

### 6. DELETE - 구매자: 구매 제안 삭제

<details>
<summary>Postman</summary>
<div>

- 구매자가 구매 제안을 삭제

![offers_delete.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Foffers_delete.png)

- DB 확인

![offers_delete_DB.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Foffers_delete_DB.png)

</div>
</details>



</div>
</details>


### 쇼핑몰 운영하기

<details>
<summary>Shop</summary>
<div>

### 1. CREATE - 쇼핑몰 생성
- 일반 사용자 -> 사업자 사용자 변경시 '준비중' 상태의 쇼핑몰 자동 등록

<details>
<summary>Postman</summary>
<div>

![shop_create_DB.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fshop_create_DB.png)

</div>
</details>

### 2. READ ALL - 사용자:쇼핑몰 조회

- 0:기본 조회, 1:이름순 조회, 2:분류별 조회

<details>
<summary>Postman</summary>
<div>

- 기본 조회

- 이름순 조회

![shop_read_1.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fshop_read_1.png)


- 분류별 조회

![shop_read_2.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fshop_read_2.png)


</div>
</details>

### 3. UPDATE - 쇼핑몰 주인: 쇼핑몰 정보 수정 -> 오픈 신청

<details>
<summary>Postman</summary>
<div>

- 쇼핑몰 정보 수정 -> 상태 APPLY로 변경됨

![shop_update.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fshop_update.png)

- DB 확인

![shop_update_DB.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fshop_update_DB.png)

</div>
</details>

### 5. ADMIN: 쇼핑몰 개설 신청 목록 보기

<details>
<summary>Postman</summary>
<div>

![shop_open_list.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fshop_open_list.png)

</div>
</details>

### 6. ADMIN: 쇼핑몰 개설 신청 허락 또는 반려

<details>
<summary>Postman</summary>
<div>

- 개설 신청 허락. OPEN으로 변경됨

![shop_open_confirm.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fshop_open_confirm.png)

- 반려. reject 사유 작성

![shop_open_reject.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fshop_open_reject.png)

</div>
</details>


### 7. DELETE - 쇼핑몰 주인: 쇼핑몰 폐쇄 요청

<details>
<summary>Postman</summary>
<div>

- 쇼핑몰 주인이 폐쇄 요청

![shop_delete.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fshop_delete.png)

- DB에 저장

![shop_delete_DB.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fshop_delete_DB.png)

</div>
</details>

### 8. ADMIN: 쇼핑몰 폐쇄 요청 목록 보기
<details>
<summary>Postman</summary>
<div>

![shop_close_list.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fshop_close_list.png)

</div>
</details>


### 9. ADMIN: 쇼핑몰 폐쇄 수락 -> 쇼핑몰 삭제

<details>
<summary>Postman</summary>
<div>

- 관리자가 폐쇄 요청을 수락하면 쇼핑몰이 삭제됨

![shop_close_accept.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fshop_close_accept.png)

- DB에서 삭제 확인

![shop_close_acceptDB.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fshop_close_acceptDB.png)

</div>
</details>


</div>
</details>



<details>
<summary>Goods</summary>
<div>

### 1. CREATE - 쇼핑몰 주인: 상품 등록

<details>
<summary>Postman</summary>
<div>

![goods_create.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fgoods_create.png)

![goods_create_DB.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fgoods_create_DB.png)

![goods_create_image.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fgoods_create_image.png)


</div>
</details>

### 2. READ ALL - 사용자: 쇼핑몰 상품 검색

- 1: 이름순 조회, 2: 가격 범위 기준
- 이름순 조회의 경우 name 입력
- 가격 범위 기준 조회 : min - 최소 가격, max - 최대 가격

<details>
<summary>Postman</summary>
<div>

![goods_read_1.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fgoods_read_1.png)

![goods_read_2.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fgoods_read_2.png)

![goods_read_22.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fgoods_read_22.png)

</div>
</details>

### 3. UPDATE - 쇼핑몰 주인: 등록한 상품을 수정

<details>
<summary>Postman</summary>
<div>

![goods_update.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fgoods_update.png)

![goods_update_image.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fgoods_update_image.png)

![goods_update_image_BD.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fgoods_update_image_BD.png)

</div>
</details>

### 4. DELETE - 쇼핑몰 주인: 등록한 상품을 삭제

<details>
<summary>Postman</summary>
<div>

![goods_delete.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fgoods_delete.png)

![goods_delete_DB.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Fgoods_delete_DB.png)

</div>
</details>

</div>
</details>





<details>
<summary>Order</summary>
<div>

### 1. CREATE - 구매자: 구매 요청

<details>
<summary>Postman</summary>
<div>

![order_create.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Forder_create.png)

</div>
</details>

### 2. READ - 쇼핑몰 주인: 구매 요청 확인

<details>
<summary>Postman</summary>
<div>

![order_readAll.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Forder_readAll.png)

</div>
</details>

### 3. UPDATE - 쇼핑몰 주인: 구매 요청 수락 또는 거절

<details>
<summary>Postman</summary>
<div>

![order_accept_true.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Forder_accept_true.png)

![order_accept_DB.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Forder_accept_DB.png)

![order_reject.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Forder_reject.png)


</div>
</details>

### 4. DELETE - 구매자: 구매 요청 취소

<details>
<summary>Postman</summary>
<div>

![orders_delete.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Forders_delete.png)

![orders_delete_error.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Forders_delete_error.png)

![orders_delete_DB.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Forders_delete_DB.png)

</div>
</details>

</div>
</details>


## 어려웠던 부분과 공부한 내용

### 1. 권한 관리
- 권한을 Enum으로 관리할 지 DB에 저장하여 관리할 지 정하기 어려웠다. 
Enum은 열거형데이터를 표현할 수 있으나 데이터의 수정이 어려워 동적인 사용자 관리에 적절하지 않다. 
그래서 String으로 저장하는 방식으로 entity를 짰다.
- 그런데 지금 생각해보니 연습용 프로젝트니 Enum으로 깔끔하게 관리해도 좋았을 것 같다. 
String으로 ROLE_TEMPORARY_USER 등을 작성할 때마다 오타가 나서 좀 힘들었다.. 나중에 Status들을 Enum으로 관리해보니 불러올 때 자동완성이 너무 좋았다..!

### 2. Bean 객체 순환참조
- CustomUserDetailsManager에 userService를 다 정리하려고 했더니
PasswordEncoder와 UserDetailsManager 사이에 순환 참조 문제가 생겼다.
WebSecurityConfig 파일에서 PasswordEncoder를 다른 class로 빼면 해결이 가능하긴 하다.
UserService를 따로 만들어서 구현하기로 했다.
- 결국에 마지막에 WebSecurityConfig에 테스트용 사용자들을 추가하면서 PasswordEncoder를 사용해야해서 
또 순환참조 문제가 발생하여 다른 Class로 빼서 관리했다.


### 3. Controller vs RestController



### 4. UserDetailsService vs UserDetailsManager

### 5. Restful API URL 구성하기

### 6. MultipartFile 파일 업로드




