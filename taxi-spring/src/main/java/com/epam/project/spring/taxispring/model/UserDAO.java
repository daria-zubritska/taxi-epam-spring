package com.epam.project.spring.taxispring.model;

import com.epam.project.spring.taxispring.database.DBManager;
import com.epam.project.spring.taxispring.model.entity.User;
import com.epam.project.spring.taxispring.utils.Security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private static UserDAO instance = null;

    public static UserDAO getInstance(){
        if(instance == null){
            instance = new UserDAO();
        }

        return instance;
    }

    public static final String SQL_GET_USER_BY_ID = "SELECT * FROM users LEFT JOIN roles ON users.role_id=roles.role_id WHERE user_id=?";
    public static final String SQL_GET_USER_BY_EMAIL = "SELECT * FROM users LEFT JOIN roles ON users.role_id = roles.role_id WHERE email=?";
    public static final String SQL_GET_ALL_USERS = "SELECT * FROM users LEFT JOIN roles ON users.role_id=roles.role_id";
    public static final String SQL_ADD_USER = "INSERT INTO users(email,password,user_name,role_id)VALUES(?, ?, ?, (SELECT role_id FROM roles WHERE role_name=? LIMIT 1))";
    public static final String SQL_GET_ROLE_AMOUNT = "SELECT COUNT(DISTINCT users.role_id) FROM users";

    private static final String FIELD_ID = "user_id";
    private static final String FIELD_NAME = "user_name";
    private static final String FIELD_EMAIl = "email";
    private static final String FIELD_PASSWORD = "password";
    private static final String FIELD_ROLE_NAME = "role_name";

    public static User mapResultSet(ResultSet rs) {
        User user = null;
        try {
            user = new User();
            user.setId(rs.getInt(FIELD_ID));
            user.setName(rs.getString(FIELD_NAME));
            user.setEmail(rs.getString(FIELD_EMAIl));
            user.setPassword(rs.getString(FIELD_PASSWORD));
            user.setRole(User.Role.valueOf(rs.getString(FIELD_ROLE_NAME).toUpperCase()));
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return user;
    }

    public User findUserById(long id) {
        User user = null;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_USER_BY_ID)) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if(rs.next())
                    user = mapResultSet(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return user;
    }

    public User findUserByEmail(String email) {
        User user = null;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_USER_BY_EMAIL)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                if(rs.next())
                    user = mapResultSet(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return user;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_ALL_USERS)) {
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return users;
    }

    public boolean addUser(String email, String password, String name) {
        boolean result = false;
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_ADD_USER)) {
            pst.setString(1, email);
            pst.setString(2, password);
            pst.setString(3, name);
            pst.setString(4, "USER");

            result = pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public User checkUser(User user) {

        User dbUser = findUserByEmail(user.getEmail());
        String hashedPass = null;

        try {
            hashedPass = Security.hashPassword(user.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(dbUser.getPassword().equals(hashedPass)) {
            return dbUser;
        }
        else{
            return null;
        }
    }

    public int getAllRoles() {
        int roleCount = 0;

        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_GET_ROLE_AMOUNT)) {
            try(ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    roleCount = rs.getInt("COUNT(DISTINCT users.role_id)");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return roleCount;
    }

}
