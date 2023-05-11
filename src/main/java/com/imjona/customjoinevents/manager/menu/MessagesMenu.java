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

public class MessagesMenu implements Listener {
    private final CustomJoinEvents plugin;

    public MessagesMenu(CustomJoinEvents plugin) {
        this.plugin = plugin;
    }

    public void messagesMenu(Player player) {
        FileConfiguration config = plugin.getMessageConfig();
        int size = config.getInt("Menu.size");
        String title = UtilsPlugin.fixColorMessages(config.getString("Menu.title_menu"));
        Inventory message = Bukkit.createInventory(null, size, title);

        for (String decorations : Objects.requireNonNull(config.getConfigurationSection("Menu.decorations")).getKeys(false)) {
            int slot = config.getInt("Menu.decorations." + decorations + ".slot");
            List<Integer> slots = config.getIntegerList("Menu.decorations." + decorations + ".slot");

            ItemStack item = UtilsBuilder.makeItem(config, "Menu.decorations." + decorations, player, null);
            if (config.getBoolean("Menu.decorations." + decorations + ".enabled")) {
                if (slots.size() > 0) {
                    for (Integer integer : slots) {
                        message.setItem(integer, item);
                    }
                    continue;
                }
                int i;
                if (slot == -1) {
                    for (i=0;i<size;i++) {
                        message.setItem(i, item);
                    }
                    continue;
                }
                message.setItem(slot, item);
            }
        }

        PlayerData pd = plugin.getPlayerConfigManager().getPlayer(player.getName());

        for (String items : Objects.requireNonNull(config.getConfigurationSection("Menu.items")).getKeys(false)) {
            int slot = config.getInt("Menu.items." + items + ".slot");
            ItemStack item = UtilsBuilder.makeItem(config, "Menu.items." + items, player, null);

            String permission = config.getString("Menu.items." + items + ".permission");

            String msgJoin = config.getString("Menu.items." + items + ".message_join");
            String msgQuit = config.getString("Menu.items." + items + ".message_quit");

            String actionBarJoin = config.getString("Menu.items." + items + ".actionbar_join");
            String actionBarQuit = config.getString("Menu.items." + items + ".actionbar_quit");

            String statusLocked, statusSelected;
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
            if (!pd.hasMessageSelected(items)) {
                statusSelected = Messages.UNSELECTED;
            } else {
                statusSelected = Messages.SELECTED;
            }

            if (lore != null) {
                for (int i = 0; i < lore.size(); i++) {
                    lore.set(i, UtilsPlugin.fixColorMessages(lore.get(i)
                            .replace("%message_name%", UtilsPlugin.getName(items, config))
                            .replace("%message_message_join%", Objects.requireNonNull(msgJoin))
                            .replace("%message_message_quit%", Objects.requireNonNull(msgQuit))
                            .replace("%message_actionbar_join%", Objects.requireNonNull(actionBarJoin))
                            .replace("%message_actionbar_quit%", Objects.requireNonNull(actionBarQuit))
                            .replace("%message_status_locked%", statusLocked)
                            .replace("%message_status_selected%", statusSelected)
                            .replace("%player%", player.getName())
                    ));
                }
                meta.setLore(lore);
            }
            item.setItemMeta(meta);
            message.setItem(slot, item);
        }

        player.openInventory(message);
    }

    @EventHandler
    public void onClickInventory(InventoryClickEvent event) {
        FileConfiguration config = plugin.getMessageConfig();
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
                for (String decoration : Objects.requireNonNull(config.getConfigurationSection("Menu.decorations")).getKeys(false)) {
                    if (slot == config.getInt("Menu.decorations." + decoration + ".slot")) {
                        String action_type = config.getString("Menu.decorations." + decoration + ".action_type");
                        String sound_on_click = config.getString("Menu.decorations." + decoration + ".sound_on_click");
                        if (!config.getBoolean("Menu.decorations." + decoration + ".enabled"))
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
                        String message_name = UtilsPlugin.getName(item, config);
                        String sound_on_click = config.getString("Menu.items." + item + ".sound_on_click");

                        if (sound_on_click != null) {
                            UtilsBuilder.playXSound(player, sound_on_click);
                        }
                        if (permission != null && !player.hasPermission(permission) && !permission.equalsIgnoreCase("none")) {
                            UtilsPlugin.sendMessageToPlayer(player, Messages.EVENT_NO_PERMISSION.replace("%event_name%", message_name));
                            return;
                        }
                        if (!pd.hasMessageSelected(item)) {
                            UtilsPlugin.sendMessageToPlayer(player, Messages.EVENT_SELECTED.replace("%event_name%", message_name));
                            pd.setMessage(item);
                        } else {
                            UtilsPlugin.sendMessageToPlayer(player, Messages.EVENT_UNSELECTED.replace("%event_name%", message_name));
                            pd.deselectMessage();
                        }
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, () -> messagesMenu(player), 5L);
                    }
                }
            }
        }
    }
}
