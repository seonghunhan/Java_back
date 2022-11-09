package com.example.demo.src.crawling;


import com.example.demo.src.crawling.model.GetNewsArticleReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class CrawlingDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void updateKeywordStack(GetNewsArticleReq getNewsArticleReq){
        String keyword = getNewsArticleReq.getKeyword();
        int userIdx = getNewsArticleReq.getUserIdx();

        String updateKeywordStackQuery = "update Keyword set `" + 217 + "` = `" + 217 + "` + 1 where Keyword_List = ? ";
        Object []insertKeywordParams = new Object[] {keyword}; //물음표에 들어갈것들을 받아오는 과정

        this.jdbcTemplate.update(updateKeywordStackQuery, insertKeywordParams);

    }





}
