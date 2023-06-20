package org.joshi234.playlegendgroupsystem.database.group;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joshi234.playlegendgroupsystem.database.DatabaseManager;

/**
 * The JoinGroup class represents the joining and management of groups for players in the PlayLegend
 * Group System. Each JoinGroup object contains information about the player's UUID, join until
 * timestamp, and group ID.
 */
public class JoinGroup {

  private static Map<String, String> prefixCache;
  private String uuid;
  private Timestamp joinUntil;
  private int groupId;

  public JoinGroup() {
    if (prefixCache == null) {
      prefixCache = new HashMap<>();
    }
  }

  /**
   * Retrieves the UUID of the player.
   *
   * @return the player's UUID
   */
  public String getUuid() {
    return uuid;
  }

  /**
   * Sets the UUID for the player.
   *
   * @param uuid the player's UUID
   */
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  /**
   * Retrieves the join until timestamp for the player.
   *
   * @return the join until timestamp
   */
  public Timestamp getJoinUntil() {
    return this.joinUntil;
  }

  /**
   * Sets the join until timestamp for the player.
   *
   * @param joinUntil the join until timestamp
   */
  public void setJoinUntil(Timestamp joinUntil) {
    this.joinUntil = joinUntil;
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
   * Sets the group ID for the player.
   *
   * @param groupId the group ID
   */
  public void setGroupId(int groupId) {
    this.groupId = groupId;
  }

  /**
   * Retrieves a list of groups that the player belongs to based on the UUID.
   *
   * @param uuid the player's UUID
   * @return a list of Group objects representing the player's groups
   */
  public List<Group> getPlayerGroups(String uuid) {
    try {
      PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(
          "SELECT g.name,g.prefix,g.description,j.groupid,g.weight FROM joingroup j " +
              "JOIN " + '"' + "group" + '"' + " g on j.groupid = g.groupid WHERE j.uuid = ?");
      List<Group> groupList = new ArrayList<>();

      statement.setString(1, uuid);

      ResultSet result = statement.executeQuery();

      while (result.next()) {
        var group = new Group(result.getString("name"), result.getString("prefix"),
            result.getString("description"), result.getInt("weight"));
        groupList.add(group);
      }

      result.close();
      statement.close();

      return groupList;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Joins the player to the specified group.
   */
  public void joinGroup() {
    try {
      PreparedStatement statement;
      if (this.joinUntil != null) {
        statement = DatabaseManager.getConnection()
            .prepareStatement("INSERT INTO joingroup (uuid,groupid,joinuntil) VALUES(?,?,?) ");
        statement.setString(1, this.uuid);
        statement.setInt(2, this.groupId);
        statement.setTimestamp(3, this.joinUntil);
      } else {
        statement = DatabaseManager.getConnection()
            .prepareStatement("INSERT INTO joingroup (uuid,groupid) VALUES(?,?) ");
        statement.setString(1, this.uuid);
        statement.setInt(2, this.groupId);
      }

      statement.executeUpdate();
      if (prefixCache.containsKey(this.uuid)) {
        prefixCache.replace(this.uuid, loadPrefixFromDatabase(this.uuid));
      } else {
        prefixCache.put(this.uuid, loadPrefixFromDatabase(this.uuid));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Retrieves the prefix associated with the specified UUID.
   *
   * @param uuid the player's UUID
   * @return the prefix associated with the UUID
   */
  public String getPrefix(String uuid) {
    if (prefixCache == null) {
      prefixCache = new HashMap<>();
    }

    if (prefixCache.containsKey(uuid)) {
      return prefixCache.get(uuid);
    } else {
      String prefix = loadPrefixFromDatabase(uuid);
      prefixCache.put(uuid, prefix);
      return prefix;
    }
  }

  /**
   * Retrieves the prefix associated with the specified UUID. If the player has joined a group, the
   * prefix cache is cleared and reloaded.
   *
   * @param uuid         the player's UUID
   * @param playerJoined indicates whether the player has joined a group
   * @return the prefix associated with the UUID
   */
  public String getPrefix(String uuid, boolean playerJoined) {
    if (prefixCache != null && playerJoined) {
      prefixCache.remove(uuid);
    }
    return getPrefix(uuid);
  }

  /**
   * Loads the prefix associated with the specified UUID from the database.
   *
   * @param uuid the player's UUID
   * @return the prefix associated with the UUID
   */
  private String loadPrefixFromDatabase(String uuid) {
    try {
      checkIfGroupsValid(uuid);
      PreparedStatement statement =
          DatabaseManager.getConnection()
              .prepareStatement(
                  "SELECT prefix,joinuntil from \"group\" "
                      + "JOIN joingroup j on \"group\".groupid = j.groupid\n"
                      + "WHERE j.uuid = ? ORDER BY \"group\".weight ASC");
      statement.setString(1, uuid);
      ResultSet result = statement.executeQuery();

      if (result.next()) {
        String prefix = result.getString("prefix");
        result.close();
        statement.close();
        return prefix;
      } else {
        return "&4none";
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Checks if the player's groups are valid based on the join until timestamp. Removes the invalid
   * group entries from the database.
   *
   * @param uuid the player's UUID
   */
  private void checkIfGroupsValid(String uuid) {
    try {
      PreparedStatement statement =
          DatabaseManager.getConnection()
              .prepareStatement(
                  "SELECT groupid,joinuntil from joingroup WHERE uuid = ? ");
      statement.setString(1, uuid);
      ResultSet result = statement.executeQuery();

      while (result.next()) {
        Timestamp joinUntil = result.getTimestamp("joinuntil");
        if (joinUntil != null) {
          if (!joinUntil.after(new Timestamp(System.currentTimeMillis()))) {
            deleteJoinGroup(uuid, result.getInt("groupid"));
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Deletes the player's join entry for the specified group from the database.
   *
   * @param uuid    the player's UUID
   * @param groupId the group ID
   */
  public void deleteJoinGroup(String uuid, int groupId) {
    try {
      PreparedStatement statement = DatabaseManager.getConnection()
          .prepareStatement("DELETE FROM joingroup WHERE uuid = ? AND groupid = ?");
      statement.setString(1, uuid);
      statement.setInt(2, groupId);
      statement.executeUpdate();
      statement.close();
      if (prefixCache.containsKey(uuid)) {
        prefixCache.replace(uuid, loadPrefixFromDatabase(this.uuid));
      } else {
        prefixCache.put(uuid, loadPrefixFromDatabase(uuid));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Joins the player to the group specified by the group name.
   *
   * @param groupName the name of the group to join
   * @param uuid      the player's UUID
   */
  public void joinGroupByName(String groupName, String uuid) {
    Group group = new Group();
    group.loadGroupByName(groupName);
    setGroupId(group.getGroupId());
    setUuid(uuid);
    joinGroup();
  }
}
