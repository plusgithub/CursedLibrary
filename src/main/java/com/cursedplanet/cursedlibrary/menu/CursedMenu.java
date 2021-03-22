package com.cursedplanet.cursedlibrary.menu;

import com.cursedplanet.cursedlibrary.LibraryPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.function.Consumer;

public class CursedMenu {

	private MenuBuilder builder;
	protected static InventoryCreator inventory;
	private HashMap<UUID, HashMap<Runnable, String>> runnables = new HashMap<>();
	private static HashMap<UUID, List<BukkitTask>> runningTasks = new HashMap<>();

	protected static List<Integer> lockedSlots = new ArrayList<>();
	protected static ItemStack[] contents;
	protected static HashMap<UUID, Consumer<InventoryClickEvent>> consumerContents;

	public CursedMenu() {
	}

	public MenuBuilder builder() {
		builder = new MenuBuilder();
		return builder;
	}


	public MenuItem addClickable(int slot, ItemStack item, Consumer<InventoryClickEvent> consumer) {
		if (slot != -1) {
			return MenuItem.clickable(slot, item, consumer);
		} else {
			int i = 0;
			while (i <= inventory.getSize()) {
				if (!inventory.isSet(i)) {
					return MenuItem.clickable(i, item, consumer);
				}
				i++;
			}
		}
		return null;
	}

	public MenuItem addStatic(int slot, ItemStack item) {
		if (slot != -1) {
			return MenuItem.empty(slot, item);
		} else {
			int i = 0;
			while (i <= inventory.getSize()) {
				if (!inventory.isSet(i)) {
					return MenuItem.empty(i, item);
				}
				i++;
			}
		}
		return null;
	}

	public void fillEmpty(ItemStack item) {
		int i = 0;
		while (i <= inventory.getSize()) {
			addStatic(-1, item);
			i++;
		}
	}


	public void setItem(int slot, MenuItem item, boolean locked) {
		inventory.setItem(slot, item.getItem());
	}

	public void pushItem(MenuItem item) {
		inventory.pushItem(item.getItem());
	}

	public void fillEmpty(MenuItem item, boolean locked) {
		fillEmpty(item, new ArrayList<>(), locked);
	}

	public void fillEmpty(MenuItem item, List<Integer> skippedSlots, boolean locked) {
		int i = 0;
		while (i <= inventory.getSize()) {
			if (!inventory.isSet(i) && !skippedSlots.contains(i)) {
				inventory.setItem(i, item.getItem());
			}
			i++;
		}
	}

	public void repeatingTask(Player viewer, long repeat, Runnable task) {
		repeatingTask(viewer, 0L, repeat, task);
	}

	public void repeatingTask(Player viewer, long delay, long repeat, Runnable task) {
		String newTimings = delay + "," + repeat;
		HashMap<Runnable, String> temp = new HashMap<>();
		if (runnables.containsKey(viewer.getUniqueId())) {
			temp = runnables.get(viewer.getUniqueId());
		}
		temp.put(task, newTimings);
		runnables.put(viewer.getUniqueId(), temp);
	}

	private void startRunnables(Player player) {
		Map<Runnable, String> temp = runnables.get(player.getUniqueId());
		List<BukkitTask> tasks = new ArrayList<>();
		for (Runnable task : temp.keySet()) {
			String[] timings = temp.get(task).split(",");
			tasks.add(Bukkit.getScheduler().runTaskTimer(LibraryPlugin.getInstance(), task, Long.decode(timings[0]), Long.decode(timings[1])));
		}
		runnables.remove(player.getUniqueId());
		runningTasks.put(player.getUniqueId(), tasks);
	}

	public static void stopRunnables(Player player) {
		List<BukkitTask> temp = runningTasks.get(player.getUniqueId());
		if (temp != null) {
			for (BukkitTask task : temp) {
				task.cancel();
			}
			runningTasks.remove(player.getUniqueId());
		}
	}


	public void display(Player player) {
		inventory.setContent(contents);
		inventory.display(player);
		startRunnables(player);
		MenuListeners.lockedSlots.put(MenuBuilder.getID(), lockedSlots);
		//Common.tell(player, (Collection<String>) player.getOpenInventory().getTopInventory().getItem(56));
	}
}
