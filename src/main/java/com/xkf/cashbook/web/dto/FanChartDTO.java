package com.xkf.cashbook.web.dto;

/** 扇形图数据
 * @author xukf01
 */
public class FanChartDTO {

    private Double value;

    private String name;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FanChartDTO() {
    }

    public FanChartDTO(Double value, String name) {
        this.value = value;
        this.name = name;
    }
}
