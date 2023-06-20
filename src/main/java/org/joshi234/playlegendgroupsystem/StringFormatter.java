package org.joshi234.playlegendgroupsystem;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;

/**
 * The StringFormatter class provides methods for formatting strings with color codes and
 * placeholders.
 */
public class StringFormatter {

  private static final LegacyComponentSerializer componentSerializer = LegacyComponentSerializer.legacySection()
      .toBuilder().character('&').build();
  private String message;
  private String languageId;

  /**
   * Formats a string by translating color codes and placeholders.
   *
   * @param key The key used to retrieve the string from the configuration.
   * @return The StringFormatter instance.
   */
  public StringFormatter getFormattedString(String key) {
    message = ChatColor.translateAlternateColorCodes('&', Configuration.getString(key));
    return this;
  }

  /**
   * Formats a string by translating color codes and placeholders. Converts a list of strings to a
   * single string.
   *
   * @param key The key used to retrieve the list of strings from the configuration.
   * @return The StringFormatter instance.
   */
  public StringFormatter getFormattedStringListAsString(String key) {
    message = ChatColor.translateAlternateColorCodes('&', Configuration.getStringListAsString(key));
    return this;
  }

  /**
   * Formats a localized string by translating color codes and placeholders.
   *
   * @param key  The key used to retrieve the localized string from the configuration.
   * @param uuid The UUID of the player for localization.
   * @return The StringFormatter instance.
   */
  public StringFormatter getLocalizedFormattedString(String key, String uuid) {
    message = ChatColor.translateAlternateColorCodes('&',
        Configuration.getLocalizedString(uuid, key));
    return this;
  }

  /**
   * Formats a localized string by translating color codes and placeholders. Converts a list of
   * strings to a single string.
   *
   * @param key  The key used to retrieve the list of localized strings from the configuration.
   * @param uuid The UUID of the player for localization.
   * @return The StringFormatter instance.
   */
  public StringFormatter getLocalizedFormattedStringListAsString(String key, String uuid) {
    message = ChatColor.translateAlternateColorCodes('&',
        Configuration.getLocalizedStringListAsString(uuid, key));
    return this;
  }

  /**
   * Replaces a placeholder key with a value in the formatted string.
   *
   * @param key   The placeholder key to replace.
   * @param value The value to replace the placeholder key with.
   * @return The StringFormatter instance.
   */
  public StringFormatter replaceKey(String key, String value) {
    message = message.replace("[" + key + "]", value);
    return this;
  }

  /**
   * Gets the formatted message.
   *
   * @return The formatted message.
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets the message to a pre-formatted string.
   *
   * @param message The pre-formatted string message.
   * @return The StringFormatter instance.
   */
  public StringFormatter setFormattedMessage(String message) {
    this.message = ChatColor.translateAlternateColorCodes('&', message);
    return this;
  }

  /**
   * Formats the message by translating color codes.
   *
   * @return The StringFormatter instance.
   */
  public StringFormatter formatMessage() {
    this.message = ChatColor.translateAlternateColorCodes('&', message);
    return this;
  }

  /**
   * Gets the message as a Kyori Component.
   *
   * @return The message as a Kyori Component.
   */
  public Component getComponent() {
    return componentSerializer.deserialize(this.message);
  }
}
