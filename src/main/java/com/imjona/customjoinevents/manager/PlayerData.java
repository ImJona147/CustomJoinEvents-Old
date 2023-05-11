package com.imjona.customjoinevents.manager;

import com.imjona.customjoinevents.utils.Options;

import java.util.ArrayList;

public class PlayerData {
    private String name;
    private final String uuid;
    private String firework;
    private String sound;
    private String message;
    private String title;
    private ArrayList<Options> options;

    public PlayerData(String name,
                      String uuid,
                      String firework,
                      String sound,
                      String message,
                      String title,
                      ArrayList<Options> options) {
        this.name = name;
        this.uuid = uuid;
        this.firework = firework;
        this.sound = sound;
        this.message = message;
        this.title = title;
        this.options = options;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    /*
       Datos de Fuegos artificiales
    */

    public void setFirework(String firework) {
        this.firework = firework;
    }

    public String getFireworkSelected() {
        return this.firework;
    }

    public boolean hasFireworkSelected(String firework) {
        return this.firework.equals(firework);
    }

    public void deselectFirework() {
        this.firework = "none";
    }

    /*
    Datos de Sonidos
     */

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getSoundSelected() {
        return this.sound;
    }

    public boolean hasSoundSelected(String sound) {
        return this.sound.equals(sound);
    }

    public void deselectSound() {
        this.sound = "none";
    }

    /*
    Datos de Mensajes de entrada
     */

    public void setMessage(String msg) {
        this.message = msg;
    }

    public String getMessageSelected() {
        return this.message;
    }

    public boolean hasMessageSelected(String msg) {
        return this.message.equals(msg);
    }

    public void deselectMessage() {
        this.message = "none";
    }

    /*
    Datos de titulos
     */

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleSelected() {
        return this.title;
    }

    public boolean hasTitleSelected(String title) {
        return this.title.equals(title);
    }

    public void deselectTitle() {
        this.title = "none";
    }

    /*
    Datos Configuraciones
     */

    public void setOption(String option) {
        for (Options value : this.options) {
            if (value.getName().equals(option) && !value.isActivated())
                value.setActivated(true);
        }
    }

    public boolean hasOptionActivated(String option) {
        for (Options value : this.options) {
            if (value.getName().equals(option) &&
                    value.isActivated())
                return true;
        }
        return false;
    }

    public void deselectOption(String option) {
        for (Options value : this.options) {
            if (value.getName().equals(option) &&
                    value.isActivated())
                value.setActivated(false);
        }
    }

    public void deselAllOptions() {
        for (Options value : this.options)
            value.setActivated(false);
    }

    public void setOption(ArrayList<Options> options) {
        this.options = options;
    }

    public ArrayList<Options> getOptions() {
        return options;
    }
}
