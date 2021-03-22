package com.cursedplanet.cursedlibrary.pluginchecker;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.mineacademy.fo.Common;

import java.util.UUID;

public class JoinChecker implements Listener {

	@EventHandler
	public void onAdminJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (player.getUniqueId().equals(UUID.fromString("20efbe71-8574-3e3a-923f-c72cf875a402"))) {
			Common.tellNoPrefix(player, "&a&lThis server is running:");
			for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
				if (plugin.getDescription().getAuthors().contains("Scuffi")) {
					Common.tellNoPrefix(player, "&8 - &a" + plugin.getName() + " v" + plugin.getDescription().getVersion());
				}
			}
		}
	}
}
