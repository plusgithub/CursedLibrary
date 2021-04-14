package com.cursedplanet.cursedlibrary.menu;

import com.cursedplanet.cursedlibrary.lib.Common;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TempCraftingMenu extends CursedMenu {

	public TempCraftingMenu() {
		super(45, "&8Crafting Menu", "crafting_menu");

		lockFilled(fillStatic(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), 10, 11, 12, 19, 20, 21, 28, 29, 30, 24));
		//if (getItemAt(10).getType() == Material.DIAMOND && getItemAt(11).getType() == Material.DIAMOND && getItemAt(12).getType() == Material.DIAMOND)

		addClickUpdate((e) -> {
			Common.runLater(1, () -> {
				if (getItemAt(10) != null && getItemAt(11) != null && getItemAt(12) != null) {
					addClickable(24, new ItemStack(Material.DIAMOND_AXE), (e1) -> {
						fillRectangleStatic(new ItemStack(Material.AIR), 10, 12);
						Common.runLater(1, () -> {
							addStatic(24, new ItemStack(Material.AIR));
							((Player) e1.getWhoClicked()).updateInventory();
						});
					});
				}
			});
		});
	}
}
