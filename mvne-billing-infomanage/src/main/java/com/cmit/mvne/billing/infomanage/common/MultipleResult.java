package com.cmit.mvne.billing.infomanage.common;

import com.cmit.mvne.billing.infomanage.entity.QueryUserCdrInfo;

import java.util.List;

public class MultipleResult {
    private List<QueryUserCdrInfo> data;
    private Integer pageNo;
    private Integer pageSize;
    private Integer total;

    public List<QueryUserCdrInfo> getData() {
        return data;
    }
    public void setData(List<QueryUserCdrInfo> data) {
        this.data = data;
    }
    public Integer getPageNo() {
        return pageNo;
    }
    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    public Integer getTotal() {
        return total;
    }
    public void setTotal(Integer total) {
        this.total = total;
    }
}
