package com.imjona.customjoinevents.configuration;

import com.imjona.customjoinevents.CustomJoinEvents;
import com.imjona.customjoinevents.utils.UtilsPlugin;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PlayerConfig {
    private final String path;
    private FileConfiguration config;
    private File configFile;
    private final CustomJoinEvents plugin;

    public PlayerConfig(String path, CustomJoinEvents plugin) {
        this.plugin = plugin;
        this.config = null;
        this.configFile = null;
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    public FileConfiguration getConfig() {
        if (this.config == null)
            reloadPlayerConfig();
        return this.config;
    }

    public void registerPlayerConfig() {
        this.configFile = new File(plugin.getDataFolder() + File.separator + "players", this.path);
        if (!this.configFile.exists())
            try {
                this.configFile.createNewFile();
                if (config.contains("show_message_on_console_when_new_player_file") && config.getBoolean("show_message_on_console_when_new_player_file")) {
                    UtilsPlugin.sendMessageToConsole("&7New player, creating his configuration file &b" + this.path);
                }
            } catch (IOException e) {
                UtilsPlugin.sendMessageToConsole("&7A problem has occurred in the plugin! Please report this problem to discord &6(&av&7" + plugin.version + "&6)");
                UtilsPlugin.sendMessageToConsole("&c&lERROR: &f");
                e.printStackTrace();
            }
        this.config = new YamlConfiguration();
        try {
            this.config.load(this.configFile);
        } catch (IOException | InvalidConfigurationException e) {
            UtilsPlugin.sendMessageToConsole("&7A problem has occurred in the plugin! Please report this problem to discord &6(&av&7" + plugin.version + "&6)");
            UtilsPlugin.sendMessageToConsole("&c&lERROR: &f");
            e.printStackTrace();
        }
    }

    public void savePlayerConfig() {
        try {
            this.config.save(this.configFile);
        } catch (IOException e) {
            UtilsPlugin.sendMessageToConsole("&7A problem has occurred in the plugin! Please report this problem to discord &6(&av&7" + plugin.version + "&6)");
            UtilsPlugin.sendMessageToConsole("&c&lERROR: &f");
            e.printStackTrace();
        }
    }

    public void reloadPlayerConfig() {
        if (this.config == null)
            this.configFile = new File(plugin.getDataFolder() + File.separator + "players", this.path);
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        if (this.configFile != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(this.configFile);
            this.config.setDefaults(defConfig);
        }
    }
}
