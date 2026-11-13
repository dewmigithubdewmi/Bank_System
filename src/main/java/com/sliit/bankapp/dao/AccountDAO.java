package com.sliit.bankapp.dao;

import com.sliit.bankapp.model.Account;
import com.sliit.bankapp.util.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    private final DBConnectionManager dbm;

    public AccountDAO(DBConnectionManager dbm) { this.dbm = dbm; }

    /* ───────────── INSERT ─────────────────────────────────── */

    /** Insert a brand-new account (user_id may be null for un-linked ones). */
    public void insert(Account a) throws Exception {
        String sql = """
            INSERT INTO accounts(user_id,account_number,pin,full_name,nic,address,mobile,balance)
            VALUES (?,?,?,?,?,?,?,?)
            """;
        try (Connection c = dbm.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (a.getUserId() > 0) ps.setInt(1, a.getUserId());
            else                   ps.setNull(1, Types.INTEGER);

            ps.setString(2, a.getAccountNumber());
            ps.setString(3, a.getPin());
            ps.setString(4, a.getFullName());
            ps.setString(5, a.getNic());
            ps.setString(6, a.getAddress());
            ps.setString(7, a.getMobile());
            ps.setDouble(8, a.getBalance());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) a.setId(rs.getInt(1));
            }
        }
    }

    /* ───────────── READ QUERIES ───────────────────────────── */

    /** All accounts currently linked to one user. */
    public List<Account> findByUser(int userId) throws Exception {
        List<Account> list = new ArrayList<>();
        try (Connection c = dbm.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT * FROM accounts WHERE user_id=?")) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    /** Lookup by number **and** PIN (used when adding an existing account). */
    public Account findByNumberAndPin(String number, String pin) throws Exception {
        String sql = "SELECT * FROM accounts WHERE account_number=? AND pin=?";
        try (Connection c = dbm.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, number);
            ps.setString(2, pin);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /** Primary-key lookup. */
    public Account findById(int id) throws Exception {
        try (Connection c = dbm.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT * FROM accounts WHERE id=?")) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /** Lookup by account number only. */
    public Account findByNumber(String accNo) throws Exception {
        try (Connection c = dbm.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT * FROM accounts WHERE account_number=?")) {

            ps.setString(1, accNo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /* ───────────── LINKS / UNLINKS ───────────────────────── */

    public void linkToUser(int accountId, int userId) throws Exception {
        try (Connection c = dbm.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE accounts SET user_id=? WHERE id=?")) {

            ps.setInt(1, userId);
            ps.setInt(2, accountId);
            ps.executeUpdate();
        }
    }

    public void unlink(int accountId) throws Exception {
        try (Connection c = dbm.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE accounts SET user_id=NULL WHERE id=?")) {

            ps.setInt(1, accountId);
            ps.executeUpdate();
        }
    }

    /* ───────────── FULL TRANSFER (from → to) ─────────────── */

    /**
     * Atomically move money from one account (by id) to another (by number).
     */
    public void transfer(int fromId, String toNumber, double amount) throws Exception {
        try (Connection c = dbm.getConnection()) {
            c.setAutoCommit(false);

            Account from = findById(fromId);
            Account to   = findByNumber(toNumber);
            if (from == null) throw new Exception("Source account not found");
            if (to   == null) throw new Exception("Destination account not found");
            if (from.getBalance() < amount) throw new Exception("Insufficient funds");

            updateBalance(from.getId(), from.getBalance() - amount, c);
            updateBalance(to.getId(),   to.getBalance()   + amount, c);

            c.commit();
        }
    }

    /* ───────────── DEBIT ONLY  (for bill-pay) ────────────── */
    /*                       ↓↓↓   NEW   ↓↓↓                  */

    /**
     * Debit the given amount from one account.
     * Used by bill-payment; rolls back if balance is insufficient.
     */
    public void debit(int id, double amount) throws Exception {
        try (Connection c = dbm.getConnection()) {
            c.setAutoCommit(false);

            Account a = findById(id);
            if (a == null)          throw new Exception("Account not found");
            if (a.getBalance() < amount)
                throw new Exception("Insufficient funds");

            updateBalance(id, a.getBalance() - amount, c);
            c.commit();
        }
    }

    /* ───────────── UPDATE DETAILS ─────────────────────────── */

    public void updateDetails(int id,
                              String nic,
                              String address,
                              String mobile,
                              double balance) throws Exception {
        String sql = """
            UPDATE accounts
               SET nic     = ?,
                   address = ?,
                   mobile  = ?,
                   balance = ?
             WHERE id = ?
            """;
        try (Connection c = dbm.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nic);
            ps.setString(2, address);
            ps.setString(3, mobile);
            ps.setDouble(4, balance);
            ps.setInt   (5, id);
            ps.executeUpdate();
        }
    }

    /* ───────────── DELETE ─────────────────────────────────── */

    public void delete(int id) throws Exception {
        try (Connection c = dbm.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM accounts WHERE id=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /* ───────────── INTERNAL HELPERS ───────────────────────── */

    /** Update *only* the balance – must be inside a transaction. */
    private void updateBalance(int id, double newBal, Connection c) throws Exception {
        try (PreparedStatement ps =
                 c.prepareStatement("UPDATE accounts SET balance=? WHERE id=?")) {
            ps.setDouble(1, newBal);
            ps.setInt   (2, id);
            ps.executeUpdate();
        }
    }

    /** ResultSet → Account mapper. */
    private static Account map(ResultSet rs) throws Exception {
        Account a = new Account();
        a.setId           (rs.getInt   ("id"));
        a.setUserId       (rs.getInt   ("user_id"));   // 0 if NULL
        a.setAccountNumber(rs.getString("account_number"));
        a.setPin          (rs.getString("pin"));
        a.setFullName     (rs.getString("full_name"));
        a.setNic          (rs.getString("nic"));
        a.setAddress      (rs.getString("address"));
        a.setMobile       (rs.getString("mobile"));
        a.setBalance      (rs.getDouble("balance"));
        return a;
    }
}
