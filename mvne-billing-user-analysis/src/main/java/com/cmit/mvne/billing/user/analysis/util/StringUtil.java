package com.cmit.mvne.billing.user.analysis.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2019/12/11 17:31
 */
public class StringUtil {
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

    public static List<Long> str2LongLost(String str) {
        if (!ObjectUtil.isNotNullOrBlank(str)) {
            return null;
        }
        ArrayList<Long> longList = new ArrayList<>();

        String[] Longs = str.split(",");
        for (String aLong : Longs) {
            longList.add(Long.parseLong(aLong));
        }

        return longList;
    }
}
