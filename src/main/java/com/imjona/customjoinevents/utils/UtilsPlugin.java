package com.imjona.customjoinevents.utils;

import com.imjona.customjoinevents.CustomJoinEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilsPlugin {
    public static String prefix = "&8[&bCustomJoin&fEvents&8] ";

    public static String fixColorMessages(String message) {
        if (Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17") || Bukkit.getVersion().contains("1.18") ||
                Bukkit.getVersion().contains("1.19")) {
            Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
            Matcher match = pattern.matcher(message);
            while (match.find()) {
                String color = message.substring(match.start(), match.end());
                message = message.replace(color, net.md_5.bungee.api.ChatColor.of(color).toString());
                match = pattern.matcher(message);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendMessageToPlayer(CommandSender player, String message) {
        player.sendMessage(fixColorMessages(message));
    }
	
	public static void sendMessageToConsole(String message) {
		ConsoleCommandSender sender = Bukkit.getServer().getConsoleSender();
        sender.sendMessage(fixColorMessages(prefix + message));
	}

    public static Boolean getPlugin(String plugin) {
        return CustomJoinEvents.get().getServer().getPluginManager().getPlugin(plugin) != null;
    }

    public static String getName(String path, FileConfiguration config) {
        if (Objects.requireNonNull(config.getString("settings_name")).equalsIgnoreCase("DISPLAY_NAME")) {
            return config.getString("Menu.items." + path + ".display_name");
        } else if (Objects.requireNonNull(config.getString("settings_name")).equalsIgnoreCase("KEY")) {
            return Objects.requireNonNull(config.getString("Menu.items." + path)).replace("MemorySection[path='Menu.items." + path + "', root='YamlConfiguration']", path);
        }
        return "null";
    }
}
