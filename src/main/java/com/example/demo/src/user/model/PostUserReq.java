package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {
    private String id;
    private String password;
    private String passwordForCheck; // 회원가입시 비밀번호 확인 박스에 넣는 두번째 비밀번호
    private String name;
    private String nickName;
    private String phone;
    private String email;
    private int userIdx;
    private String mbti;
}
