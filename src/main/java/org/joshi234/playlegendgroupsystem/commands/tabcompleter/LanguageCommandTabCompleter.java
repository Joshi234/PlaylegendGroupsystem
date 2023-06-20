package org.joshi234.playlegendgroupsystem.commands.tabcompleter;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joshi234.playlegendgroupsystem.database.group.Language;

/**
 * The LanguageCommandTabCompleter class provides tab completion functionality for the language
 * command. It extends the SubCommandTabCompleter class and is specifically designed for the
 * language command.
 */
public class LanguageCommandTabCompleter extends SubCommandTabCompleter {

  /**
   * Called when tab completion is requested for the language command. This method overrides the
   * onTabComplete method in the SubCommandTabCompleter class and provides custom tab completion
   * logic for the language command.
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

    if (args.length == 1) {
      List<String> result = new ArrayList<>();
      List<Language> availableLanguages = new Language().getAvailableLanguages();
      for (Language lang : availableLanguages) {
        result.add(lang.getName());
      }
      return result;
    }
    return new ArrayList<>();
  }
}
