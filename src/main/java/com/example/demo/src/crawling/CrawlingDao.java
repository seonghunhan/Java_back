package com.example.demo.src.crawling;


import com.example.demo.src.crawling.model.GetNewsArticleReq;
import com.example.demo.src.crawling.model.GetTopFiveKeywordsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

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

        String updateKeywordStackQuery = "update Keyword set `" + userIdx + "` = `" + userIdx + "` + 1 where Keyword_List = ? ";
        Object []insertKeywordParams = new Object[] {keyword}; //물음표에 들어갈것들을 받아오는 과정

        this.jdbcTemplate.update(updateKeywordStackQuery, insertKeywordParams);

    }

    // .query() 써서 리스트로 보낸거! (여기는 한컬럼에 여러개 보낼때 사용!!!)!!!!!!!!!!!!!!!!!!!!!!!!!!
    public List<GetTopFiveKeywordsRes> selectTopFiveKeywords(int userIdx){

        //String selectFiveKeywordsQuery = "select Keyword_List from Keyword where `"+ userIdx +"` LIMIT 5";
        String selectFiveKeywordsQuery = "select Keyword_List from Keyword where ? LIMIT 5"; //jdbcTemplate에 파람스 넣으면 위처럼 쿼리짜지말고 ? 넣기!
        int selectFiveKeywordsQueryParams = userIdx;

        return this.jdbcTemplate.query(selectFiveKeywordsQuery,
                (rs,rowNum) -> new GetTopFiveKeywordsRes(
                        rs.getString("Keyword_List")
                ),selectFiveKeywordsQueryParams);
        }



}
