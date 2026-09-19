package home.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import home.model.ConnectionModel;

public final class Connector {

    public static Connection getConnection(ConnectionModel connModel)
            throws SQLException {
        String url = connModel.getUrl();
        Properties props = connModel.getConnectionProperties(false);
        String jdbcDriver = connModel.getDriver();
        return getConnection(url, props, jdbcDriver);
    }

    public static Connection getConnection(String url, Properties props, String jdbcDriver)
            throws SQLException {
        try {
            Class.forName(jdbcDriver);

            // Driver driver = (Driver) Class.forName(jdbcDriver).newInstance();
            // DriverManager.registerDriver(driver);

            Connection conn = DriverManager.getConnection(url, props);

            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            conn.setAutoCommit(true);

            return conn;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver class not found.", e);
        } catch (SQLException e) {
            throw new SQLException("Error while connecting to the database. %s"
                    .formatted(e.getMessage()), e);
        }
    }

    private Connector() {
    }
}
