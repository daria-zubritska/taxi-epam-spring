package com.epam.project.spring.taxispring.model.entity;

import java.util.Objects;

public class User {

    private int id;
    private String name;
    private String email;
    private Role role;
    private String password;

    public User() {
    }

    public User(String name, String email, Role role, String password, int id) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.id = id;
    }

    public User(String name, String email, Role role, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ",pass = '" + password + '\'' +
                '}';
    }

    public boolean isValid() {
        return this != null;
    }

    public enum Role {
        ADMIN, USER;

        public boolean isUser() {
            return this == USER;
        }

        public boolean isAdmin() {
            return this == ADMIN;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
}
