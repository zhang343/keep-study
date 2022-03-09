package com.kuang.springcloud.utils;

import com.kuang.springcloud.exceptionhandler.XiaoXiaException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author XiaoZhang
 * @date 2022/1/22 15:20
 * md5加密器
 */
public class MD5Util {

    //进行md5加密
    public static String getMD5(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("md5");
        } catch(NoSuchAlgorithmException e) {
            throw new XiaoXiaException();
        }
        byte[] result = digest.digest(password.getBytes());
        StringBuffer buffer = new StringBuffer();
        // 把每一个byte 做一个与运算 0xff;
        for (byte b : result) {
            // 与运算
            int number = b & 0xff;// 加盐
            String str = Integer.toHexString(number);
            if (str.length() == 1) {
                buffer.append("0");
            }
            buffer.append(str);
        }
        // 标准的md5加密后的结果
        return buffer.toString();
    }
}
