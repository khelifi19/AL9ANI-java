package utils.user;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataUtils {
    private static final  String url = "jdbc:mysql://localhost:3306/al9anij";
    private static final  String user ="root";
    private static final  String password ="";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
