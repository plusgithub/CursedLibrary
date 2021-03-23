package com.cursedplanet.cursedlibrary.collection.command;

import com.cursedplanet.cursedlibrary.collection.CollectMenu;
import com.cursedplanet.cursedlibrary.collection.CollectionLoader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.cursedplanet.cursedlibrary.lib.Common;
import com.cursedplanet.cursedlibrary.lib.command.SimpleCommandGroup;
import com.cursedplanet.cursedlibrary.lib.command.SimpleSubCommand;

public class CollectionsView extends SimpleSubCommand {

	protected CollectionsView(final SimpleCommandGroup parent) {
		super(parent, "view|v");

		setUsage("[player]");
		setPermission("cursedlibrary.collections.view.other");
		setDescription("Views a selected players collections");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		Player player = getPlayer();

		if (args.length >= 1) {
			checkNotNull(player, args[0] + "&c could not be found");
			player = Bukkit.getServer().getPlayerExact(args[0]);
		}

		CollectMenu.INVENTORY.open(getPlayer());
		Common.tell(getPlayer(), ((String) CollectionLoader.getConfigPart("view_command")).replaceAll("\\{player}", player.getName()));
	}
}
