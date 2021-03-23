package com.cursedplanet.cursedlibrary.collection;

import org.bukkit.entity.Player;
import com.cursedplanet.cursedlibrary.lib.command.SimpleCommand;

public class CollectionCommand extends SimpleCommand {

	public CollectionCommand() {
		super("collect");

		setDescription("Open your collectable items menu");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		CollectMenu.setTarget(getPlayer());
		CollectMenu.INVENTORY.open(getPlayer());

		Player player = getPlayer();
		
		if (args.length >= 1) {
			player = findPlayer(args[0]);
		}
		CollectionAPI.openCollectionMenu(getPlayer(), player);
	}
}
