package com.cursedplanet.cursedlibrary.collection.command;

import org.mineacademy.fo.command.SimpleCommandGroup;

public class CollectionsParent extends SimpleCommandGroup {

	@Override
	protected void registerSubcommands() {
		registerSubcommand(new CollectionsView(this));
		registerSubcommand(new CollectionsAdd(this));
		registerSubcommand(new CollectionsRemove(this));
		registerSubcommand(new CollectionsClear(this));
	}

	@Override
	protected String getCredits() {
		return "\nVisit cursedplanet.com for more information";
	}
}
