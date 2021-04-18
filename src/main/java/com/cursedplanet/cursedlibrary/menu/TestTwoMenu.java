package com.cursedplanet.cursedlibrary.menu;

import com.cursedplanet.cursedlibrary.lib.menu.model.ItemCreator;
import com.cursedplanet.cursedlibrary.lib.remain.CompMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class TestTwoMenu extends CursedMenu {


	public TestTwoMenu() {
		super(54, "Cool menu", "cool_menu");

		/*addStatic(5, new ItemStack(Material.DIAMOND)).lock();

		addClickable(6, new ItemStack(Material.IRON_BARS), () -> {
			addStatic(6, new ItemStack(Material.DIAMOND)).lock();
		}).lock();


		lockFilled(fillRectangle(new ItemStack(Material.DIAMOND, 5), 10, 33));

		repeatingTask(20, () -> {
			Common.tell(getViewers().get(0), "hello");
		});

		addCloseCallback(e -> {
			Common.tell(e.getPlayer(), "You closed the menu");
		});

		setOpenSound(Sound.BLOCK_IRON_DOOR_OPEN);
		setCloseSound(Sound.BLOCK_IRON_TRAPDOOR_CLOSE);

		setPattern("#&#&#&#&#", "&#&#&#&#&");
		assignPatternItem('#', new ItemStack(Material.IRON_BLOCK));
		assignPatternItem('&', new ItemStack(Material.GOLD_BLOCK));
		//compilePattern();

		lockFilled(fillPattern());

		MenuItem[] items = compilePattern();
		for (int i = 0; i < items.length; i++) {

			int slot = i;

			items[i] = addClickable(slot, items[i].getItem(), () -> {
				Common.tell(getViewers().get(0), "this is slot " + slot);
			});

			items[i].lock();
		}*/

		//fillStatic(new ItemStack(Material.AIR));

		setPattern("?&?&?&?&?", "&0000000&", "?0000000?", "&0000000&", "?0000000?", "&?&?&?&?&");
		assignPatternItem('?', new ItemStack(Material.BLUE_STAINED_GLASS_PANE));
		assignPatternItem('&', new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE));
		lockFilled(fillPatternStatic());

		setPagePattern("&#######&", "&&#####&&", "&&&###&&&", "&&&&#&&&&", "&&&&&&&&&");

		ItemStack[] items = new ItemStack[Material.values().length];


		for (int i = 0; i < Material.values().length; i++) {
			items[i] = new ItemStack(Material.values()[i]);
		}

		addPageItems(items);
		lockFilled(fillPagesStatic());

		addClickable(51, ItemCreator.of(CompMaterial.ARROW, "&aNext page").build().makeSurvival(), (e) -> {
			nextPage();
			lockFilled(fillPagesStatic());
		}).lock();

		addClickable(47, ItemCreator.of(CompMaterial.ARROW, "&cPrevious page").build().makeSurvival(), (e) -> {
			MenuPrompt.engageInPrompt((Player) getViewers().get(0), this, 100, string -> {
				addStatic(47, new ItemStack(Objects.requireNonNull(Material.getMaterial(string))));
			}, "", "&b&lType your input below:", "");
		}).lock();

	}
}
