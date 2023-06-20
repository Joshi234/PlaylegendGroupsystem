package org.joshi234.playlegendgroupsystem.commands.subcommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.joshi234.playlegendgroupsystem.PlayerInformation;
import org.joshi234.playlegendgroupsystem.StringFormatter;
import org.joshi234.playlegendgroupsystem.database.group.Group;
import org.joshi234.playlegendgroupsystem.database.group.JoinGroup;

/**
 * The RemoveSubCommand class represents the subcommand for removing a player from a group. It
 * implements the SubCommand interface and provides the necessary behavior for the command.
 */
public class RemoveSubCommand implements SubCommand {

  /**
   * Called when the remove subcommand is executed.
   *
   * @param player  the player who executed the command
   * @param command the executed command
   * @param args    the command arguments
   */
  @Override
  public void onCommand(Player player, Command command, String[] args) {
    if (args.length == 3) {
      org.joshi234.playlegendgroupsystem.database.group.Player dbPlayer =
          new org.joshi234.playlegendgroupsystem.database.group.Player();
      try {
        dbPlayer.loadByName(args[1]);
      } catch (Exception e) {
        player.sendMessage(
            new StringFormatter()
                .getFormattedString("group-commands.remove.message-unknown-player")
                .replaceKey("playername", args[1])
                .getMessage());
        return;
      }
      Group group = new Group();
      try {
        group.loadGroupByName(args[2]);
      } catch (Exception e) {
        player.sendMessage(
            new StringFormatter()
                .getFormattedString("group-commands.remove.message-unknown-group")
                .replaceKey("groupname", args[2])
                .getMessage());
        return;
      }
      JoinGroup joinGroup = new JoinGroup();
      joinGroup.deleteJoinGroup(dbPlayer.getUuid(), group.getGroupId());
      for (Player bukkitPlayer : Bukkit.getOnlinePlayers()) {

        UUID uuid = bukkitPlayer.getUniqueId();
        if (Objects.equals(uuid.toString(), dbPlayer.getUuid())) {

          String prefix = new JoinGroup().getPrefix(bukkitPlayer.getUniqueId().toString());
          bukkitPlayer.playerListName(
              new StringFormatter()
                  .setFormattedMessage("&f[" + prefix + "&f] " + bukkitPlayer.getName())
                  .getComponent());
        }
      }

      player.sendMessage(
          new StringFormatter()
              .getLocalizedFormattedString("group-commands.remove.message-success",
                  player.getUniqueId().toString())
              .replaceKey("playername", args[1])
              .replaceKey("groupname", args[2])
              .getMessage());
    } else {
      player.sendMessage(
          new StringFormatter()
              .getLocalizedFormattedString("commands-general.missing-argument",
                  player.getUniqueId().toString())
              .getMessage());
    }
  }

  /**
   * Gets the permission required to execute the remove subcommand.
   *
   * @return the permission string
   */
  @Override
  public String getPermission() {
    return null;
  }

  /**
   * Gets the arguments for the remove subcommand at the specified level.
   *
   * @param args  the command arguments
   * @param level the argument level
   * @return the list of arguments
   */
  @Override
  public List<String> getArguments(String[] args, int level) {
    if (level == 1) {
      return PlayerInformation.getPlayerNames();
    } else if (level == 2) {
      return new Group().getGroupNames();
    }
    return new ArrayList<>();
  }
}
