package com.cmit.mvne.billing.settle.util;

import java.util.List;

/**
 * 自定义List分页工具
 * @author ws
 */
public class PageUtil<T> {

    /**
     * 开始分页
     * @param list
     * @param pageNum 页码
     * @param pageSize 每页多少条数据
     * @return
     */
    public  List<T> startPage(List<T> list, Integer pageNum, Integer pageSize) {
        if (list == null) {
            return null;
        }
        if (list.size() == 0) {
            return null;
        }

        Integer count = list.size(); // 记录总数
        Integer pageCount = 0; // 页数
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }

        int fromIndex = 0; // 开始索引
        int toIndex = 0; // 结束索引

        if (pageNum != pageCount) {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = fromIndex + pageSize;
        } else {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = count;
        }
        if ( list.size() == 0 || pageNum > list.size()) {
            return null;
        }
        if ( list.size()  > 0) {
            List<T> pageList = list.subList(fromIndex, toIndex);
            return pageList;
        }
        return null;
    }
}
