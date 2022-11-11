package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public ArrayList<Integer> check(String id, String nickName, String email) throws BaseException{
        try{
            return userDao.checkInfo(id, nickName, email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostUserRes checkId(String nickName, String email) throws BaseException{
        ArrayList<Integer> checkList = new ArrayList<Integer>(userDao.checkInfo(null, nickName, email));
        System.out.println(checkList);

        if(checkList.get(3) == 0) {
            throw new BaseException(POST_USERS_NOT_EXISTS_ID);
        }
        try{
                return userDao.selectIdByNicknameEmail(nickName, email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public GetUserNicknameRes  retrieveUserNickname(String nickName) throws BaseException{
        ArrayList<Integer> checkList = new ArrayList<Integer>(userDao.checkInfo(null, nickName, null));
        System.out.println(checkList);
        boolean isTrue = false; // default : 사용 불가능
        if(checkList.get(1) == 0) {
            isTrue = true; // 사용가능
        }
        try{
            return new GetUserNicknameRes(isTrue); // set으로 모델 안바꾸고 이렇게 파람스로 바꾸기!!
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetUserIdRes  retrieveUserId(String id) throws BaseException{
        ArrayList<Integer> checkList = new ArrayList<Integer>(userDao.checkInfo(id, null, null));
        System.out.println(checkList);
        boolean isTrue = false; // default : 사용 불가능
        if(checkList.get(0) == 0) {
            isTrue = true; // 사용가능
        }
        try{
            return new GetUserIdRes(isTrue); // set으로 모델 안바꾸고 이렇게 파람스로 바꾸기!!
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetUserInfoRes retrieveUserInfo(int userIdx) throws BaseException{
    try{
        GetUserInfoRes getUserInfoRes = userDao.selectUserInfoByUserIdx(userIdx);
        return getUserInfoRes;
    }
    catch (Exception exception) {
        throw new BaseException(DATABASE_ERROR);
    }
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


//    public int checkUserExist(int userIdx) throws BaseException{
//        try{
//            return userDao.checkUserExist(userIdx);
//        } catch (Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }



}
