package com.imjona.customjoinevents.manager.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class SubCommand {
    public abstract String getPermission();
    public abstract String getName();
    public abstract List<String> getAliases();
    public abstract void perform(CommandSender sender, String[] args);
    public abstract List<String> getSubcommandArguments(Player player, String[] args);
}
