package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@RestController
@RequestMapping("/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;




    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원가입 API (users)
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("") // (POST) localhost:9000/users
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {

        //System.out.println(postUserReq.getId().getClass().getName());

        // 회원가입 정보누락 Body Check
        if(postUserReq.getId().length() == 0 || postUserReq.getPassword().length() == 0 || postUserReq.getPasswordForCheck().length() == 0 || postUserReq.getPhone().length() == 0 || postUserReq.getEmail().length() == 0){
            return new BaseResponse<>(POST_USERS_EMPTY_INFO);
        }
        // 이메일 정규표현
        if(!isRegexEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        // 전화번호 정규표현
        if(!isRegexPhoneNumber(postUserReq.getPhone())){
            return new BaseResponse<>(POST_USERS_INVALID_PHONE);
        }
        // 비밀번호 정규표현
        if(!isRegexPassword(postUserReq.getPassword())){
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }
        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 닉네임설정 API
     * [POST] /users/nickName
     * @return BaseResponse<PostUserRes>
     */
    @ResponseBody
    @PostMapping("nickName") // (POST) localhost:9000/users/nickName
    public BaseResponse<PostUserRes> createUserNickname(@RequestBody PostUserReq postUserReq){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            int userIdx = postUserReq.getUserIdx();

            // 정보누락 유효성검사
            if(postUserReq.getNickName().length() == 0 ){
                return new BaseResponse<>(POST_USERS_EMPTY_INFO);
            }
            // 실제 Idx와 jwt로 추출한 Idx가 맞는지 유효성검사
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PostUserRes postUserRes = userService.createNickname(postUserReq);
            return new BaseResponse<>(postUserRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * MBTI설정 API
     * [POST] /users/mbti
     * @return BaseResponse<PostUserRes>
     */
    @ResponseBody
    @PostMapping("mbti") // (POST) localhost:9000/users/mbti
    public BaseResponse<PostUserRes> createMbti(@RequestBody PostUserReq postUserReq){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            int userIdx = postUserReq.getUserIdx();

            // 정보누락 유효성검사
            if(postUserReq.getMbti().length() == 0 ){
                return new BaseResponse<>(POST_USERS_EMPTY_INFO);
            }
            // 실제 Idx와 jwt로 추출한 Idx가 맞는지 유효성검사
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PostUserRes postUserRes = userService.createMbti(postUserReq);
            return new BaseResponse<>(postUserRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 아이디찾기 API (users)
     * [POST] /users/searchId
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("searchId") // (POST) localhost:9000/users/searchId
    public BaseResponse<PostUserRes> searchId(@RequestBody PostUserReq postUserReq) {

        if(postUserReq.getEmail().length() == 0){
            return new BaseResponse<>(POST_USERS_EMPTY_INFO);
        }
        // 이메일 정규표현
        if(!isRegexEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        try{
            //System.out.println(postUserReq.getEmail() + "Asd");
            PostUserRes postUserRes = userProvider.checkId(postUserReq.getNickName(), postUserReq.getEmail());

            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }



    /**
     * 아이디찾기후 비번바꾸기 API (users)
     * [POST] /users/searchId/changePassword
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("/searchId/changePassword") // (POST) localhost:9000/users/searchId/changePassword
    public BaseResponse<PostUserRes> changePassword(@RequestBody PostUserPasswordReq postUserPasswordReq) {

        if(postUserPasswordReq.getPassword().length() == 0 || postUserPasswordReq.getNewPassword().length() == 0 || postUserPasswordReq.getNewPasswordForCheck().length() == 0){
            return new BaseResponse<>(POST_USERS_EMPTY_INFO);
        }

        // 비밀번호 정규표현
        if(!isRegexPassword(postUserPasswordReq.getNewPassword())){
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }

        if(! postUserPasswordReq.getNewPassword().equals(postUserPasswordReq.getNewPasswordForCheck())) {
            return new BaseResponse<>(POST_USERS_UNMATCH_NEWPASSWORD);
        }
        try{
            //System.out.println(postUserReq.getEmail() + "Asd");
            PostUserRes postUserRes = userService.updatePassword(postUserPasswordReq);

            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


//        if(postUserReq.getId() == null || postUserReq.getPassword() == null || postUserReq.getPasswordForCheck() == null || postUserReq.getName() == null || postUserReq.getNickName() == null || postUserReq.getPhone() == null || postUserReq.getEmail() == null){
//        return new BaseResponse<>(POST_USERS_EMPTY_INFO);
}
//    /**
//     * 유저피드조회 API
//     * [GET] /users/:userIdx
//     * @return BaseResponse<PostUserRes>
//     */
//    @ResponseBody
//    @GetMapping("/{userIdx}")
//    public BaseResponse<GetUserFeedRes> getUserFeed(@PathVariable("userIdx")int userIdx) {
//        try{
//
//            GetUserFeedRes getUserFeedRes = userProvider.retrieveUserFeed(userIdx,userIdx); //조회 담당인 Provider로 넘긴다
//            return new BaseResponse<>(getUserFeedRes);
//        } catch(BaseException exception){
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }
//
//
//
//}
