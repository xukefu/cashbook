package com.xkf.cashbook.web.vo;

import java.util.List;

public class TableVO {
    private String data;

    private List<ConsumeReportVO> list;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<ConsumeReportVO> getList() {
        return list;
    }

    public void setList(List<ConsumeReportVO> list) {
        this.list = list;
    }
}
