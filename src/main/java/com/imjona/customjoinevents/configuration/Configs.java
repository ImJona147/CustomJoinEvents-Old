package com.imjona.customjoinevents.configuration;

import com.imjona.customjoinevents.CustomJoinEvents;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Configs {
	private final CustomJoinEvents plugin;
	/*
	Firework Config
	 */
	private FileConfiguration fireworkConfig = null;
	private File fireworkFile = null;

	/*
	Message Config
	 */
	private FileConfiguration messageConfig = null;
	private File messageFile = null;

	/*
	Message Config
	 */
	private FileConfiguration settingConfig = null;
	private File settingFile = null;

	/*
	Sound Config
	 */
	private FileConfiguration soundConfig = null;
	private File soundFile = null;

	/*
	Title Config
	 */
	private FileConfiguration titleConfig = null;
	private File titleFile = null;

	public Configs(CustomJoinEvents plugin) {
		this.plugin = plugin;
		loadFireworkConfig();
		loadMessageConfig();
		loadSettingsConfig();
		loadSoundConfig();
		loadTitleConfig();
	}

	/*
	Firework Config
	 */

	public void loadFireworkConfig() {
		fireworkFile = new File(plugin.getDataFolder(), "fireworks.yml");
		if (!this.fireworkFile.exists()) {
			getFireworkConfig().options().copyDefaults(true);
			plugin.saveResource("fireworks.yml", false);
		}
	}

	public FileConfiguration getFireworkConfig() {
		if (this.fireworkConfig == null)
			reloadFireworkConfig();
		return this.fireworkConfig;
	}

	public void reloadFireworkConfig() {
		if (this.fireworkConfig == null)
			this.fireworkFile = new File(plugin.getDataFolder(), "fireworks.yml");
		this.fireworkConfig = YamlConfiguration.loadConfiguration(this.fireworkFile);
		Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(plugin.getResource("fireworks.yml")), StandardCharsets.UTF_8);
		YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		this.fireworkConfig.setDefaults(defConfig);
	}

	/*
	Messages Config
	 */

	public void loadMessageConfig() {
		messageFile = new File(plugin.getDataFolder(), "messages.yml");
		if (!this.messageFile.exists()) {
			getMessageConfig().options().copyDefaults(true);
			plugin.saveResource("messages.yml", false);
		}
	}


	public FileConfiguration getMessageConfig() {
		if (this.messageConfig == null)
			reloadMessageConfig();
		return this.messageConfig;
	}

	public void reloadMessageConfig() {
		if (this.messageConfig == null)
			this.messageFile = new File(plugin.getDataFolder(), "messages.yml");
		this.messageConfig = YamlConfiguration.loadConfiguration(this.messageFile);
		Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(plugin.getResource("messages.yml")), StandardCharsets.UTF_8);
		YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		this.messageConfig.setDefaults(defConfig);
	}

	/*
	Settings Config
	 */

	public void loadSettingsConfig() {
		settingFile = new File(plugin.getDataFolder(), "settings.yml");
		if (!this.settingFile.exists()) {
			getSettingConfig().options().copyDefaults(true);
			plugin.saveResource("settings.yml", false);
		}
	}

	public FileConfiguration getSettingConfig() {
		if (this.settingConfig == null)
			reloadSettingConfig();
		return this.settingConfig;
	}

	public void reloadSettingConfig() {
		if (this.settingConfig == null)
			this.settingFile = new File(plugin.getDataFolder(), "settings.yml");
		this.settingConfig = YamlConfiguration.loadConfiguration(this.settingFile);
		Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(plugin.getResource("settings.yml")), StandardCharsets.UTF_8);
		YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		this.settingConfig.setDefaults(defConfig);
	}

	/*
	Sound Config
	 */
	public void loadSoundConfig() {
		soundFile = new File(plugin.getDataFolder(), "sounds.yml");
		if (!this.soundFile.exists()) {
			getSoundConfig().options().copyDefaults(true);
			plugin.saveResource("sounds.yml", false);
		}
	}

	public FileConfiguration getSoundConfig() {
		if (this.soundConfig == null)
			reloadSoundConfig();
		return this.soundConfig;
	}

	public void reloadSoundConfig() {
		if (this.soundConfig == null)
			this.soundFile = new File(plugin.getDataFolder(), "sounds.yml");
		this.soundConfig = YamlConfiguration.loadConfiguration(this.soundFile);
		Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(plugin.getResource("sounds.yml")), StandardCharsets.UTF_8);
		YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		this.soundConfig.setDefaults(defConfig);
	}

	/*
	Title Config
	 */
	public void loadTitleConfig() {
		titleFile = new File(plugin.getDataFolder(), "titles.yml");
		if (!this.titleFile.exists()) {
			getTitleConfig().options().copyDefaults(true);
			plugin.saveResource("titles.yml", false);
		}
	}

	public FileConfiguration getTitleConfig() {
		if (this.titleConfig == null)
			reloadTitleConfig();
		return this.titleConfig;
	}

	public void reloadTitleConfig() {
		if (this.titleConfig == null)
			this.titleFile = new File(plugin.getDataFolder(), "titles.yml");
		this.titleConfig = YamlConfiguration.loadConfiguration(this.titleFile);
		Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(plugin.getResource("titles.yml")), StandardCharsets.UTF_8);
		YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		this.titleConfig.setDefaults(defConfig);
	}
}
