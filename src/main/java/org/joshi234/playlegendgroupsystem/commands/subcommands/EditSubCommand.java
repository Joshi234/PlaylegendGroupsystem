package org.joshi234.playlegendgroupsystem.commands.subcommands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.joshi234.playlegendgroupsystem.StringFormatter;
import org.joshi234.playlegendgroupsystem.database.group.Group;

/**
 * The EditSubCommand class represents the subcommand for editing a group. It implements the
 * SubCommand interface and provides the necessary behavior for the command.
 */
public class EditSubCommand implements SubCommand {

  private final List<String> possibleField = new ArrayList<>();

  /**
   * Constructor for EditSubCommand. Initializes the list of possible fields to edit.
   */
  public EditSubCommand() {
    possibleField.add("name");
    possibleField.add("prefix");
    possibleField.add("description");
    possibleField.add("weight");
  }

  /**
   * Called when the edit subcommand is executed.
   *
   * @param player  the player who executed the command
   * @param command the executed command
   * @param args    the command arguments
   */
  @Override
  public void onCommand(Player player, Command command, String[] args) {
    // Check the length of arguments to determine if it's valid
    if (args.length == 4) {
      Group group = new Group();
      try {
        group.loadGroupByName(args[1]);
      } catch (Exception e) {
        player.sendMessage(
            new StringFormatter()
                .getFormattedString("group-commands.add.message-unknown-group")
                .replaceKey("groupname", args[2])
                .getMessage());
        return;
      }
      if (!possibleField.contains(args[2].toLowerCase())) {
        player.sendMessage(
            new StringFormatter()
                .getFormattedString("group-commands.edit.message-unknown-field")
                .replaceKey("fieldname", args[2])
                .getMessage());
        return;
      }
      try {
        // Update the group with the specified field and value
        group.updateGroup(args[2].toLowerCase(), args[3], group.getGroupId());
        player.sendMessage(
            new StringFormatter().getLocalizedFormattedString("group-commands.edit.message-success",
                    player.getUniqueId().toString()).replaceKey("playername", args[1])
                .replaceKey("groupname", args[2]).getMessage());
      } catch (RuntimeException e) {
        player.sendMessage(
            new StringFormatter().getLocalizedFormattedString("group-commands.edit.message-fail",
                    player.getUniqueId().toString()).replaceKey("playername", args[1])
                .replaceKey("groupname", args[2]).getMessage());

      }
    } else {
      player.sendMessage(
          new StringFormatter().getLocalizedFormattedString("commands-general.missing-argument",
              player.getUniqueId().toString()).getMessage());
    }
  }

  /**
   * Gets the permission required to execute the edit subcommand.
   *
   * @return the permission string
   */
  @Override
  public String getPermission() {
    return null;
  }

  /**
   * Gets the arguments for the edit subcommand at the specified level.
   *
   * @param args  the command arguments
   * @param level the argument level
   * @return the list of arguments
   */
  @Override
  public List<String> getArguments(String[] args, int level) {
    if (level == 1) {
      return new Group().getGroupNames();
    } else if (level == 2) {
      return possibleField;
    }
    return new ArrayList<>();
  }
}
