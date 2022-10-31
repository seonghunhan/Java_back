package com.example.demo.src.auth;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.auth.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.POST_USERS_INVALID_PASSWORD;
import static com.example.demo.utils.ValidationRegex.isRegexPassword;

@RestController
@RequestMapping("/auth")
public class AuthController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final AuthProvider authProvider;
    @Autowired
    private final AuthService authService;
    @Autowired
    private final JwtService jwtService;


    public AuthController(AuthProvider authProvider, AuthService authService, JwtService jwtService) {
        this.authProvider = authProvider;
        this.authService = authService;
        this.jwtService = jwtService;
        //asd

    }
    //로그인
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> login(@RequestBody PostLoginReq postLoginReq) {
        try{
            if(postLoginReq.getId().length() == 0){
                return new BaseResponse<>(BaseResponseStatus.POST_USERS_EMPTY_ID);
            }
            if(postLoginReq.getPassword().length() == 0){
                return new BaseResponse<>(BaseResponseStatus.POST_USERS_EMPTY_PASSWORD);
            }

            PostLoginRes postLoginRes = authService.login(postLoginReq) ;

            return new BaseResponse<>(postLoginRes);

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    @ResponseBody
    @PostMapping("/check/sendSMS")
    public BaseResponse<PostAuthCodeRes> sendSMS(@RequestBody PostAuthCodeReq postAuthCodeReq) {

        try {
            if (postAuthCodeReq.getId().length() == 0 || postAuthCodeReq.getPhone().length() == 0) {
                return new BaseResponse<>(BaseResponseStatus.POST_USERS_EMPTY_INFO);
            }

            if ( postAuthCodeReq.getPhone().length() != 11){
                return new BaseResponse<>(BaseResponseStatus.POST_USERS_UNREGEX_PHONE);
            }

            String id = postAuthCodeReq.getId();
            String phoneNumber = postAuthCodeReq.getPhone();
            System.out.println("수신자 번호 : " + phoneNumber);
//        System.out.println("인증번호 : " + numStr);
            PostAuthCodeRes postAuthCodeRes = authService.certifiedPhoneNumber(postAuthCodeReq);

            return new BaseResponse<>(postAuthCodeRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/check/password")
    public BaseResponse<PostAuthPasswordCheckRes> checkPassword(@RequestBody PostAuthPasswordCheckReq postAuthPasswordCheckReq) {

        try{
            if (postAuthPasswordCheckReq.getPassword().length() == 0 || postAuthPasswordCheckReq.getPasswordForCheck().length() == 0) {
                return new BaseResponse<>(BaseResponseStatus.POST_USERS_EMPTY_INFO);
            }

            if (!postAuthPasswordCheckReq.getPassword().equals(postAuthPasswordCheckReq.getPasswordForCheck()) ){
                return new BaseResponse<>(BaseResponseStatus.POST_USERS_UNMATCH_PASSWORD);
            }
            else if(!isRegexPassword(postAuthPasswordCheckReq.getPassword())){
                return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
            }

            PostAuthPasswordCheckRes postAuthPasswordCheckRes = authService.updatePassword(postAuthPasswordCheckReq);

            return new BaseResponse<>(postAuthPasswordCheckRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

//    @PostMapping("/check/sendSMS")
//    public @ResponseBody String sendSMS(@RequestBody PostAuthCodeReq postAuthCodeReq) {
//
//        Random rand  = new Random();
//        String numStr = "";
//        for(int i=0; i<4; i++) {
//            String ran = Integer.toString(rand.nextInt(10));
//            numStr+=ran;
//        }
//        String phoneNumber = postAuthCodeReq.getPhone();
//        System.out.println("수신자 번호 : " + phoneNumber);
//        System.out.println("인증번호 : " + numStr);
//        authService.certifiedPhoneNumber(phoneNumber,numStr);
//        return numStr;
//    }


}
