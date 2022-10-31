package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_EMPTY_ID(false, 2016, "아이디를 입력해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),

    POST_POSTS_INVALID_CONTENTS(false, 2018, "내용의 글자수를 확인해주세요."),
    POST_POSTS_EMPTY_IMGURL(false, 2019, "게시물의 이미지를 입력해주세요."),

    POST_USERS_EMPTY_PASSWORD(false, 2030, "비밀번호를 입력해주세요."),
    POST_USERS_INVALID_PASSWORD(false, 2031, "비밀번호 형식을 확인해주세요."),

    POST_USERS_UNMATCH_PASSWORD(false, 2032, "비밀번호를 다시 확인해주세요."),
    POST_USERS_EMPTY_INFO(false, 2033, "모든 정보를 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2034, "이메일 형식을 확인해주세요."),
    POST_USERS_INVALID_PHONE(false,2035, "전화번호 형식을 확인해주세요." ),
    POST_USERS_EXISTS_ID(false,2036,"이미 존재하는 아이디입니다."),
    POST_USERS_EXISTS_NICKNAME(false,2037,"이미 존재하는 닉네임입니다."),
    POST_USERS_EXISTS_EMAIL2(false,2038,"이미 존재하는 이메일입니다."),
    POST_USERS_NOT_EXISTS_ID(false,2039,"존재하지 않는 계정입니다."),
    POST_USERS_UNREGEX_PHONE(false,2040,"휴대폰 번호를 확인해주세요."),
    POST_USERS_UNMATCH_NEWPASSWORD(false, 2041, "새로운 비밀번호가 서로 다릅니다."),



    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"비밀번호가 틀렸습니다."),

    // [POST] /auth
    FAILED_TO_SEND_SNS_AUTH_CODE(false,3015,"인증코드를 보내는데 실패하였습니다."),
    FAILED_TO_UPDATE_PASSWORD(false, 3016, "비밀번호를 변경하는데 실패하였습니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
