/**
 * The Language class represents language preferences in the PlayLegend Group System. It provides
 * methods to retrieve available languages, load a player's language preference, and set a player's
 * language preference in the database.
 */
package org.joshi234.playlegendgroupsystem.database.group;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joshi234.playlegendgroupsystem.database.DatabaseManager;

public class Language {

  private static Map<String, String> languageCache;
  private int languageId;
  private String name;
  private String code;

  /**
   * Get the language code.
   *
   * @return The language code.
   */
  public String getCode() {
    return code;
  }

  /**
   * Set the language code.
   *
   * @param code The language code.
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Get the language name.
   *
   * @return The language name.
   */
  public String getName() {
    return name;
  }

  /**
   * Set the language name.
   *
   * @param name The language name.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get the language ID.
   *
   * @return The language ID.
   */
  public int getLanguageId() {
    return languageId;
  }

  /**
   * Set the language ID.
   *
   * @param languageId The language ID.
   */
  public void setLanguageId(int languageId) {
    this.languageId = languageId;
  }

  /**
   * Get a list of available languages.
   *
   * @return A list of Language objects representing the available languages.
   */
  public List<Language> getAvailableLanguages() {
    try {
      List<Language> languages = new ArrayList<>();
      PreparedStatement statement = DatabaseManager.getConnection()
          .prepareStatement("SELECT * FROM language");
      ResultSet result = statement.executeQuery();
      while (result.next()) {
        Language language = new Language();
        language.setCode(result.getString("code"));
        language.setLanguageId(result.getInt("languageId"));
        language.setName(result.getString("name"));
        languages.add(language);
      }
      return languages;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Load the language preference for the specified player.
   *
   * @param uuid The UUID of the player.
   * @return The language code for the player's language preference.
   */
  public String loadLanguageByPlayer(String uuid) {
    if (languageCache == null) {
      languageCache = new HashMap<>();
    }

    if (languageCache.containsKey(uuid)) {
      return languageCache.get(uuid);
    } else {
      String code = loadLanguageByPlayerFromDatabase(uuid);
      languageCache.put(uuid, code);
      return getCode();
    }
  }

  /**
   * Load the language preference for the specified player from the database.
   *
   * @param uuid The UUID of the player.
   * @return The language code for the player's language preference.
   */
  private String loadLanguageByPlayerFromDatabase(String uuid) {
    try {
      List<Language> languages = new ArrayList<>();

      PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(
          "SELECT * FROM language JOIN player p ON p.languageId = language.languageId AND p.uuid = ?");
      statement.setString(1, uuid);

      ResultSet result = statement.executeQuery();

      if (result.next()) {
        setCode(result.getString("code"));
        setLanguageId(result.getInt("languageId"));
        setName(result.getString("name"));
      }

      return getCode();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Set the language preference for the specified player in the database.
   *
   * @param uuid The UUID of the player.
   * @param id   The ID of the language preference.
   */
  public void setPlayerLanguage(String uuid, int id) {
    try {
      PreparedStatement statement = DatabaseManager.getConnection()
          .prepareStatement("UPDATE player SET languageid = ? WHERE uuid = ?");
      statement.setInt(1, id);
      statement.setString(2, uuid);

      statement.executeUpdate();

      statement.close();

      languageCache.replace(uuid, loadLanguageByPlayerFromDatabase(uuid));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
