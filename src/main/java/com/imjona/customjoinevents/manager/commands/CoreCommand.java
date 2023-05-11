package com.imjona.customjoinevents.manager.commands;

import com.imjona.customjoinevents.CustomJoinEvents;
import com.imjona.customjoinevents.manager.menu.MainMenu;
import com.imjona.customjoinevents.utils.UtilsPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CoreCommand extends Command {

    private final ArrayList<SubCommand> subcommands;

    public CoreCommand(String name, String description, String usageMessage, List<String> aliases, ArrayList<SubCommand> subCommands) {
        super(name, description, usageMessage, aliases);
        this.subcommands = subCommands;
    }

    public ArrayList<SubCommand> getSubCommands() {
        return subcommands;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            UtilsPlugin.sendMessageDependingOnItsLanguage(
                    "&cOnly players can run this command",
                    "&cSolo los jugadores pueden ejecutar este comando");
            return false;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            new MainMenu(CustomJoinEvents.get()).mainMenu(player);
        } else  if (args.length == 1) {
            for (SubCommand subCommand : subcommands) {
                if (args[0].equalsIgnoreCase(subCommand.getName()) || subCommand.getAliases() != null && subCommand.getAliases().contains(args[0])) {
                    subCommand.perform(sender, args);
                }
            }
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) throws IllegalArgumentException {
        for (SubCommand subCommand : subcommands) {
            if (sender.hasPermission(subCommand.getPermission())) {
                if (args.length == 1) {
                    ArrayList<String> subcommandsArguments = new ArrayList<>();
                    for (int i = 0; i < getSubCommands().size(); i++) {
                        subcommandsArguments.add(getSubCommands().get(i).getName());
                    }
                    return subcommandsArguments;
                } else if (args.length >= 2) {
                    for (int i = 0; i < getSubCommands().size(); i++) {
                        if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())) {
                            List<String> subCommandArgs = getSubCommands().get(i).getSubcommandArguments((Player) sender, args
                            );
                            return Objects.requireNonNullElse(subCommandArgs, Collections.emptyList());

                        }
                    }
                }
            }
        }
        return Collections.emptyList();
    }
}
