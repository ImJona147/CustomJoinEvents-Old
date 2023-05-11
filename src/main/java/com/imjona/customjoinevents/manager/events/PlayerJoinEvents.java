package com.imjona.customjoinevents.manager.events;

import com.imjona.customjoinevents.CustomJoinEvents;
import com.imjona.customjoinevents.manager.PlayerData;
import com.imjona.customjoinevents.utils.Options;
import com.imjona.customjoinevents.utils.UtilsBuilder;
import com.imjona.customjoinevents.utils.UtilsPlugin;
import com.imjona.customjoinevents.xseries.ActionBar;
import com.imjona.customjoinevents.xseries.Titles;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class PlayerJoinEvents implements Listener {
    private final CustomJoinEvents plugin;

    public PlayerJoinEvents(CustomJoinEvents plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoinData(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        ArrayList<Options> op = new ArrayList<>();
        op.add(new Options("message", true));
        op.add(new Options("sound", true));
        op.add(new Options("title", true));
        op.add(new Options("firework", true));
        op.add(new Options("actionbar", true));

        plugin.getPlayerConfigManager().registerPlayer(player.getUniqueId() + ".yml");
        if (plugin.getPlayerConfigManager().getPlayer(player.getName()) == null)
            plugin.getPlayerConfigManager().addPlayerData(new PlayerData(
                    player.getName(),
                    player.getUniqueId().toString(),
                    "none",
                    "none",
                    "default",
                    "default",
                         op));
    }

    @EventHandler
    public void onPlayerJoinFirework(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData pd = plugin.getPlayerConfigManager().getPlayer(player.getName());
        FileConfiguration config = plugin.getFireworkConfig();

        if (pd == null)
            return;
        if (pd.hasOptionActivated("firework")) {
            String fireworkSelected = pd.getFireworkSelected();
            String firework_type = config.getString("Menu.items." + fireworkSelected + ".firework_type");
            if (firework_type != null && !fireworkSelected.equals("none"))
                UtilsBuilder.fireworkOnJoin(player, firework_type);
        }
    }

    @EventHandler
    public void onPlayerJoinMessage(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData pd = plugin.getPlayerConfigManager().getPlayer(player.getName());
        FileConfiguration config = plugin.getMessageConfig();

        if (pd == null)
            return;
        if (pd.hasOptionActivated("message")) {
            String messageSelected = pd.getMessageSelected();
            String msgJoin = config.getString("Menu.items." + messageSelected + ".message_join");
            String first_join = config.getString("Menu.first_join");

            if (first_join != null) {
                if (UtilsPlugin.getPlugin("PlaceholderAPI"))
                    first_join = PlaceholderAPI.setPlaceholders(player, first_join);
                first_join = first_join.replace("%player%", player.getName());
            }

            if (msgJoin != null) {
                if (UtilsPlugin.getPlugin("PlaceholderAPI"))
                    msgJoin = PlaceholderAPI.setPlaceholders(player, msgJoin);
                msgJoin = msgJoin.replace("%player%", player.getName());
            }

            if (!player.hasPlayedBefore()) {
                event.setJoinMessage(UtilsPlugin.fixColorMessages(first_join));
            } else {
                if (!messageSelected.equals("none")) {
                    event.setJoinMessage(UtilsPlugin.fixColorMessages(msgJoin));
                } else {
                    event.setJoinMessage("");
                }
            }
        } else {
            event.setJoinMessage("");
        }
    }

    @EventHandler
    public void onPlayerJoinTitle(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData pd = plugin.getPlayerConfigManager().getPlayer(player.getName());
        FileConfiguration config = plugin.getTitleConfig();

        if (pd == null)
            return;
        if (pd.hasOptionActivated("title")) {
            String titleSelected = pd.getTitleSelected();
            String title = UtilsPlugin.fixColorMessages(config.getString("Menu.items." + titleSelected + ".title"));

            if (UtilsPlugin.getPlugin("PlaceholderAPI"))
                title = PlaceholderAPI.setPlaceholders(player, title);
            title = title.replace("%player%", player.getName());

            String subtitle = UtilsPlugin.fixColorMessages(config.getString("Menu.items." + titleSelected + ".subtitle"));
            if (UtilsPlugin.getPlugin("PlaceholderAPI"))
                subtitle = PlaceholderAPI.setPlaceholders(player, subtitle);
            subtitle = subtitle.replace("%player%", player.getName());

            if (!title.equals("none")) {
                if (config.getBoolean("Menu.items." + titleSelected + ".title_for_all")) {
                    Titles.sendTitleForAll(title, subtitle);
                }  else {
                    Titles.sendTitle(player, title, subtitle);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoinSound(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData pd = plugin.getPlayerConfigManager().getPlayer(player.getName());
        FileConfiguration config = plugin.getSoundConfig();

        if (pd == null)
            return;
        if (pd.hasOptionActivated("sound")) {
            String soundSelected = pd.getSoundSelected();
            String sound = config.getString("Menu.items." + soundSelected + ".sound_type");
            if (sound != null && !soundSelected.equals("none")) {
                for (Player players : Bukkit.getServer().getOnlinePlayers())
                    UtilsBuilder.playXSound(players, sound);
            }
        }
    }

    @EventHandler
    public void onPlayerJoinActionBar(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData pd = plugin.getPlayerConfigManager().getPlayer(player.getName());
        FileConfiguration config = plugin.getMessageConfig();

         if (pd == null)
             return;
        if (pd.hasOptionActivated("actionbar")) {
            String messageSelected = pd.getMessageSelected();
            String actionJoin = UtilsPlugin.fixColorMessages(config.getString("Menu.items." + messageSelected + ".actionbar_join"));

            if (UtilsPlugin.getPlugin("PlaceholderAPI"))
                actionJoin = PlaceholderAPI.setPlaceholders(player, actionJoin);
            actionJoin = actionJoin.replace("%player%", player.getName());

            if (!messageSelected.equals("none") && config.contains("Menu.items." + messageSelected + ".actionbar_join")) {
                ActionBar.sendPlayersActionBar(actionJoin);
            }
        }
    }

    @EventHandler
    public void onPlayerQuitMessage(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerData pd = plugin.getPlayerConfigManager().getPlayer(player.getName());
        FileConfiguration config = plugin.getMessageConfig();

        if (pd == null)
            return;
        if (pd.hasOptionActivated("message")) {
            String messageSelected = pd.getMessageSelected();
            String msgQuit = config.getString("Menu.items." + messageSelected + ".message_quit");

            if (msgQuit != null) {
                if (UtilsPlugin.getPlugin("PlaceholderAPI"))
                    msgQuit = PlaceholderAPI.setPlaceholders(player, msgQuit);
                msgQuit = msgQuit.replace("%player%", player.getName());
            }

            if (!messageSelected.equals("none")) {
                event.setQuitMessage(UtilsPlugin.fixColorMessages(msgQuit));
            } else {
                event.setQuitMessage("");
            }
        } else {
            event.setQuitMessage("");
        }
    }

    @EventHandler
    public void onPlayerQuitActionBar(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerData pd = plugin.getPlayerConfigManager().getPlayer(player.getName());
        FileConfiguration config = plugin.getMessageConfig();

        if (pd == null)
            return;
        if (pd.hasOptionActivated("actionbar")) {
            String messageSelected = pd.getMessageSelected();
            String actionQuit = UtilsPlugin.fixColorMessages(config.getString("Menu.items." + messageSelected + ".actionbar_quit"));

            if (UtilsPlugin.getPlugin("PlaceholderAPI"))
                actionQuit = PlaceholderAPI.setPlaceholders(player, actionQuit);
            actionQuit = actionQuit.replace("%player%", player.getName());

            if (!messageSelected.equals("none") && config.contains("Menu.items." + messageSelected + ".actionbar_quit")) {
                ActionBar.sendPlayersActionBar(actionQuit);
            }
        }
    }
}
