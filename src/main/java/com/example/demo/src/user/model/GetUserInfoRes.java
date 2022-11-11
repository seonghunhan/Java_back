package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserInfoRes {

    private String id;
    private String nickName;
    private String phone;
    private String email;

}
