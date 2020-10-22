package com.xkf.cashbook.web.dto;

public class ConsumeProportionDTO {

    private Double amount;

    private Long consumeCategoryId;

    private String consumeCategoryName;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getConsumeCategoryId() {
        return consumeCategoryId;
    }

    public void setConsumeCategoryId(Long consumeCategoryId) {
        this.consumeCategoryId = consumeCategoryId;
    }

    public String getConsumeCategoryName() {
        return consumeCategoryName;
    }

    public void setConsumeCategoryName(String consumeCategoryName) {
        this.consumeCategoryName = consumeCategoryName;
    }
}
