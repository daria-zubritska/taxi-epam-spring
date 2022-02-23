package com.epam.project.spring.taxispring.model.dto;

import java.math.BigDecimal;

public class DoubleOrderDTO {
    private OrderDTO order1;
    private OrderDTO order2;
    private BigDecimal costWithDiscount;
    private BigDecimal fullCost;

    public OrderDTO getOrder1() {
        return order1;
    }

    public void setOrder1(OrderDTO order1) {
        this.order1 = order1;
    }

    public OrderDTO getOrder2() {
        return order2;
    }

    public void setOrder2(OrderDTO order2) {
        this.order2 = order2;
    }

    public BigDecimal getCostWithDiscount() {
        return costWithDiscount;
    }

    public void setCostWithDiscount(BigDecimal costWithDiscount) {
        this.costWithDiscount = costWithDiscount;
    }

    public BigDecimal getFullCost() {
        return fullCost;
    }

    public void setFullCost(BigDecimal fullCost) {
        this.fullCost = fullCost;
    }


}
