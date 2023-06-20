package org.joshi234.playlegendgroupsystem.commands.subcommands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.joshi234.playlegendgroupsystem.StringFormatter;

/**
 * The HelpSubCommand class represents the subcommand for displaying help information. It implements
 * the SubCommand interface and provides the necessary behavior for the command.
 */
public class HelpSubCommand implements SubCommand {

  /**
   * Called when the help subcommand is executed.
   *
   * @param player  the player who executed the command
   * @param command the executed command
   * @param args    the command arguments
   */
  @Override
  public void onCommand(Player player, Command command, String[] args) {
    player.sendMessage(
        new StringFormatter().getLocalizedFormattedStringListAsString("group-commands.help.message",
            player.getUniqueId().toString()).getMessage());
  }

  /**
   * Gets the permission required to execute the help subcommand.
   *
   * @return the permission string
   */
  @Override
  public String getPermission() {
    return null;
  }
}
