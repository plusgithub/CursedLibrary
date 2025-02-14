package com.cursedplanet.cursedlibrary.lib.model;

import com.cursedplanet.cursedlibrary.lib.Common;
import com.cursedplanet.cursedlibrary.lib.PlayerUtil;
import com.cursedplanet.cursedlibrary.lib.Valid;
import com.cursedplanet.cursedlibrary.lib.collection.SerializedMap;
import com.cursedplanet.cursedlibrary.lib.settings.YamlConfig;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class Variable extends YamlConfig {

	/**
	 * Return the prototype file path for the given variable fiel name
	 */
	public static Function<String, String> PROTOTYPE_PATH = t -> NO_DEFAULT;

	/**
	 * A list of all loaded variables
	 */
	private static final ConfigItems<Variable> loadedVariables = ConfigItems.fromFolder("variable", "variables", Variable.class);

	/**
	 * The kind of this variable
	 */
	@Getter
	private Type type;

	/**
	 * The variable key what we should find
	 */
	@Getter
	private String key;

	/**
	 * The variable value what we should replace the key with
	 * JavaScript engine
	 */
	private String value;

	/**
	 * The JavaScript condition that must return TRUE for this variable to be shown
	 */
	@Nullable
	@Getter
	private String senderCondition;

	/**
	 * The JavaScript condition that must return TRUE for this variable to be shown to a receiver
	 */
	@Nullable
	@Getter
	private String receiverCondition;

	/**
	 * The permission the sender must have to show the part
	 */
	@Nullable
	@Getter
	private String senderPermission;

	/**
	 * The permission receiver must have to see the part
	 */
	@Nullable
	@Getter
	private String receiverPermission;

	/**
	 * The hover text or null if not set
	 */
	@Getter
	@Nullable
	private List<String> hoverText;

	/**
	 * The JavaScript pointing to a particular {@link ItemStack}
	 */
	@Getter
	@Nullable
	private String hoverItem;

	/**
	 * What URL should be opened on click? Null if none
	 */
	@Getter
	@Nullable
	private String openUrl;

	/**
	 * What command should be suggested on click? Null if none
	 */
	@Getter
	@Nullable
	private String suggestCommand;

	/**
	 * What command should be run on click? Null if none
	 */
	@Getter
	@Nullable
	private String runCommand;

	/*
	 * Shall we save comments for this file?
	 */
	private final boolean saveComments;

	/*
	 * Create and load a new variable (automatically called)
	 */
	private Variable(String file) {
		final String prototypePath = PROTOTYPE_PATH.apply(file);

		this.saveComments = prototypePath != null;
		this.loadConfiguration(prototypePath, "variables/" + file + ".yml");
	}

	/**
	 * @see com.cursedplanet.cursedlibrary.lib.settings.YamlConfig#saveComments()
	 */
	@Override
	protected boolean saveComments() {
		return saveComments;
	}

	// ----------------------------------------------------------------------------------
	// Loading
	// ----------------------------------------------------------------------------------

	/**
	 * @see com.cursedplanet.cursedlibrary.lib.settings.YamlConfig#onLoadFinish()
	 */
	@Override
	protected void onLoadFinish() {

		this.type = get("Type", Type.class);
		this.key = getString("Key");
		this.value = getString("Value");
		this.senderCondition = getString("Sender_Condition");
		this.receiverCondition = getString("Receiver_Condition");
		this.senderPermission = getString("Sender_Permission");
		this.receiverPermission = getString("Receiver_Permission");

		// Correct common mistakes
		if (this.type == null) {
			this.type = Type.FORMAT;

			save();
		}

		if (this.key.startsWith("{") || this.key.startsWith("[")) {
			this.key = this.key.substring(1);

			save();
		}

		if (this.key.endsWith("}") || this.key.endsWith("]")) {
			this.key = this.key.substring(0, this.key.length() - 1);

			save();
		}

		if (this.type == Type.MESSAGE) {
			this.hoverText = getStringList("Hover");
			this.hoverItem = getString("Hover_Item");
			this.openUrl = getString("Open_Url");
			this.suggestCommand = getString("Suggest_Command");
			this.runCommand = getString("Run_Command");
		}

		// Check for known mistakes
		if (this.key == null || this.key.isEmpty())
			throw new NullPointerException("(DO NOT REPORT, PLEASE FIX YOURSELF) Please set 'Key' as variable name in " + getFile());

		if (this.value == null || this.value.isEmpty())
			throw new NullPointerException("(DO NOT REPORT, PLEASE FIX YOURSELF) Please set 'Value' key as what the variable shows in " + getFile() + " (this can be a JavaScript code)");

		// Test for key validity
		if (!Common.regExMatch("^\\w+$", this.key))
			throw new IllegalArgumentException("(DO NOT REPORT, PLEASE FIX YOURSELF) The 'Key' variable in " + getFile() + " must only contains letters, numbers or underscores. Do not write [] or {} there!");
	}

	/**
	 * Return this class as a map
	 *
	 * @return
	 */
	public SerializedMap serialize() {
		final SerializedMap map = new SerializedMap();

		map.putIf("Type", this.type);
		map.putIf("Key", this.key);
		map.putIf("Sender_Condition", this.senderCondition);
		map.putIf("Receiver_Condition", this.receiverCondition);
		map.putIf("Hover", this.hoverText);
		map.putIf("Hover_Item", this.hoverItem);
		map.putIf("Open_Url", this.openUrl);
		map.putIf("Suggest_Command", this.suggestCommand);
		map.putIf("Run_Command", this.runCommand);
		map.putIf("Sender_Permission", this.senderPermission);
		map.putIf("Receiver_Permission", this.receiverPermission);

		return map;
	}

	/**
	 * @see com.cursedplanet.cursedlibrary.lib.settings.YamlConfig#save()
	 */
	@Override
	public void save() {

		final SerializedMap map = serialize();

		// We serialize null values too but they are not saved
		if (!map.isEmpty() && !"{}".equals(map.toString())) {
			for (final Map.Entry<String, Object> entry : map.entrySet())
				setNoSave(entry.getKey(), entry.getValue());

			super.save();
		}
	}

	// ----------------------------------------------------------------------------------
	// Getters
	// ----------------------------------------------------------------------------------

	/**
	 * Runs the script for the given player and the replacements,
	 * returns the output
	 *
	 * @param sender
	 * @param replacements
	 * @return
	 */
	public String getValue(CommandSender sender, @Nullable Map<String, Object> replacements) {
		Variables.REPLACE_JAVASCRIPT = false;

		try {
			// Replace variables in script
			final String script = Variables.replace(this.value, sender, replacements);
			final String result = String.valueOf(JavaScriptExecutor.run(script, sender));

			return result;

		} catch (final RuntimeException ex) {

			// Assume console or Discord lack proper methods to call
			if (sender instanceof Player)
				throw ex;

			return "";

		} finally {
			Variables.REPLACE_JAVASCRIPT = true;
		}
	}

	/**
	 * Create the variable and append it to the existing component as if the player initiated it
	 *
	 * @param sender
	 * @param existingComponent
	 * @return
	 */
	public SimpleComponent build(CommandSender sender, SimpleComponent existingComponent, @Nullable Map<String, Object> replacements) {

		if (this.senderPermission != null && !this.senderPermission.isEmpty() && !PlayerUtil.hasPerm(sender, this.senderPermission))
			return SimpleComponent.of("");

		if (this.senderCondition != null && !this.senderCondition.isEmpty()) {
			final Object result = JavaScriptExecutor.run(this.senderCondition, sender);

			if (result != null) {
				Valid.checkBoolean(result instanceof Boolean, "Variable '" + getName() + "' option Condition must return boolean not " + (result == null ? "null" : result.getClass()));

				if ((boolean) result == false)
					return SimpleComponent.of("");
			}
		}

		final String value = this.getValue(sender, replacements);

		if (value == null || value.isEmpty() || "null".equals(value))
			return SimpleComponent.of("");

		final SimpleComponent component = existingComponent
				.append(Variables.replace(value, sender, replacements))
				.viewPermission(this.receiverPermission)
				.viewCondition(this.receiverCondition);

		if (!Valid.isNullOrEmpty(this.hoverText))
			component.onHover(Variables.replace(this.hoverText, sender, replacements));

		if (this.hoverItem != null && !this.hoverItem.isEmpty()) {
			final Object result = JavaScriptExecutor.run(Variables.replace(this.hoverItem, sender, replacements), sender);
			Valid.checkBoolean(result instanceof ItemStack, "Variable '" + getName() + "' option Hover_Item must return ItemStack not " + result.getClass());

			component.onHover((ItemStack) result);
		}

		if (this.openUrl != null && !this.openUrl.isEmpty())
			component.onClickOpenUrl(Variables.replace(this.openUrl, sender, replacements));

		if (this.suggestCommand != null && !this.suggestCommand.isEmpty())
			component.onClickSuggestCmd(Variables.replace(this.suggestCommand, sender, replacements));

		if (this.runCommand != null && !this.runCommand.isEmpty())
			component.onClickRunCmd(Variables.replace(this.runCommand, sender, replacements));

		return component;
	}

	/**
	 * @see com.cursedplanet.cursedlibrary.lib.settings.YamlConfig#toString()
	 */
	@Override
	public String toString() {
		return serialize().toStringFormatted();
	}

	// ------–------–------–------–------–------–------–------–------–------–------–------–
	// Static
	// ------–------–------–------–------–------–------–------–------–------–------–------–

	/**
	 * Load all variables from variables/ folder
	 */
	public static void loadVariables() {
		loadedVariables.loadItems();
	}

	/**
	 * Remove the given variable in case it exists
	 *
	 * @param variable
	 */
	public static void removeVariable(final Variable variable) {
		loadedVariables.removeItem(variable);
	}

	/**
	 * Return true if the given variable by key is loaded
	 *
	 * @param name
	 * @return
	 */
	public static boolean isVariableLoaded(final String name) {
		return loadedVariables.isItemLoaded(name);
	}

	/**
	 * Return a variable, or null if not loaded
	 *
	 * @param name
	 * @return
	 */
	public static Variable findVariable(@NonNull final String name) {
		for (final Variable item : getVariables())
			if (item.getKey().equalsIgnoreCase(name))
				return item;

		return null;
	}

	/**
	 * Return a list of all variables
	 *
	 * @return
	 */
	public static List<Variable> getVariables() {
		return loadedVariables.getItems();
	}

	/**
	 * Return a list of all variable names
	 *
	 * @return
	 */
	public static List<String> getVariableNames() {
		return loadedVariables.getItemNames();
	}

	// ------–------–------–------–------–------–------–------–------–------–------–------–
	// Classes
	// ------–------–------–------–------–------–------–------–------–------–------–------–

	/**
	 * Represents a variable type
	 */
	@RequiredArgsConstructor
	public enum Type {

		/**
		 * This variable is used in chat format and "server to player" messages
		 * Cannot be used by players. Example: [{channel}] {player}: {message}
		 */
		FORMAT("format"),

		/**
		 * This variable can be used by players in chat such as "I have an [item]"
		 */
		MESSAGE("message"),
		;

		/**
		 * The saveable non-obfuscated key
		 */
		@Getter
		private final String key;

		/**
		 * Attempt to load the type from the given config key
		 *
		 * @param key
		 * @return
		 */
		public static Type fromKey(String key) {
			for (final Type mode : values())
				if (mode.key.equalsIgnoreCase(key))
					return mode;

			throw new IllegalArgumentException("No such item type: " + key + ". Available: " + Common.join(values()));
		}

		/**
		 * Returns {@link #getKey()}
		 */
		@Override
		public String toString() {
			return this.key;
		}
	}
}