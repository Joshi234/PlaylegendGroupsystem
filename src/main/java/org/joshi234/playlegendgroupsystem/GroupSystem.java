package org.joshi234.playlegendgroupsystem;

import java.sql.SQLException;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.joshi234.playlegendgroupsystem.commands.GroupCommand;
import org.joshi234.playlegendgroupsystem.commands.LanguageCommand;
import org.joshi234.playlegendgroupsystem.commands.tabcompleter.GroupTabCompleter;
import org.joshi234.playlegendgroupsystem.commands.tabcompleter.LanguageCommandTabCompleter;
import org.joshi234.playlegendgroupsystem.database.DatabaseManager;
import org.joshi234.playlegendgroupsystem.database.group.JoinGroup;
import org.joshi234.playlegendgroupsystem.database.group.Player;

/**
 * The GroupSystem class is the main plugin class for the Legend Group System plugin.
 */
public class GroupSystem extends JavaPlugin implements Listener {

  private static GroupSystem instance;

  /**
   * Retrieves the instance of the GroupSystem plugin.
   *
   * @return The GroupSystem instance.
   */
  public static GroupSystem getInstance() {
    return instance;
  }

  @Override
  public void onEnable() {
    instance = this;

    Bukkit.getPluginManager().registerEvents(this, this);
    registerCommands();
    startConfig();
    Configuration.setConfig(this.getConfig());
    try {
      DatabaseManager.connect(Configuration.getString("database.user"),
          Configuration.getString("database.password"), "group",
          (Configuration.getString("database.connection-string")));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    Configuration.loadLanguageFiles(this.getDataFolder());

    getServer().getPluginManager().registerEvents(new ChatListener(), this);

    SignHandler.loadSigns();
  }

  private void startConfig() {
    this.saveDefaultConfig();
  }

  private void registerCommands() {
    GroupTabCompleter completer = new GroupTabCompleter();
    this.getCommand("group").setTabCompleter(completer);
    this.getCommand("group").setExecutor(new GroupCommand(completer));
    LanguageCommandTabCompleter langCompleter = new LanguageCommandTabCompleter();
    this.getCommand("language").setTabCompleter(langCompleter);
    this.getCommand("language").setExecutor(new LanguageCommand(langCompleter));
  }

  /**
   * Handles the PlayerJoinEvent.
   *
   * @param event The PlayerJoinEvent.
   */
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    event.joinMessage(null);

    String prefix = new JoinGroup().getPrefix(event.getPlayer().getUniqueId().toString(), true);
    Player player = new Player(event.getPlayer().getUniqueId().toString(),
        event.getPlayer().getName());

    event.getPlayer().playerListName(
        LegacyComponentSerializer.legacySection().toBuilder().character('&').build()
            .deserialize("[" + prefix + "&f]" + player.getName()));

    event.getPlayer().sendMessage(
        new StringFormatter().getLocalizedFormattedString("miscellaneous.player-join",
                player.getUuid()).replaceKey("prefix", prefix).
            replaceKey("playername", event.getPlayer().getName()).formatMessage().getMessage());
  }
}
