package com.example.demo.utils;

public class FormatUtil {


    // 01000000000 -> 010-0000-0000 형변환
    public String phoneFormat(String number) {

        return number.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$","$1-$2-$3");

    }
}
