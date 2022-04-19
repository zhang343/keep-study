package com.kuang.bbs.utils;

public class ArrayUtils {

    public static String toString(Object[] a) {
        if (a == null)
            return "";

        int iMax = a.length - 1;
        if (iMax == -1)
            return "";

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(String.valueOf(a[i]));
            if (i == iMax)
                return b.toString();
            b.append(" ");
        }
    }
}
