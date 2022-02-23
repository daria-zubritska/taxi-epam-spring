package com.epam.project.spring.taxispring.model.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Car {
    private int id;
    private String name;
    private BigDecimal cost;
    private Status status;
    private CarType category;
    private int passengers;

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public CarType getCategory() {
        return category;
    }

    public void setCategory(CarType category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return name.equals(car.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cost);
    }

    public enum Status {
        FREE, BUSY, UNAVAILABLE;

        public boolean isAvailable() {
            return this == FREE;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    public enum CarType{
        CHEAP, COMFORT, BUSINESS;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
}
