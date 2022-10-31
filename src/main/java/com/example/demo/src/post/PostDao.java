package com.example.demo.src.post;


import com.example.demo.src.post.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PostDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    public int insertPosts(int userIdx, String content){
        String insertPostQuery = "INSERT INTO Post(userIdx, content) VALUES (?,?)";
        Object []insertPostParams = new Object[] {userIdx,content}; //물음표에 들어갈것들을 받아오는 과정

        this.jdbcTemplate.update(insertPostQuery, //인서트문은 업데이트를 해줘야함
                insertPostParams);    // 여기까지는 데이터 입력

        String lastInsertIdxQuery = "select last_insert_id()"; //여기 쿼리문을 쓰면 자동으로 마지막 들간거 가져옴
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery,int.class); //위 쿼리문 실행하는 구문, 여기는 방금넣은 데이터들을 클라에게 알려주기위함
    }

    public int insertPostImgs(int postIdx, PostImgsUrlReq postImgsUrlReq){ //post객체와 이미지 객체를 한번에 받아옴
        String insertPostImgsQuery = "INSERT INTO PostImgUrl(postIdx, imgUrl) VALUES (?,?)";
        Object [] insertPostImgsParams = new Object[] {postIdx,postImgsUrlReq.getImgUrl()};

        this.jdbcTemplate.update(insertPostImgsQuery,
                insertPostImgsParams);

        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery,int.class);
    }



}
