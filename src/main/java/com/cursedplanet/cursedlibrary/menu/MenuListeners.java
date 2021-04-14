package com.cursedplanet.cursedlibrary.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class MenuListeners implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getClickedInventory() != null) {
			Player player = (Player) event.getWhoClicked();
			if (MenuHandler.isViewing(player)) {

				CursedMenu menu = MenuHandler.getInventory((Player) event.getWhoClicked());

				if (!event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {

					if (menu.updateTask != null)
						menu.updateTask.accept(event);

					//Check for locked slots
					if (menu.lockedSlots.get(event.getSlot()))
						event.setCancelled(true);

					//Check for clickable items
					if (menu.slotRunnables.get(event.getSlot()) != null) {
						menu.slotRunnables.get(event.getSlot()).accept(event);
					}
				}

				//Check if a shift clicked item has been shifted into an empty locked slot
				if (event.getClick() == ClickType.SHIFT_RIGHT || event.getClick() == ClickType.SHIFT_LEFT) {

					if (menu.updateTask != null)
						menu.updateTask.accept(event);

					if (event.getClickedInventory().getType() == InventoryType.PLAYER) { //Check if a player is shift clicking IN to the inventory instead of OUT
						ItemStack item = new ItemStack(Objects.requireNonNull(event.getCurrentItem()));
						item.setAmount(1); //Set the amount to 1 to avoid itemstack mismatching

						for (int i = 0; i < menu.getSize(); i++) {

							if (menu.inv.getItem(i) != null) {
								ItemStack temp = new ItemStack(menu.inv.getItem(i));

								temp.setAmount(1);

								if (Objects.equals(temp, item) && menu.lockedSlots.get(i)) {
									event.setCancelled(true); //If the clicked item equals the current item, cancels the shift click
								}
							}
						}
					} else {
						if (menu.slotRunnables.get(event.getSlot()) != null) {
							menu.slotRunnables.get(event.getSlot()).accept(event);
						}
					}

					if (menu.lockedSlots.get(menu.inv.firstEmpty()) != null) {
						if (menu.lockedSlots.get(menu.inv.firstEmpty())) {
							event.setCancelled(true);
						}
					}
				}

				if (event.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (MenuHandler.isViewing(player) && !event.getInventory().getType().equals(InventoryType.PLAYER)) {
			CursedMenu menu = MenuHandler.getInventory((Player) event.getWhoClicked());

			//if (menu.updateTask != null) {
			//	menu.updateTask.accept(event);
			//}

			for (int slot : event.getRawSlots()) {
				if (slot >= player.getOpenInventory().getTopInventory().getSize())
					continue;


				if (menu.lockedSlots.get(slot)) {
					event.setCancelled(true);
					break;
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if (MenuHandler.isViewing((Player) event.getPlayer())) {
			CursedMenu menu = MenuHandler.getInventory((Player) event.getPlayer());

			menu.stopRunnables();

			if (menu.consumer != null)
				menu.consumer.accept(event);

			if (menu.closeSound != null)
				((Player) event.getPlayer()).playSound(event.getPlayer().getLocation(), menu.closeSound, 1, 1);

			MenuHandler.closeInventory((Player) event.getPlayer());
		}
	}
}
