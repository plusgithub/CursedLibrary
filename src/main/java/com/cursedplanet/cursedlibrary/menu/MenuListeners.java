package com.cursedplanet.cursedlibrary.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.mineacademy.fo.Common;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MenuListeners implements Listener {

	protected static HashMap<String, List<Integer>> lockedSlots = new HashMap<>();
	protected static HashMap<UUID, List<Integer>> currentInv = new HashMap<>();

	@EventHandler
	public void onMenuClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		CursedMenu.stopRunnables(player);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Inventory inv = event.getClickedInventory();
		String id = event.getWhoClicked().getOpenInventory().getTitle().contains("ID:") ? event.getWhoClicked().getOpenInventory().getTitle().split("ID:")[1] : "";
		Common.tell(event.getWhoClicked(), id);
		if (lockedSlots.get(id).contains(event.getSlot()))
			event.setCancelled(true);
	}

	@EventHandler
	public void inventoryOpenEvent(InventoryOpenEvent event) {
		String id = event.getPlayer().getOpenInventory().getTitle().contains("ID:") ? event.getPlayer().getOpenInventory().getTitle().split("ID:")[1] : "";
	}
}
