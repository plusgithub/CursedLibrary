package com.cursedplanet.cursedlibrary.collection;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;

import java.util.*;

public final class CollectionAPI {

	public static HashMap<UUID, List<ItemStack>> getCollectionItems() {
		return CollectionStorage.playerItems;
	}

	public static List<ItemStack> getPlayerItems(Player player) {
		List<ItemStack> itemList = new ArrayList<>();

		if (getCollectionItems().containsKey(player.getUniqueId())) {
			itemList = getCollectionItems().get(player.getUniqueId());
		}
		return itemList;
	}

	public static void setCollectionItems(String playerUUID, List<ItemStack> encodedItems) {
		setCollectionItems(UUID.fromString(playerUUID), encodedItems);
	}

	public static void setCollectionItems(UUID playerUUID, List<ItemStack> encodedItems) {
		CollectionStorage.setPlayerItems(playerUUID, encodedItems);
	}

	public static void clearPlayerItems(Player player) {
		List<ItemStack> itemList = new ArrayList<>();
		setCollectionItems(player.getUniqueId(), itemList);
	}

	public static void addPlayerItem(Player player, ItemStack item) {
		if (item != null || item.getType() != Material.AIR) {
			addPlayerItems(player, new ArrayList<>(Collections.singletonList(item)));
		}
	}

	public static void addPlayerItems(Player player, List<ItemStack> items) {
		List<ItemStack> itemList = getPlayerItems(player);

		itemList.removeIf(item -> item == null || item.getType() == Material.AIR);

		itemList.addAll(items);
		setCollectionItems(player.getUniqueId(), itemList);
	}

	public static void removePlayerItem(Player player, ItemStack item) {
		if (item != null || item.getType() != Material.AIR) {
			removePlayerItems(player, new ArrayList<>(Collections.singletonList(item)));
		}
	}

	public static void removePlayerItems(Player player, List<ItemStack> items) {
		List<ItemStack> itemList = getPlayerItems(player);

		itemList.removeIf(item -> item == null || item.getType() == Material.AIR);

		itemList.removeAll(items);
		setCollectionItems(player.getUniqueId(), itemList);
	}

	public static void openCollectionMenu(Player player) {
		openCollectionMenu(player, player);
	}

	public static void openCollectionMenu(Player player, Player target) {
		CollectMenu.setTarget(target);
		CollectMenu.INVENTORY.open(player);
	}

	public static void giveItemNaturally(Player player, ItemStack item) {
		if (!player.getInventory().addItem(item).isEmpty()) {
			Common.tellNoPrefix(player, ((String) CollectionLoader.getConfigPart("item_added_collect")));
			addPlayerItem(player, item);
		}
	}

	public static void giveItemNaturally(Player player, List<ItemStack> items) {
		int amountAdded = 0;
		for (ItemStack iteratedItem : items) {
			if (!player.getInventory().addItem(iteratedItem).isEmpty()) {
				amountAdded++;
				addPlayerItem(player, iteratedItem);
			}
		}
		if (amountAdded > 0) {
			Common.tellNoPrefix(player, ((String) CollectionLoader.getConfigPart("items_added_collect")).replaceAll("\\{amount}", String.valueOf(amountAdded)).replaceAll("\\{item/items}", (amountAdded > 1 ? "items" : "item")).replaceAll("\\{has/have}", (amountAdded > 1 ? "have" : "has")));
		}
	}
}
