package org.joshi234.playlegendgroupsystem.commands.tabcompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joshi234.playlegendgroupsystem.commands.subcommands.SubCommand;

/**
 * The SubCommandTabCompleter class provides tab completion functionality for sub-commands. It
 * implements the TabCompleter interface and can be used to customize tab completion behavior for
 * commands with sub-commands.
 */
public abstract class SubCommandTabCompleter implements TabCompleter {

  /**
   * The map of sub-commands associated with their respective command names.
   */
  public Map<String, SubCommand> commands;

  /**
   * Called when tab completion is requested for a command. This method should be overridden by
   * subclasses to provide custom tab completion logic.
   *
   * @param sender  the command sender
   * @param command the executed command
   * @param label   the command label
   * @param args    the command arguments
   * @return a list of tab-completed strings
   */
  public @Nullable List<String> onTabComplete(
      @NotNull CommandSender sender,
      @NotNull Command command,
      @NotNull String label,
      @NotNull String[] args) {
    return new ArrayList<>();
  }

  /**
   * Sets the map of sub-commands associated with their respective command names.
   *
   * @param commands the map of sub-commands
   */
  public void setCommands(Map<String, SubCommand> commands) {
    this.commands = commands;
  }
}
