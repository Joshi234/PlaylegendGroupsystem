package org.joshi234.playlegendgroupsystem;

import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.joshi234.playlegendgroupsystem.database.group.JoinGroup;
import org.joshi234.playlegendgroupsystem.database.group.Sign;

/**
 * The SignHandler class provides methods for handling signs in the group system.
 */
public class SignHandler {

  /**
   * Sets the text of a sign based on the player's prefix and name.
   *
   * @param location The location of the sign.
   * @param player   The player associated with the sign.
   */
  private static void setSignText(Location location, Player player) {
    String prefix = new JoinGroup().getPrefix(player.getUniqueId().toString());

    List<Component> lines = new ArrayList<>();
    lines.add(LegacyComponentSerializer.legacySection().toBuilder().character('&').build()
        .deserialize("&f[" + prefix + "&f]"));
    lines.add(Component.text(player.getName()));
    lines.add(Component.text(""));
    lines.add(Component.text(""));

    player.sendSignChange(location, lines);
  }

  /**
   * Loads the signs and periodically updates their text for nearby players.
   */
  public static void loadSigns() {
    Bukkit.getScheduler().runTaskTimer(GroupSystem.getInstance(), () -> {
      for (Player player : Bukkit.getOnlinePlayers()) {
        for (Location signLocation : Sign.getSigns()) {
          if (player.getLocation().getWorld().equals(signLocation.getWorld())
              && player.getLocation().distance(signLocation) < 128) {
            setSignText(signLocation, player);
          }
        }
      }
    }, 50, 50);
  }

  /**
   * Sets the text of a newly added sign for all online players.
   *
   * @param location The location of the added sign.
   */
  public static void signAdded(Location location) {
    for (Player player : Bukkit.getOnlinePlayers()) {
      setSignText(location, player);
    }
  }
}
