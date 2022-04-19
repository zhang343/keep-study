package com.kuang.springcloud.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;



public class ExceptionUtil {

    //提取异常类内含的异常信息,即栈堆信息
    public static String getMessage(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        printWriter.flush();
        stringWriter.flush();
        try {
            stringWriter.close();
        } catch(IOException ioException) {
            ioException.printStackTrace();
        }
        printWriter.close();
        return stringWriter.toString();
    }
}
