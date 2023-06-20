package org.joshi234.playlegendgroupsystem.commands.subcommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.joshi234.playlegendgroupsystem.GroupSystem;
import org.joshi234.playlegendgroupsystem.SignHandler;
import org.joshi234.playlegendgroupsystem.StringFormatter;
import org.joshi234.playlegendgroupsystem.database.group.Sign;

/**
 * The SignSubCommand class represents the subcommand for managing signs. It implements the
 * SubCommand interface and provides the necessary behavior for the command.
 */
public class SignSubCommand implements SubCommand {

  /**
   * Called when the sign subcommand is executed.
   *
   * @param player  the player who executed the command
   * @param command the executed command
   * @param args    the command arguments
   */
  @Override
  public void onCommand(org.bukkit.entity.Player player, Command command, String[] args) {
    if (Objects.equals(args[1], "add")) {
      if (player.getWorld().getBlockAt(player.getLocation()).getType() == Material.OAK_SIGN) {
        Location loc = player.getLocation().clone();
        Bukkit.getScheduler().runTaskLaterAsynchronously(
            GroupSystem.getInstance(),
            () -> {
              Sign.addSign(loc);
              SignHandler.signAdded(loc);
            },
            10);
      } else {
        player.sendMessage(
            new StringFormatter()
                .getLocalizedFormattedString("group-commands.sign.message-no-sign",
                    player.getUniqueId().toString())
                .getMessage());
      }
    } else {
      if (Sign.isSignPresent(player.getLocation())) {
        Location loc = player.getLocation().clone();
        Sign.removeSign(loc);
      } else {
        player.sendMessage(
            new StringFormatter()
                .getLocalizedFormattedString("group-commands.sign.message-no-sign",
                    player.getUniqueId().toString())
                .getMessage());
      }
    }
  }

  /**
   * Gets the permission required to execute the sign subcommand.
   *
   * @return the permission string
   */
  @Override
  public String getPermission() {
    return null;
  }

  /**
   * Gets the list of arguments for the sign subcommand at the given level.
   *
   * @param args  the command arguments
   * @param level the argument level
   * @return the list of arguments
   */
  @Override
  public List<String> getArguments(String[] args, int level) {
    if (level == 1) {
      return List.of(new String[]{"add", "remove"});
    } else {
      return new ArrayList<>();
    }
  }
}
