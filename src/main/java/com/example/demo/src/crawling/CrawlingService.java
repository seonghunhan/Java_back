package com.example.demo.src.crawling;


import com.example.demo.config.BaseException;
import com.example.demo.src.crawling.model.GetNewsArticleReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

// Service Create, Update, Delete 의 로직 처리
@Service
public class CrawlingService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CrawlingDao crawlingDao;
    private final CrawlingProvider crawlingProvider;
    private final JwtService jwtService;


    @Autowired
    public CrawlingService(CrawlingDao crawlingDao, CrawlingProvider crawlingProvider, JwtService jwtService) {
        this.crawlingDao = crawlingDao;
        this.crawlingProvider = crawlingProvider;
        this.jwtService = jwtService;

    }

    public void stackKeyword(GetNewsArticleReq getNewsArticleReq) throws  BaseException{

        try{

            crawlingDao.updateKeywordStack(getNewsArticleReq);

            return;
        }
        catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


//    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
//        // 이메일 중복 확인
//        if(userProvider.checkEmail(postUserReq.getEmail()) ==1){
//            throw new BaseException(POST_USERS_EXISTS_EMAIL);
//        }
//
//        String pwd;
//        try{
//            //암호화
//            pwd = new SHA256().encrypt(postUserReq.getPassword());  postUserReq.setPassword(pwd);
//        } catch (Exception ignored) {
//            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
//        }
//        try{
//            int userIdx = postDao.createUser(postUserReq);
//            //jwt 발급.
//            // TODO: jwt는 다음주차에서 배울 내용입니다!
//            String jwt = jwtService.createJwt(userIdx);
//            return new PostUserRes(jwt,userIdx);
//        } catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }

//    public void modifyUserName(PatchUserReq patchUserReq) throws BaseException {
//        try{
//            int result = postDao.modifyUserName(patchUserReq);
//            if(result == 0){
//                throw new BaseException(MODIFY_FAIL_USERNAME);
//            }
//        } catch(Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }

}
