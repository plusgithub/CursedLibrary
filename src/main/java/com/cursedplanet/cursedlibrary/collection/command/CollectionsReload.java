package com.cursedplanet.cursedlibrary.collection.command;

import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

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
