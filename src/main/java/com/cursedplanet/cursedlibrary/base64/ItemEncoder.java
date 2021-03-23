package com.cursedplanet.cursedlibrary.base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.inventory.ItemStack;
import com.cursedplanet.cursedlibrary.lib.Common;

import java.util.Base64;
import java.util.Map;

public class ItemEncoder {

	protected static final Gson gson = new Gson();

	public static String itemToBase64(ItemStack item) {
		String jsonString = gson.toJson(item.serialize());
		return Base64.getEncoder().encodeToString(jsonString.getBytes());
	}

	public static ItemStack base64ToItemStack(String string) {

		byte[] decodedBytes = Base64.getDecoder().decode(string);
		String decodedString = new String(decodedBytes);
		Common.log(decodedString);
		//String jsonString = gson.toJson(decodedString);
		//Common.log(jsonString);

		Map<String, Object> map = gson.fromJson(decodedString, new TypeToken<Map<String, Object>>() {
		}.getType());

		ItemStack target = ItemStack.deserialize(map);

		//ItemStack target = gson.fromJson(jsonString, ItemStack.class);
		Common.log("Item decoded from base64 to:" + String.valueOf(target));

		return target;
	}
}