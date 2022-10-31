package com.example.demo.src.post;


import com.example.demo.config.BaseException;
import com.example.demo.src.post.model.GetUserFeedRes;
import com.example.demo.src.post.model.GetUserInfoRes;
import com.example.demo.src.post.model.GetUserPostsRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.USERS_EMPTY_USER_ID;

//Provider : Read의 비즈니스 로직 처리
@Service
public class PostProvider {

    private final PostDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PostProvider(PostDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }


//    public GetUserFeedRes retrieveUserFeed(int userIdxByjwt, int userIdx) throws BaseException{
//        Boolean isMyFeed = true;
//
//        if(checkUserExist(userIdx) == 0)
//        {
//            throw new BaseException(USERS_EMPTY_USER_ID);
//        }
//
//        try{
//            if(userIdxByjwt != userIdx)
//                isMyFeed = false;
//
//            GetUserInfoRes getUserInfoRes = userDao.selectUserInfo(userIdx);
//            List<GetUserPostsRes> getUserPosts = userDao.selectUserPosts(userIdx);
//            GetUserFeedRes getUsersRes = new GetUserFeedRes(isMyFeed, getUserInfoRes, getUserPosts);
//            return getUsersRes;
//        }
//        catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//                    }


//    public GetUserFeedRes getUsersByIdx(int userIdx) throws BaseException{
//        try{
//            GetUserFeedRes getUsersRes = userDao.getUsersByIdx(userIdx);
//            return getUsersRes;
//        }
//        catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }


//    public int checkEmail(String email) throws BaseException{
//        try{
//            return userDao.checkEmail(email);
//        } catch (Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//    public int checkUserExist(int userIdx) throws BaseException{
//        try{
//            return userDao.checkUserExist(userIdx);
//        } catch (Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }



}
