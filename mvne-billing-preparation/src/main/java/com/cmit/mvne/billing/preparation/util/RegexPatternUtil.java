package com.cmit.mvne.billing.preparation.util;

import org.springframework.lang.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/3/13
 */
public class RegexPatternUtil {

    public static boolean simpleMatch(@Nullable String regex, @Nullable String str) {
        if(regex == null || str == null) {
            return false;
        }

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        return matcher.matches();
    }


}
