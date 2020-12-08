package com.cmit.mvne.billing.user.analysis.util;

import java.util.List;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/5/28 14:36
 */
public class ObjectUtil {
    public static <T> Boolean isNotNullOrBlank(T object) {
        if (object == null) {
            return false;
        } else if (object.equals("")) {
            return false;
        }

        return true;
    }

    public static <T> Boolean isNotNullOrEmpty(List<T> object) {
        if (object == null) {
            return false;
        } else if (object.size()==0) {
            return false;
        }

        return true;
    }
}
