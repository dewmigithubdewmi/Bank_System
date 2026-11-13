package com.sliit.bankapp.dao;

import com.sliit.bankapp.model.Payee;
import com.sliit.bankapp.util.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PayeeDAO {

    private final DBConnectionManager dbm;
    public PayeeDAO(DBConnectionManager dbm){ this.dbm = dbm; }

    /* ──────── CREATE ────────────────────────────────────── */
    public void insert(Payee p) throws Exception {
        String sql = """
            INSERT INTO payees(user_id,bill_type,identifier,acct_name,nick_name)
            VALUES (?,?,?,?,?)
            """;
        try (Connection c = dbm.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt   (1, p.getUserId());
            ps.setString(2, p.getBillType());
            ps.setString(3, p.getIdentifier());
            ps.setString(4, p.getAcctName());
            ps.setString(5, p.getNickName());
            ps.executeUpdate();
        }
    }

    /* ──────── UPDATE ────────────────────────────────────── */
    public void update(Payee p) throws Exception {
        String sql = """
            UPDATE payees
               SET bill_type  = ?,
                   identifier = ?,
                   acct_name  = ?,
                   nick_name  = ?
             WHERE id = ? AND user_id = ?
            """;
        try (Connection c=dbm.getConnection();
             PreparedStatement ps=c.prepareStatement(sql)) {

            ps.setString(1, p.getBillType());
            ps.setString(2, p.getIdentifier());
            ps.setString(3, p.getAcctName());
            ps.setString(4, p.getNickName());
            ps.setInt   (5, p.getId());
            ps.setInt   (6, p.getUserId());
            ps.executeUpdate();
        }
    }

    /* ──────── DELETE ────────────────────────────────────── */
    public void delete(int id, int userId) throws Exception {
        try (Connection c = dbm.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM payees WHERE id=? AND user_id=?")) {

            ps.setInt(1, id);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    /* ──────── READ LIST ─────────────────────────────────── */
    public List<Payee> findByUser(int uid) throws Exception {
        List<Payee> list = new ArrayList<>();
        try (Connection c = dbm.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT * FROM payees WHERE user_id=?")) {

            ps.setInt(1, uid);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    /* ──────── READ SINGLE ───────────────────────────────── */
    public Payee findByIdAndUser(int payeeId, int userId) throws Exception {
        try (Connection c = dbm.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT * FROM payees WHERE id=? AND user_id=?")) {

            ps.setInt(1, payeeId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /* (plain by-id helper) */
    public Payee findById(int id) throws Exception {
        try (Connection c = dbm.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT * FROM payees WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /* ──────── mapper ────────────────────────────────────── */
    private static Payee map(ResultSet rs) throws Exception {
        Payee p = new Payee();
        p.setId        (rs.getInt   ("id"));
        p.setUserId    (rs.getInt   ("user_id"));
        p.setBillType  (rs.getString("bill_type"));
        p.setIdentifier(rs.getString("identifier"));
        p.setAcctName  (rs.getString("acct_name"));
        p.setNickName  (rs.getString("nick_name"));
        return p;
    }
}
