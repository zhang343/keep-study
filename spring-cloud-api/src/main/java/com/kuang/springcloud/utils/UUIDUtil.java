package com.kuang.springcloud.utils;

import java.util.UUID;


public class UUIDUtil {

    //uuid生成，不带‘-’
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
