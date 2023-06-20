/**
 * The Player class represents a player in the PlayLegend Group System. It provides methods to
 * retrieve and update player information from the database.
 */
package org.joshi234.playlegendgroupsystem.database.group;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import org.joshi234.playlegendgroupsystem.Configuration;
import org.joshi234.playlegendgroupsystem.database.DatabaseManager;

public class Player {

  private String uuid;
  private String name;

  /**
   * Constructs a Player object with the specified UUID and name. If the player does not exist in
   * the database, it creates a new entry. If the player already exists, it updates the player's
   * name if necessary.
   *
   * @param uuid The UUID of the player.
   * @param name The name of the player.
   */
  public Player(String uuid, String name) {
    try {
      this.uuid = uuid;

      PreparedStatement statement = DatabaseManager.getConnection()
          .prepareStatement("SELECT * FROM player WHERE uuid = ?");
      statement.setString(1, this.uuid);

      ResultSet result = statement.executeQuery();

      if (!result.next()) {
        createUser(uuid, name);
      } else {
        this.uuid = result.getString("uuid");
        this.name = result.getString("name");
        // Update name
        if (!Objects.equals(this.name, name)) {
          updateUserName(name);
        }
      }

      result.close();
      statement.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Default constructor for the Player class.
   */
  public Player() {
  }

  /**
   * Get the UUID of the player.
   *
   * @return The UUID of the player.
   */
  public String getUuid() {
    return uuid;
  }

  /**
   * Set the UUID of the player.
   *
   * @param uuid The UUID of the player.
   */
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  /**
   * Get the name of the player.
   *
   * @return The name of the player.
   */
  public String getName() {
    return name;
  }

  /**
   * Set the name of the player.
   *
   * @param name The name of the player.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Load player information by name from the database. Updates the UUID and name of the Player
   * object.
   *
   * @param name The name of the player.
   * @throws SQLException if the player cannot be found.
   */
  public void loadByName(String name) {
    try {
      this.uuid = uuid;

      PreparedStatement statement = DatabaseManager.getConnection()
          .prepareStatement("SELECT * FROM player WHERE name = ?");
      statement.setString(1, name);

      ResultSet result = statement.executeQuery();
      if (!result.next()) {
        throw new SQLException("Couldn't find player!");
      } else {
        this.uuid = result.getString("uuid");
        this.name = result.getString("name");
        // Update name
        if (!this.name.equals(name)) {
          updateUserName(name);
        }
      }

      result.close();
      statement.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Update the name of the player in the database.
   *
   * @param name The new name of the player.
   * @throws SQLException if an error occurs while updating the name.
   */
  private void updateUserName(String name) throws SQLException {
    PreparedStatement statement = DatabaseManager.getConnection()
        .prepareStatement("UPDATE player SET name = ? WHERE uuid = ?");
    statement.setString(1, name);
    statement.setString(2, this.uuid);

    statement.executeUpdate();
    statement.close();
  }

  /**
   * Create a new player entry in the database.
   *
   * @param uuid The UUID of the player.
   * @param name The name of the player.
   * @throws SQLException if an error occurs while creating the player.
   */
  private void createUser(String uuid, String name) throws SQLException {
    PreparedStatement statement = DatabaseManager.getConnection()
        .prepareStatement("INSERT INTO player (uuid,name) VALUES(?,?)");
    statement.setString(1, uuid);
    statement.setString(2, name);

    statement.executeUpdate();
    statement.close();
    try {
      new JoinGroup().joinGroupByName(Configuration.getString("default-group"), uuid);
    } catch (Exception e) {

    }
    this.name = name;
    this.uuid = uuid;
  }
}
