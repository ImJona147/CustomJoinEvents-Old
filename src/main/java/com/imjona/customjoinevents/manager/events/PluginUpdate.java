package com.imjona.customjoinevents.manager.events;

import com.imjona.customjoinevents.CustomJoinEvents;
import com.imjona.customjoinevents.updateChecker.UpdateChecker;
import com.imjona.customjoinevents.utils.UtilsPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PluginUpdate implements Listener {
    private final CustomJoinEvents plugin;

    public PluginUpdate(CustomJoinEvents plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onNewUpdate(PlayerJoinEvent event) {
        FileConfiguration config = plugin.getConfig();
        Player player = event.getPlayer();

        if (config.getBoolean("check_updates")) {
            UpdateChecker up = new UpdateChecker(plugin, 91288);
            if (player.hasPermission("customjoinevents.admin.update") || player.isOp()) {
                if (up.requestIsValid()) {
                    if (!up.isRunningLatestVersion()) {
                        UtilsPlugin.sendMessageDependingOnItsLanguage(
                                player,
                                UtilsPlugin.prefix + "&7You are using version &b" + up.getVersion() + "&7 and the latest version is &a" + up.getLatestVersion(),
                                UtilsPlugin.prefix + "&7Estás usando la versión &b" + up.getVersion() + "&7 y la última versión es &a" + up.getLatestVersion());
                        UtilsPlugin.sendMessageDependingOnItsLanguage(
                                player,
                                UtilsPlugin.prefix +"&7You can download the latest version at: &b" + up.getSpigotResource().getDownloadUrl(),
                                UtilsPlugin.prefix +"&7Puedes descargar la última versión en: &b" + up.getSpigotResource().getDownloadUrl());
                    }
                } else {
                    UtilsPlugin.sendMessageDependingOnItsLanguage(
                            player,
                            "&7Could not verify if you are using the latest version of &bCustomJoin&fEvents&7!",
                            "No se pudo comprobar si está utilizando la versión más reciente de &bCustomJoin&fEvents&7!");
                    UtilsPlugin.sendMessageDependingOnItsLanguage(
                            player,
                            "&7You can disable update checker in &bconfig.yml &7file",
                            "&7Puedes desactivar el verificador automático de actualización en la &bconfig.yml");
                }
            }
        }
    }
}
