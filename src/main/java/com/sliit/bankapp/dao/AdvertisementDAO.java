package com.sliit.bankapp.dao;

import com.sliit.bankapp.model.Advertisement;
import com.sliit.bankapp.util.DBConnectionManager;
import java.sql.*;
import java.util.*;

public class AdvertisementDAO {

    private final DBConnectionManager dbm;
    public AdvertisementDAO(DBConnectionManager dbm){ this.dbm = dbm; }

    /* ---------- CREATE ---------- */
    public void insert(Advertisement ad) throws Exception {
        String sql = """
          INSERT INTO advertisements(admin_id,title,rules,description)
          VALUES (?,?,?,?)
          """;
        try (Connection c = dbm.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt   (1, ad.getAdminId());
            ps.setString(2, ad.getTitle());
            ps.setString(3, ad.getRules());
            ps.setString(4, ad.getDescription());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) ad.setId(rs.getInt(1));
            }
        }
    }
    
    public void update(Advertisement ad) throws Exception {
        String sql = """
           UPDATE advertisements
              SET title = ?, rules = ?, description = ?
            WHERE id = ? AND admin_id = ?
           """;
        try (Connection c = dbm.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ad.getTitle());
            ps.setString(2, ad.getRules());
            ps.setString(3, ad.getDescription());
            ps.setInt   (4, ad.getId());
            ps.setInt   (5, ad.getAdminId());
            ps.executeUpdate();
        }
    }

    /* ---------- DELETE ---------- */
    public void delete(int id, int adminId) throws Exception {
        try (Connection c = dbm.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM advertisements WHERE id=? AND admin_id=?")) {
            ps.setInt(1, id);
            ps.setInt(2, adminId);
            ps.executeUpdate();
        }
    }

    /* ---------- READ helpers ---------- */
    public Advertisement findByIdAndAdmin(int id,int adminId) throws Exception {
        try (Connection c=dbm.getConnection();
             PreparedStatement ps=c.prepareStatement(
                 "SELECT * FROM advertisements WHERE id=? AND admin_id=?")) {
            ps.setInt(1,id); ps.setInt(2,adminId);
            try(ResultSet rs=ps.executeQuery()){ return rs.next()? map(rs):null; }
        }
    }
    public List<Advertisement> findByAdmin(int adminId) throws Exception{
        List<Advertisement> list=new ArrayList<>();
        try(Connection c=dbm.getConnection();
            PreparedStatement ps=c.prepareStatement(
                "SELECT * FROM advertisements WHERE admin_id=? ORDER BY created_at DESC")){
            ps.setInt(1,adminId);
            try(ResultSet rs=ps.executeQuery()){ while(rs.next()) list.add(map(rs)); }
        }
        return list;
    }
    
    /* ---------- PUBLIC READ (by id) ---------- */
    public Advertisement findById(int id) throws Exception {
        try (Connection c = dbm.getConnection();
             PreparedStatement ps =
                 c.prepareStatement("SELECT * FROM advertisements WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }


    
    /* ---------- READ ALL ---------- */
    public List<Advertisement> findAll() throws Exception{
        List<Advertisement> list = new ArrayList<>();
        try (Connection c = dbm.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(
                 "SELECT * FROM advertisements ORDER BY created_at DESC")) {

            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    /* helper mapper */
    private static Advertisement map(ResultSet rs) throws Exception{
        Advertisement a = new Advertisement();
        a.setId         (rs.getInt   ("id"));
        a.setAdminId    (rs.getInt   ("admin_id"));
        a.setTitle      (rs.getString("title"));
        a.setRules      (rs.getString("rules"));
        a.setDescription(rs.getString("description"));
        return a;
    }
}
