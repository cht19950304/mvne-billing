package com.cmit.mvne.billing.infomanage.common;

public class MultipleSelectPage {
    private Integer start = null;

    private Integer end = null;
    public Integer getStart() {
        return start;
    }
    public Integer getEnd() {
        return end;
    }

    public void setPage(Integer pageNo, Integer pageSize) {

        if (pageSize == null || pageSize <= 0 || pageNo == null || pageNo <= 0) {
            start = null; end = null;
        } else {
            start = (pageNo - 1) * pageSize;
            end = pageSize;
        }

    }
}
