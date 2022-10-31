package com.example.demo.src.user.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor // 아무것도 선택안해도가능
@RequiredArgsConstructor // 선택적으로 사용가능(NonNull만)
@AllArgsConstructor // All쓰면 여기 변수갯수 다 만들어줘서 new로 객체생성해줘야함
public class PostUserRes {

    private String jwt;  //프롬투에서는 로그인할때 jwt발급해줬음

    private int userIdx;

    @NonNull
    private String id;
}
