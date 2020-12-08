package com.cmit.mvne.billing.infomanage.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2019/12/11 17:31
 */
public class StringUtils {
    /**
     * 将异常的堆栈信息转成String返回
     * @param e
     * @return
     */
    public static String getExceptionText(Exception e){
        String text = "" ;
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        text = sw.toString();
        return text;
    }
}
