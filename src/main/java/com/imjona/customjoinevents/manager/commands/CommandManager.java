package com.imjona.customjoinevents.manager.commands;

import com.imjona.customjoinevents.CustomJoinEvents;
import com.imjona.customjoinevents.utils.UtilsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {

    @SafeVarargs
    public static void createCoreCommand(String commandName,
                                         String commandDescription,
                                         String commandUsage,
                                         List<String> aliases,
                                         Class<? extends SubCommand>... subCommands) {
        ArrayList<SubCommand> commands = new ArrayList<>();
        Arrays.stream(subCommands).map(subCommand -> {
            try {
                Constructor<? extends SubCommand> constructor = subCommand.getConstructor();
                return constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                UtilsPlugin.sendMessageDependingOnItsLanguage(
                        "&7A problem has occurred in the plugin! Please report this problem to discord &6(&av&7" + CustomJoinEvents.get().version + "&6)",
                        "&7¡Ha ocurrido un problema en el plugin! Por favor, informe de este problema a discord &6(&av&7" + CustomJoinEvents.get().version + "&6)");
                UtilsPlugin.sendMessageToConsole("&c&lERROR: &f");
                e.printStackTrace();
            }
            return null;
        }).forEach(commands::add);
        CommandMap commandMap;
        try {
            Field field = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
            commandMap.register(commandName, new CoreCommand(commandName, commandDescription, commandUsage, aliases, commands));
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
            e.printStackTrace();
        }
    }
}
