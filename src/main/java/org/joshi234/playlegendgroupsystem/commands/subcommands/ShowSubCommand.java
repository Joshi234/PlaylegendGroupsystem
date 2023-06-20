package org.joshi234.playlegendgroupsystem.commands.subcommands;

import org.bukkit.command.Command;
import org.joshi234.playlegendgroupsystem.StringFormatter;
import org.joshi234.playlegendgroupsystem.database.group.Group;
import org.joshi234.playlegendgroupsystem.database.group.JoinGroup;

/**
 * The ShowSubCommand class represents the subcommand for showing the player's group. It implements
 * the SubCommand interface and provides the necessary behavior for the command.
 */
public class ShowSubCommand implements SubCommand {

  /**
   * Called when the show subcommand is executed.
   *
   * @param player  the player who executed the command
   * @param command the executed command
   * @param args    the command arguments
   */
  @Override
  public void onCommand(org.bukkit.entity.Player player, Command command, String[] args) {
    JoinGroup joinGroup = new JoinGroup();
    var groups = joinGroup.getPlayerGroups(player.getUniqueId().toString());
    if (groups.size() != 0) {
      for (Group group : groups) {
        player.sendMessage(
            new StringFormatter().getLocalizedFormattedString("group-commands.show.message",
                    player.getUniqueId().toString()).replaceKey("groupname", group.getName())
                .replaceKey("description", group.getDescription())
                .getMessage());
      }
    } else {
      player.sendMessage(
          new StringFormatter().getLocalizedFormattedString("group-commands.show.message-no-group",
              player.getUniqueId().toString()).getMessage());
    }
  }

  /**
   * Gets the permission required to execute the show subcommand.
   *
   * @return the permission string
   */
  @Override
  public String getPermission() {
    return null;
  }
}
