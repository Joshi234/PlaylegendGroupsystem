import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.joshi234.playlegendgroupsystem.database.DatabaseManager;
import org.joshi234.playlegendgroupsystem.database.group.Group;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

class GroupTest {

  private static PostgreSQLContainer<?> container;

  @AfterAll
  static void tearDown() throws SQLException {
    // Drop the group table
    try (Connection connection = DatabaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "DROP TABLE IF EXISTS \"group\"")) {
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
    // Create the group table
    Connection connection = DatabaseManager.getConnection();
    PreparedStatement statement = connection.prepareStatement(
        "CREATE TABLE \"group\" ("
            + "groupId SERIAL PRIMARY KEY,"
            + "name VARCHAR(255),"
            + "prefix VARCHAR(255),"
            + "description VARCHAR(255),"
            + "weight int"
            + ")");
    statement.executeUpdate();

  }

  @Test
  void testCreateGroup() {
    // Arrange
    Group group = new Group("Group1", "G1", "Group 1 Description", 1);

    // Act
    group.createGroup();

    group = new Group();
    group.loadGroupByName("Group1");

    // Assert
    assertEquals(1, group.getGroupId());
  }


  @Test
  void testLoadGroupByName() {
    // Arrange
    Group group = new Group("group1", "test", "test", 1);
    group.createGroup();
    // Act
    Group newGroup = new Group();
    newGroup.loadGroupByName(group.getName());

    // Assert
    assertEquals(newGroup.getName(), group.getName());
    assertEquals(newGroup.getPrefix(), group.getPrefix());
    assertEquals(newGroup.getDescription(), group.getDescription());
  }

  @Test
  void testUpdateGroup() {
    // Arrange
    Group group = new Group("Group1", "G1", "Group 1 Description", 1);
    group.createGroup();
    group.loadGroupByName(group.getName());
    // Act
    group.updateGroup("prefix", "G2", group.getGroupId());
    group.loadGroupByName(group.getName());

    // Assert
    assertEquals("Group1", group.getName());
    assertEquals("G2", group.getPrefix());
    assertEquals("Group 1 Description", group.getDescription());
    assertEquals(1, group.getWeight());
  }

  @Test
  void testGetGroupNames() {
    // Arrange
    Group group1 = new Group("Group1", "G1", "Group 1 Description", 1);
    group1.createGroup();

    Group group2 = new Group("Group2", "G2", "Group 2 Description", 2);
    group2.createGroup();

    // Act
    List<String> groupNames = group1.getGroupNames();

    // Assert
    assertEquals(2, groupNames.size());
    assertEquals("Group1", groupNames.get(0));
    assertEquals("Group2", groupNames.get(1));
  }

  @Test
  void testDeleteGroup() {
    // Arrange
    Group group = new Group("Group1", "G1", "Group 1 Description", 1);
    group.createGroup();

    // Act
    group.deleteGroup(group.getGroupId());

    // Assert
    assertThrows(RuntimeException.class, () -> group.loadGroup(group.getGroupId()));
  }
}
