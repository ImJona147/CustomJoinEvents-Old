package com.imjona.customjoinevents.commands;

import com.imjona.customjoinevents.CustomJoinEvents;
import com.imjona.customjoinevents.manager.commands.SubCommand;
import com.imjona.customjoinevents.utils.Messages;
import com.imjona.customjoinevents.utils.UtilsPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ReloadCommand  extends SubCommand {

    @Override
    public String getPermission() {
        return "customjoinevents.admin.reload";
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission(getPermission())) {
            UtilsPlugin.sendMessageToPlayer(player, Messages.NO_PERMISSION);
            return;
        }
        CustomJoinEvents.get().reloadConfig();
        /*
        CustomJoinEvents.get().getConfigs().reloadSoundConfig();
        CustomJoinEvents.get().getConfigs().reloadFireworkConfig();
        CustomJoinEvents.get().getConfigs().reloadTitleConfig();
        CustomJoinEvents.get().getConfigs().reloadMessageConfig();
        CustomJoinEvents.get().getConfigs().reloadSettingConfig();
         */
        CustomJoinEvents.get().getConfigManager().reloadConfigs();
        CustomJoinEvents.get().getLanguageManager().setSelectedLang(CustomJoinEvents.get().getConfig().getString("language"));
        UtilsPlugin.sendMessageToPlayer(player, Messages.RELOAD);
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
