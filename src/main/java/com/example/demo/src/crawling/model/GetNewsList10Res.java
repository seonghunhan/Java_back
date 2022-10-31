package com.example.demo.src.crawling.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class GetNewsList10Res {

    private ArrayList<ArrayList<GetNewsListRes>> getNews10;  //객체가 아닌 리스트형식 반환


}
