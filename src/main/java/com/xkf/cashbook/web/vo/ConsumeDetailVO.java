package com.xkf.cashbook.web.vo;

import java.util.Date;

/**
 * 消费详情
 *
 * @author xukf01
 */
public class ConsumeDetailVO {

    private Long id;

    private Long consumeCategoryId;

    private Double consumeAmount;

    private Integer consumeWay;

    private Date recordDate;

    private String recordBy;

    private String consumeBy;

    private String consumeDate;

    private String consumeCategoryName;

    public String getConsumeCategoryName() {
        return consumeCategoryName;
    }

    public void setConsumeCategoryName(String consumeCategoryName) {
        this.consumeCategoryName = consumeCategoryName;
    }

    public String getConsumeDate() {
        return consumeDate;
    }

    public void setConsumeDate(String consumeDate) {
        this.consumeDate = consumeDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getConsumeWay() {
        return consumeWay;
    }

    public void setConsumeWay(Integer consumeWay) {
        this.consumeWay = consumeWay;
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

    public String getConsumeBy() {
        return consumeBy;
    }

    public void setConsumeBy(String consumeBy) {
        this.consumeBy = consumeBy;
    }

    @Override
    public String toString() {
        return "ConsumeDetailVO{" +
                "id=" + id +
                ", consumeCategoryId=" + consumeCategoryId +
                ", consumeAmount=" + consumeAmount +
                ", consumeWay=" + consumeWay +
                ", recordDate=" + recordDate +
                ", recordBy='" + recordBy + '\'' +
                ", consumeFor='" + consumeBy + '\'' +
                ", consumeDate=" + consumeDate +
                '}';
    }
}
