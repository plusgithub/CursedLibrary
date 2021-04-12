package com.cursedplanet.cursedlibrary.menu;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class MenuHandler {

	private static final HashMap<UUID, CursedMenu> inventories = new HashMap<>();

	public static void openInventory(Player player, CursedMenu menu) {
		inventories.put(player.getUniqueId(), menu);
	}

	public static void closeInventory(Player player) {
		//Common.runLater(0, () -> {
		//	inventories.remove(player.getUniqueId());
		//});
		inventories.remove(player.getUniqueId());
	}

	public static CursedMenu getInventory(Player player) {
		return inventories.get(player.getUniqueId());
	}

	public static boolean isViewing(Player player) {
		return inventories.containsKey(player.getUniqueId());
	}
}
