
## 국회는 지금 어때? '국룰' 앱  

![image](https://user-images.githubusercontent.com/88662101/230559934-2054b830-7e65-44ab-99cd-73a9180911ae.png)


<br>

## 프로젝트 설명  

<br>

- 국회에 관한 다양한 정보를 제공하는 앱 개발  
- 국회 관련 뉴스 및 예산사용 현황 제공  
- 사용 언어 / 프레임워크 : Java / Spring Boot framework  
- 개발 tool : IntelliJ,  DataGrip, Postman, GitHub  



<br>
<br>


## Structure

<br>

java (패키지매니저 =  Gradle)  
> Request(시작) / Response(끝) ⇄ Controller(= Router + Controller) ⇄ Service (CUD) / Provider (R) ⇄ DAO (DB)


```text
api-server-spring-boot
  > * build
  > gradle
  > * logs
    | app.log // warn, error 레벨에 해당하는 로그가 작성 되는 파일
    | app-%d{yyyy-MM-dd}.%i.gz
    | error.log // error 레벨에 해당하는 로그가 작성 되는 파일
    | error-%d{yyyy-MM-dd}.%i.gz
  > src.main.java.com.example.demo
    > config
      > secret
        | Secret.java // git에 추적되지 않아야 할 시크릿 키 값들이 작성되어야 하는 곳
      | BaseException.java // Controller, Service, Provider 에서 Response 용으로 공통적으로 사용 될 익셉션 클래스
      | BaseResponse.java // Controller 에서 Response 용으로 공통적으로 사용되는 구조를 위한 모델 클래스
      | BaseResponseStatus.java // Controller, Service, Provider 에서 사용 할 Response Status 관리 클래스 
      | Constant.java // 공통적으로 사용될 상수 값들을 관리하는 곳
    > src
      > test
        | TestController.java // logger를 어떻게 써야하는지 보여주는 테스트 클래스
      > user
        > models
          | GetUserRes.java        
          | PostUserReq.java 
          | PostUserRes.java 
        | UserController.java
        | UserProvider.java
        | UserService.java
        | UserDao.java
      | WebSecurityConfig.java // spring-boot-starter-security, jwt 를 사용하기 위한 클래스 
    > utils
      | AES128.java // 암호화 관련 클래스
      | JwtService.java // jwt 관련 클래스
      | ValidateRegex.java // 정규표현식 관련 클래
    | DemoApplication // SpringBootApplication 서버 시작 지점
  > resources
    | application.yml // Database 연동을 위한 설정 값 세팅 및 Port 정의 파일
    | logback-spring.xml // logger 사용시 console, file 설정 값 정의 파일
build.gradle // gradle 빌드시에 필요한 dependency 설정하는 곳
.gitignore // git 에 포함되지 않아야 하는 폴더, 파일들을 작성 해놓는 곳

```

<br>
<br>

## Wire-Frame

![image](https://user-images.githubusercontent.com/88662101/230558490-1e3071e4-e7ba-42ca-a488-f57b46690764.png)  


<br>
<br>

## 시연영상

- https://youtu.be/wc-d5JSbVng






