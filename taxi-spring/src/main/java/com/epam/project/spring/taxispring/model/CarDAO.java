package com.epam.project.spring.taxispring.model;

import com.epam.project.spring.taxispring.database.DBManager;
import com.epam.project.spring.taxispring.model.entity.Car;
import com.epam.project.spring.taxispring.model.service.CarService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {

    private static CarDAO instance = null;

    public static CarDAO getInstance(){
        if(instance == null){
            instance = new CarDAO();
        }

        return instance;
    }

    public static final String SQL_GET_CAR_BY_ID = "SELECT * FROM cars LEFT JOIN status ON cars.status_id=status.status_id LEFT JOIN car_type ON cars.type_id = car_type.type_id WHERE car_id=?";
    public static final String SQL_GET_CAR_BY_TYPE = "SELECT * FROM cars LEFT JOIN status ON cars.status_id=status.status_id LEFT JOIN car_type ON cars.type_id = car_type.type_id WHERE type_name=? AND cars.status_id =?";
    public static final String SQL_GET_ALL_CARS = "SELECT * FROM cars LEFT JOIN status ON cars.status_id=status.status_id LEFT JOIN car_type ON cars.type_id = car_type.type_id";
    public static final String SQL_GET_CAR_BY_PASSENGERS = "SELECT * FROM cars LEFT JOIN status ON cars.status_id=status.status_id LEFT JOIN car_type ON cars.type_id = car_type.type_id WHERE passengers=? AND cars.status_id =?";
    public static final String SQL_UPDATE_STATUS = "UPDATE cars SET cars.status_id=? WHERE cars.car_id=?";
    public static final String SQL_GET_APPROPRIATE_CARS = "SELECT * FROM cars LEFT JOIN status ON cars.status_id=status.status_id LEFT JOIN car_type ON cars.type_id = car_type.type_id WHERE passengers=? AND cars.status_id =? AND type_name=?";
    public static final String SQL_GET_APPROPRIATE_CAR_COST = "SELECT cost FROM cars LEFT JOIN status ON cars.status_id=status.status_id LEFT JOIN car_type ON cars.type_id = car_type.type_id WHERE passengers=? AND type_name=?";

    public static final String SQL_GET_TWO_CARS_BY_TYPE = "SELECT x.car_id as x_id , y.car_id as y_id FROM cars x JOIN cars y ON y.car_id > x.car_id WHERE x.passengers + y.passengers =? AND x.type_id = ? AND y.type_id = ? AND x.status_id = 1 AND y.status_id = 1 LIMIT 1;";

    private static final String FIELD_ID = "car_id";
    private static final String FIELD_NAME = "car_name";
    private static final String FIELD_COST = "cost";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_TYPE = "type_name";
    private static final String FIELD_PASSENGERS = "passengers";

    private static Car mapResultSet(ResultSet rs) {
        Car car = null;
        try {
            car = new Car();
            car.setId(rs.getInt(FIELD_ID));
            car.setName(rs.getString(FIELD_NAME));
            car.setCost(rs.getBigDecimal(FIELD_COST));
            car.setStatus(Car.Status.valueOf(rs.getString(FIELD_STATUS).toUpperCase()));
            car.setCategory(Car.CarType.valueOf(rs.getString(FIELD_TYPE).toUpperCase()));
            car.setPassengers(rs.getInt(FIELD_PASSENGERS));

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return car;
    }

    public Car findCarById(long id) {
        Car car = null;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_CAR_BY_ID)) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next())
                    car = mapResultSet(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return car;
    }

    public List<Car> findTwoCarsByType(String type, int passengers) {
        List<Car> cars = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_TWO_CARS_BY_TYPE)) {
            pst.setInt(1, passengers);
            pst.setInt(2, CarService.getTypeId(type));
            pst.setInt(3, CarService.getTypeId(type));
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    cars.add(findCarById(rs.getInt("x_id")));
                    cars.add(findCarById(rs.getInt("y_id")));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return cars;

    }

    public Car findAppropriateCar(String type, int passengers) {
        Car car = null;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_APPROPRIATE_CARS)) {
            pst.setInt(1, passengers);
            pst.setInt(2, 1);
            pst.setString(3, type);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next())
                    car = mapResultSet(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return car;

    }

    public void updateStatus(long carId, int statusId) {
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_UPDATE_STATUS)) {
            pst.setInt(1, statusId);
            pst.setLong(2, carId);
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Car findCarByPassengers(int passengers) {
        Car car = null;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_CAR_BY_PASSENGERS)) {
            pst.setInt(1, passengers);
            pst.setInt(2, 1);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    car = mapResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return car;
    }

    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_ALL_CARS)) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    cars.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return cars;
    }


    public BigDecimal findAppropriateCarCost(String type, int passengers) {
        BigDecimal cost = null;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_APPROPRIATE_CAR_COST)) {
            pst.setInt(1, passengers);
            pst.setString(2, type);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next())
                    cost = rs.getBigDecimal(FIELD_COST);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return cost;
    }
}

