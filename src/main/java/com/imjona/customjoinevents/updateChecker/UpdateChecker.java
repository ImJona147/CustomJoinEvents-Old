package com.imjona.customjoinevents.updateChecker;


import com.imjona.customjoinevents.CustomJoinEvents;

public class UpdateChecker {
	private final String version;
	private final SpigotResource spigotResource;
	
	public UpdateChecker(CustomJoinEvents plugin, int resourceId) {
		this.version = plugin.version;
		this.spigotResource = SpigotAPI.getSpigotResource(resourceId);
	}
	
    public boolean isRunningLatestVersion() {
        return this.version.equals(this.spigotResource.getCurrentVersion());
    }

    public String getVersion() {
        return this.version;
    }

    public String getLatestVersion() {
        return this.spigotResource.getCurrentVersion();
    }

    public SpigotResource getSpigotResource() {
        return this.spigotResource;
    }

    public boolean requestIsValid() {
        return (this.spigotResource != null);
    }
}
