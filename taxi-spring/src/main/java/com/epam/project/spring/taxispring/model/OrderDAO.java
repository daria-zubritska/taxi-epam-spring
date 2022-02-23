package com.epam.project.spring.taxispring.model;

import com.epam.project.spring.taxispring.database.DBManager;
import com.epam.project.spring.taxispring.model.entity.Order;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    private static OrderDAO instance = null;

    public static OrderDAO getInstance(){
        if(instance == null) {
            instance = new OrderDAO();
        }

        return instance;
    }

    public static final String SQL_ORDER_BY_ID = "SELECT * FROM orders WHERE id=?";
    public static final String SQL_CREATE_ORDER = "INSERT INTO orders(user_id, car_id, location_from, location_to, order_date, passengers, cost) VALUES(?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_GET_ORDERS = "SELECT * FROM orders LEFT JOIN cars c on c.car_id = orders.car_id LEFT JOIN users u on u.user_id = orders.user_id";
    public static final String SQL_GET_LOCATIONS = "SELECT * FROM locations";
    public static final String SQL_GET_LOCATION_ID = "SELECT location_id FROM locations WHERE location_name=?";
    public static final String SQL_GET_DISTANCE = "SELECT distance FROM distances WHERE location_1=? AND location_2=?";
    public static final String SQL_PAGINATION_ORDERS = "SELECT * FROM orders LEFT JOIN cars c on c.car_id = orders.car_id LEFT JOIN users u on u.user_id = orders.user_id LIMIT ?, ?";
    public static final String SQL_PAGINATION_ORDERS_WITH_USER = "SELECT * FROM orders LEFT JOIN cars c on c.car_id = orders.car_id LEFT JOIN users u on u.user_id = orders.user_id WHERE user_name=? LIMIT ?, ?";
    public static final String SQL_PAGINATION_ORDERS_WITH_DATE = "SELECT * FROM orders LEFT JOIN cars c on c.car_id = orders.car_id LEFT JOIN users u on u.user_id = orders.user_id WHERE order_date=? LIMIT ?, ?";
    public static final String SQL_ROWS_NUM = "SELECT COUNT(id) FROM orders";
    public static final String SQL_PAGINATION_ORDERS_WITH_USER_AND_DATE = "SELECT * FROM orders LEFT JOIN cars c on c.car_id = orders.car_id LEFT JOIN users u on u.user_id = orders.user_id WHERE order_date=? AND user_name=? LIMIT ?, ?";
    public static final String SQL_PAGINATION_ORDERS_WITH_USER_ROWS = "SELECT COUNT(id) FROM orders LEFT JOIN users u on u.user_id = orders.user_id WHERE user_name=?";
    public static final String SQL_PAGINATION_ORDERS_WITH_DATE_ROWS = "SELECT COUNT(id) FROM orders WHERE order_date=?";
    public static final String SQL_PAGINATION_ORDERS_WITH_USER_AND_DATE_ROWS = "SELECT COUNT(id) FROM orders LEFT JOIN users u on u.user_id = orders.user_id WHERE order_date=? AND user_name=?";

    public static final String SQL_PAGINATION_ORDERS_ORDERED_DATE = "SELECT * FROM orders LEFT JOIN cars c on c.car_id = orders.car_id LEFT JOIN users u on u.user_id = orders.user_id ORDER BY order_date LIMIT ?, ?";
    public static final String SQL_PAGINATION_ORDERS_WITH_USER_ORDERED_DATE = "SELECT * FROM orders LEFT JOIN cars c on c.car_id = orders.car_id LEFT JOIN users u on u.user_id = orders.user_id WHERE user_name=? ORDER BY order_date LIMIT ?, ?";
    public static final String SQL_PAGINATION_ORDERS_WITH_DATE_ORDERED_DATE = "SELECT * FROM orders LEFT JOIN cars c on c.car_id = orders.car_id LEFT JOIN users u on u.user_id = orders.user_id WHERE order_date=? ORDER BY order_date LIMIT ?, ?";
    public static final String SQL_PAGINATION_ORDERS_WITH_USER_AND_DATE_ORDERED_DATE = "SELECT * FROM orders LEFT JOIN cars c on c.car_id = orders.car_id LEFT JOIN users u on u.user_id = orders.user_id WHERE order_date=? AND user_name=? ORDER BY order_date LIMIT ?, ?";

    public static final String SQL_PAGINATION_ORDERS_ORDERED_COST = "SELECT * FROM orders LEFT JOIN cars c on c.car_id = orders.car_id LEFT JOIN users u on u.user_id = orders.user_id ORDER BY orders.cost LIMIT ?, ?";
    public static final String SQL_PAGINATION_ORDERS_WITH_USER_ORDERED_COST = "SELECT * FROM orders LEFT JOIN cars c on c.car_id = orders.car_id LEFT JOIN users u on u.user_id = orders.user_id WHERE user_name=? ORDER BY orders.cost LIMIT ?, ?";
    public static final String SQL_PAGINATION_ORDERS_WITH_DATE_ORDERED_COST = "SELECT * FROM orders LEFT JOIN cars c on c.car_id = orders.car_id LEFT JOIN users u on u.user_id = orders.user_id WHERE order_date=? ORDER BY orders.cost LIMIT ?, ?";
    public static final String SQL_PAGINATION_ORDERS_WITH_USER_AND_DATE_ORDERED_COST = "SELECT * FROM orders LEFT JOIN cars c on c.car_id = orders.car_id LEFT JOIN users u on u.user_id = orders.user_id WHERE order_date=? AND user_name=? ORDER BY orders.cost LIMIT ?, ?";

    public static final String FIELD_ID = "id";
    public static final String FIELD_USER_ID = "user_id";
    public static final String FIELD_CAR_ID = "car_id";
    public static final String FIELD_LOCATION_FROM = "location_from";
    public static final String FIELD_LOCATION_TO = "location_to";
    public static final String FIELD_ORDER_DATE = "order_date";
    public static final String FIELD_PASSENGERS = "passengers";
    public static final String FIELD_COST = "cost";

    public static final String FIELD_CAR_NAME = "car_name";
    public static final String FIELD_USER_NAME = "user_name";


    private static Order mapResultSet(ResultSet rs) {
        Order order = null;
        try {
            order = new Order();
            order.setId(rs.getInt(FIELD_ID));
            order.setUserId(rs.getInt(FIELD_USER_ID));
            order.setCarId(rs.getInt(FIELD_CAR_ID));
            order.setLocationFrom(rs.getString(FIELD_LOCATION_FROM));
            order.setLocationTo(rs.getString(FIELD_LOCATION_TO));
            order.setOrderDate(rs.getDate(FIELD_ORDER_DATE).toLocalDate());
            order.setPassengers(rs.getInt(FIELD_PASSENGERS));
            order.setCost(rs.getBigDecimal(FIELD_COST));
            order.setUserName(rs.getString(FIELD_USER_NAME));
            order.setCarName(rs.getString(FIELD_CAR_NAME));
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return order;
    }

    public boolean addOrder(int user_id, int car_id, String location_from, String location_to, LocalDate order_date, int passengers, BigDecimal cost) {
        boolean result = false;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_CREATE_ORDER)) {

            pst.setInt(1, user_id);
            pst.setInt(2, car_id);
            pst.setString(3, location_from);
            pst.setString(4, location_to);
            pst.setDate(5, Date.valueOf(order_date));

            pst.setInt(6, passengers);
            pst.setBigDecimal(7, cost);

            result = pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return result;
    }

    public List<Order> getOrdersNoFilter(int start, int recordsPerPage) {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_PAGINATION_ORDERS)) {
            pst.setInt(1, start);
            pst.setInt(2, recordsPerPage);
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return orders;
    }

    public List<Order> getOrdersNoFilterOrderedDate(int start, int recordsPerPage) {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_PAGINATION_ORDERS_ORDERED_DATE)) {
            pst.setInt(1, start);
            pst.setInt(2, recordsPerPage);
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return orders;
    }

    public List<Order> getOrdersNoFilterOrderedCost(int start, int recordsPerPage) {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_PAGINATION_ORDERS_ORDERED_COST)) {
            pst.setInt(1, start);
            pst.setInt(2, recordsPerPage);
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return orders;
    }

    public List<Order> getOrdersUserFilter(int start, int recordsPerPage, String userName) {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_PAGINATION_ORDERS_WITH_USER)) {
            pst.setString(1, userName);
            pst.setInt(2, start);
            pst.setInt(3, recordsPerPage);
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return orders;
    }

    public List<Order> getOrdersUserFilterOrderedDate(int start, int recordsPerPage, String userName) {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_PAGINATION_ORDERS_WITH_USER_ORDERED_DATE)) {
            pst.setString(1, userName);
            pst.setInt(2, start);
            pst.setInt(3, recordsPerPage);
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return orders;
    }

    public List<Order> getOrdersUserFilterOrderedCost(int start, int recordsPerPage, String userName) {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_PAGINATION_ORDERS_WITH_USER_ORDERED_COST)) {
            pst.setString(1, userName);
            pst.setInt(2, start);
            pst.setInt(3, recordsPerPage);
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return orders;
    }

    public List<Order> getOrdersDateFilter(int start, int recordsPerPage, String date) {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_PAGINATION_ORDERS_WITH_DATE)) {
            pst.setDate(1, Date.valueOf(LocalDate.parse(date)));
            pst.setInt(2, start);
            pst.setInt(3, recordsPerPage);
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return orders;
    }

    public List<Order> getOrdersDateFilterOrderedDate(int start, int recordsPerPage, String date) {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_PAGINATION_ORDERS_WITH_DATE_ORDERED_DATE)) {
            pst.setDate(1, Date.valueOf(LocalDate.parse(date)));
            pst.setInt(2, start);
            pst.setInt(3, recordsPerPage);
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return orders;
    }

    public List<Order> getOrdersDateFilterOrderedCost(int start, int recordsPerPage, String date) {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_PAGINATION_ORDERS_WITH_DATE_ORDERED_COST)) {
            pst.setDate(1, Date.valueOf(LocalDate.parse(date)));
            pst.setInt(2, start);
            pst.setInt(3, recordsPerPage);
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return orders;
    }

    public List<Order> getOrdersUserAndDateFilter(int start, int recordsPerPage, String userName, String date) {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_PAGINATION_ORDERS_WITH_USER_AND_DATE)) {
            pst.setDate(1, Date.valueOf(LocalDate.parse(date)));
            pst.setString(2, userName);
            pst.setInt(3, start);
            pst.setInt(4, recordsPerPage);
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return orders;
    }

    public List<Order> getOrdersUserAndDateFilterOrderedDate(int start, int recordsPerPage, String userName, String date) {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_PAGINATION_ORDERS_WITH_USER_AND_DATE_ORDERED_DATE)) {
            pst.setDate(1, Date.valueOf(LocalDate.parse(date)));
            pst.setString(2, userName);
            pst.setInt(3, start);
            pst.setInt(4, recordsPerPage);
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return orders;
    }

    public List<Order> getOrdersUserAndDateFilterOrderedCost(int start, int recordsPerPage, String userName, String date) {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_PAGINATION_ORDERS_WITH_USER_AND_DATE_ORDERED_COST)) {
            pst.setDate(1, Date.valueOf(LocalDate.parse(date)));
            pst.setString(2, userName);
            pst.setInt(3, start);
            pst.setInt(4, recordsPerPage);
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return orders;
    }

    public List<String> getAllLocations() {
        List<String> locations = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_LOCATIONS)) {
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    locations.add(rs.getString("location_name"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return locations;
    }

    private Integer getLocIdByName(String location){
        Integer locId = null;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_LOCATION_ID)) {
            pst.setString(1, location);
            try (ResultSet rs = pst.executeQuery()) {
                if(rs.next())
                    locId = Integer.valueOf(rs.getInt("location_id"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return locId;
    }

    public int getDistance(String loc_from, String loc_to){
        int dist = 1;

        int loc1Id = getLocIdByName(loc_from);
        int loc2Id = getLocIdByName(loc_to);

        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_DISTANCE)) {

            if(loc1Id <= loc2Id){
                pst.setInt(1, loc1Id);
                pst.setInt(2, loc2Id);
            }else{
                pst.setInt(1, loc2Id);
                pst.setInt(2, loc1Id);
            }

            try (ResultSet rs = pst.executeQuery()) {
                if(rs.next())
                    dist = rs.getInt("distance");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return dist;
    }

    public int getNumberOfRows() {
        int num = 0;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_ROWS_NUM)) {
            try (ResultSet rs = pst.executeQuery()) {
                if(rs.next())
                    num = rs.getInt("COUNT(id)");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return num;
    }

    public int getNumberOfRowsFilterUser(String userName) {
        int num = 0;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_PAGINATION_ORDERS_WITH_USER_ROWS)) {
            pst.setString(1, userName);
            try (ResultSet rs = pst.executeQuery()) {
                if(rs.next())
                    num = rs.getInt("COUNT(id)");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return num;
    }

    public int getNumberOfRowsFilterDate(String date) {
        int num = 0;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_PAGINATION_ORDERS_WITH_DATE_ROWS)) {
            pst.setDate(1, Date.valueOf(LocalDate.parse(date)));
            try (ResultSet rs = pst.executeQuery()) {
                if(rs.next())
                    num = rs.getInt("COUNT(id)");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return num;
    }

    public int getNumberOfRowsFilterDateUser(String date, String userName) {
        int num = 0;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_PAGINATION_ORDERS_WITH_USER_AND_DATE_ROWS)) {
            pst.setDate(1, Date.valueOf(LocalDate.parse(date)));
            pst.setString(2, userName);
            try (ResultSet rs = pst.executeQuery()) {
                if(rs.next())
                    num = rs.getInt("COUNT(id)");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return num;
    }




}
