package com.imjona.customjoinevents.commands;

import com.imjona.customjoinevents.CustomJoinEvents;
import com.imjona.customjoinevents.manager.commands.SubCommand;
import com.imjona.customjoinevents.utils.Messages;
import com.imjona.customjoinevents.utils.UtilsPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HelpCommand extends SubCommand {

    @Override
    public String getPermission() {
        return "customjoinevents.admin.help";
    }

    @Override
    public String getName() {
        return "help";
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
        if (CustomJoinEvents.get().getLanguageManager().getLanguage().equals("en")) {
            sendHelpEnglish(player);
        } else if (CustomJoinEvents.get().getLanguageManager().getLanguage().equals("es")) {
            sendHelpSpanish(player);
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }

    private void sendHelpEnglish(Player sender) {
        UtilsPlugin.sendMessageToPlayer(sender, "&8&m-------------------------------------");
        UtilsPlugin.sendMessageToPlayer(sender, "&e&l» &b&lCustomJoin&f&lEvents &8- &7" + CustomJoinEvents.get().version + "&e&l «");
        UtilsPlugin.sendMessageToPlayer(sender, "");
        UtilsPlugin.sendMessageToPlayer(sender, "&a&l* &b/cj &9- &fOpen the main menu");
        UtilsPlugin.sendMessageToPlayer(sender, "&a&l* &b/cj help &9- &fSend this message");
        UtilsPlugin.sendMessageToPlayer(sender, "&a&l* &b/cj reload &9- &fReload config");
        UtilsPlugin.sendMessageToPlayer(sender, "&a&l* &b/cj save &9- &fSave player data");
        //UtilsPlugin.sendMessageToPlayer(sender, "&a&l* &b/cj open <menu> <player> &9- &fOpen a menu specifically for a player");
        //UtilsPlugin.sendMessageToPlayer(sender, "&a&l* &b/cj create <type> &9- &fCrea un nuevo evento mediante menu");
        UtilsPlugin.sendMessageToPlayer(sender, "");
        UtilsPlugin.sendMessageToPlayer(sender, "&b&lGet support at my discord: &f_ImJona#2065");
        UtilsPlugin.sendMessageToPlayer(sender, "&8&m-------------------------------------");
    }

    private void sendHelpSpanish(Player sender) {
        UtilsPlugin.sendMessageToPlayer(sender, "&8&m-------------------------------------");
        UtilsPlugin.sendMessageToPlayer(sender, "&e&l» &b&lCustomJoin&f&lEvents &8- &7" + CustomJoinEvents.get().version + "&e&l «");
        UtilsPlugin.sendMessageToPlayer(sender, "");
        UtilsPlugin.sendMessageToPlayer(sender, "&a&l* &b/cj &9- &fAbrir el menú principal");
        UtilsPlugin.sendMessageToPlayer(sender, "&a&l* &b/cj help &9- &fEnvía este mensaje");
        UtilsPlugin.sendMessageToPlayer(sender, "&a&l* &b/cj reload &9- &fRecarga el plugin");
        UtilsPlugin.sendMessageToPlayer(sender, "&a&l* &b/cj save &9- &fGuardar los datos de los jugadores");
        //UtilsPlugin.sendMessageToPlayer(sender, "&a&l* &b/cj open <menu> <player> &9- &fOpen a menu specifically for a player");
        //UtilsPlugin.sendMessageToPlayer(sender, "&a&l* &b/cj create <type> &9- &fCrea un nuevo evento mediante menu");
        UtilsPlugin.sendMessageToPlayer(sender, "");
        UtilsPlugin.sendMessageToPlayer(sender, "&b&lObtén soporte en mi discord: &f_ImJona#2065");
        UtilsPlugin.sendMessageToPlayer(sender, "&8&m-------------------------------------");
    }
}
