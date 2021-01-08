package com.cmit.mvne.billing.rating.gprs.util;

import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/2/17 21:54
 */
public class ClassConvertUtils {
    public static <F, T> void ListConvert(List<F> fromList, List<T> toList) {
        fromList.stream().forEach(f -> {
            // 泛型实例化对象，有点麻烦
            /*T t = new T();
            BeanUtils.copyProperties(f, cdrGprsRerat);
            cdrGprsReratList.add(cdrGprsRerat);*/
        });
    }
}
