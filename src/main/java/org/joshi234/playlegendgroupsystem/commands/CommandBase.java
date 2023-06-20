package org.joshi234.playlegendgroupsystem.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.joshi234.playlegendgroupsystem.commands.subcommands.SubCommand;
import org.joshi234.playlegendgroupsystem.commands.tabcompleter.SubCommandTabCompleter;

public abstract class CommandBase implements CommandExecutor {

  public final Map<String, SubCommand> commands = new HashMap<>();

  /**
   * Constructs a CommandBase object with a SubCommandTabCompleter.
   *
   * @param tabCompleter The SubCommandTabCompleter used for tab completion.
   */
  public CommandBase(SubCommandTabCompleter tabCompleter) {
    registerCommands();
    tabCompleter.setCommands(commands);
  }

  /**
   * Registers the subcommands.
   */
  public void registerCommands() {
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender,
      org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
    return true;
  }

  /**
   * Formats the command arguments, allowing the use of double quotes in the arguments.
   *
   * @param args The command arguments.
   * @return The formatted command arguments.
   */
  public String[] argFormatter(String[] args) {
    List<String> result = new ArrayList<>(List.of(args));

    StringBuilder temp = new StringBuilder();

    for (String arg : args) {

      if (temp.toString().equals("")) {
        if (arg.startsWith("\"")) {
          temp = new StringBuilder(arg);
          result.remove(arg);
        }
      } else {
        temp.append(" ");

        if (arg.endsWith("\"")) {
          temp.append(arg);
          result.set(result.indexOf(arg), temp.toString().replace("\"", ""));
          temp = new StringBuilder();
        } else {
          temp.append(arg);
          result.remove(arg);
        }
      }
    }

    String[] resultArray = new String[result.size()];

    resultArray = result.toArray(resultArray);

    return resultArray;
  }
}
