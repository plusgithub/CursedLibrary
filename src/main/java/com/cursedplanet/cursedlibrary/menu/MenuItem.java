package com.cursedplanet.cursedlibrary.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class MenuItem {

	private final ItemStack item;
	//private Runnable consumer;
	private final int slot;
	private final CursedMenu menu;

	public MenuItem(int slot, ItemStack item, CursedMenu menu) {
		this.item = item;
		//this.consumer = consumer;
		this.slot = slot;
		this.menu = menu;
	}

	protected MenuItem empty() {
		//menu.contents.put(slot, item);
		menu.inv.setItem(slot, item);
		menu.lockedSlots.put(slot, false);
		return clickable((e) -> {
		});
	}

	protected MenuItem clickable(Consumer<InventoryClickEvent> consumer) {
		//menu.contents.put(slot, item);
		menu.inv.setItem(slot, item);
		menu.slotRunnables.put(slot, consumer);
		menu.lockedSlots.put(slot, false);
		//menu.updateInventory();
		return new MenuItem(slot, item, menu);
	}

	public ItemStack getItem() {
		return item;
	}

	public int getSlot() {
		return this.slot;
	}

	public void lock() {
		menu.lockedSlots.put(slot, true);
	}

	public void lock(MenuItem[] items) {
		for (MenuItem item : items) {
			item.lock();
		}
	}

}

