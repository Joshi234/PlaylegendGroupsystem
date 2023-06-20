import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.joshi234.playlegendgroupsystem.database.DatabaseManager;
import org.joshi234.playlegendgroupsystem.database.group.Language;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

class LanguageTest {

  private static PostgreSQLContainer<?> container;
  private static Connection connection;

  @AfterAll
  static void tearDown() throws SQLException {
    // Drop the language and player tables
    try (Connection connection = DatabaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "DROP TABLE IF EXISTS language, player")) {
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

    // Create the language table
    connection = DatabaseManager.getConnection();
    PreparedStatement statement = connection.prepareStatement(
        "CREATE TABLE language ("
            + "languageId SERIAL PRIMARY KEY,"
            + "name VARCHAR(255),"
            + "code VARCHAR(255)"
            + ")");
    statement.executeUpdate();

    // Create the player table
    statement = connection.prepareStatement(
        "CREATE TABLE player ("
            + "uuid VARCHAR(255) PRIMARY KEY,"
            + "languageId INT REFERENCES language(languageId)"
            + ")");
    statement.executeUpdate();

    // Insert sample languages
    insertSampleLanguages();
  }

  @AfterEach
  void clearTables() throws SQLException {
    PreparedStatement statement = connection.prepareStatement("DELETE FROM player");
    statement.executeUpdate();

    // Clear the tables after each test
    statement = connection.prepareStatement("DELETE FROM language");
    statement.executeUpdate();


  }

  private void insertSampleLanguages() throws SQLException {
    // Insert sample languages into the language table
    PreparedStatement statement = connection.prepareStatement(
        "INSERT INTO language (name, code) VALUES (?, ?)");

    statement.setString(1, "English");
    statement.setString(2, "en");
    statement.executeUpdate();

    statement.setString(1, "French");
    statement.setString(2, "fr");
    statement.executeUpdate();

    statement.setString(1, "German");
    statement.setString(2, "de");
    statement.executeUpdate();

    statement.setString(1, "Spanish");
    statement.setString(2, "es");
    statement.executeUpdate();
  }

  @Test
  void testGetAvailableLanguages() {
    // Arrange
    Language language = new Language();

    // Act
    List<Language> availableLanguages = language.getAvailableLanguages();

    // Assert
    assertEquals(4, availableLanguages.size());
    assertEquals("en", availableLanguages.get(0).getCode());
    assertEquals("fr", availableLanguages.get(1).getCode());
    assertEquals("de", availableLanguages.get(2).getCode());
    assertEquals("es", availableLanguages.get(3).getCode());
  }

  @Test
  void testLoadLanguageByPlayer() {
    // Arrange
    Language language = new Language();
    language.setCode("en");
    language.setLanguageId(1);
    language.setName("English");

    // Create a sample player
    PreparedStatement statement;
    try {
      statement = connection.prepareStatement(
          "INSERT INTO player (uuid, languageId) VALUES (?, ?)");
      statement.setString(1, "player1");
      statement.setInt(2, language.getLanguageId());
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // Act
    String languageCode = language.loadLanguageByPlayer("player1");

    // Assert
    assertEquals("en", languageCode);
  }

  @Test
  void testSetPlayerLanguage() {
    // Arrange
    Language language = new Language();
    language.setCode("en");
    language.setLanguageId(1);
    language.setName("English");

    // Create a sample player
    PreparedStatement statement;
    try {
      statement = connection.prepareStatement(
          "INSERT INTO player (uuid, languageId) VALUES (?, ?)");
      statement.setString(1, "player1");
      statement.setInt(2, language.getLanguageId());
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // Act
    language.setPlayerLanguage("player1", 1);

    // Assert
    String languageCode = language.loadLanguageByPlayer("player1");
    assertEquals("en", languageCode);
  }
}
