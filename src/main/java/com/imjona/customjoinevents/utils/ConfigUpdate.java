package com.imjona.customjoinevents.utils;

import com.imjona.customjoinevents.CustomJoinEvents;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;


public class ConfigUpdate {
    private final CustomJoinEvents plugin;

    public ConfigUpdate(CustomJoinEvents plugin) {
        this.plugin = plugin;
        addNewOfConfig();
    }

    public void addNewOfConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (!config.contains("show_message_on_console_when_new_player_file")) {
            config.set("show_message_on_console_when_new_player_file", true);
            UtilsPlugin.sendMessageDependingOnItsLanguage(
                    "&7The new option '&bshow_message_on_console_when_new_player_file&7' has been added to the config.yml file.",
                    "&7Se ha agregado nueva opción en la config.yml '&bshow_message_on_console_whe_new_player_file&7'");
            UtilsPlugin.sendMessageDependingOnItsLanguage(
                    "&7Please check the &bconfig.yml &7file",
                    "&7Por favor checa el archivo &bconfig.yml");
            plugin.saveConfig();
        }
    }
}
