package com.sunny.BizCode.util;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class SqlUtil {
    public String Deleteunderline(String para) {
        StringBuilder result = new StringBuilder();
        String a[] = para.split("_");
        if (a.length == 0) {
            return para.toLowerCase();
        }

        for (String s : a) {
            s = s.toLowerCase();
            if (result.length() == 0) {
                result.append(s);
            } else {
                result.append(s);

            }
        }
        return result.toString().toLowerCase();
    }
}
