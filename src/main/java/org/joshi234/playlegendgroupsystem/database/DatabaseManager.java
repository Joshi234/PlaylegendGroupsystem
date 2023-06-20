package org.joshi234.playlegendgroupsystem.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * The DatabaseManager class handles the connection to the database. It provides methods to connect
 * to the database and retrieve the connection object.
 */
public class DatabaseManager {

  private static Connection conn;

  /**
   * Establishes a connection to the database.
   *
   * @throws SQLException if an SQL exception occurs during the connection
   */
  public static void connect(String user, String password, String currentSchema,
      String connectionString)
      throws SQLException {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    Properties props = new Properties();
    props.setProperty("user", user);
    props.setProperty("password", password);
    props.setProperty("currentSchema", currentSchema);

    conn = DriverManager.getConnection(connectionString,
        props);
  }

  /**
   * Retrieves the connection object.
   *
   * @return the connection object
   */
  public static Connection getConnection() {
    return conn;
  }

  public static void createGroupSchema() throws SQLException {
    String sql = "CREATE SCHEMA IF NOT EXISTS \"group\"";

    try (Statement statement = conn.createStatement()) {
      statement.executeUpdate(sql);
    }
  }
}
