package com.cursedplanet.cursedlibrary.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class MenuItem {

	private ItemStack item;
	private Consumer<InventoryClickEvent> consumer;
	private int slot;

	private MenuItem(int slot, ItemStack item, Consumer<InventoryClickEvent> consumer) {
		this.item = item;
		this.consumer = consumer;
		this.slot = slot;
	}

	public static MenuItem empty(int slot, ItemStack item) {
		CursedMenu.contents[slot] = item;
		return clickable(slot, item, e -> {
		});
	}

	public static MenuItem clickable(int slot, ItemStack item, Consumer<InventoryClickEvent> consumer) {
		CursedMenu.contents[slot] = item;
		return new MenuItem(slot, item, consumer);
	}

	public void run(InventoryClickEvent e) {
		consumer.accept(e);
	}

	public ItemStack getItem() {
		return item;
	}

	public void lock() {
		CursedMenu.lockedSlots.add(slot);
	}

}

