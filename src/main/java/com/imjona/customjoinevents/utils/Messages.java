package com.imjona.customjoinevents.utils;

import com.imjona.customjoinevents.CustomJoinEvents;

public class Messages {

    public static String getMessages(String message) {
        return CustomJoinEvents.get().getLanguageManager().getMessage(message);
    }

    public static String PREFIX = getMessages("translations.prefix");
    public static String RELOAD = getMessages("translations.reload").replace("%prefix%", PREFIX);
    public static String NO_PERMISSION = getMessages("translations.no_permission").replace("%prefix%", PREFIX);
    public static String SAVE_PLAYER_DATA = getMessages("translations.save_player_data").replace("%prefix%", PREFIX);
    public static String EVENT_NO_PERMISSION = getMessages("translations.event_no_permission").replace("%prefix%", PREFIX);
    public static String SETTING_ACTIVATED = getMessages("translations.settings_activated").replace("%prefix%", PREFIX);
    public static String SETTING_DISABLED = getMessages("translations.settings_disabled").replace("%prefix%", PREFIX);

    public static String EVENT_SELECTED = getMessages("translations.event_selected").replace("%prefix%", PREFIX);
    public static String EVENT_UNSELECTED = getMessages("translations.event_unselected").replace("%prefix%", PREFIX);

    public static String LOCKED = getMessages("translations.locked");
    public static String UNLOCKED = getMessages("translations.unlocked");
    public static String SELECTED = getMessages("translations.selected");
    public static String UNSELECTED = getMessages("translations.unselected");

    public static String ACTIVATED = getMessages("translations.activated");
    public static String DISABLED = getMessages("translations.disabled");


    public static String ss;
}
