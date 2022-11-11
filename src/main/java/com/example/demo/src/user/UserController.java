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
     * [POST] /users/signup
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("/signup") // (POST) localhost:9000/users/signup
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {

        //System.out.println(postUserReq.getId().getClass().getName());

        // 회원가입 정보누락 Body Check
        if(postUserReq.getId().length() == 0 || postUserReq.getPassword().length() == 0 || postUserReq.getPasswordForCheck().length() == 0 || postUserReq.getPhone().length() == 0 || postUserReq.getEmail().length() == 0 || postUserReq.getNickName().length() == 0){
            return new BaseResponse<>(POST_USERS_EMPTY_INFO);
        }
        // 이메일 정규표현(cnf101219@gmail.com)
        if(!isRegexEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        // 전화번호 정규표현(000-0000-0000)
        if(!isRegexPhoneNumber(postUserReq.getPhone())){
            return new BaseResponse<>(POST_USERS_INVALID_PHONE);
        }
        // 비밀번호 정규표현(8자리 이상 대,소문자,숫자,특수기호 포함)
        if(!isRegexPassword(postUserReq.getPassword())) {
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
     * 회원가입 키워드5개 API (users)
     * [POST] /users/signup/5keywords
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("/signup/5keywords") // (POST) localhost:9000/users/signup/5keywords
    public BaseResponse<PostUserRes> updateUserKeyword(@RequestBody PostUserReq postUserReq) {

        //System.out.println(postUserReq.getId().getClass().getName());

        // 회원가입 정보누락 Body Check
        if(postUserReq.getKeyword1().length() == 0 || postUserReq.getKeyword2().length() == 0 || postUserReq.getKeyword3().length() == 0 || postUserReq.getKeyword4().length() == 0 || postUserReq.getKeyword5().length() == 0) {
            return new BaseResponse<>(POST_USERS_EMPTY_INFO);
        }

        try{
            //jwt에서 idx 추출.
            //getUserIdx 타고 들가면 거기서 jwt 키 있는지 없는지 유효성 검사 실행함
            int userIdxByJwt = jwtService.getUserIdx();
            int userIdx = postUserReq.getUserIdx();

            // 실제 Idx와 jwt로 추출한 Idx가 맞는지 유효성검사
            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PostUserRes postUserRes = userService.updateKeyword(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 닉네임 중복체크 API (users)
     * [GET] /users/signup/checkNickname?nickName=
     * @return BaseResponse<GetUserNicknameRes>
     */
    @ResponseBody
    @GetMapping("/signup/checkNickname")
    public BaseResponse<GetUserNicknameRes> getUserNicknameForDuplicateCheck(@RequestParam(name = "nickName", defaultValue = "1")String nickName) {

        // 닉네임 정규표현(2자리 이상)
        if(nickName.length() < 2) {
            return new BaseResponse<>(POST_USERS_INVALID_NICKNAME);
        }
        try{
            System.out.println("닉네임 : " + nickName);
            GetUserNicknameRes getUserNicknameRes = userProvider.retrieveUserNickname(nickName); //조회 담당인 Provider로 넘긴다
            return new BaseResponse<>(getUserNicknameRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 아이디 중복체크 API (users)
     * [GET] /users/signup/checkId?id=
     * @return BaseResponse<GetUserIdRes>
     */
    @ResponseBody
    @GetMapping("/signup/checkId")
    public BaseResponse<GetUserIdRes> getUserIdForDuplicateCheck(@RequestParam(name = "id", defaultValue = "1")String id) {


        try{
            System.out.println("아이디 : " + id);
            GetUserIdRes getUserIdRes = userProvider.retrieveUserId(id); //조회 담당인 Provider로 넘긴다
            return new BaseResponse<>(getUserIdRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
//// 사용 X
//    /**
//     * 닉네임설정 API
//     * [POST] /users/nickName
//     * @return BaseResponse<PostUserRes>
//     */
//    @ResponseBody
//    @PostMapping("nickName") // (POST) localhost:9000/users/nickName
//    public BaseResponse<PostUserRes> createUserNickname(@RequestBody PostUserReq postUserReq){
//        try {
//            //jwt에서 idx 추출.
//            int userIdxByJwt = jwtService.getUserIdx();
//            int userIdx = postUserReq.getUserIdx();
//
//            // 정보누락 유효성검사
//            if(postUserReq.getNickName().length() == 0 ){
//                return new BaseResponse<>(POST_USERS_EMPTY_INFO);
//            }
//            // 실제 Idx와 jwt로 추출한 Idx가 맞는지 유효성검사
//            if(userIdx != userIdxByJwt){
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
//
//            PostUserRes postUserRes = userService.createNickname(postUserReq);
//            return new BaseResponse<>(postUserRes);
//
//        } catch (BaseException exception) {
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }

// 사용 X
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
     * 아이디찾기후 비번바꾸기 API (users) -> 이거 그냥 마이페이지(비번아는상태에서) 바꾸는걸로
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


    /**
     * 마이페이지 API (users)
     * [GET] /users/mypage?userIdx=
     * @return BaseResponse<GetUserNicknameRes>
     */
    @ResponseBody
    @GetMapping("/mypage")
    public BaseResponse<GetUserInfoRes> getUserInfo(@RequestParam(name = "userIdx", defaultValue = "1")int userIdx)  {



        try{
            //jwt에서 idx 추출.
            //getUserIdx 타고 들가면 거기서 jwt 키 있는지 없는지 유효성 검사 실행함
            int userIdxByJwt = jwtService.getUserIdx();

            // 실제 Idx와 jwt로 추출한 Idx가 맞는지 유효성검사
            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            GetUserInfoRes getUserInfoRes = userProvider.retrieveUserInfo(userIdx);
            return new BaseResponse<>(getUserInfoRes);
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
