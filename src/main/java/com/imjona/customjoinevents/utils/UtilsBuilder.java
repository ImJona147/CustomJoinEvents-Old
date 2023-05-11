package com.imjona.customjoinevents.utils;

import com.imjona.customjoinevents.xseries.SkullUtils;
import com.imjona.customjoinevents.xseries.XMaterial;
import com.imjona.customjoinevents.xseries.XSound;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;


public class UtilsBuilder {
	private static void playerXSound(Player player, String sound, byte volume, float pitch) {
		if(XSound.matchXSound(sound).isPresent()) {
			if (XSound.matchXSound(sound).get().isSupported()) {
				player.playSound(player.getLocation(), Objects.requireNonNull(XSound.matchXSound(sound).get().parseSound()), volume, pitch);
			} else {
				UtilsPlugin.sendMessageDependingOnItsLanguage(
						"&7Sound &b" + sound + "&7 is not supported on this version",
						"&7El sonido &b" + sound + "&7 no tiene soporte en esta versión");
				if (player.isOp()) {
					UtilsPlugin.sendMessageDependingOnItsLanguage(player,
							UtilsPlugin.prefix + "&cAn error has occurred with the sounds, please check the console",
							UtilsPlugin.prefix + "&cHa ocurrido un error con los sonidos, por favor, checa la consola");
				}
			}
		} else {
			UtilsPlugin.sendMessageDependingOnItsLanguage(
					"&7The value &b" + sound + "&7 is not a sound",
					"&7El valor &b" + sound + "&7 no es un sonido");
			if (player.isOp()) {
				UtilsPlugin.sendMessageDependingOnItsLanguage(player,
						UtilsPlugin.prefix + "&cAn error has occurred with the sounds, please check the console",
						UtilsPlugin.prefix + "&cHa ocurrido un error con los sonidos, por favor, checa la consola");
			}
		}
	}
	
	public static void playXSound(Player player, String path) {
		if (!path.equalsIgnoreCase("none")) {
			String[] getSound = path.split(";");
			float pitch = 1;
			byte vol = 1;
			String sound = getSound[0];
			if (getSound.length == 2) {
				vol = Byte.parseByte(getSound[1]);
			} else if (getSound.length == 3) {
				pitch = Float.parseFloat(getSound[2]);
			}
			playerXSound(player, sound, vol, pitch);
		}
	}

	public static void fireworkOnJoin(Player player, String path) {
		if (!path.equalsIgnoreCase("none")) {
			String[] sp = path.split(";");
			ArrayList<Color> colors = new ArrayList<>();
			FireworkEffect.Type type = null;
			ArrayList<Color> fade = new ArrayList<>();
			int power = 1;
			int amount = 1;

			for (String newStr : sp) {
				if (newStr.startsWith("Type:")) {
					newStr = newStr.replace("Type:", "");
					type = FireworkEffect.Type.valueOf(newStr);
				} else if (newStr.startsWith("Colors:")) {
					newStr = newStr.replace("Colors:", "");
					String[] spColor = newStr.split(",");
					for (String newColor : spColor)
						colors.add(getColorFromRGB(newColor));
				} else if (newStr.startsWith("Fade:")) {
					newStr = newStr.replace("Fade:", "");
					String[] spFade = newStr.split(",");
					for (String newFade : spFade)
						fade.add(getColorFromRGB(newFade));
				} else if (newStr.startsWith("Power:")) {
					newStr = newStr.replace("Power:", "");
					power = Integer.parseInt(newStr);
				} else if (newStr.startsWith("Amount:")) {
					newStr = newStr.replace("Amount:", "");
					amount = Integer.parseInt(newStr);
				}
			}

			for (int i=0;i<amount;i++) {
				Location loc = player.getLocation();
				Firework firework = (Firework) Objects.requireNonNull(loc.getWorld()).spawnEntity(loc, EntityType.FIREWORK);
				FireworkMeta fireworkMeta = firework.getFireworkMeta();
				if (type != null) {
					FireworkEffect fireworkEffect = FireworkEffect.builder().with(type).withColor(colors).withFade(fade).flicker(false).build();
					fireworkMeta.addEffect(fireworkEffect);
					fireworkMeta.setPower(power);
					firework.setFireworkMeta(fireworkMeta);
				}
			}
		}
	}

	private static XMaterial getMaterial(String material) {
		Optional<XMaterial> xMaterial = XMaterial.matchXMaterial(material);
		XMaterial xmat = xMaterial.get();
		if (xmat.parseMaterial() == null) {
			return XMaterial.REDSTONE_BLOCK;
		}
		return xmat;
	}
	
