package com.sliit.bankapp.dao;

import com.sliit.bankapp.model.Comment;
import com.sliit.bankapp.util.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

    private final DBConnectionManager dbm;
    public CommentDAO(DBConnectionManager dbm) {
        this.dbm = dbm;
    }

    /* ---------- CREATE ---------- */
    public void insert(Comment c) throws Exception {
        try (Connection conn = dbm.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO comments(ad_id,user_id,content) VALUES (?,?,?)",
                 Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt   (1, c.getAdId());
            ps.setInt   (2, c.getUserId());
            ps.setString(3, c.getContent());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setId(rs.getInt(1));
            }
        }
    }

    /* ---------- UPDATE (owner-checked) ---------- */
    public void update(Comment c) throws Exception {
        try (Connection conn = dbm.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "UPDATE comments SET content=? WHERE id=? AND user_id=?")) {

            ps.setString(1, c.getContent());
            ps.setInt   (2, c.getId());
            ps.setInt   (3, c.getUserId());
            ps.executeUpdate();
        }
    }

    /* ---------- DELETE (owner-checked) ---------- */
    public void delete(int id, int userId) throws Exception {
        try (Connection conn = dbm.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "DELETE FROM comments WHERE id=? AND user_id=?")) {

            ps.setInt(1, id);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    /* ---------- READ SINGLE (now includes author name) ---------- */
    public Comment findById(int id) throws Exception {
        String sql = """
            SELECT c.*, u.first_name, u.last_name
              FROM comments c
              JOIN users u ON u.id = c.user_id
             WHERE c.id = ?
            """;
        try (Connection conn = dbm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /* ---------- READ ALL FOR AN AD (already has author name) ---------- */
    public List<Comment> findByAd(int adId) throws Exception {
        List<Comment> list = new ArrayList<>();
        String sql = """
            SELECT c.*, u.first_name, u.last_name
              FROM comments c
              JOIN users u ON u.id = c.user_id
             WHERE c.ad_id = ?
          ORDER BY c.created_at DESC
            """;
        try (Connection conn = dbm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, adId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    /* ─── ResultSet → Comment mapper ───────────────────────── */
    private static Comment map(ResultSet rs) throws Exception {
        Comment c = new Comment();
        c.setId        (rs.getInt   ("id"));
        c.setAdId      (rs.getInt   ("ad_id"));
        c.setUserId    (rs.getInt   ("user_id"));
        c.setContent   (rs.getString("content"));
        c.setAuthor    (rs.getString("first_name") + " " + rs.getString("last_name"));
        c.setCreatedAt (rs.getTimestamp("created_at"));
        return c;
    }
}
