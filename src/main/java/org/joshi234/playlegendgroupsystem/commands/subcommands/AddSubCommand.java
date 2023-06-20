package org.joshi234.playlegendgroupsystem.commands.subcommands;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
 * The AddSubCommand class represents the subcommand for adding a player to a group. It implements
 * the SubCommand interface and provides the necessary behavior for the command.
 */
public class AddSubCommand implements SubCommand {

  /**
   * Called when the add subcommand is executed.
   *
   * @param player  the player who executed the command
   * @param command the executed command
   * @param args    the command arguments
   */
  @Override
  public void onCommand(Player player, Command command, String[] args) {
    // Check the length of arguments to determine if it's valid
    if (args.length == 3 || args.length == 7) {
      org.joshi234.playlegendgroupsystem.database.group.Player dbPlayer = new org.joshi234.playlegendgroupsystem.database.group.Player();
      try {
        dbPlayer.loadByName(args[1]);
      } catch (Exception e) {
        player.sendMessage(
            new StringFormatter().getFormattedString("group-commands.add.message-unknown-player")
                .replaceKey("playername", args[1]).getMessage());
        return;
      }
      Group group = new Group();
      try {
        group.loadGroupByName(args[2]);
      } catch (Exception e) {
        player.sendMessage(
            new StringFormatter().getFormattedString("group-commands.add.message-unknown-group")
                .replaceKey("groupname", args[2]).getMessage());
        return;
      }
      JoinGroup joinGroup = new JoinGroup();
      joinGroup.setUuid(dbPlayer.getUuid());
      joinGroup.setGroupId(group.getGroupId());
      if (args.length == 7) {
        try {
          // Parse the duration values from arguments and add them to the joinUntil timestamp
          LocalDateTime joinUntil = new Timestamp(System.currentTimeMillis()).toLocalDateTime();
          joinUntil = joinUntil.plusDays(Integer.parseInt(args[3]));
          joinUntil = joinUntil.plusHours(Integer.parseInt(args[4]));
          joinUntil = joinUntil.plusMinutes(Integer.parseInt(args[5]));
          joinUntil = joinUntil.plusSeconds(Integer.parseInt(args[6]));
          joinGroup.setJoinUntil(Timestamp.valueOf(joinUntil));
        } catch (Exception e) {
          player.sendMessage(new StringFormatter().getLocalizedFormattedString(
                  "group-commands.add.message-wrong-format", player.getUniqueId().toString())
              .getMessage());
        }
      }
      joinGroup.joinGroup();
      for (Player bukkitPlayer : Bukkit.getOnlinePlayers()) {

        UUID uuid = bukkitPlayer.getUniqueId();
        if (Objects.equals(uuid.toString(), dbPlayer.getUuid())) {

          // Update the player's list name with the new prefix
          String prefix = new JoinGroup().getPrefix(bukkitPlayer.getUniqueId().toString());
          bukkitPlayer.playerListName(new StringFormatter().setFormattedMessage(
              "&f[" + prefix + "&f] " + bukkitPlayer.getName()).getComponent());
        }
      }

      player.sendMessage(
          new StringFormatter().getLocalizedFormattedString("group-commands.add.message-success",
                  player.getUniqueId().toString()).replaceKey("playername", args[1])
              .replaceKey("groupname", args[2]).getMessage());
    } else {
      player.sendMessage(
          new StringFormatter().getLocalizedFormattedString("commands-general.missing-argument",
              player.getUniqueId().toString()).getMessage());
    }
  }

  /**
   * Gets the permission required to execute the add subcommand.
   *
   * @return the permission string
   */
  @Override
  public String getPermission() {
    return null;
  }

  /**
   * Gets the list of arguments for tab completion at the specified level. This method provides tab
   * completion options for the add subcommand.
   *
   * @param args  the command arguments
   * @param level the argument level
   * @return a list of tab-completed strings
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
