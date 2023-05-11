package com.imjona.customjoinevents.configuration;

import com.imjona.customjoinevents.CustomJoinEvents;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ConfigManager {
    private final CustomJoinEvents plugin;
    private final String[] configNames = {"fireworks.yml", "messages.yml", "settings.yml", "sounds.yml", "titles.yml"};
    private final FileConfiguration[] configs = new FileConfiguration[configNames.length];

    public ConfigManager(CustomJoinEvents plugin) {
        this.plugin = plugin;
    }

    public void loadConfigs() {
        for (int i = 0; i < configNames.length; i++) {
            String fileName = configNames[i];
            File configFile = new File(plugin.getDataFolder(), fileName);

            if (!configFile.exists()) {
                getConfigs()[i].options().copyDefaults(true);
                plugin.saveResource(fileName, false);
            }

            getConfigs()[i] = YamlConfiguration.loadConfiguration(configFile);
            Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(plugin.getResource(fileName)), StandardCharsets.UTF_8);
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);

            getConfigs()[i].setDefaults(defConfig);
        }
    }

    public void reloadConfigs() {
        for (int i = 0; i < configNames.length; i++) {
            String fileName = configNames[i];
            File configFile = new File(plugin.getDataFolder(), fileName);

            getConfigs()[i] = YamlConfiguration.loadConfiguration(configFile);

            Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(plugin.getResource(fileName)), StandardCharsets.UTF_8);
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);

            getConfigs()[i].setDefaults(defConfig);
        }
    }

    public FileConfiguration getConfig(int index) {
        return getConfigs()[index];
    }

    public FileConfiguration[] getConfigs() {
        return configs;
    }
}
