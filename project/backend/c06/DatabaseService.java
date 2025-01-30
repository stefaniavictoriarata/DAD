import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseService {
    private static final String URL = "jdbc:mysql://C06_HOST:3306/images";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static void saveImage(String filename, byte[] imageData) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO images (filename, data) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, filename);
            pstmt.setBytes(2, imageData);
            pstmt.executeUpdate();
            System.out.println("Image stored in database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
