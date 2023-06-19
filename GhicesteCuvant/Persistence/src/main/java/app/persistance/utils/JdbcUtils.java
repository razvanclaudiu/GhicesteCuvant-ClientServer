package app.persistance.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {

    private final Properties properties;

    private Connection connection = null;

    public JdbcUtils(Properties props) {
        this.properties = props;
    }

    public Connection getConnection() {
       try{
           if(connection == null || connection.isClosed())
               connection = startNewConnection();
       } catch(SQLException e) {
           System.out.println("Error DB " + e);
       }
       return connection;
    }

    private Connection startNewConnection() {
        String url = properties.getProperty("jdbc.url");
        String user = properties.getProperty("jdbc.user");
        String pass = properties.getProperty("jdbc.password");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            System.out.println("Error connecting to the database " + e);
        }
        return connection;
    }
}
