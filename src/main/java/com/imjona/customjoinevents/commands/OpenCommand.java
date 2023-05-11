package com.imjona.customjoinevents.commands;

import com.imjona.customjoinevents.CustomJoinEvents;
import com.imjona.customjoinevents.manager.commands.SubCommand;
import com.imjona.customjoinevents.manager.menu.*;
import com.imjona.customjoinevents.utils.Messages;
import com.imjona.customjoinevents.utils.UtilsPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class OpenCommand extends SubCommand {
    @Override
    public String getPermission() {
        return "customjoinevents.admin.open";
    }

    @Override
    public String getName() {
        return "open";
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
        if (args.length == 1) {
            String menuName = args[0];
            if (menuName.matches("firework")) {
                new FireworksMenu(CustomJoinEvents.get()).fireworksMenu(player);
            } else if (menuName.matches("sound")) {
                new SoundsMenu(CustomJoinEvents.get()).soundsMenu(player);
            } else if (menuName.matches("message")) {
                new MessagesMenu(CustomJoinEvents.get()).messagesMenu(player);
            } else if (menuName.matches("setting")) {
                new SettingsMenu(CustomJoinEvents.get()).settingsMenu(player);
            }   else if (menuName.matches("title")) {
                new TitleMenu(CustomJoinEvents.get()).titleMenu(player);
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        List<String> sub = new ArrayList<>();
        sub.add("firework");
        sub.add("sound");
        sub.add("message");
        sub.add("setting");
        sub.add("title");
        return sub;
    }
}
