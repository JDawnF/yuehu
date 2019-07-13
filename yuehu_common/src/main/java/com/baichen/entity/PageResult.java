package com.baichen.entity;

import java.util.List;

/**
 * @Program: PageResult
 * @Author: baichen
 * @Description: 分页结果类
 */
public class PageResult<T> {
    private Long total;
    private List<T> rows;       // 属性上的泛型要在类上声明，方法可以直接声明

    public PageResult(Long total, List<T> rows) {
        super();
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
