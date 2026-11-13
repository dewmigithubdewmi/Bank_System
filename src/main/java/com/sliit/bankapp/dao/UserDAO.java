package com.sliit.bankapp.dao;

import com.sliit.bankapp.model.User;
import com.sliit.bankapp.util.DBConnectionManager;

import java.sql.*;

public class UserDAO {
    private final DBConnectionManager dbm;
    public UserDAO(DBConnectionManager dbm) { this.dbm = dbm; }

    public void insert(User u) throws Exception {
        String sql =
            "INSERT INTO users(first_name,last_name,email,password,role) VALUES (?,?,?,?,?)";
        try (Connection c = dbm.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getFirstName());
            ps.setString(2, u.getLastName());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getPassword());
            ps.setString(5, u.getRole());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) u.setId(rs.getInt(1)); }
        }
    }

    public User findByEmailAndPassword(String email, String pw) throws Exception {
        try (Connection c = dbm.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT * FROM users WHERE email=? AND password=?")) {
            ps.setString(1, email);
            ps.setString(2, pw);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        }
    }

    public User findById(int id) throws Exception {
        try (Connection c = dbm.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM users WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        }
    }

    private static User map(ResultSet rs) throws Exception {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setFirstName(rs.getString("first_name"));
        u.setLastName(rs.getString("last_name"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setRole(rs.getString("role"));
        return u;
    }
}
