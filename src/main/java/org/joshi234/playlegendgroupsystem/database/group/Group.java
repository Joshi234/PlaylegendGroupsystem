package org.joshi234.playlegendgroupsystem.database.group;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.joshi234.playlegendgroupsystem.database.DatabaseManager;

/**
 * The Group class represents a group in the PlayLegend Group System. Each group has a unique
 * identifier, name, prefix, description, and weight.
 */
public class Group {

  private int groupId;
  private String name;
  private String prefix;
  private String description;
  private int weight;

  /**
   * Constructs a Group object with the specified name, prefix, description, and weight.
   *
   * @param name        the name of the group
   * @param prefix      the prefix associated with the group
   * @param description the description of the group
   * @param weight      the weight of the group
   */
  public Group(String name, String prefix, String description, int weight) {
    this.name = name;
    this.prefix = prefix;
    this.description = description;
    this.weight = weight;
  }

  /**
   * Constructs an empty Group object.
   */
  public Group() {
  }

  /**
   * Retrieves the group ID.
   *
   * @return the group ID
   */
  public int getGroupId() {
    return groupId;
  }

  /**
   * Retrieves the prefix associated with the group.
   *
   * @return the group's prefix
   */
  public String getPrefix() {
    return prefix;
  }

  /**
   * Sets the prefix for the group.
   *
   * @param prefix the new prefix for the group
   */
  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  /**
   * Retrieves the description of the group.
   *
   * @return the group's description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description for the group.
   *
   * @param description the new description for the group
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Retrieves the name of the group.
   *
   * @return the group's name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name for the group.
   *
   * @param name the new name for the group
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Retrieves the weight of the group.
   *
   * @return the group's weight
   */
  public int getWeight() {
    return weight;
  }

  /**
   * Sets the weight for the group.
   *
   * @param weight the new weight for the group
   */
  public void setWeight(int weight) {
    this.weight = weight;
  }

  /**
   * Loads the group data from the database based on the specified group ID.
   *
   * @param groupId the ID of the group to load
   * @throws RuntimeException if no group is found for the given group ID
   */
  public void loadGroup(int groupId) {
    try {
      PreparedStatement statement = DatabaseManager.getConnection()
          .prepareStatement("SELECT * FROM \"group\" WHERE groupId = ?");
      statement.setInt(1, groupId);

      ResultSet result = statement.executeQuery();
      if (!result.next()) {
        throw new RuntimeException(
            "No result found in group for groupId: " + groupId);
      } else {
        this.groupId = result.getInt("groupId");
        setName(result.getString("name"));
        setDescription(result.getString("description"));
        setPrefix(result.getString("prefix"));
        setWeight(result.getInt("weight"));
      }

      result.close();
      statement.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Creates a new group in the database with the current group's properties. Upon successful
   * creation, the group's ID will be updated with the generated ID.
   *
   * @throws RuntimeException if creating the group fails
   */
  public void createGroup() {
    try {
      PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(
          "INSERT INTO \"group\" (name, prefix, description, weight) VALUES (?,?,?,?)");
      statement.setString(1, getName());
      statement.setString(2, getPrefix());
      statement.setString(3, getDescription());
      statement.setInt(4, getWeight());
      int affectedRows = statement.executeUpdate();
      if (affectedRows == 0) {
        throw new SQLException("Creating group failed");
      }

      ResultSet result = statement.getGeneratedKeys();
      if (result.next()) {
        this.groupId = result.getInt(1);
        statement.close();
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Loads the group data from the database based on the specified group name.
   *
   * @param name the name of the group to load
   * @throws RuntimeException if no group is found for the given name
   */
  public void loadGroupByName(String name) {
    try {
      PreparedStatement statement = DatabaseManager.getConnection()
          .prepareStatement("SELECT * FROM \"group\" WHERE name = ?");
      statement.setString(1, name);

      ResultSet result = statement.executeQuery();
      if (!result.next()) {
        throw new RuntimeException(
            "No result found in group for groupname: " + name);
      } else {
        this.groupId = result.getInt("groupId");
        setName(result.getString("name"));
        setDescription(result.getString("description"));
        setPrefix(result.getString("prefix"));
        setWeight(result.getInt("weight"));
      }

      result.close();
      statement.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Updates a specific property of the group in the database.
   *
   * @param key     the key of the property to update
   * @param value   the new value for the property
   * @param groupId the ID of the group to update
   * @throws RuntimeException if updating the group fails
   */
  public void updateGroup(String key, String value, int groupId) {
    try {
      PreparedStatement statement = DatabaseManager.getConnection()
          .prepareStatement("UPDATE \"group\" SET " + key + " = ? WHERE groupid = ?");

      if (Objects.equals(key, "weight")) {
        statement.setInt(1, Integer.parseInt(value));
      } else {
        statement.setString(1, value);
      }
      statement.setInt(2, groupId);
      statement.executeUpdate();
      statement.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Retrieves a list of all group names from the database.
   *
   * @return a list of group names
   * @throws RuntimeException if retrieving the group names fails
   */
  public List<String> getGroupNames() {

    try {
      List<String> groupNames = new ArrayList<>();

      PreparedStatement statement = DatabaseManager.getConnection()
          .prepareStatement("SELECT name FROM \"group\"");
      ResultSet result = statement.executeQuery();

      while (result.next()) {
        groupNames.add(result.getString("name"));
      }

      result.close();
      statement.close();

      return groupNames;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Deletes the group with the specified group ID from the database.
   *
   * @param groupId the ID of the group to delete
   * @throws RuntimeException if deleting the group fails
   */
  public void deleteGroup(int groupId) {
    try {
      PreparedStatement statement = DatabaseManager.getConnection()
          .prepareStatement("DELETE FROM \"group\" WHERE groupid = ?");
      statement.setInt(1, groupId);

      statement.executeUpdate();
      statement.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
