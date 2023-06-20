package org.joshi234.playlegendgroupsystem.database.group;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.joshi234.playlegendgroupsystem.database.DatabaseManager;

/**
 * The Sign class represents signs in the PlayLegend Group System. It provides methods to retrieve,
 * add, and remove sign locations from the database.
 */
public class Sign {

  private static List<Location> signLocations;

  /**
   * Retrieves all sign locations from the database. If the signLocations list is {@code null}, it
   * queries the database and populates the list.
   *
   * @return a list of sign locations
   */
  public static List<Location> getSigns() {
    if (signLocations == null) {
      signLocations = new ArrayList<>();
      try {
        PreparedStatement statement = DatabaseManager.getConnection()
            .prepareStatement("SELECT * FROM sign");

        ResultSet result = statement.executeQuery();

        while (result.next()) {
          signLocations.add(new Location(Bukkit.getWorld(result.getString("world")),
              result.getInt("posX"),
              result.getInt("posY"),
              result.getInt("posZ")));
        }

        return signLocations;
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    } else {
      return signLocations;
    }
  }

  /**
   * Adds a sign location to the database. Also updates the signLocations list.
   *
   * @param location the location of the sign
   */
  public static void addSign(Location location) {
    try {
      PreparedStatement statement = DatabaseManager.getConnection()
          .prepareStatement("INSERT INTO sign(world,posX,posY,posZ) VALUES(?,?,?,?)");
      statement.setString(1, location.getWorld().getName());
      statement.setInt(2, location.getBlockX());
      statement.setInt(3, location.getBlockY());
      statement.setInt(4, location.getBlockZ());

      int affectedRows = statement.executeUpdate();
      if (affectedRows == 0) {
        throw new SQLException("Creating sign failed");
      }

      signLocations.add(convertGameLocationToDbLocation(location));

      statement.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Removes a sign location from the database. Also updates the signLocations list.
   *
   * @param location the location of the sign to be removed
   */
  public static void removeSign(Location location) {
    try {
      PreparedStatement statement = DatabaseManager.getConnection()
          .prepareStatement(
              "DELETE FROM sign WHERE world = ? AND posX = ? AND posY = ? AND posZ = ?");
      statement.setString(1, location.getWorld().getName());
      statement.setInt(2, location.getBlockX());
      statement.setInt(3, location.getBlockY());
      statement.setInt(4, location.getBlockZ());

      int affectedRows = statement.executeUpdate();

      if (affectedRows == 0) {
        throw new SQLException("Deleting sign failed");
      }
      signLocations.remove(convertGameLocationToDbLocation(location));

      statement.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Checks if a sign location is present in the signLocations list.
   *
   * @param location the location to check
   * @return true if the sign location is present, false otherwise
   */
  public static boolean isSignPresent(Location location) {
    for (Location loc : signLocations) {
      if (locationEquals(loc, location)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if two locations are equal in terms of block coordinates and world.
   *
   * @param loc1 the first location
   * @param loc2 the second location
   * @return true if the locations are equal, false otherwise
   */
  private static boolean locationEquals(Location loc1, Location loc2) {
    if (loc1.getBlockX() != loc2.getBlockX()) {
      return false;
    }
    if (loc1.getBlockY() != loc2.getBlockY()) {
      return false;
    }
    if (loc1.getBlockZ() != loc2.getBlockZ()) {
      return false;
    }
    return loc1.getWorld().getName().equals(loc2.getWorld().getName());
  }

  /**
   * Converts a game location to a database location.
   *
   * @param loc the game location to convert
   * @return the converted database location
   */
  private static Location convertGameLocationToDbLocation(Location loc) {
    return new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(),
        loc.getBlockZ());
  }
}
