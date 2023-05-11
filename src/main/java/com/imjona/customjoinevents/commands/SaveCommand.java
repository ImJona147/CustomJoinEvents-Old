package com.imjona.customjoinevents.commands;

import com.imjona.customjoinevents.CustomJoinEvents;
import com.imjona.customjoinevents.manager.commands.SubCommand;
import com.imjona.customjoinevents.utils.Messages;
import com.imjona.customjoinevents.utils.UtilsPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SaveCommand extends SubCommand {
    @Override
    public String getPermission() {
        return "customjoinevents.admin.save";
    }

    @Override
    public String getName() {
        return "save";
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
        UtilsPlugin.sendMessageToPlayer(player, Messages.SAVE_PLAYER_DATA);
        CustomJoinEvents.get().getPlayerConfigManager().saves();
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
