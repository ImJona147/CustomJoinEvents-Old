package com.imjona.customjoinevents.configuration;

import com.imjona.customjoinevents.CustomJoinEvents;
import com.imjona.customjoinevents.manager.PlayerData;
import com.imjona.customjoinevents.utils.Options;
import com.imjona.customjoinevents.utils.UtilsPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class PlayerConfigManager {
    private final CustomJoinEvents plugin;
    private final ArrayList<PlayerConfig> playerConfigs;
    private final ArrayList<PlayerData> playerData;

    public PlayerConfigManager(CustomJoinEvents plugin) {
        this.playerConfigs = new ArrayList<>();
        this.playerData = new ArrayList<>();
        this.plugin = plugin;
        createPlayersFolder();
        registerPlayers();
        loads();
    }

    public void registerPlayers() {
        String path = this.plugin.getDataFolder() + File.separator + "players";
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    String pathName = listOfFile.getName();
                    PlayerConfig config = new PlayerConfig(pathName, this.plugin);
                    config.registerPlayerConfig();
                    this.playerConfigs.add(config);
                }
            }
        }
    }

    public void savePlayersData() {
        for (PlayerConfig playerConfig : this.playerConfigs)
            playerConfig.savePlayerConfig();
    }

    public void createPlayersFolder() {
        try {
            File folder = new File(this.plugin.getDataFolder() + File.separator + "players");
            if (!folder.exists())
                UtilsPlugin.sendMessageDependingOnItsLanguage(
                        "&7Creating the players data folder",
                        "&7Creando la carpeta de datos de los jugadores");
            folder.mkdirs();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public boolean FileAlreadyRegistered(String pathName) {
        for (PlayerConfig playerConfig : this.playerConfigs) {
            if (playerConfig.getPath().equals(pathName))
                return true;
        }
        return false;
    }

    public PlayerConfig getPlayerConfig(String pathName) {
        for (PlayerConfig playerConfig : this.playerConfigs) {
            if (playerConfig.getPath().equals(pathName))
                return playerConfig;
        }
        return null;
    }

    public void registerPlayer(String pathName) {
        if (!FileAlreadyRegistered(pathName)) {
            PlayerConfig config = new PlayerConfig(pathName, this.plugin);
            config.registerPlayerConfig();
            this.playerConfigs.add(config);
        }
    }

    public void removeConfigPlayer(String path) {
        for (int i = 0; i < this.playerConfigs.size(); i++) {
            if (this.playerConfigs.get(i).getPath().equals(path))
                this.playerConfigs.remove(i);
        }
    }

    public void loads() {
        for (PlayerConfig playerConfig : this.playerConfigs) {
            FileConfiguration players = playerConfig.getConfig();
            String player_name = players.getString("player_name");
            String player_uuid = playerConfig.getPath().replace(".yml", "");
            String firework = players.getString("firework_selected");
            String sound = players.getString("sound_selected");
            String message = players.getString("message_selected");
            String title = players.getString("title_selected");

            ArrayList<Options> options = new ArrayList<>();

            for (String op : Objects.requireNonNull(players.getConfigurationSection("Options")).getKeys(false)) {
                boolean activated;
                if (players.contains("Options." + op + ".activated")) {
                    activated = players.getBoolean("Options." + op + ".activated");
                    options.add(new Options(op, activated));
                }
            }
            addPlayerData(new PlayerData(player_name, player_uuid, firework, sound, message, title, options));
        }
    }

    public void saves() {
        for (PlayerData pd : this.playerData) {
            PlayerConfig playerConfig = getPlayerConfig(pd.getUuid() + ".yml");
            FileConfiguration players = playerConfig.getConfig();
            String name = pd.getName();
            String firework = pd.getFireworkSelected();
            String sound = pd.getSoundSelected();
            String message = pd.getMessageSelected();
            String title = pd.getTitleSelected();
            players.set("player_name", name);
            players.set("firework_selected", firework);
            players.set("sound_selected", sound);
            players.set("message_selected", message);
            players.set("title_selected", title);
            for (Options op : pd.getOptions()) {
                String option = op.getName();
                players.set("Options." + option + ".activated", op.isActivated());
            }
        }
        savePlayersData();
        UtilsPlugin.sendMessageDependingOnItsLanguage(
                "&7Saving players data...",
                "&7Guardando datos de los jugadores...");
    }

    public void addPlayerData(PlayerData playerData) {
        this.playerData.add(playerData);
    }

    public PlayerData getPlayer(String name) {
        for (PlayerData p : this.playerData) {
            if (p != null && p.getName() != null && p.getName().equals(name))
                return p;
        }
        return null;
    }
}
