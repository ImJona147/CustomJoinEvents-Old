package com.imjona.customjoinevents.utils;

import com.imjona.customjoinevents.CustomJoinEvents;
import com.imjona.customjoinevents.commands.HelpCommand;
import com.imjona.customjoinevents.commands.ReloadCommand;
import com.imjona.customjoinevents.commands.SaveCommand;
import com.imjona.customjoinevents.manager.CustomJoinEventsPAPI;
import com.imjona.customjoinevents.manager.commands.CommandManager;
import com.imjona.customjoinevents.manager.events.PlayerJoinEvents;
import com.imjona.customjoinevents.manager.events.PluginUpdate;
import com.imjona.customjoinevents.manager.menu.*;
import com.imjona.customjoinevents.updateChecker.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;

public class Checks {
	private final CustomJoinEvents plugin;

	public Checks(CustomJoinEvents plugin) {
		this.plugin = plugin;
		registerCommand();
		checkPlaceholdersAPI();
		registerEvents();
		UpdateChecker();
	}

	private void registerCommand() {
		List<String> commandAliases = new ArrayList<>();
		commandAliases.add("customjoinevents");
		commandAliases.add("customjoin");
		commandAliases.add("cje");
		CommandManager.createCoreCommand(
				"cj",
				"Main Command",
				"/cj",
				commandAliases,
				ReloadCommand.class,
				HelpCommand.class,
				SaveCommand.class);
		//OpenCommand.class);
	}
	
	private void checkPlaceholdersAPI() {
		if (UtilsPlugin.getPlugin("PlaceholderAPI")) {
			(new CustomJoinEventsPAPI()).register();
            UtilsPlugin.sendMessageDependingOnItsLanguage(
					"&7PlaceholderAPI support: &aTrue",
					"&7Soporte a PlaceholderAPI: &aSi");
        } else {
			UtilsPlugin.sendMessageDependingOnItsLanguage(
					"&7PlaceholderAPI support: &cFalse",
					"&7Soporte a PlaceholderAPI: &cNo");
        }
	}
	
	private void registerEvents() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new MainMenu(plugin), plugin);
		pm.registerEvents(new PlayerJoinEvents(plugin), plugin);
		pm.registerEvents(new FireworksMenu(plugin), plugin);
		pm.registerEvents(new SettingsMenu(plugin), plugin);
		pm.registerEvents(new MessagesMenu(plugin), plugin);
		pm.registerEvents(new SoundsMenu(plugin), plugin);
		pm.registerEvents(new TitleMenu(plugin), plugin);
		pm.registerEvents(new PluginUpdate(plugin), plugin);
	}
	
	private void UpdateChecker() {
		FileConfiguration config = plugin.getConfig();
		if (config.getBoolean("check_updates")) {
			UpdateChecker up = new UpdateChecker(plugin, 91288);
			if (up.requestIsValid()) {
				if (!up.isRunningLatestVersion()) {
					UtilsPlugin.sendMessageDependingOnItsLanguage(
							UtilsPlugin.prefix + "&7You are using version &b" + up.getVersion() + "&7 and the latest version is &a" + up.getLatestVersion(),
							UtilsPlugin.prefix + "&7Estás usando la versión &b" + up.getVersion() + "&7 y la última versión es &a" + up.getLatestVersion());
					UtilsPlugin.sendMessageDependingOnItsLanguage(
							UtilsPlugin.prefix +"&7You can download the latest version at: &b" + up.getSpigotResource().getDownloadUrl(),
							UtilsPlugin.prefix +"&7Puedes descargar la última versión en: &b" + up.getSpigotResource().getDownloadUrl());
				}
			} else {
				UtilsPlugin.sendMessageDependingOnItsLanguage(
						"&7Could not verify if you are using the latest version of &bCustomJoin&fEvents&7!",
						"No se pudo comprobar si está utilizando la versión más reciente de &bCustomJoin&fEvents&7!");
				UtilsPlugin.sendMessageDependingOnItsLanguage(
						"&7You can disable update checker in &bconfig.yml &7file",
						"&7Puedes desactivar el verificador automático de actualización en la &bconfig.yml");
			}
		}
	}
}