	private static ItemStack getItem(@NotNull String material) {
		XMaterial mat;
		if (material.startsWith("TEXTURE-")) {
			return SkullUtils.getSkullValue(material.replace("TEXTURE-", ""));
		} else if (material.startsWith("PLAYER_HEAD_UUID-")) {
			return SkullUtils.getSkull(UUID.fromString(material.replace("PLAYER_HEAD_UUID-", "")));
		} else if (material.startsWith("PLAYER_HEAD-")) {
			return SkullUtils.getSkull(material.replace("PLAYER_HEAD-", ""));
		}
		mat = getMaterial(material);

		return mat.parseItem();
	}
	
	public static ItemStack makeItems(FileConfiguration config, String key, Player player, @Nullable String type) {
		String material = config.getString(key + ".material");
		if (type != null) {
			switch (type) {
				case "no_permission":
					material = config.getString("Menu.material_no_permission");
					break;
				case "disabled":
					material = config.getString("Menu.material_disabled");
					break;
				default:
					material = config.getString(key + ".material");
			}
		}
		ItemStack item = XMaterial.REDSTONE_BLOCK.parseItem();
		if (material != null) {
			item = getItem(material);
		}
		String name;
		List<String> lore;
		if(item != null) {
			ItemMeta meta = item.getItemMeta();
			if (meta != null) {
				name = Objects.requireNonNull(config.getString(key + ".display_name")).replace("%player%", player.getName());
				lore = config.getStringList(key + ".lore");

				for (int i=0;i< lore.size();i++) {
					lore.set(i, UtilsPlugin.fixColorMessages(lore.get(i)));

					if (UtilsPlugin.getPlugin("PlaceholderAPI")) {
						String newLore = PlaceholderAPI.setPlaceholders(player, lore.get(i));
						lore.set(i, UtilsPlugin.fixColorMessages(newLore));
					}
				}
				meta.setDisplayName(UtilsPlugin.fixColorMessages(name));

				if (UtilsPlugin.getPlugin("PlaceholderAPI")) {
					String display = PlaceholderAPI.setPlaceholders(player, name);
					meta.setDisplayName(UtilsPlugin.fixColorMessages(display));
				}
                meta.setLore(lore);
                item.setItemMeta(meta);
			}
		}
		return item;
	}

	public static ItemStack makeItem(FileConfiguration config, String key, Player player, @Nullable String type) {
		String material;
		if (type != null) {
			switch (type) {
				case "no_permission":
					material = config.getString("Menu.material_no_permission");
					break;
				case "disabled":
					material = config.getString("Menu.material_disabled");
					break;
				default:
					material = config.getString(key + ".material");
			}
		} else {
			material = config.getString(key + ".material");
		}

		ItemStack item;
		try {
			item = XMaterial.valueOf(material).parseItem();
		} catch (IllegalArgumentException e) {
			item = null;
		}

		if (item == null) {
			// Manejar la falla
			return null;
		}

		ItemMeta meta = item.getItemMeta();
		if (meta == null) {
			// Manejar la falla
			return null;
		}

		String displayName = config.getString(key + ".display_name");
		if (displayName == null) {
			// Manejar la falla
			return null;
		}
		displayName = displayName.replace("%player%", player.getName());
		displayName = UtilsPlugin.fixColorMessages(displayName);
		if (UtilsPlugin.getPlugin("PlaceholderAPI")) {
			displayName = PlaceholderAPI.setPlaceholders(player, displayName);
			displayName = UtilsPlugin.fixColorMessages(displayName);
		}
		meta.setDisplayName(displayName);

		List<String> lore = config.getStringList(key + ".lore");
		for (int i = 0; i < lore.size(); i++) {
			String loreLine = lore.get(i);
			loreLine = UtilsPlugin.fixColorMessages(loreLine);
			if (UtilsPlugin.getPlugin("PlaceholderAPI")) {
				loreLine = PlaceholderAPI.setPlaceholders(player, loreLine);
				loreLine = UtilsPlugin.fixColorMessages(loreLine);
			}
			lore.set(i, loreLine);
		}
		meta.setLore(lore);

		item.setItemMeta(meta);
		return item;
	}

	public static Color getColorFromRGB(String paramString) {
		Map<String, Color> colors = new HashMap<String, Color>() {{
			put("RED", Color.RED);
			put("GREEN", Color.GREEN);
			put("AQUA", Color.AQUA);
			put("BLACK", Color.BLACK);
			put("BLUE", Color.BLUE);
			put("FUCHSIA", Color.FUCHSIA);
			put("GRAY", Color.GRAY);
			put("LIME", Color.LIME);
			put("MAROON", Color.MAROON);
			put("WHITE", Color.WHITE);
			put("SILVER", Color.SILVER);
			put("YELLOW", Color.YELLOW);
			put("OLIVE", Color.OLIVE);
			put("TEAL", Color.TEAL);
			put("NAVY", Color.NAVY);
			put("PURPLE", Color.PURPLE);
			put("ORANGE", Color.ORANGE);
		}};

		Color color = colors.get(paramString.toUpperCase());
		if (color != null) {
			return color;
		} else {
			return Color.fromRGB(Integer.parseInt(paramString.replace("#", "0x"), 16));
		}
	}
}
