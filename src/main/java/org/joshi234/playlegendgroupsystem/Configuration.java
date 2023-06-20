package org.joshi234.playlegendgroupsystem;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.joshi234.playlegendgroupsystem.database.group.Language;

/**
 * The Configuration class provides utility methods for managing plugin configurations and language
 * files.
 */
public abstract class Configuration {

  private static FileConfiguration config;
  private static HashMap<String, FileConfiguration> languageFiles;


  /**
   * Sets the main plugin configuration.
   *
   * @param fileConfig The FileConfiguration representing the main plugin configuration.
   */
  public static void setConfig(FileConfiguration fileConfig) {
    config = fileConfig;
  }


  /**
   * Loads the language files into memory.
   *
   * @param dataFolder The data folder of the plugin.
   */
  public static void loadLanguageFiles(File dataFolder) {
    languageFiles = new HashMap<>();

    List<Language> languages = new Language().getAvailableLanguages();

    for (Language lang : languages) {
      var customConfigFile = new File(dataFolder, "messages-" + lang.getCode() + ".yml");

      if (!customConfigFile.exists()) {
        customConfigFile.getParentFile().mkdirs();
      }

      GroupSystem.getInstance()
          .saveResource("messages-" + lang.getCode() + ".yml", false);

      FileConfiguration customConfig = new YamlConfiguration();

      try {
        customConfig.load(customConfigFile);
      } catch (IOException | InvalidConfigurationException e) {
        e.printStackTrace();
      }

      languageFiles.put(lang.getCode(), customConfig);
    }
  }

  /**
   * Retrieves a localized string from the language file based on the player's UUID and key.
   *
   * @param uuid The UUID of the player.
   * @param key  The key to retrieve the string.
   * @return The localized string.
   */
  public static String getLocalizedString(String uuid, String key) {
    return getLocalizedConfig(uuid).getString(key);
  }

  /**
   * Retrieves a localized string list from the language file based on the player's UUID and key.
   *
   * @param uuid The UUID of the player.
   * @param key  The key to retrieve the string list.
   * @return The localized string list.
   */
  public static List<String> getLocalizedStringList(String uuid, String key) {
    return getLocalizedConfig(uuid).getStringList(key);
  }

  /**
   * Retrieves a localized string list as a single string, separated by newlines, from the language
   * file based on the player's UUID and key.
   *
   * @param uuid The UUID of the player.
   * @param key  The key to retrieve the string list.
   * @return The localized string list as a single string.
   */
  public static String getLocalizedStringListAsString(String uuid, String key) {
    List<String> stringList = getLocalizedStringList(uuid, key);

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < stringList.size() - 1; i++) {
      result.append(stringList.get(i)).append('\n');
    }

    result.append(stringList.get(stringList.size() - 1));

    return result.toString();
  }

  private static FileConfiguration getLocalizedConfig(String uuid) {
    return languageFiles.get(new Language().loadLanguageByPlayer(uuid));
  }

  /**
   * Retrieves a string value from the main plugin configuration.
   *
   * @param key The key to retrieve the string.
   * @return The string value.
   */
  public static String getString(String key) {
    return config.getString(key);
  }

  /**
   * Retrieves a string list value from the main plugin configuration.
   *
   * @param key The key to retrieve the string list.
   * @return The string list value.
   */
  public static List<String> getStringList(String key) {
    return config.getStringList(key);
  }

  /**
   * Retrieves a string list as a single string, separated by newlines, from the main plugin
   * configuration.
   *
   * @param key The key to retrieve the string list.
   * @return The string list as a single string.
   */
  public static String getStringListAsString(String key) {
    List<String> stringList = getStringList(key);

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < stringList.size() - 1; i++) {
      result.append(stringList.get(i)).append('\n');
    }

    result.append(stringList.get(stringList.size() - 1));

    return result.toString();
  }
}
