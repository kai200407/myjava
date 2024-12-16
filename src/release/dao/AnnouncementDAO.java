package release.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementDAO {
    private static final String DB_URL = "jdbc:sqlite:myproject.db";

    static {
        // 确保表已创建
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            String createTable = "CREATE TABLE IF NOT EXISTS announcements (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL," +
                    "content TEXT NOT NULL," +
                    "publish_date TEXT NOT NULL)";
            stmt.executeUpdate(createTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addAnnouncement(String title, String content, String publishDate) {
        String sql = "INSERT INTO announcements (title, content, publish_date) VALUES (?,?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setString(3, publishDate);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String[]> searchAnnouncements(String keyword, String date) {
        StringBuilder sql = new StringBuilder("SELECT title, content, publish_date FROM announcements WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (title LIKE ? OR content LIKE ?)");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }

        if (date != null && !date.trim().isEmpty()) {
            // 模糊查询日期，用户输入的日期加上 "%" 实现前缀匹配
            sql.append(" AND publish_date LIKE ?");
            params.add(date.trim() + "%");
        }

        List<String[]> results = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String t = rs.getString("title");
                String c = rs.getString("content");
                String d = rs.getString("publish_date");
                results.add(new String[] { t, c, d });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

}
