package com.epam.project.spring.taxispring.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class OrderDTO {
    private int id;
    private String carName;
    private String userName;
    private int userId;
    private int carId;
    private LocalDate orderDate;
    private String locationTo;
    private String locationFrom;
    private int passengers;
    private BigDecimal cost;
    private BigDecimal costWithDiscount = null;
    private String carClass;

    public OrderDTO(int id, String carName, int userId, int carId, LocalDate orderDate, String locationTo, String locationFrom, int passengers, BigDecimal cost, String carClass) {
        this.id = id;
        this.carName = carName;
        this.userId = userId;
        this.carId = carId;
        this.orderDate = orderDate;
        this.locationTo = locationTo;
        this.locationFrom = locationFrom;
        this.passengers = passengers;
        this.cost = cost;
        this.carClass = carClass;
    }

    public OrderDTO() {
    }

    public String getCarClass() {
        return carClass;
    }

    public void setCarClass(String carClass) {
        this.carClass = carClass;
    }

    public BigDecimal getCostWithDiscount() {
        return costWithDiscount;
    }

    public void setCostWithDiscount(BigDecimal costWithDiscount) {
        this.costWithDiscount = costWithDiscount;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int orderId) {
        this.id = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getLocationTo() {
        return locationTo;
    }

    public void setLocationTo(String locationTo) {
        this.locationTo = locationTo;
    }

    public String getLocationFrom() {
        return locationFrom;
    }

    public void setLocationFrom(String locationFrom) {
        this.locationFrom = locationFrom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO order = (OrderDTO) o;
        return userId == order.userId && carId == order.carId && orderDate.equals(order.orderDate) && locationTo.equals(order.locationTo) && locationFrom.equals(order.locationFrom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, carId, orderDate, locationTo, locationFrom);
    }
}
