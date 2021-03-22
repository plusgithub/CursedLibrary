package com.cursedplanet.cursedlibrary.menu;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;

public class MenuBuilder {

	@Getter
	private int size = 27;
	
	protected static String id = "";

	protected static String getID() {
		return id;
	}

	@Getter
	private String title = "&7Menu";

	public MenuBuilder() {
	}

	public MenuBuilder setID(String id) {
		this.id = id;
		return this;
	}

	public MenuBuilder setTitle(String title) {
		this.title = Common.colorize(title);
		return this;
	}

	public MenuBuilder setSize(int size) {
		this.size = (size % 9 == 0) && size <= 54 ? size : 27;
		CursedMenu.contents = new ItemStack[size];
		return this;
	}

	public void build() {
		InventoryCreator inventory = InventoryCreator.of(size, title, id);
		//NBTContainer container = new NBTContainer(inventory);
		//container.setString("Menu_ID", id);
		//inventory.setItem(55, ItemCreator.of(CompMaterial.AIR, this.id).build().make());
		CursedMenu.inventory = inventory;
	}
}
