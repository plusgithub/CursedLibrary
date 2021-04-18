package com.cursedplanet.cursedlibrary.menu;

import com.cursedplanet.cursedlibrary.lib.Common;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MenuPrompt implements Listener {

	//private CursedMenu menu = null;
	public static Map<Player, Consumer<String>> engagedPlayers = new HashMap<>();
	private static final Map<Player, CursedMenu> lastMenu = new HashMap<>();

	public static void engageInPrompt(Player player, CursedMenu menu, Integer cancelTicks, Consumer<String> action, String... lines) {

		lastMenu.put(player, menu);

		//this.menu = menu;

		engagedPlayers.put(player, action); // Engage our player in a prompt for listener purposes

		menu.close(player);

		Common.runLater(cancelTicks, () -> {
			if (player.getOpenInventory() != null && getLastMenu(player) != null) {
				disengagePrompt(player);
			}
		});

		Common.tellNoPrefix(player, lines);
	}

	private static void disengagePrompt(Player player) {
		engagedPlayers.remove(player);
		getLastMenu(player).display(player);
		//Common.tell(player, String.valueOf(getLastMenu(player)));
		lastMenu.remove(player);
	}

	private static CursedMenu getLastMenu(Player player) {
		return lastMenu.get(player);
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		boolean inPrompt = engagedPlayers.containsKey(player);

		if (inPrompt) {
			Common.runLater(0, () -> {
				engagedPlayers.get(player).accept(event.getMessage());
				event.setCancelled(true);
				disengagePrompt(player);
			});
		}

		event.getRecipients().removeAll(engagedPlayers.keySet());
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		boolean inPrompt = engagedPlayers.containsKey(player);

		if (inPrompt) {
			engagedPlayers.remove(player);
		}
	}
}
