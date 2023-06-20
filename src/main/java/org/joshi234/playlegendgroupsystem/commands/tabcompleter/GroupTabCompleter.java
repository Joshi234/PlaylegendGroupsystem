package org.joshi234.playlegendgroupsystem.commands.tabcompleter;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The GroupTabCompleter class provides tab completion functionality for the group command. It
 * extends the SubCommandTabCompleter class and is specifically designed for the group command.
 */
public class GroupTabCompleter extends SubCommandTabCompleter {

  /**
   * Called when tab completion is requested for the group command. This method overrides the
   * onTabComplete method in the SubCommandTabCompleter class and provides custom tab completion
   * logic for the group command.
   *
   * @param sender  the command sender
   * @param command the executed command
   * @param label   the command label
   * @param args    the command arguments
   * @return a list of tab-completed strings
   */
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
      @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    //Show SubCommands
    if (args.length == 1) {
      if (sender.isOp()) {
        List<String> options = new ArrayList<>(commands.keySet());
        List<String> result = new ArrayList<>(options);

        for (String option : options) {
          if (!option.startsWith(args[args.length - 1])) {
            result.remove(option);
          }
        }

        return new ArrayList<>(result);
      } else {
        return new ArrayList<>(List.of("show"));
      }
    } else {
      //Get arguments from subcommand
      String subCommand = args[0].toLowerCase();

      if (commands.containsKey(subCommand)) {
        List<String> options = commands.get(subCommand).getArguments(args, args.length - 1);
        List<String> result = new ArrayList<>(options);

        for (String option : options) {
          if (!option.startsWith(args[args.length - 1])) {
            result.remove(option);
          }
        }
        return result;
      } else {
        return new ArrayList<>();
      }
    }
  }
}
