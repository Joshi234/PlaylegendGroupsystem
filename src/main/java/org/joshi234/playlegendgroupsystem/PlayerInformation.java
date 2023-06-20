package org.joshi234.playlegendgroupsystem;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * The PlayerInformation class provides utility methods related to player information.
 */
public class PlayerInformation {

  /**
   * Retrieves a list of player names of all online players.
   *
   * @return A list of player names.
   */
  public static List<String> getPlayerNames() {
    List<String> playerNames = new ArrayList<>();
    for (Player player : Bukkit.getOnlinePlayers()) {
      playerNames.add(player.getName());
    }
    return playerNames;
  }
}
