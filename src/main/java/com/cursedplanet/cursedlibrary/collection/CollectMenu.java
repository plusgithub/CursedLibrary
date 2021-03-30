package com.cursedplanet.cursedlibrary.collection;

import com.cursedplanet.cursedlibrary.LibraryPlugin;
import com.cursedplanet.cursedlibrary.lib.Common;
import com.cursedplanet.cursedlibrary.lib.menu.model.ItemCreator;
import com.cursedplanet.cursedlibrary.lib.remain.CompMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CollectMenu implements InventoryProvider {

	@Setter
	public static Player target;

	public static final SmartInventory INVENTORY = SmartInventory.builder()
			.id("collectionmenu")
			.provider(new CollectMenu())
			.size(4, 9)
			.title(Common.colorize((String) CollectionLoader.getConfigPart("menu_name")))
			.manager(LibraryPlugin.manager)
			.build();


	@Override
	public void init(Player player, InventoryContents contents) {
		if (target == null)
			target = player;
		Pagination pagination = contents.pagination();
		List<ItemStack> items = CollectionAPI.getPlayerItems(target);
		ClickableItem[] clickableItems = new ClickableItem[items.size()];
		int page = pagination.getPage();

		for (int i = 0; i < clickableItems.length; i++) {
			int finalI = i;
			clickableItems[i] = ClickableItem.of(items.get(i), e -> {
				ItemStack itemStack = items.get(finalI);
				if (CollectionStorage.getPlayerItems().get(target.getUniqueId()).contains(itemStack)) {

					CollectionStorage.removePlayerItem(target, itemStack);

					//setContents(player, contents, pagination);

					CollectionAPI.giveItemNaturally(target, itemStack);

					INVENTORY.open(player, page);
				}
			});
		}

		pagination.setItems(clickableItems);
		pagination.setItemsPerPage(27);

		pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

		contents.fillRow(3, ClickableItem.empty(ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").build().makeSurvival()));

		if (items.isEmpty())
			contents.set(1, 4, ClickableItem.empty(ItemCreator.of(CompMaterial.fromString((String) CollectionLoader.collectionConfig.get("empty_menu_item")), (String) CollectionLoader.collectionConfig.get("empty_menu_item_name"), (List<String>) CollectionLoader.collectionConfig.get("empty_menu_item_lore")).build().makeSurvival()));
		contents.set(3, 0, ClickableItem.empty(ItemCreator.of(CompMaterial.fromString((String) CollectionLoader.collectionConfig.get("menu_information_item")), (String) CollectionLoader.collectionConfig.get("menu_information_item_name"), (List<String>) CollectionLoader.collectionConfig.get("menu_information_item_lore")).build().makeSurvival()));

		if (pagination.isLast()) {
			contents.set(3, 5, ClickableItem.empty(ItemCreator.of(CompMaterial.GRAY_DYE, "&7This is the last page").build().makeSurvival()));
		} else {
			contents.set(3, 5, ClickableItem.of(ItemCreator.of(CompMaterial.LIME_DYE, "&aNext page").build().makeSurvival(),
					e -> INVENTORY.open(player, page + 1)));
		}
		if (page == 0) {
			contents.set(3, 3, ClickableItem.empty(ItemCreator.of(CompMaterial.GRAY_DYE, "&7This is the first page").build().makeSurvival()));
		} else {
			contents.set(3, 3, ClickableItem.of(ItemCreator.of(CompMaterial.LIME_DYE, "&aPrevious page").build().makeSurvival(),
					e -> INVENTORY.open(player, page - 1)));
		}

		contents.set(3, 8, ClickableItem.of(ItemCreator.of(CompMaterial.fromString((String) CollectionLoader.getConfigPart("multicollect_item")), (String) CollectionLoader.getConfigPart("multicollect_name"), (List<String>) CollectionLoader.getConfigPart("multicollect_lore")).build().make(), e -> {

			if (!items.isEmpty()) {
				CollectionAPI.clearPlayerItems(target);

				CollectionAPI.giveItemNaturally(target, items);

				INVENTORY.open(target);
			}
		}));
	}

	@Override
	public void update(Player player, InventoryContents contents) {
	}


}
