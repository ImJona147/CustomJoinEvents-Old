package com.imjona.customjoinevents.configuration.lang;

import com.imjona.customjoinevents.CustomJoinEvents;
import com.imjona.customjoinevents.utils.UtilsPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LanguageManager {
    private final CustomJoinEvents plugin;
    private String selectedLanguage;
    private FileConfiguration languageConfig = null;
    private File languageFile = null;

    public LanguageManager(CustomJoinEvents plugin, String selectedLanguage) {
        this.plugin = plugin;
        this.selectedLanguage = selectedLanguage;
        loadLang();
    }

    public void loadLang() {
        this.languageFile = new File(plugin.getDataFolder(), "translations/" + this.selectedLanguage + ".yml");
        if (!this.languageFile.exists()) {
            getLanguageConfig().options().copyDefaults(true);
            plugin.saveResource("translations/" + this.selectedLanguage + ".yml", false);
        }
        this.languageConfig = YamlConfiguration.loadConfiguration(this.languageFile);

        try {
            this.languageConfig = YamlConfiguration.loadConfiguration(this.languageFile);
            UtilsPlugin.sendMessageDependingOnItsLanguage("&7Loading language: &b" + selectedLanguage,
                                                          "&7Cargando idioma: &b" + selectedLanguage);
        } catch (Exception e) {
            this.languageConfig = new YamlConfiguration();
            e.printStackTrace();
        }
    }

    public FileConfiguration getLanguageConfig() {
        if (this.languageConfig == null)
            reloadLang();
        return this.languageConfig;
    }

    public String getLanguage() {
        return this.selectedLanguage;
    }

    public void setSelectedLang(String lang) {
        this.selectedLanguage = lang;
        this.reloadLang();
    }

    public void reloadLang() {
        if (this.languageConfig == null)
            this.languageFile = new File(this.plugin.getDataFolder(), "translations/" + this.selectedLanguage + ".yml");

        loadLang();
    }

    public String getMessage(String key) {
        return UtilsPlugin.fixColorMessages(getLanguageConfig().getString(key));
    }
}

