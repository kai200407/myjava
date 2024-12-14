package com.example.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class DeviceDAO {
    private static final String DB_URL = "jdbc:sqlite:myproject.db";

    static {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            String createTable = "CREATE TABLE IF NOT EXISTS devices (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "type TEXT NOT NULL," +
                    "status TEXT NOT NULL)";
            stmt.executeUpdate(createTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addDevice(String name, String type, String status) {
        String sql = "INSERT INTO devices (name, type, status) VALUES (?,?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setString(3, status);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDevice(int id, String name, String type, String status) {
        String sql = "UPDATE devices SET name=?, type=?, status=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setString(3, status);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDevice(int id) {
        String sql = "DELETE FROM devices WHERE id=?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String[]> searchDevices(String keyword, String typeFilter) {
        StringBuilder sql = new StringBuilder("SELECT id, name, type, status FROM devices WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (name LIKE ? OR type LIKE ? OR status LIKE ?)");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }

        if (typeFilter != null && !typeFilter.trim().isEmpty()) {
            sql.append(" AND type LIKE ?");
            params.add("%" + typeFilter + "%");
        }

        List<String[]> results = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String dId = String.valueOf(rs.getInt("id"));
                String dName = rs.getString("name");
                String dType = rs.getString("type");
                String dStatus = rs.getString("status");
                results.add(new String[] { dId, dName, dType, dStatus });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public Map<String, Integer> getStatusCount() {
        // 查询各状态数量
        String sql = "SELECT status, COUNT(*) AS cnt FROM devices GROUP BY status";
        Map<String, Integer> countMap = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String status = rs.getString("status");
                int count = rs.getInt("cnt");
                countMap.put(status, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countMap;
    }
}
