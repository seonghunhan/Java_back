package com.example.demo.src.crawling.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetNewsArticleReq {
    private int userIdx;
    private String keyword;
    private String url;
}
