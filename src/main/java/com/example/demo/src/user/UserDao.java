package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into User (id, password, phone, email) VALUES (?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getId(), postUserReq.getPassword(), postUserReq.getPhone(),postUserReq.getEmail()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        //last_insert_id 함수는 테이블의 마지막 auto_increment 값을 리턴한다. (mysql)
        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int createUserNickname(PostUserReq postUserReq){
        String modifyUserNicknameQuery = "update User set nickName = ? where userIdx = ? ";
        Object[] modifyUserNicknameParams = new Object[]{postUserReq.getNickName(), postUserReq.getUserIdx()};
        this.jdbcTemplate.update(modifyUserNicknameQuery,modifyUserNicknameParams);

        return postUserReq.getUserIdx();
    }

    public int createUserMbti(PostUserReq postUserReq){
        String modifyMbtiQuery = "update User set mbti = ? where userIdx = ? ";
        Object[] modifyUserMbtiParams = new Object[]{postUserReq.getMbti(), postUserReq.getUserIdx()};
        this.jdbcTemplate.update(modifyMbtiQuery,modifyUserMbtiParams);

        return postUserReq.getUserIdx();
    }

    public ArrayList<Integer> checkInfo(String id, String nickName, String email){

        String checkIdQuery = "select exists(select id from User where id = ?)";
        String checkNicknameQuery = "select exists(select nickName from User where nickName = ?)";
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkIdxQuery = "select exists(select userIdx from User where nickName=? AND email = ?)";


        ArrayList<Integer> check = new ArrayList<Integer>();

        check.add(this.jdbcTemplate.queryForObject(checkIdQuery, int.class, id));
        check.add(this.jdbcTemplate.queryForObject(checkNicknameQuery, int.class, nickName));
        check.add(this.jdbcTemplate.queryForObject(checkEmailQuery, int.class, email));
        check.add(this.jdbcTemplate.queryForObject(checkIdxQuery, int.class, nickName,email));

        return(check);

    }


    public PostUserRes selectIdByNicknameEmail(String nickName, String email){

        String selectIdQuery = "select id\n" +
                "from User\n" +
                "where nickName = ? AND email = ?";
        Object[] checkIdParams = new Object[]{nickName, email};


        return this.jdbcTemplate.queryForObject(selectIdQuery, //쿼리포오브젝트는 하나의 객체만 반환할때 사용 그냥 쿼리는 리스트같은거 반환할때사용
                (rs, rowNum) -> new PostUserRes(
                        rs.getString("id")),
                        checkIdParams);
    }

    public int selectPwdByIdxForCompare(int userIdx, String comparePwd){
        String selectPwdForCompareQuery = "select exists(select userIdx from User where userIdx = ? and password = ?)";
        Object[] selectPwdForCompareParams = new Object[]{userIdx, comparePwd};

        return this.jdbcTemplate.queryForObject(selectPwdForCompareQuery, int.class, selectPwdForCompareParams);

    }

    public void updateUserPwd(String changePwd, int userIdx){
        String modifyUserPasswordQuery = "update User set password = ? where userIdx = ? ";
        Object[] modifyUserPasswordParams = new Object[]{changePwd, userIdx};
        this.jdbcTemplate.update(modifyUserPasswordQuery,modifyUserPasswordParams);


        return;

    }



//    public GetUserFeedRes getUsersByEmail(String email){ //Params인 email은 밑에 ?에 들어갈거임
//        String getUsersByEmailQuery = "select userIdx,name,nickName,email from User where email=?"; //모델에서 원하는대로
//        String getUsersByEmailParams = email;                                                    //userIdx,name,nickName,email가 나오게끔 처리함
//        return this.jdbcTemplate.queryForObject(getUsersByEmailQuery, //쿼리포오브젝트는 하나의 객체만 반환할때 사용 그냥 쿼리는 리스트같은거 반환할때사용
//                (rs, rowNum) -> new GetUserFeedRes(
//                        rs.getInt("userIdx"),
//                        rs.getString("name"),
//                        rs.getString("nickName"),
//                        rs.getString("email")),
//                getUsersByEmailParams);
//    }
//            //모델명 함수명(파라메터)
//    public GetUserInfoRes selectUserInfo(int userIdx){
//        String selectUsersInfoQuery = "SELECT U.nickName, U.name, F.profileImgUrl, F.introduction, F.followerCount, F.followingCount,F.postCount\n" +
//                "FROM Feed F \n" +
//                "INNER JOIN User U \n" +
//                "ON F.userIdx = U.userIdx \n" +
//                "WHERE F.userIdx = ? AND U.status ='ACTIVE'";
//
//        int selectUserInfoParam = userIdx; //함수에 들어온 파라메터는 위 쿼리문에 ?로 들어간다
//        return this.jdbcTemplate.queryForObject(selectUsersInfoQuery,
//                (rs,rowNum) -> new GetUserInfoRes(
//                        rs.getString("name"),
//                        rs.getString("nickName"),
//                        rs.getString("profileImgUrl"),
//                        rs.getString("introduction"),
//                        rs.getInt("followerCount"),
//                        rs.getInt("followingCount"),
//                        rs.getInt("postCount")
//                ),selectUserInfoParam);
//    }
//
//    public List<GetUserPostsRes> selectUserPosts(int userIdx){
//        String selectPostsQuery = "SELECT P.PostImgUrl, P.Idx\n" +
//                "FROM Post P\n" +
//                "INNER JOIN User U\n" +
//                "ON P.userIdx = U.userIdx\n" +
//                "WHERE P.userIdx = ? AND P.status ='ACTIVE'";
//
//        int selectUserPostsParam = userIdx;
//        return this.jdbcTemplate.query(selectPostsQuery,
//                (rs,rowNum) -> new GetUserPostsRes(
//                        rs.getInt("Idx"),
//                        rs.getString("PostImgUrl")
//
//                ),selectUserPostsParam);
//    }



//    public GetUserFeedRes getUsersByEmail(String email){ //Params인 email은 밑에 ?에 들어갈거임
//        String getUsersByEmailQuery = "select userIdx,name,nickName,email from User where email=?"; //모델에서 원하는대로
//        String getUsersByEmailParams = email;                                                    //userIdx,name,nickName,email가 나오게끔 처리함
//        return this.jdbcTemplate.queryForObject(getUsersByEmailQuery, //쿼리포오브젝트는 하나의 객체만 반환할때 사용 그냥 쿼리는 리스트같은거 반환할때사용
//                (rs, rowNum) -> new GetUserFeedRes(
//                        rs.getInt("userIdx"),
//                        rs.getString("name"),
//                        rs.getString("nickName"),
//                        rs.getString("email")),
//                getUsersByEmailParams);
//    }


//    public GetUserFeedRes getUsersByIdx(int userIdx){
//        String getUsersByIdxQuery = "select userIdx,name,nickName,email from User where userIdx=?";
//        int getUsersByIdxParams = userIdx;
//        return this.jdbcTemplate.queryForObject(getUsersByIdxQuery,
//                (rs, rowNum) -> new GetUserFeedRes(
//                        rs.getInt("userIdx"),
//                        rs.getString("name"),
//                        rs.getString("nickName"),
//                        rs.getString("email")),
//                getUsersByIdxParams);
//    }


//    public int checkUserExist(int userIdx){
//        String checkUserExistQuery = "select exists(select userIdx from User where userIdx = ?)";
//        int checkUserExistParams = userIdx;
//        return this.jdbcTemplate.queryForObject(checkUserExistQuery,
//                int.class,
//                checkUserExistParams);
//    }
//
//    public int modifyUserName(PatchUserReq patchUserReq){
//        String modifyUserNameQuery = "update User set nickName = ? where userIdx = ? ";
//        Object[] modifyUserNameParams = new Object[]{patchUserReq.getNickName(), patchUserReq.getUserIdx()};
//
//        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
//    }




}
