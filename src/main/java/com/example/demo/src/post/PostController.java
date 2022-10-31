package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.post.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PostProvider postProvider;
    @Autowired
    private final PostService postService;
    @Autowired
    private final JwtService jwtService;


    public PostController(PostProvider postProvider, PostService postService, JwtService jwtService) {
        this.postProvider = postProvider;
        this.postService = postService;
        this.jwtService = jwtService;

    }


//    /**
//     * 회원 조회 API
//     * [GET] /users
//     * 이메일 검색 조회 API
//     * [GET] /users? Email=
//     * @return BaseResponse<GetUserRes>
//     */
//    //Query String
//    @ResponseBody
//    @GetMapping("") // (GET) 127.0.0.1:9000/users 겟맵핑에 아무것도 명시된것이 없으므로 127.0.0.1:9000/users 이거로 퉁친다는거임
//    public BaseResponse<GetUserFeedRes> getUsers(@RequestParam(required = true) String Email) { //GetUserRes 는 모델임(여기서 명시한대로 리턴함)
//        try{                                                             // 리퀘스트파람은 쿼리스트링임
//            // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
//            if(Email.length()==0){
//                return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
//            }
//            // 이메일 정규표현
//            if(!isRegexEmail(Email)){
//                return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
//            }
//            GetUserFeedRes getUsersRes = userProvider.getUsersByEmail(Email); //조회 담당인 Provider로 넘긴다
//            return new BaseResponse<>(getUsersRes);
//        } catch(BaseException exception){
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }


//    @ResponseBody
//    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/users/:userIdx
//    public BaseResponse<GetUserFeedRes> getUserByIdx(@PathVariable("userIdx")int userIdx) {
//        try{
//
//            GetUserFeedRes getUsersRes = userProvider.getUsersByIdx(userIdx);
//            return new BaseResponse<>(getUsersRes);
//        } catch(BaseException exception){
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }


    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostPostsRes> createPosts(@RequestBody PostPostsReq postPostsReq) {  //여기는 바디로 받을것 그래서 모델 이용
        try {
            if (postPostsReq.getContent().length() > 450) {
                // postPostsReq에서 (요청한것) content만 떼오는것
                return new BaseResponse<>(BaseResponseStatus.POST_POSTS_INVALID_CONTENTS);
            }
            if (postPostsReq.getPostImgUrls().size() < 1) {
                return new BaseResponse<>(BaseResponseStatus.POST_POSTS_EMPTY_IMGURL);
            }
            //System.out.println("여기");                      //getUserIdx는 모델에서 getter를 롬북으로 가져왔고 내장함수 규칙이 앞에 대문자임
            PostPostsRes postPostsRes = postService.createPosts(postPostsReq.getUserIdx(), postPostsReq); // 뭔가를 만드는거니까 서비스에서 처리
                                                                // 여기는 리퀘스트(요청)한 postPostReq에서 UserIdx만 빼오는것 이것을 서비스에 넘기는것, 나중에는 jwt로 userIdx만 줄거니깐 일단 이렇게 처리한것
            return new BaseResponse<>(postPostsRes); //응답값은 우리가 만든 포스트의 id값을 리턴할꺼니깐 postPostsRes 사용
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}




    /**
     * 유저정보변경 API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
//    @ResponseBody
//    @PatchMapping("/{userIdx}") // (PATCH) 127.0.0.1:9000/users/:userIdx
//    public BaseResponse<String> modifyUserName(@PathVariable("userIdx") int userIdx, @RequestBody User user){
//        try {
//            /* TODO: jwt는 다음주차에서 배울 내용입니다!
//            jwt에서 idx 추출.
//            int userIdxByJwt = jwtService.getUserIdx();
//            userIdx와 접근한 유저가 같은지 확인
//            if(userIdx != userIdxByJwt){
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
//            */
//
//            PatchUserReq patchUserReq = new PatchUserReq(userIdx,user.getNickName());
//            userService.modifyUserName(patchUserReq);
//
//            String result = "";
//        return new BaseResponse<>(result);
//        } catch (BaseException exception) {
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }
//
//}
