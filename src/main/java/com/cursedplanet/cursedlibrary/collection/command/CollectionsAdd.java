package com.cursedplanet.cursedlibrary.collection.command;

import com.cursedplanet.cursedlibrary.collection.CollectionLoader;
import com.cursedplanet.cursedlibrary.collection.CollectionStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.cursedplanet.cursedlibrary.lib.command.SimpleCommandGroup;
import com.cursedplanet.cursedlibrary.lib.command.SimpleSubCommand;

public class CollectionsAdd extends SimpleSubCommand {

	protected CollectionsAdd(SimpleCommandGroup parent) {
		super(parent, "add|give");

		setUsage("[player]");
		setPermission("cursedlibrary.collections.add");
		setDescription("Adds the item in your hand to selected players collection");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		Player player = getPlayer();

		if (args.length >= 1) {
			checkNotNull(player, args[0] + "&c could not be found");
			player = Bukkit.getServer().getPlayerExact(args[0]);
		}

		CollectionStorage.addPlayerItem(player, getPlayer().getInventory().getItemInMainHand());
		tell(((String) CollectionLoader.getConfigPart("add_command")).replaceAll("\\{player}", player.getName()));
	}
}
