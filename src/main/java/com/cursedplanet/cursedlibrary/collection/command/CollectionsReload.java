package com.cursedplanet.cursedlibrary.collection.command;

import com.cursedplanet.cursedlibrary.lib.command.SimpleCommandGroup;
import com.cursedplanet.cursedlibrary.lib.command.SimpleSubCommand;

public class CollectionsReload extends SimpleSubCommand {

	protected CollectionsReload(SimpleCommandGroup parent) {
		super(parent, "reload");

		setPermission("cursedlibrary.collections.reload");
		setDescription("Reloads the config files");
	}

	@Override
	protected void onCommand() {
	}
}
