package org.joshi234.playlegendgroupsystem.commands.subcommands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.joshi234.playlegendgroupsystem.StringFormatter;
import org.joshi234.playlegendgroupsystem.database.group.Group;

/**
 * The DeleteSubCommand class represents the subcommand for deleting a group. It implements the
 * SubCommand interface and provides the necessary behavior for the command.
 */
public class DeleteSubCommand implements SubCommand {

  /**
   * Called when the delete subcommand is executed.
   *
   * @param player  the player who executed the command
   * @param command the executed command
   * @param args    the command arguments
   */
  @Override
  public void onCommand(Player player, Command command, String[] args) {
    // Check the length of arguments to determine if it's valid
    if (args.length == 2) {
      Group group = new Group();
      try {
        group.loadGroupByName(args[1]);
      } catch (Exception e) {
        player.sendMessage(
            new StringFormatter()
                .getLocalizedFormattedString("group-commands.remove.message-unknown-group",
                    player.getUniqueId().toString())
                .replaceKey("groupname", args[2])
                .getMessage());
        return;
      }
      try {
        // Delete the group
        group.deleteGroup(group.getGroupId());

        player.sendMessage(
            new StringFormatter()
                .getLocalizedFormattedString("group-commands.delete.message-success",
                    player.getUniqueId().toString())
                .replaceKey("groupname", args[1])
                .getMessage());
      } catch (RuntimeException e) {
        player.sendMessage(
            new StringFormatter()
                .getLocalizedFormattedString("group-commands.delete.message-player-in-group",
                    player.getUniqueId().toString())
                .replaceKey("groupname", args[1])
                .getMessage());
      }
    } else {
      player.sendMessage(
          new StringFormatter()
              .getLocalizedFormattedString("commands-general.missing-argument",
                  player.getUniqueId().toString())
              .getMessage());
    }
  }

  /**
   * Gets the permission required to execute the delete subcommand.
   *
   * @return the permission string
   */
  @Override
  public String getPermission() {
    return null;
  }

  /**
   * Gets the arguments for the delete subcommand at the specified level.
   *
   * @param args  the command arguments
   * @param level the argument level
   * @return the list of arguments
   */
  @Override
  public List<String> getArguments(String[] args, int level) {
    if (level == 1) {
      return new Group().getGroupNames();
    }
    return new ArrayList<>();
  }
}
