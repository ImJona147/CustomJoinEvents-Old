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

public class TitleMenu implements Listener {
    private final CustomJoinEvents plugin;

    public TitleMenu(CustomJoinEvents plugin) {
        this.plugin = plugin;
    }

    public void titleMenu(Player player) {
        FileConfiguration config = plugin.getTitleConfig();
        int size = config.getInt("Menu.size");
        String title = UtilsPlugin.fixColorMessages(config.getString("Menu.title_menu"));
        Inventory titles = Bukkit.createInventory(null, size, title);

        for (String decoration : Objects.requireNonNull(config.getConfigurationSection("Menu.decorations")).getKeys(false)) {
            int slot = config.getInt("Menu.decorations." + decoration + ".slot");
            List<Integer> slots = config.getIntegerList("Menu.decorations." + decoration + ".slot");

            ItemStack item = UtilsBuilder.makeItem(config, "Menu.decorations." + decoration, player, null);

            if (config.getBoolean("Menu.decorations." + decoration + ".enabled")) {
                if (slots.size() > 0) {
                    for (Integer integer : slots) {
                        titles.setItem(integer, item);
                    }
                    continue;
                }
                if (slot == -1) {
                    for (int i=0;i<size;i++) {
                        titles.setItem(i, item);
                    }
                    continue;
                }
                titles.setItem(slot, item);
            }
        }

        PlayerData pd = plugin.getPlayerConfigManager().getPlayer(player.getName());

        for (String items : Objects.requireNonNull(config.getConfigurationSection("Menu.items")).getKeys(false)) {
            int slot = config.getInt("Menu.items." + items + ".slot");
            ItemStack item = UtilsBuilder.makeItem(config, "Menu.items." + items, player, null);
            String statusLocked, statusSelected;
            String titlee = config.getString("Menu.items." + items + ".title");
            String subtitle = config.getString("Menu.items." + items + ".subtitle");
            String permission = config.getString("Menu.items." + items + ".permission");

            ItemMeta meta = item.getItemMeta();
            List<String> lore = null;
            if (meta != null) {
                lore = meta.getLore();
            }
            if (!player.hasPermission(Objects.requireNonNull(permission)) && !permission.equalsIgnoreCase("none")) {
                statusLocked = Messages.LOCKED;
                item = UtilsBuilder.makeItem(config, "Menu.items." + items, player,"no_permission");
            } else {
                statusLocked = Messages.UNLOCKED;
            }
            if (!pd.hasTitleSelected(items)) {
                statusSelected = Messages.UNSELECTED;
            } else {
                statusSelected = Messages.SELECTED;
            }

            if (lore != null) {
                for (int i=0;i<lore.size();i++) {
                    lore.set(i, UtilsPlugin.fixColorMessages(lore.get(i)
                            .replace("%titles_name%", UtilsPlugin.getName(items, config))
                            .replace("%titles_title%", Objects.requireNonNull(titlee))
                            .replace("%titles_subtitle%", Objects.requireNonNull(subtitle))
                            .replace("%titles_status_locked%", statusLocked)
                            .replace("%titles_status_selected%", statusSelected)
                            .replace("%player%", player.getName())
                    ));
                }
                meta.setLore(lore);
            }
            item.setItemMeta(meta);
            titles.setItem(slot, item);
        }

        player.openInventory(titles);
    }

    @EventHandler
    public void onClickInventory(InventoryClickEvent event) {
        FileConfiguration config = plugin.getTitleConfig();
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
                        String title_name = UtilsPlugin.getName(item, config);
                        String sound_on_click = config.getString("Menu.items." + item + ".sound_on_click");

                        if (sound_on_click != null) {
                            UtilsBuilder.playXSound(player, sound_on_click);
                        }
                        if (permission != null && !player.hasPermission(permission) && !permission.equalsIgnoreCase("none")) {
                            UtilsPlugin.sendMessageToPlayer(player, Messages.EVENT_NO_PERMISSION.replace("%event_name%", title_name));
                            return;
                        }
                        if (!pd.hasTitleSelected(item)) {
                            UtilsPlugin.sendMessageToPlayer(player, Messages.EVENT_SELECTED.replace("%event_name%", title_name));
                            pd.setTitle(item);
                        } else {
                            UtilsPlugin.sendMessageToPlayer(player, Messages.EVENT_UNSELECTED.replace("%event_name%", title_name));
                            pd.deselectTitle();
                        }
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, () -> titleMenu(player), 5L);
                    }
                }
            }
        }
    }
}
