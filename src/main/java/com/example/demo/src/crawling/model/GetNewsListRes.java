package com.example.demo.src.crawling.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetNewsListRes {

    private String title;
    private String date;
    private String picture;
    private String brief;
    private String url;
    private int page;


}
