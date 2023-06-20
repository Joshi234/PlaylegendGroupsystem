package org.joshi234.playlegendgroupsystem.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.joshi234.playlegendgroupsystem.commands.subcommands.AddSubCommand;
import org.joshi234.playlegendgroupsystem.commands.subcommands.CreateSubCommand;
import org.joshi234.playlegendgroupsystem.commands.subcommands.DeleteSubCommand;
import org.joshi234.playlegendgroupsystem.commands.subcommands.EditSubCommand;
import org.joshi234.playlegendgroupsystem.commands.subcommands.HelpSubCommand;
import org.joshi234.playlegendgroupsystem.commands.subcommands.RemoveSubCommand;
import org.joshi234.playlegendgroupsystem.commands.subcommands.ShowSubCommand;
import org.joshi234.playlegendgroupsystem.commands.subcommands.SignSubCommand;
import org.joshi234.playlegendgroupsystem.commands.tabcompleter.GroupTabCompleter;

public class GroupCommand extends CommandBase {

  /**
   * Constructs a GroupCommand object with a GroupTabCompleter.
   *
   * @param tabCompleter The GroupTabCompleter used for tab completion.
   */
  public GroupCommand(GroupTabCompleter tabCompleter) {
    super(tabCompleter);
  }

  @Override
  public void registerCommands() {
    this.commands.put("help", new HelpSubCommand());
    this.commands.put("show", new ShowSubCommand());
    this.commands.put("create", new CreateSubCommand());
    this.commands.put("add", new AddSubCommand());
    this.commands.put("remove", new RemoveSubCommand());
    this.commands.put("delete", new DeleteSubCommand());
    this.commands.put("edit", new EditSubCommand());
    this.commands.put("sign", new SignSubCommand());
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender,
      org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
    args = argFormatter(args);
    if (sender instanceof Player) {
      if (args.length > 0) {
        String subCommand = args[0].toLowerCase();
        if (commands.containsKey(subCommand)) {
          commands.get(subCommand).onCommand((Player) sender, command, args);
        } else {
          commands.get("help").onCommand((Player) sender, command, args);
        }
      }
    }
    return true;

    // If the player (or console) uses our command correctly, we can return true
  }
}
