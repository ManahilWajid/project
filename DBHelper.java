import java.sql.*;

public class DBHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/rps__game?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "lihanam@20";  // <-- change this

    public static void saveResult(String player, String result) {
        int score = switch (result) {
            case "Win" -> 3;
            case "Draw" -> 1;
            default -> 0;
        };

        String insertResultSQL = "INSERT INTO results (player, result, score) VALUES (?, ?, ?)";
        String updateScoreSQL = "INSERT INTO scores (player, total_score) VALUES (?, ?) " +
                                "ON DUPLICATE KEY UPDATE total_score = total_score + ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);

            try (PreparedStatement psInsert = conn.prepareStatement(insertResultSQL);
                 PreparedStatement psUpdate = conn.prepareStatement(updateScoreSQL)) {

                psInsert.setString(1, player);
                psInsert.setString(2, result);
                psInsert.setInt(3, score);
                psInsert.executeUpdate();

                psUpdate.setString(1, player);
                psUpdate.setInt(2, score);
                psUpdate.setInt(3, score);
                psUpdate.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getCountByResult(String player, String result) {
        String sql = "SELECT COUNT(*) FROM results WHERE player = ? AND result = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, player);
            ps.setString(2, result);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getTotalScore(String player) {
        String sql = "SELECT total_score FROM scores WHERE player = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, player);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_score");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
