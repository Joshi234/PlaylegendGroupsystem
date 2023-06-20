package org.joshi234.playlegendgroupsystem;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.joshi234.playlegendgroupsystem.database.group.JoinGroup;

public class ChatListener implements Listener {

  @EventHandler
  private void onPlayerChat(AsyncPlayerChatEvent e) {
    String message = new StringFormatter().setFormattedMessage(
            "[" + new JoinGroup().getPrefix(e.getPlayer().getUniqueId().toString()) + "&f] %s: %s")
        .getMessage();
    e.setFormat(message);
  }
}
