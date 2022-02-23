package com.epam.project.spring.taxispring.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBManager {

    private static final String USER = "root";
    private static final String PASSWORD = "64hogove";

    private static DBManager instance;

    private DBManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static DBManager getInstance() {
        if (instance == null)
            instance = new DBManager();

        return instance;
    }

    public Connection getConnection() throws SQLException {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", USER);
        connectionProps.put("password", PASSWORD);

        conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/taxiapp?autoReconnect=true&useSSL=false",
                USER, PASSWORD);

        System.out.println("Connected to database");
        return conn;
    }


}
