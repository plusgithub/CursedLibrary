package com.cursedplanet.cursedlibrary.menu;

import com.cursedplanet.cursedlibrary.lib.command.SimpleCommand;

public class TempMenuTest extends SimpleCommand {


	public TempMenuTest() {
		super("tempmenu");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		if (args[0].equals("temp"))
			new TestTwoMenu().display(getPlayer());
		if (args[0].equalsIgnoreCase("craft"))
			new TempCraftingMenu().display(getPlayer());
	}
}
