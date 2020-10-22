package com.xkf.cashbook.web.vo;

import java.util.Date;

public class IncomeDetailVO {

    private Long id;

    private String incomeBy;

    private Double incomeAmount;

    private Long incomeCategoryId;

    private String incomeDate;

    private Date recordDate;

    private String recordBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIncomeBy() {
        return incomeBy;
    }

    public void setIncomeBy(String incomeBy) {
        this.incomeBy = incomeBy;
    }

    public double getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(Double incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public Long getIncomeCategoryId() {
        return incomeCategoryId;
    }

    public void setIncomeCategoryId(Long incomeCategoryId) {
        this.incomeCategoryId = incomeCategoryId;
    }

    public String getIncomeDate() {
        return incomeDate;
    }

    public void setIncomeDate(String incomeDate) {
        this.incomeDate = incomeDate;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public String getRecordBy() {
        return recordBy;
    }

    public void setRecordBy(String recordBy) {
        this.recordBy = recordBy;
    }
}
