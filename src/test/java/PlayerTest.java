import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import org.joshi234.playlegendgroupsystem.database.DatabaseManager;
import org.joshi234.playlegendgroupsystem.database.group.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

class PlayerTest {

  private static PostgreSQLContainer<?> container;

  @AfterAll
  static void tearDown() throws SQLException {
    // Drop the player table
    try (Connection connection = DatabaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "DROP TABLE IF EXISTS player")) {
      statement.executeUpdate();
    }

    // Stop the container
    if (container != null) {
      container.stop();
    }
  }

  @BeforeEach
  void setUp() throws SQLException {
    // Start a PostgreSQL container
    container = new PostgreSQLContainer<>("postgres:latest");
    container.start();

    // Set the database connection properties
    System.setProperty("database.user", container.getUsername());
    System.setProperty("database.password", container.getPassword());
    System.setProperty("database.connection-string", container.getJdbcUrl());
    DatabaseManager.connect(container.getUsername(), container.getPassword(), "group",
        container.getJdbcUrl());
    DatabaseManager.createGroupSchema();

    // Create the player table
    Connection connection = DatabaseManager.getConnection();
    PreparedStatement statement = connection.prepareStatement(
        "CREATE TABLE player ("
            + "uuid VARCHAR(255) PRIMARY KEY,"
            + "name VARCHAR(255)"
            + ")");
    statement.executeUpdate();
  }

  @Test
  void testPlayerConstructor_NewPlayer() {
    // Arrange
    String uuid = UUID.randomUUID().toString();
    String name = "Test Player";

    // Act
    Player player = new Player(uuid, name);

    // Assert
    assertEquals(uuid, player.getUuid());
    assertEquals(name, player.getName());
  }

  @Test
  void testPlayerConstructor_ExistingPlayer() {
    // Arrange
    String uuid = UUID.randomUUID().toString();
    String name = "Test Player";

    // Create a player
    try {
      PreparedStatement statement = DatabaseManager.getConnection()
          .prepareStatement("INSERT INTO player (uuid, name) VALUES (?, ?)");
      statement.setString(1, uuid);
      statement.setString(2, name);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // Act
    Player player = new Player(uuid, "New Name");

    // Assert
    assertEquals(uuid, player.getUuid());
    assertNotEquals("New Name", player.getName());
  }

  @Test
  void testLoadByName() {
    // Arrange
    String uuid = UUID.randomUUID().toString();
    String name = "Test Player";

    // Create a player
    try {
      PreparedStatement statement = DatabaseManager.getConnection()
          .prepareStatement("INSERT INTO player (uuid, name) VALUES (?, ?)");
      statement.setString(1, uuid);
      statement.setString(2, name);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // Act
    Player player = new Player();
    player.loadByName(name);

    // Assert
    assertEquals(uuid, player.getUuid());
    assertEquals(name, player.getName());
  }

  @Test
  void testUpdateUserName() {
    // Arrange
    String uuid = UUID.randomUUID().toString();
    String name = "Test Player";

    // Create a player
    try {
      PreparedStatement statement = DatabaseManager.getConnection()
          .prepareStatement("INSERT INTO player (uuid, name) VALUES (?, ?)");
      statement.setString(1, uuid);
      statement.setString(2, name);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // Create a Player object
    Player player = new Player(uuid, name);

    // Act
    player.setName("New Name");

    // Assert
    assertEquals(uuid, player.getUuid());
    assertEquals("New Name", player.getName());
  }
}
