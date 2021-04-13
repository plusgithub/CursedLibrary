package com.cursedplanet.cursedlibrary.menu;

import com.cursedplanet.cursedlibrary.LibraryPlugin;
import com.cursedplanet.cursedlibrary.lib.Common;
import com.cursedplanet.cursedlibrary.lib.PlayerUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.function.Consumer;

public class CursedMenu {

	private final Random r = new Random();

	//private MenuBuilder builder;
	//protected InventoryCreator inventory;
	private HashMap<CursedMenu, HashMap<Runnable, String>> runnables;
	private HashMap<CursedMenu, List<BukkitTask>> runningTasks;
	protected Consumer<InventoryCloseEvent> consumer;
	protected Runnable updateTask;

	protected LinkedHashMap<Integer, ItemStack> contents;
	protected Inventory inv;
	protected LinkedHashMap<Integer, Boolean> lockedSlots;
	protected LinkedHashMap<Integer, Consumer<InventoryClickEvent>> slotRunnables;

	@Getter
	protected int size;
	@Getter
	protected String title;
	protected String id;

	public String getId() {
		return this.id;
	}

	private Sound openSound;
	protected Sound closeSound;

	private String[] pattern;
	private HashMap<Character, ItemStack> assignedPatterns;

	private String[] pagePattern;
	private ItemStack[] pageItems;
	private Integer currentPage;

	public CursedMenu(int size, String title, String id) {

		runnables = new HashMap<>();
		runningTasks = new HashMap<>();
		this.size = size;
		this.title = Common.colorize(title);
		this.id = id;
		this.openSound = null;
		this.closeSound = null;
		this.pattern = null;
		this.assignedPatterns = new HashMap<>();
		this.updateTask = null;

		this.pagePattern = null;
		this.pageItems = null;
		this.currentPage = 0;

		consumer = null;

		inv = Bukkit.createInventory(null, this.size, this.title);

		contents = new LinkedHashMap<>();
		lockedSlots = new LinkedHashMap<>();
		slotRunnables = new LinkedHashMap<>();

		for (int i = 0; i < size; i++) {
			contents.put(i, new ItemStack(Material.AIR));
			lockedSlots.put(i, false);
			slotRunnables.put(i, null);
		}
	}

	public List<HumanEntity> getViewers() {
		return inv.getViewers();
	}

	public ItemStack getItemAt(int slot) {
		return inv.getItem(slot);
	}

	public void updateTitle(String title) {
		for (HumanEntity player : getViewers()) {
			PlayerUtil.updateInventoryTitle((Player) player, title);
		}
	}

	public void updateTitleTimed(String title, int ticks) {
		String oldTitle = getTitle();
		
		updateTitle(title);

		Common.runLater(ticks, () -> {
			updateTitle(oldTitle);
		});
	}


	public MenuItem addClickable(int slot, ItemStack item, Consumer<InventoryClickEvent> consumer) {
		return new MenuItem(slot, item, this).clickable(consumer);
	}

	public MenuItem addStatic(int slot, ItemStack item) {
		return new MenuItem(slot, item, this).empty();
	}

