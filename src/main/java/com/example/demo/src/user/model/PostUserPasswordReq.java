package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserPasswordReq {
    private int userIdx;
    private String password;
    private String newPassword; // 회원가입시 비밀번호 확인 박스에 넣는 두번째 비밀번호
    private String newPasswordForCheck;
}
