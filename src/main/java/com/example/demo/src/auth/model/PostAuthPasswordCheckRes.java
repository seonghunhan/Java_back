package com.example.demo.src.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor

public class PostAuthPasswordCheckRes {

    private int userIdx;
    private String jwt;
}
