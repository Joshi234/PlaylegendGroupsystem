import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import org.joshi234.playlegendgroupsystem.database.DatabaseManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

public class DatabaseManagerTest {

  private static PostgreSQLContainer<?> container;

  @BeforeAll
  static void setUp() throws SQLException {

    // Start a PostgreSQL container
    container = new PostgreSQLContainer<>("postgres:latest");
    container.start();

    // Set the database connection properties
    System.setProperty("database.user", container.getUsername());
    System.setProperty("database.password", container.getPassword());
    System.setProperty("database.connection-string", container.getJdbcUrl());

    // Connect to the database
    DatabaseManager.connect(container.getUsername(), container.getPassword(), "group",
        container.getJdbcUrl());

  }

  @AfterAll
  static void tearDown() {
    // Close the connection and stop the container
    try {
      Connection connection = DatabaseManager.getConnection();
      if (connection != null) {
        connection.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    if (container != null) {
      container.stop();
    }
  }

  @Test
  void testConnection() throws SQLException {
    // Retrieve the connection object
    Connection connection = DatabaseManager.getConnection();

    // Assert that the connection is not null
    assertNotNull(connection);
  }
}
