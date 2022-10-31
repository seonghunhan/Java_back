package com.example.demo.src.auth;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.auth.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

// Service Create, Update, Delete 의 로직 처리
@Service
public class AuthService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuthDao authDao;
    private final AuthProvider authProvider;
    private final JwtService jwtService;


    @Autowired
    public AuthService(AuthDao authDao, AuthProvider authProvider, JwtService jwtService) {
        this.authDao = authDao;
        this.authProvider = authProvider;
        this.jwtService = jwtService;

    }

    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException{

        if (authDao.checkId(postLoginReq.getId()) == 0){
            throw new BaseException(BaseResponseStatus.POST_USERS_NOT_EXISTS_ID);
        }

        User user = authDao.getPwd(postLoginReq.getId());
        String encryptPwd;

        try{
            encryptPwd = new SHA256().encrypt(postLoginReq.getPassword()); //utils패키지에 SHA256에서 암호화 진행

        }catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.PASSWORD_ENCRYPTION_ERROR);
        }
        if(user.getPwd().equals(encryptPwd)){ //비번 맞는지 확인 맞다면 jwt 발급
            int userIdx = user.getUserIdx();
            String jwt = jwtService.createJwt(userIdx);
            return new PostLoginRes(userIdx, jwt);
        }else
            throw new BaseException(BaseResponseStatus.FAILED_TO_LOGIN);
    }

    public PostAuthCodeRes certifiedPhoneNumber(PostAuthCodeReq postAuthCodeReq) throws BaseException{

        System.out.println("여기1");
        Integer checkResult = authDao.checkInfo(postAuthCodeReq.getId(), postAuthCodeReq.getPhone() );
        System.out.println("여기2");
        System.out.println(checkResult);
        if(checkResult == 0){
            throw new BaseException(BaseResponseStatus.POST_USERS_NOT_EXISTS_ID);
        }

        // Secret.java에 gitignore처리
        String api_key = Secret.SNS_SERVICE_API_KEY; //본인의 API KEY"
        String api_secret = Secret.SNS_SERVICE_API_SECRET_KEY; //본인의 API SECRET
        Message coolsms = new Message(api_key, api_secret);

        // 랜덤4자리 난수 발생
        Random rand  = new Random();
        String numStr = "";
        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr+=ran;
        }

        String phoneNumber = postAuthCodeReq.getPhone();
        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNumber);    // 수신전화번호
        params.put("from", "01039319312");    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", "MBTM_ 어플 비밀번호 찾기 : 인증번호는" + "["+numStr+"]" + "입니다.");
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
            int userIdx = checkResult;
            return new PostAuthCodeRes(numStr, userIdx);
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        } throw new BaseException(BaseResponseStatus.FAILED_TO_SEND_SNS_AUTH_CODE);
    }

    public PostAuthPasswordCheckRes updatePassword(PostAuthPasswordCheckReq postAuthPasswordCheckReq) throws BaseException {

        int userIdx = postAuthPasswordCheckReq.getUserIdx();
        String jwt = jwtService.createJwt(userIdx);

        if (authDao.updateUserPassword(postAuthPasswordCheckReq) == null) {
            throw new BaseException(BaseResponseStatus.POST_USERS_NOT_EXISTS_ID);
        }
        String pwd;
        try {
            //암호화
            pwd = new SHA256().encrypt(postAuthPasswordCheckReq.getPassword());
            postAuthPasswordCheckReq.setPassword(pwd);
            authDao.updateUserPassword(postAuthPasswordCheckReq);

            return new PostAuthPasswordCheckRes(userIdx, jwt);
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_UPDATE_PASSWORD);


        }
    }

//    public void certifiedPhoneNumber(String phoneNumber, String cerNum) {
//
//        String api_key = "NCS92OGWSOR1JCA0"; //본인의 API KEY"
//        String api_secret = "7CPPODY1O0B0GWCCTOD0FSGLVG87L6VL"; //본인의 API SECRET
//        Message coolsms = new Message(api_key, api_secret);
//
//        // 4 params(to, from, type, text) are mandatory. must be filled
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("to", phoneNumber);    // 수신전화번호
//        params.put("from", "01039319312");    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
//        params.put("type", "SMS");
//        params.put("text", "MBTM_ 어플 비밀번호 찾기 : 인증번호는" + "["+cerNum+"]" + "입니다.");
//        params.put("app_version", "test app 1.2"); // application name and version
//
//        try {
//            JSONObject obj = (JSONObject) coolsms.send(params);
//            System.out.println(obj.toString());
//        } catch (CoolsmsException e) {
//            System.out.println(e.getMessage());
//            System.out.println(e.getCode());
//        }
//
//    }

}
