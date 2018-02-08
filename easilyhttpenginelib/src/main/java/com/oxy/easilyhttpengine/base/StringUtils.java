package com.oxy.easilyhttpengine.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BelleOU on 18/1/31.
 */

public class StringUtils {
    public static String unicode2String(String text){
        if(text == null || text.length() == 0){
            return text;
        }
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(text);

        char ch;
        while (matcher.find()){
            ch = (char)Integer.parseInt(matcher.group(2),16);
            text = text.replace(matcher.group(1),ch+"");
        }
        return text;
    }
}
