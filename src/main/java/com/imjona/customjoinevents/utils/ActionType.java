package com.imjona.customjoinevents.utils;

import com.imjona.customjoinevents.CustomJoinEvents;
import com.imjona.customjoinevents.manager.menu.*;
import org.bukkit.entity.Player;

public class ActionType {
    private final CustomJoinEvents plugin;

    public ActionType(CustomJoinEvents plugin) {
        this.plugin = plugin;
    }

    public void getActions(String action_type, Player player) {
        if (action_type.startsWith("CLOSE_MENU")) {
            player.getOpenInventory().close();
        }
        if (action_type.startsWith("OPEN_MENU_")) {
            String action =  action_type.replace("OPEN_MENU_", "");
            getInventory(action, player);
        }
        if (action_type.startsWith("BACK_MENU_")) {
            String action = action_type.replace("BACK_MENU_", "");
            getInventory(action, player);
        }
    }

    public void getInventory(String inv, Player player) {
        if (inv.equalsIgnoreCase("FIREWORK"))
            new FireworksMenu(plugin).fireworksMenu(player);
        if (inv.equalsIgnoreCase("MAIN"))
           new MainMenu(plugin).mainMenu(player);
        if (inv.equalsIgnoreCase("SOUND"))
            new SoundsMenu(plugin).soundsMenu(player);
        if (inv.equalsIgnoreCase("SETTINGS"))
            new SettingsMenu(CustomJoinEvents.get()).settingsMenu(player);
        if (inv.equalsIgnoreCase("MESSAGE"))
            new MessagesMenu(CustomJoinEvents.get()).messagesMenu(player);
        if (inv.equalsIgnoreCase("TITLE"))
            new TitleMenu(CustomJoinEvents.get()).titleMenu(player);
    }
}
