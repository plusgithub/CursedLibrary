package com.cursedplanet.cursedlibrary.collection;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CollectionStorage {

	public static HashMap<UUID, List<ItemStack>> playerItems = new HashMap<>();

	public static void setPlayerItems(String playerUUID, List<ItemStack> encodedItems) {
		setPlayerItems(UUID.fromString(playerUUID), encodedItems);
	}

	public static void setPlayerItems(UUID playerUUID, List<ItemStack> encodedItems) {
		playerItems.put(playerUUID, encodedItems);
	}


	public static HashMap<UUID, List<ItemStack>> getPlayerItems() {
		return playerItems;
	}

	public static void addPlayerItem(Player player, ItemStack item) {
		if (item != null || item.getType() != Material.AIR) {
			List<ItemStack> itemList = new ArrayList<>();

			if (playerItems.containsKey(player.getUniqueId())) {
				itemList = playerItems.get(player.getUniqueId());
			}

			itemList.add(item);
			setPlayerItems(player.getUniqueId(), itemList);
		}
	}

	public static void removePlayerItem(Player player, ItemStack item) {
		List<ItemStack> itemList = new ArrayList<>();

		if (playerItems.containsKey(player.getUniqueId())) {
			itemList = playerItems.get(player.getUniqueId());
		}

		itemList.remove(item);

		setPlayerItems(player.getUniqueId(), itemList);
	}

	public static void clearPlayerItems(Player player) {
		List<ItemStack> itemList = new ArrayList<>();

		setPlayerItems(player.getUniqueId(), itemList);
	}
}
