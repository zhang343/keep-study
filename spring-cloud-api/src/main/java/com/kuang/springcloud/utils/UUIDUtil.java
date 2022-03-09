package com.kuang.springcloud.utils;

import java.util.UUID;

/**
 * @author XiaoZhang
 * @date 2022/1/22 15:20
 * uuid生成器
 */
public class UUIDUtil {

    //uuid生成，不带‘-’
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
