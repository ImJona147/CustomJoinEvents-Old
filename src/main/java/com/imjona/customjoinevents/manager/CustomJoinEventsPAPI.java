package com.imjona.customjoinevents.manager;

import com.imjona.customjoinevents.CustomJoinEvents;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomJoinEventsPAPI extends PlaceholderExpansion {

    @Override
    public boolean persist() {
        return super.persist();
    }

    @Override
    public boolean canRegister() {
        return super.canRegister();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "cj";
    }

    @Override
    public @NotNull String getAuthor() {
        return "_ImJona";
    }

    @Override
    public @NotNull String getVersion() {
        return CustomJoinEvents.get().version;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null)
            return "";
        PlayerData pd = CustomJoinEvents.get().getPlayerConfigManager().getPlayer(player.getName());
        if (pd == null)
            return null;
        if (identifier.startsWith("getFireworkSelected"))
            return pd.getFireworkSelected();
        if (identifier.startsWith("hasFireworkSelected_"))
            return String.valueOf(pd.hasFireworkSelected(identifier.replace("hasFireworkSelected_", "")));

        if (identifier.startsWith("getSoundSelected"))
            return pd.getSoundSelected();
        if (identifier.startsWith("hasSoundSelected_"))
            return String.valueOf(pd.hasFireworkSelected(identifier.replace("hasSoundSelected_", "")));

        if (identifier.startsWith("getMessageSelected"))
            return pd.getMessageSelected();
        if (identifier.startsWith("hasMessageSelected_"))
            return String.valueOf(pd.hasFireworkSelected(identifier.replace("hasMessageSelected_", "")));

        if (identifier.startsWith("getTitleSelected"))
            return pd.getTitleSelected();
        if (identifier.startsWith("hasTitleSelected_"))
            return String.valueOf(pd.hasFireworkSelected(identifier.replace("hasTitleSelected_", "")));

        if (identifier.startsWith("hasOptionActivated_"))
            return String.valueOf(pd.hasFireworkSelected(identifier.replace("hasOptionActivated_", "")));
        return null;
    }
}
