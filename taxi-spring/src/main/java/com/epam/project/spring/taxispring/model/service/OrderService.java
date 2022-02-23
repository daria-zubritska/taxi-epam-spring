package com.epam.project.spring.taxispring.model.service;

import com.epam.project.spring.taxispring.model.OrderDAO;
import com.epam.project.spring.taxispring.model.entity.Car;

import java.math.BigDecimal;
import java.util.List;

public class OrderService {

    private final static double DISCOUNT = 30;
    private final OrderDAO orderDAO;

    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    /**
     * Counting the cost depending on  car cost and locations
     */
    public BigDecimal cost(BigDecimal costPerK, String loc_from, String loc_to) {
        BigDecimal cost = costPerK;

        BigDecimal dist = BigDecimal.valueOf(orderDAO.getDistance(loc_from, loc_to));

        return cost.multiply(dist);
    }

    /**
     * Counting the cost depending on  car cost and locations with discount
     */
    public BigDecimal costWithDiscount(BigDecimal idealCost, BigDecimal costPerK, String loc_from, String loc_to) {
        double cost = costPerK.doubleValue();
        double costIdeal = idealCost.doubleValue();

        double dist = orderDAO.getDistance(loc_from, loc_to);

        double diskVal = cost/100 * DISCOUNT;

        cost = cost*dist;

        return BigDecimal.valueOf(cost - diskVal);
    }

    /**
     * Counting the cost for double order depending on cars cost and locations
     */
    public BigDecimal costForTwoCars(List<Car> cars, String loc_from, String loc_to) {
        BigDecimal costPerK = cars.get(0).getCost();

        costPerK.add(cars.get(1).getCost());

        return cost(costPerK, loc_from, loc_to);
    }

    /**
     * Counting the cost for double order depending on cars cost and locations with discount
     */
    public BigDecimal costWithDiscountForTwoCars(BigDecimal idealCost, List<Car> cars, String loc_from, String loc_to) {
        BigDecimal costPerK = cars.get(0).getCost();

        costPerK.add(cars.get(1).getCost());

        return costWithDiscount(idealCost, costPerK, loc_from, loc_to);
    }

}
