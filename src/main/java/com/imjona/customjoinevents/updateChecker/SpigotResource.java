package com.imjona.customjoinevents.updateChecker;

public class SpigotResource {
	private final int id;
	private final String current_version;
	
	public SpigotResource(int id, String current_version) {
		this.id = id;
		this.current_version = current_version;
	}
	
	public String getCurrentVersion() {
		return current_version;
	}

	public String getDownloadUrl() {
		String url = ("https://www.spigotmc.org/resources/" + this.id + "/").replaceAll(" ", "-");
		String validChars = "abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ0123456789:/.-_";
        StringBuilder validUrl = new StringBuilder();
        for (int i=0;i<url.length();i++) {
        	String c = Character.toString(url.charAt(i));
        	if (validChars.contains(c))
        		validUrl.append(c);
        }
        return validUrl.toString();
	}
}
