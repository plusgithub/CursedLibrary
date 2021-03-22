package com.cursedplanet.cursedlibrary.collection;

import com.cursedplanet.cursedlibrary.LibraryPlugin;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CollectionLoader extends CollectionStorage {

	public static final HashMap<String, Object> collectionConfig = new HashMap<>();
	protected static final YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(LibraryPlugin.getInstance().getDataFolder(), "config.yml"));
	@Getter
	public static final String tellPrefix = config.getString("prefix");

	public static Object getConfigPart(String part) {
		return collectionConfig.get(part);
	}

	public static void loadCollectionConfig() {
		for (String string : config.getConfigurationSection("collections").getKeys(false)) {
			collectionConfig.put(string, config.get("collections." + string));
		}
	}


	public static void loadCollectionItems() {
		YamlConfiguration c = YamlConfiguration.loadConfiguration(new File(LibraryPlugin.getInstance().getDataFolder(), "items.yml"));

		c.getKeys(false).forEach(playerUUID -> {
			List<ItemStack> encodedItems = (List<ItemStack>) c.getList(playerUUID);
			setPlayerItems(playerUUID, encodedItems);
		});
	}

	public static void saveCollectionItems() {
		YamlConfiguration c = YamlConfiguration.loadConfiguration(new File(LibraryPlugin.getInstance().getDataFolder(), "items.yml"));
		HashMap<UUID, List<ItemStack>> playerItems = getPlayerItems();

		for (UUID uuid : playerItems.keySet()) {

			c.set(String.valueOf(uuid), playerItems.get(uuid));

			try {
				c.save("plugins" + File.separator + "CursedLibrary" + File.separator + "items.yml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
