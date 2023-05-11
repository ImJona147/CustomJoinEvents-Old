package com.imjona.customjoinevents.updateChecker;

import com.google.gson.Gson;
import com.imjona.customjoinevents.CustomJoinEvents;
import com.imjona.customjoinevents.utils.UtilsPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class SpigotAPI {
	
	public static SpigotResource getSpigotResource(int id) {
		StringBuilder response = new StringBuilder();
		try {
			URL urlObject = new URL("https://api.spigotmc.org/simple/0.2/index.php?action=getResource&id=" + id);
			URLConnection urlConnection = urlObject.openConnection();
			BufferedReader bf = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String line;
			while((line = bf.readLine()) != null) 
				response.append(line);
			bf.close();
		} catch(IOException e) {
			UtilsPlugin.sendMessageDependingOnItsLanguage(
					"&7A problem has occurred in the plugin! Please report this problem to discord &6(&av&7" + CustomJoinEvents.get().version + "&6)",
					"&7¡Ha ocurrido un problema en el plugin! Por favor, informe de este problema a discord &6(&av&7" + CustomJoinEvents.get().version + "&6)");
			UtilsPlugin.sendMessageToConsole("&c&lERROR: &f");
			e.printStackTrace();
		}
		Gson gson = new Gson();
		return gson.fromJson(response.toString(), SpigotResource.class);
	}
}
