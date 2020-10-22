package com.xkf.cashbook.web.vo;

/** 消费类别
 * @author xukf01
 */
public class ConsumeCategoryVO {

    private Long id;

    private String categoryName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "ConsumeCategoryVO{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
