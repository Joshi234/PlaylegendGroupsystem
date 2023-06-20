package org.joshi234.playlegendgroupsystem.commands.subcommands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.joshi234.playlegendgroupsystem.StringFormatter;
import org.joshi234.playlegendgroupsystem.database.group.Group;

/**
 * The CreateSubCommand class represents the subcommand for creating a new group. It implements the
 * SubCommand interface and provides the necessary behavior for the command.
 */
public class CreateSubCommand implements SubCommand {

  /**
   * Called when the create subcommand is executed.
   *
   * @param player  the player who executed the command
   * @param command the executed command
   * @param args    the command arguments
   */
  @Override
  public void onCommand(Player player, Command command, String[] args) {
    // Check the length of arguments to determine if it's valid
    if (args.length != 5 && args.length != 4) {
      player.sendMessage(
          new StringFormatter().getLocalizedFormattedString("commands-general.missing-argument",
              player.getUniqueId().toString()).getMessage());
    } else {
      Group group;
      if (args.length == 4) {
        // Create a group without a description
        group = new Group(args[1], args[2], "", Integer.parseInt(args[3]));
      } else {
        // Create a group with a description
        group = new Group(args[1], args[2], args[4], Integer.parseInt(args[3]));
      }
      group.createGroup();
      player.sendMessage(
          new StringFormatter().getLocalizedFormattedString("group-commands.create.message-success",
              player.getUniqueId().toString()).replaceKey("groupname", args[1]).getMessage());
    }
  }

  /**
   * Gets the permission required to execute the create subcommand.
   *
   * @return the permission string
   */
  @Override
  public String getPermission() {
    return null;
  }
}
