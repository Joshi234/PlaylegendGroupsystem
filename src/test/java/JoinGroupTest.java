import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.joshi234.playlegendgroupsystem.database.DatabaseManager;
import org.joshi234.playlegendgroupsystem.database.group.Group;
import org.joshi234.playlegendgroupsystem.database.group.JoinGroup;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

class JoinGroupTest {

  private static PostgreSQLContainer<?> container;

  @AfterAll
  static void tearDown() throws SQLException {
    // Drop the joingroup and group table
    try (Connection connection = DatabaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "DROP TABLE IF EXISTS joingroup,\"group\"")) {
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

    // Create the joingroup table
    Connection connection = DatabaseManager.getConnection();
    PreparedStatement statement = connection.prepareStatement(
        "CREATE TABLE joingroup ("
            + "uuid VARCHAR(255),"
            + "groupid INT,"
            + "joinuntil TIMESTAMP"
            + ")");
    statement.executeUpdate();

    // Create the group table
    statement = connection.prepareStatement(
        "CREATE TABLE \"group\" ("
            + "groupId SERIAL PRIMARY KEY,"
            + "name VARCHAR(255),"
            + "prefix VARCHAR(255),"
            + "description VARCHAR(255),"
            + "weight int"
            + ")");
    statement.executeUpdate();

    Group group = new Group("group1", "test", "test", 1);
    group.createGroup();
  }

  @Test
  void testGetPlayerGroups() {
    // Arrange
    JoinGroup joinGroup = new JoinGroup();
    String uuid = "player1";
    joinGroup.setUuid(uuid);
    joinGroup.setGroupId(1);
    joinGroup.setJoinUntil(null);
    joinGroup.joinGroup();

    // Act
    List<Group> playerGroups = joinGroup.getPlayerGroups(uuid);

    // Assert
    assertEquals(1, playerGroups.size());
    assertEquals(0, playerGroups.get(0).getGroupId());
  }

  @Test
  void testJoinGroup() {
    // Arrange
    JoinGroup joinGroup = new JoinGroup();
    String uuid = "player1";
    joinGroup.setUuid(uuid);
    joinGroup.setGroupId(1);
    joinGroup.setJoinUntil(null);

    // Act
    joinGroup.joinGroup();

    // Assert
    List<Group> playerGroups = joinGroup.getPlayerGroups(uuid);
    assertEquals(1, playerGroups.size());
    assertEquals(0, playerGroups.get(0).getGroupId());
  }

  @Test
  void testGetPrefix() {
    // Arrange
    JoinGroup joinGroup = new JoinGroup();
    String uuid = "player1";
    joinGroup.setUuid(uuid);
    joinGroup.setGroupId(1);
    joinGroup.setJoinUntil(null);
    joinGroup.joinGroup();

    // Act
    String prefix = joinGroup.getPrefix(uuid);

    // Assert
    assertEquals("test", prefix);
  }

  @Test
  void testGetPrefixWithPlayerJoined() {
    // Arrange
    JoinGroup joinGroup = new JoinGroup();
    String uuid = "player1";
    joinGroup.setUuid(uuid);
    joinGroup.setGroupId(1);
    joinGroup.setJoinUntil(null);
    joinGroup.joinGroup();

    // Act
    String prefix = joinGroup.getPrefix(uuid, true);

    // Assert
    assertEquals("test", prefix);
  }

  @Test
  void testDeleteJoinGroup() {
    // Arrange
    JoinGroup joinGroup = new JoinGroup();
    String uuid = "player1";
    joinGroup.setUuid(uuid);
    joinGroup.setGroupId(1);
    joinGroup.setJoinUntil(null);
    joinGroup.joinGroup();

    // Act
    joinGroup.deleteJoinGroup(uuid, 1);

    // Assert
    List<Group> playerGroups = joinGroup.getPlayerGroups(uuid);
    assertEquals(0, playerGroups.size());
  }

  @Test
  void testJoinGroupByName() {

    // Arrange
    JoinGroup joinGroup = new JoinGroup();
    String uuid = "player1";
    String groupName = "group1";
    joinGroup.joinGroupByName(groupName, uuid);

    // Act
    List<Group> playerGroups = joinGroup.getPlayerGroups(uuid);

    // Assert
    assertEquals(1, playerGroups.size());
    assertEquals(groupName, playerGroups.get(0).getName());
  }
}
