package com.imjona.customjoinevents.manager.menu;

import com.imjona.customjoinevents.CustomJoinEvents;
import com.imjona.customjoinevents.manager.PlayerData;
import com.imjona.customjoinevents.utils.ActionType;
import com.imjona.customjoinevents.utils.Messages;
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
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class FireworksMenu implements Listener {
	private final CustomJoinEvents plugin;

	public FireworksMenu(CustomJoinEvents plugin) {
		this.plugin = plugin;
	}

	public void fireworksMenu(Player player) {
		FileConfiguration config = plugin.getFireworkConfig();
		String title = UtilsPlugin.fixColorMessages(config.getString("Menu.title_menu"));
		int size = config.getInt("Menu.size");
		Inventory firework = Bukkit.createInventory(null, size, title);

		for (String decorations : Objects.requireNonNull(config.getConfigurationSection("Menu.decorations")).getKeys(false)) {
			int slot = config.getInt("Menu.decorations." + decorations + ".slot");
			List<Integer> slots = config.getIntegerList("Menu.decorations." + decorations + ".slot");
			ItemStack item = UtilsBuilder.makeItem(config, "Menu.decorations." + decorations, player, null);
			if (config.getBoolean("Menu.decorations." + decorations + ".enabled")) {
				if (slots.size() > 0) {
					for (Integer integer : slots) {
						firework.setItem(integer, item);
					}
					continue;
				}
				if (slot == -1) {
					for (int i=0;i<firework.getSize();i++) {
						firework.setItem(i, item);
					}
					continue;
				}
				firework.setItem(slot, item);
			}
		}

		PlayerData pd = plugin.getPlayerConfigManager().getPlayer(player.getName());

		for (String items : Objects.requireNonNull(config.getConfigurationSection("Menu.items")).getKeys(false)) {
			int slot = config.getInt("Menu.items." + items + ".slot");
			ItemStack item = UtilsBuilder.makeItem(config, "Menu.items." + items, player, null);
			String statusLocked, statusSelected;
			String permission = config.getString("Menu.items." + items + ".permission");

			if (!Objects.requireNonNull(permission).equalsIgnoreCase("none") && !player.hasPermission(Objects.requireNonNull(permission))) {
				statusLocked = Messages.LOCKED;
				item = UtilsBuilder.makeItem(config, "Menu.items." + items, player, "no_permission");
			} else {
				statusLocked = Messages.UNLOCKED;
			}

			if (!pd.hasFireworkSelected(items)) {
				statusSelected = Messages.UNSELECTED;
			} else {
				statusSelected = Messages.SELECTED;
			}

			ItemMeta meta = item.getItemMeta();
			List<String> lore = null;
			if (meta != null)
				lore = meta.getLore();

			if (lore != null) {
				for (int i = 0; i < lore.size(); i++) {
					lore.set(i, UtilsPlugin.fixColorMessages(lore.get(i)
							.replace("%firework_name%", UtilsPlugin.getName(items, config))
							.replace("%firework_type%", getFireworkCharacteristic("Type:", items, config))
							.replace("%firework_colors%", getFireworkCharacteristic("Colors:", items, config))
							.replace("%firework_fade%", getFireworkCharacteristic("Fade:", items, config))
							.replace("%firework_power%", getFireworkCharacteristic("Power:", items, config))
							.replace("%firework_amount%", getFireworkCharacteristic("Amount:", items, config))
							.replace("%firework_status_locked%", statusLocked)
							.replace("%firework_status_selected%", statusSelected)
							.replace("%player%%", player.getName())
					));
				}
				meta.setLore(lore);
			}
			item.setItemMeta(meta);
			firework.setItem(slot, item);
		}
		player.openInventory(firework);
	}

	@EventHandler
	public void onClickInventory(InventoryClickEvent event) {
		FileConfiguration config = plugin.getFireworkConfig();
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
					if (slot == config.getInt("Menu.items." + item + ".slot")) {
						PlayerData pd = plugin.getPlayerConfigManager().getPlayer(player.getName());

						String permission = config.getString("Menu.items." + item + ".permission");
						String fire_name = UtilsPlugin.getName(item, config);
						String sound_on_click = config.getString("Menu.items." + item + ".sound_on_click");
						if (sound_on_click != null) {
							UtilsBuilder.playXSound(player, sound_on_click);
						}
						if (!Objects.requireNonNull(permission).equalsIgnoreCase("none") && !player.hasPermission(Objects.requireNonNull(permission))) {
							UtilsPlugin.sendMessageToPlayer(player, Messages.EVENT_NO_PERMISSION.replace("%event_name%", fire_name));
							return;
						}
						if (!pd.hasFireworkSelected(item)) {
							UtilsPlugin.sendMessageToPlayer(player, Messages.EVENT_SELECTED.replace("%event_name%", fire_name));
							pd.setFirework(item);
						} else {
							UtilsPlugin.sendMessageToPlayer(player, Messages.EVENT_UNSELECTED.replace("%event_name%", fire_name));
							pd.deselectFirework();
						}
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, () -> fireworksMenu(player), 1L);
					}
				}
			}
		}
	}

	public String getFireworkCharacteristic(String chara, String path, FileConfiguration config) {
		String fire = config.getString("Menu.items." + path + ".firework_type");
		String[] sp = new String[0];
		if (fire != null) {
			sp = fire.split(";");
		}
		for (String cs : sp) {
			if (cs.startsWith(chara))
				return cs.replace(chara, "");
		}
		return "null";
	}
}