	private Integer getRandomSlot() {
		ArrayList<Integer> emptySlots = new ArrayList<>();
		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i) == null) {
				emptySlots.add(i);
			}

		}
		if (emptySlots.isEmpty())
			return null;
		return emptySlots.get(r.nextInt(emptySlots.size()));
	}

	public MenuItem randomClickable(ItemStack item, Consumer<InventoryClickEvent> consumer) {
		if (getRandomSlot() != null)
			return new MenuItem(getRandomSlot(), item, this).clickable(consumer);
		return null;
	}

	public MenuItem randomStatic(ItemStack item) {
		if (getRandomSlot() != null)
			return new MenuItem(getRandomSlot(), item, this).empty();
		return null;
	}

	public void lockFilled(MenuItem[] items) {
		for (MenuItem item : items) {
			if (item != null)
				item.lock();
		}
	}


	public MenuItem[] fillEmpty(ItemStack item) {
		return fillEmpty(item, new ArrayList<>());
	}

	public MenuItem[] fillEmpty(ItemStack item, Integer... skippedSlots) {
		return fillEmpty(item, (List<Integer>) Arrays.asList(skippedSlots));
	}

	public MenuItem[] fillEmpty(ItemStack item, List<Integer> skippedSlots) {
		int i = 0;

		MenuItem[] menuItems = new MenuItem[getEmptyAmount()];

		while (i < getSize()) {
			if (inv.getItem(i) == null && !skippedSlots.contains(i)) {
				menuItems[i] = new MenuItem(i, item, this).empty();
			}
			i++;
		}

		return menuItems;
	}

	private int getEmptyAmount() {
		int i = 0;
		for (ItemStack item : inv.getContents()) {
			if (item == null)
				i++;
		}
		return i;
	}

	public MenuItem[] fillBorders(ItemStack item) {
		return fillBorders(item, 0, getSize() - 1);
	}

	public MenuItem[] fillBorders(ItemStack item, int startSlot, int endSlot) {
		int iterator = startSlot;

		MenuItem[] menuItems = new MenuItem[getSize()];

		int length = (endSlot - startSlot) % 9;
		int corner1 = startSlot + length;
		int corner2 = endSlot - length;

		while (iterator <= endSlot) {

			boolean b = iterator % 9 == startSlot % 9 || iterator % 9 == endSlot % 9;


			if (iterator >= startSlot && iterator <= corner1)
				menuItems[iterator] = addStatic(iterator, item);
			if (iterator >= corner2 && iterator <= endSlot)
				menuItems[iterator] = addStatic(iterator, item);
			if (b)
				menuItems[iterator] = addStatic(iterator, item);

			iterator++;
		}
		return menuItems;
	}

	public MenuItem[] fillRectangle(ItemStack item, int startSlot, int endSlot) {
		int iterator = startSlot;

		MenuItem[] menuItems = new MenuItem[getSize()];

		while (iterator <= endSlot) {
			boolean b = iterator % 9 <= endSlot % 9 && iterator % 9 >= startSlot % 9;


			if (b)
				menuItems[iterator] = addStatic(iterator, item);

			iterator++;
		}
		return menuItems;
	}

	public void repeatingTask(long repeat, Runnable task) {
		repeatingTask(0L, repeat, task);
	}

	public void repeatingTask(long delay, long repeat, Runnable task) {
		String newTimings = delay + "," + repeat;
		HashMap<Runnable, String> temp = new HashMap<>();
		if (runnables.containsKey(this)) {
			temp = runnables.get(this);
		}
		temp.put(task, newTimings);
		runnables.put(this, temp);
	}

	private void startRunnables() {
		Map<Runnable, String> temp = runnables.get(this);
		if (temp != null && !temp.isEmpty()) {
			List<BukkitTask> tasks = new ArrayList<>();
			for (Runnable task : temp.keySet()) {
				String[] timings = temp.get(task).split(",");
				tasks.add(Bukkit.getScheduler().runTaskTimer(LibraryPlugin.getInstance(), task, Long.decode(timings[0]), Long.decode(timings[1])));
			}
			runnables.remove(this);
			runningTasks.put(this, tasks);
		}
	}

	public void stopRunnables() {
		List<BukkitTask> temp = runningTasks.get(this);
		if (temp != null) {
			for (BukkitTask task : temp) {
				task.cancel();
			}
			runningTasks.remove(this);
		}
	}

	public void addCloseCallback(Consumer<InventoryCloseEvent> consumer) {
		this.consumer = consumer;
	}

	public void addClickUpdate(Runnable task) {
		this.updateTask = task;
	}

	public void setOpenSound(Sound sound) {
		this.openSound = sound;
	}

	public void setCloseSound(Sound sound) {
		this.closeSound = sound;
	}

	public void setPattern(String... rowPatterns) {
		this.pattern = rowPatterns;
	}

	public void assignPatternItem(char character, ItemStack item) {
		assignedPatterns.put(character, item);
	}

	public MenuItem[] fillPattern() {
		ItemStack empty = new ItemStack(Material.AIR);
		MenuItem[] menuItems = new MenuItem[pattern.length * 9];
		for (int i = 0; i < pattern.length; i++) {
			for (int o = 0; o < pattern[i].toCharArray().length; o++) {
				menuItems[(i * 9) + o] = addStatic((i * 9) + o, assignedPatterns.getOrDefault(pattern[i].toCharArray()[o], empty));
			}
		}
		return menuItems;
	}

	public void setPagePattern(String... rowPatterns) {
		this.pagePattern = rowPatterns;
	}

	public void addPageItems(ItemStack[] items) {
		this.pageItems = items;
	}

	private int getStartSlot() {
		String single = String.join("", pagePattern);

		for (int o = 0; o < single.toCharArray().length; o++) {
			if (single.charAt(o) == '#')
				return o;
		}
		return 0;
	}

	private boolean isPagedSlot(int slot) {
		String single = String.join("", pagePattern);
		return single.charAt(slot) == '#';
	}

	public MenuItem[] fillPages() {
		int itemsPerPage = getFillableSlots();
		int startSlot = getStartSlot() - 1;
		Common.log(String.valueOf(currentPage));
		int firstIndex = itemsPerPage * currentPage;
		//int lastIndex = firstIndex + itemsPerPage;
		MenuItem[] menuItems = new MenuItem[itemsPerPage];

		for (int i = 0; i < itemsPerPage; i++) {
			if (isPagedSlot(i + startSlot)) {
				menuItems[i] = addStatic(i + startSlot, pageItems[firstIndex + i]);
			}
		}
		return menuItems;
	}

	public void setPage(int page) {
		this.currentPage = page;
	}

	public Integer getPage() {
		return this.currentPage;
	}

	public boolean isFirstPage() {
		return getPage() == 0;
	}

	public boolean isLastPage() {
		return getPage() == getMaxPages();
	}

	public void nextPage() {
		if (!((getPage() + 1) > getMaxPages()))
			this.currentPage++;
	}

	public void previousPage() {
		if (getPage() != 0)

			this.currentPage--;
	}

	public int getMaxPages() {
		return (int) Math.ceil((double) pageItems.length / (double) getFillableSlots());
	}


	private int getFillableSlots() {
		int fillableSlots = 0;
		for (String s : pagePattern) {
			for (int o = 0; o < s.toCharArray().length; o++) {
				if (s.toCharArray()[o] == '#')
					fillableSlots++;
			}
		}
		return fillableSlots;
	}


	public void display(Player player) {
		MenuHandler.openInventory(player, this);

		player.openInventory(inv);
		if (openSound != null)
			player.playSound(player.getLocation(), this.openSound, 1, 1);

		startRunnables();
	}
}
