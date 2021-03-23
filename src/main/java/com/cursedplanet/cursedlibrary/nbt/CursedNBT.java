package com.cursedplanet.cursedlibrary.nbt;

import com.google.gson.internal.LinkedTreeMap;
import org.bukkit.inventory.ItemStack;
import com.cursedplanet.cursedlibrary.lib.remain.nbt.NBTItem;

import java.util.HashMap;

public class CursedNBT {

	protected final NBTItem nbtItem;
	protected static HashMap<String, LinkedTreeMap<Object, Object>> map = new HashMap<>();
	protected String mapID;

	public CursedNBT(String mainID, ItemStack item) {
		NBTItem tempItem = new NBTItem(item);
		if (!tempItem.hasKey("CURSEDPLANET")) {
			tempItem.setObject("CURSEDPLANET", map);
		} else {
			map = tempItem.getObject("CURSEDPLANET", HashMap.class);
		}
		this.nbtItem = tempItem;
		mapID = mainID;
	}

	protected ItemStack setMap() {
		nbtItem.setObject("CURSEDPLANET", map);
		return nbtItem.getItem();
	}

	public boolean hasMap() {
		return map.containsKey(mapID);
	}

	public boolean hasItemKey(String key) {
		LinkedTreeMap<Object, Object> temp = getItemMap();
		return temp.containsKey(key);
	}

	public LinkedTreeMap<Object, Object> getItemMap() {
		return map.containsKey(mapID) ? map.get(mapID) : new LinkedTreeMap<>();
	}

	public Object getItemValue(String key) {
		LinkedTreeMap<Object, Object> temp = getItemMap();
		return temp.get(key);
	}

	public ItemStack setItemValue(String key, Object value) {
		LinkedTreeMap<Object, Object> temp = getItemMap();
		temp.put(key, value);
		map.put(mapID, temp);
		return setMap();
	}

	public ItemStack clearItemValue(String key) {
		LinkedTreeMap<Object, Object> temp = getItemMap();
		temp.remove(key);
		map.put(mapID, temp);
		return setMap();
	}
}
