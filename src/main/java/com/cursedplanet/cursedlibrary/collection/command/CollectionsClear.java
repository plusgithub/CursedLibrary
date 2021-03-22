package com.cursedplanet.cursedlibrary.collection.command;

import com.cursedplanet.cursedlibrary.collection.CollectionLoader;
import com.cursedplanet.cursedlibrary.collection.CollectionStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

public class CollectionsClear extends SimpleSubCommand {

	protected CollectionsClear(SimpleCommandGroup parent) {
		super(parent, "clear|c");

		setUsage("[player]");
		setPermission("cursedlibrary.collections.clear");
		setDescription("Clears the selected players collections");
	}

	@Override
	protected void onCommand() {
		Player player = null;

		if (args.length >= 1) {
			player = Bukkit.getServer().getPlayerExact(args[0]);
		} else if (this.isPlayer()) {
			player = getPlayer();
			checkNotNull(player, "&cPlease specify a player");
		}

		CollectionStorage.clearPlayerItems(player);
		tell(((String) CollectionLoader.getConfigPart("clear_command")).replaceAll("\\{player}", player.getName()));
	}
}
