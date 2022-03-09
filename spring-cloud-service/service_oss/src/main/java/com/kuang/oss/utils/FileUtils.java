package com.kuang.oss.utils;


public class FileUtils {

    //获取文件后缀
    public static String getFileSuffix(String fileName){
        String suffix = "";
        if(fileName != null){
            String location = fileName.substring(0 , fileName.indexOf("."));
            suffix = fileName.substring(location.length() + 1 , fileName.length());
        }
        return suffix;
    }
}
