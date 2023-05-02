package control.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;

public abstract class ConnectionMaster {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "ARBUSER";
    private static final String PASSWORD = "1234";

    protected static Connection connection;

    static {
        try {
            Locale.setDefault(Locale.ENGLISH);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
