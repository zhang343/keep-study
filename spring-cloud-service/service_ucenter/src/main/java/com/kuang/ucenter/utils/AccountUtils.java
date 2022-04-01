package com.kuang.ucenter.utils;

import com.kuang.springcloud.utils.RedisUtils;

public class AccountUtils {

    public static final String ACCOUNTKEY = "account";

    public static String getAccount(){
        return RedisUtils.increment(ACCOUNTKEY);
    }

}
