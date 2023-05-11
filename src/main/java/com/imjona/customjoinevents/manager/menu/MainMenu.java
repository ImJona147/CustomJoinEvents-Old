package com.imjona.customjoinevents.manager.menu;

import com.imjona.customjoinevents.CustomJoinEvents;
import com.imjona.customjoinevents.utils.ActionType;
import com.imjona.customjoinevents.utils.UtilsBuilder;
import com.imjona.customjoinevents.utils.UtilsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class MainMenu implements Listener {
	private final CustomJoinEvents plugin;

	public MainMenu(CustomJoinEvents plugin) {
		this.plugin = plugin;
	}

	public void mainMenu(Player player) {
		FileConfiguration config = plugin.getConfig();
		int size = config.getInt("Menu.size");
		String title = UtilsPlugin.fixColorMessages(config.getString("Menu.title_menu"));
		Inventory main = Bukkit.createInventory(null, size, title);

        for (String key : Objects.requireNonNull(config.getConfigurationSection("Menu.decorations")).getKeys(false)) {
			ItemStack item = UtilsBuilder.makeItem(config, "Menu.decorations." + key, player, null);
			int slot = config.getInt("Menu.decorations." + key + ".slot");
			List<Integer> slots = config.getIntegerList("Menu.decorations." + key + ".slot");
			if (config.getBoolean("Menu.decorations." + key + ".enabled")) {
				if (slots.size() > 0) {
					for (Integer integer : slots) {
						main.setItem(integer, item);
					}
					continue;
				}
				if (slot == -1) {
					for (int i=0;i<main.getSize();i++) {
						main.setItem(i, item);
					}
					continue;
				}
				main.setItem(slot, item);
			}
        }

		for (String key : Objects.requireNonNull(config.getConfigurationSection("Menu.items")).getKeys(false)) {
			int slot = config.getInt("Menu.items." + key + ".slot");
			ItemStack item = UtilsBuilder.makeItem(config, "Menu.items." + key, player, null);
			main.setItem(slot, item);
		}
		player.openInventory(main);
	}

	@EventHandler
	public void onClickMainInventory(InventoryClickEvent event) {
		FileConfiguration config = plugin.getConfig();
		String title = UtilsPlugin.fixColorMessages(config.getString("Menu.title_menu"));

		Player player = (Player) event.getWhoClicked();
		if (player.getOpenInventory().getTitle().equals(title)) {
			if (event.getCurrentItem() == null || event.getCurrentItem().getType().name().contains("AIR")) {
				event.setCancelled(true);
				return;
			}
			int slot = event.getSlot();
			event.setCancelled(true);
			if (Objects.equals(event.getClickedInventory(), player.getOpenInventory().getTopInventory())) {
				for (String decorations : Objects.requireNonNull(config.getConfigurationSection("Menu.decorations")).getKeys(false)) {
					if (slot == config.getInt("Menu.decorations." + decorations + ".slot")) {
						String action_type = config.getString("Menu.decorations." + decorations + ".action_type");
						String sound_on_click = config.getString("Menu.decorations." + decorations + ".sound_on_click");
						if (!config.getBoolean("Menu.decorations." + decorations + ".enabled"))
							return;
						if (sound_on_click != null) {
							UtilsBuilder.playXSound(player, sound_on_click);
						}
						if (action_type != null) {
							new ActionType(plugin).getActions(action_type, player);
						}
					}
				}

				for (String item : Objects.requireNonNull(config.getConfigurationSection("Menu.items")).getKeys(false)) {
					String action_type = config.getString("Menu.items." + item + ".action_type");
					String sound_on_click = config.getString("Menu.items." + item + ".sound_on_click");
					if (slot == config.getInt("Menu.items." + item + ".slot")) {
						if (sound_on_click != null) {
							UtilsBuilder.playXSound(player, sound_on_click);
						}
						if (action_type != null) {
							new ActionType(plugin).getActions(action_type, player);
						}
					}
				}
			}
		}
	}
}
