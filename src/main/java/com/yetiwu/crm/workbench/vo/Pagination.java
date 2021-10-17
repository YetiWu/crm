package com.yetiwu.crm.workbench.vo;

import com.yetiwu.crm.workbench.domain.Clue;

import java.util.List;

public class Pagination<T> {
    private Integer total;
    private List<T> pageList;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getPageList() {
        return pageList;
    }

    public void setPageList(List<T> pageList) {
        this.pageList = pageList;
    }
}
