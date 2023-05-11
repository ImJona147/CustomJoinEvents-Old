package com.imjona.customjoinevents.utils;

public class Options {
    private String name;

    private boolean activated;

    public Options(String name, boolean activated) {
        this.name = name;
        this.activated = activated;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActivated() {
        return this.activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
