package org.joshi234.playlegendgroupsystem.commands.subcommands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 * The SubCommand interface represents a subcommand for a command. Classes implementing this
 * interface define the behavior of a specific subcommand.
 */
public interface SubCommand {

  /**
   * Called when the subcommand is executed.
   *
   * @param player  the player who executed the command
   * @param command the executed command
   * @param args    the command arguments
   */
  void onCommand(Player player, Command command, String[] args);

  /**
   * Gets the permission required to execute the subcommand.
   *
   * @return the permission string
   */
  String getPermission();

  /**
   * Gets the list of arguments for tab completion at the specified level. This method provides tab
   * completion options for the subcommand. Subclasses can override this method to customize the tab
   * completion behavior.
   *
   * @param args  the command arguments
   * @param level the argument level
   * @return a list of tab-completed strings
   */
  default List<String> getArguments(String[] args, int level) {
    return new ArrayList<>();
  }
}
