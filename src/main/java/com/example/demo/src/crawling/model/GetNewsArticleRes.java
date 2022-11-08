package com.example.demo.src.crawling.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetNewsArticleRes {

    private String ArticleText;
    private String Title;
    private String date;


}
