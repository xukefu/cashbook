package com.xkf.cashbook.pojo.vo;

public class ConsumeReportVO {
    private Long consumeCategoryId;

    public Long getConsumeCategoryId() {
        return consumeCategoryId;
    }

    public void setConsumeCategoryId(Long consumeCategoryId) {
        this.consumeCategoryId = consumeCategoryId;
    }

    public Double getConsumeAmount() {
        return consumeAmount;
    }

    public void setConsumeAmount(Double consumeAmount) {
        this.consumeAmount = consumeAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private Double consumeAmount;
    private String type;

    public ConsumeReportVO(Long consumeCategoryId, Double consumeAmount, String type) {
        this.consumeCategoryId = consumeCategoryId;
        this.consumeAmount = consumeAmount;
        this.type = type;
    }

    public ConsumeReportVO() {
    }
}
