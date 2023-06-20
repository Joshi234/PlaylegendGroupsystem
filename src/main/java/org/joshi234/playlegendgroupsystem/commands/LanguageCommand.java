package org.joshi234.playlegendgroupsystem.commands;

import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.joshi234.playlegendgroupsystem.StringFormatter;
import org.joshi234.playlegendgroupsystem.commands.tabcompleter.SubCommandTabCompleter;
import org.joshi234.playlegendgroupsystem.database.group.Language;

public class LanguageCommand extends CommandBase {

  /**
   * Constructs a LanguageCommand object with a SubCommandTabCompleter.
   *
   * @param tabCompleter The SubCommandTabCompleter used for tab completion.
   */
  public LanguageCommand(SubCommandTabCompleter tabCompleter) {
    super(tabCompleter);
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender,
      org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (sender instanceof Player) {
      if (args.length == 1) {
        List<Language> availableLanguages = new Language().getAvailableLanguages();
        for (Language lang : availableLanguages) {
          if (lang.getName().equalsIgnoreCase(args[0])) {
            new Language().setPlayerLanguage(((Player) sender).getUniqueId().toString(),
                lang.getLanguageId());
            sender.sendMessage(new StringFormatter().getLocalizedFormattedString("language.success",
                ((Player) sender).getUniqueId().toString()).getMessage());
            return true;
          }
        }
      }
    } else {
      return false;
    }

    // If the player (or console) uses our command correctly, we can return true
    return false;
  }
}
