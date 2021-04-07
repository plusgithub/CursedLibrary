package com.cursedplanet.cursedlibrary.nbt;

import com.cursedplanet.cursedlibrary.lib.remain.nbt.NBTItem;
import com.google.gson.internal.LinkedTreeMap;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CursedNBT {

	protected NBTItem nbtItem;
	protected static HashMap<String, LinkedTreeMap<Object, Object>> map;
	protected String mapID;

	public CursedNBT(String mainID, ItemStack item) {
		NBTItem tempItem = new NBTItem(item);
		if (!tempItem.hasKey("CURSEDPLANET")) {
			map = new HashMap<>();
			tempItem.setObject("CURSEDPLANET", new HashMap<>());
		} else {
			map = tempItem.getObject("CURSEDPLANET", HashMap.class);
		}
		nbtItem = tempItem;
		mapID = mainID;
	}

	protected void setMap() {
		nbtItem.setObject("CURSEDPLANET", map);
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

	public void setItemValue(String key, Object value) {
		LinkedTreeMap<Object, Object> temp = getItemMap();
		temp.put(key, value);
		map.put(mapID, temp);
		setMap();
	}

	public void clearItemValue(String key) {
		LinkedTreeMap<Object, Object> temp = getItemMap();
		temp.remove(key);
		map.put(mapID, temp);
		setMap();
	}

	public ItemStack getItemStack() {
		return nbtItem.getItem();
	}
}