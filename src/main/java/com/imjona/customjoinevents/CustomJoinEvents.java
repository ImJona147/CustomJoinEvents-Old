package com.imjona.customjoinevents;

import com.imjona.customjoinevents.configuration.ConfigManager;
import com.imjona.customjoinevents.configuration.PlayerConfigManager;
import com.imjona.customjoinevents.configuration.lang.LanguageManager;
import com.imjona.customjoinevents.utils.Checks;
import com.imjona.customjoinevents.utils.ConfigUpdate;
import com.imjona.customjoinevents.utils.Metrics;
import com.imjona.customjoinevents.utils.UtilsPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class CustomJoinEvents extends JavaPlugin {
	private static CustomJoinEvents cje;

	private LanguageManager languageManager;
	private PlayerConfigManager playerConfigManager;
	private ConfigManager configManager;
	
	private final PluginDescriptionFile file = getDescription();
	public String version = file.getVersion();

	
	@Override
	public void onEnable() {
		cje = this;
		UtilsPlugin.sendMessageToConsole("&7Enabling &bCustomJoin&fEvents &7v&a" + version + "&7 by &6_ImJona");
        loadConfig();
        this.languageManager = new LanguageManager(this, getConfig().getString("language"));
		playerConfigManager = new PlayerConfigManager(this);
		configManager = new ConfigManager(this);
		configManager.loadConfigs();
		new ConfigUpdate(this);
		new Checks(this);
		new Metrics(this, 11037);
		UtilsPlugin.sendMessageToConsole("&7Get support at my discord: &b_ImJona#2065");
	}
	
	@Override
	public void onDisable() {
		UtilsPlugin.sendMessageToConsole("&7Disabling &bCustomJoin&fEvents &7v&a" + version + "&7 by &6_ImJona");
		getPlayerConfigManager().saves();
		UtilsPlugin.sendMessageToConsole("&7Get support at my discord: &b_ImJona#2065");
	}

	private void loadConfig() {
		File config = new File(getDataFolder(), "config.yml");
		if (!config.exists()) {
			this.saveResource("config.yml", false);
		}
		try {
			getConfig().load(config);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static CustomJoinEvents get() {
		return cje;
	}

	public LanguageManager getLanguageManager() {
		return languageManager;
	}

	public FileConfiguration getFireworkConfig() {
		return getConfigManager().getConfig(0);
	}

	public FileConfiguration getMessageConfig() {
		return getConfigManager().getConfig(1);
	}

	public FileConfiguration getSettingConfig() {
		return getConfigManager().getConfig(2);
	}

	public FileConfiguration getSoundConfig() {
		return getConfigManager().getConfig(3);
	}

	public FileConfiguration getTitleConfig() {
		return getConfigManager().getConfig(4);
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public PlayerConfigManager getPlayerConfigManager() {
		return playerConfigManager;
	}

}
