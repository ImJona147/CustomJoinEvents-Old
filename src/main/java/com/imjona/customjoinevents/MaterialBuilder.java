package com.imjona.customjoinevents;

import com.google.common.collect.Multimap;
import com.imjona.customjoinevents.utils.UtilsPlugin;
import com.imjona.customjoinevents.xseries.SkullUtils;
import com.imjona.customjoinevents.xseries.XMaterial;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MaterialBuilder {

    public static ItemStack getSkullFromValue(String value) {
        ItemStack skull = XMaterial.PLAYER_HEAD.parseItem();
        if (skull != null) {
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            if (meta != null)
                skull.setItemMeta(SkullUtils.applySkin(meta, value));
        }
        return skull;
    }

    public static ItemStack getItemFromString(String material) {
        if (material.startsWith("TEXTURE-")) {
            return getSkullFromValue(material.replace("TEXTURE-", ""));
        } else if (material.startsWith("PLAYER_HEAD_UUID-")) {
            return SkullUtils.getSkull(UUID.fromString(material.replace("PLAYER_HEAD_UUID-", "")));
        } else if (material.startsWith("PLAYER_HEAD-")) {
            return SkullUtils.getSkull(material.replace("PLAYER_HEAD-", ""));
        } else {
            return XMaterial.valueOf(material.split(":")[0]).parseItem();
        }
    }

    public static ItemStack getItemFromConfig(FileConfiguration config, String key, Player player) {
        String material = config.getString(key + ".material");

        ItemStack item = material != null ? getItemFromString(material) : XMaterial.REDSTONE_BLOCK.parseItem();
        int amount = config.getInt(key + "amount");
        String display_name = config.getString(key + ".display_name");
        List<String> lore = config.getStringList(key + ".lore");
        boolean unbreakable = config.getBoolean(key + ".unbreakable");
        List<String> flags = config.getStringList(key + ".flags");
        List<String> enchanstments = config.getStringList(key + ".enchantments");
        List<String> attributes = config.getStringList(key + ".attributes");

        if (item != null) {
            item.setAmount(amount);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(UtilsPlugin.fixColorMessages(display_name));
                if (unbreakable) {
                    meta.setUnbreakable(unbreakable);
                }
                if (config.contains(key + ".custom_model_data")) {
                    meta.setCustomModelData(config.getInt(key + ".custom_model_data"));
                }
                if (flags.size() > 0) {
                    for (String flag : flags) {
                        try {
                            ItemFlag itemFlag = ItemFlag.valueOf(flag);
                            meta.addItemFlags(itemFlag);
                        } catch (IllegalArgumentException e) {
                            UtilsPlugin.sendMessageToConsole("&7Flag &b" + flag + "&7is not a valid");
                        }
                    }
                }

                if (enchanstments.size() > 0) {
                    for (String enchant : enchanstments) {
                        String[] en = enchant.split(" ");
                        int level = 0;
                        try {
                            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(en[0]));
                            level = Integer.parseInt(en[1]);
                            if (enchantment != null) {
                                meta.addEnchant(enchantment, level, true);
                            } else {
                                UtilsPlugin.sendMessageToConsole("&7The enchantment &b" + enchant + "&7is not a valid");
                            }
                        } catch (NumberFormatException e) {
                            UtilsPlugin.sendMessageToConsole("&7The value &b" + en[1] + "&7is not a valid value");
                        }
                    }
                }

                if (attributes.size() > 0) {
                    for (String attr : attributes) {
                        String[] att = attr.split(" ");
                        Attribute attribute = Attribute.valueOf(att[0]);
                        EquipmentSlot slot = EquipmentSlot.valueOf(att[2]);

                        double value = 0;
                        try {
                            value = Double.parseDouble(att[1]);
                        } catch (NumberFormatException e) {
                            UtilsPlugin.sendMessageToConsole("&7The value &b" + att[1] + "&7is not a valid");
                        }
                        meta.addAttributeModifier(
                                attribute,
                                new AttributeModifier(UUID.randomUUID(), "JonaAttribute", value, AttributeModifier.Operation.ADD_NUMBER, slot));
                        for (int i=0;i< lore.size();i++) {
                            Multimap<Attribute, AttributeModifier> attrbutes = meta.getAttributeModifiers();
                            assert attrbutes != null;
                            for (Map.Entry<Attribute, AttributeModifier> entry : attrbutes.entries()) {
                                lore.set(i, lore.get(i).replaceAll(
                                        "\\{" + entry.getKey().toString() + "}",
                                        entry.getValue().getAmount() + "")
                                        .replaceAll(
                                                "\\{" + entry.getKey().toString() + "_INT}",
                                                (int)entry.getValue().getAmount() + ""
                                        ));
                            }
                        }
                    }
                }

                for (int i=0;i< lore.size();i++) {
                    lore.set(i, UtilsPlugin.fixColorMessages(lore.get(i)));

                    if (UtilsPlugin.getPlugin("PlaceholderAPI")) {
                        String newLore = PlaceholderAPI.setPlaceholders(player, lore.get(i));
                        lore.set(i, UtilsPlugin.fixColorMessages(newLore));
                    }
                }

                if (meta instanceof LeatherArmorMeta) {
                    String color = config.getString(key + ".leatherarmor.color");
                    if (color != null) {
                        if(color.matches("[0-9]{1,3},( )[0-9]{1,3},( )[0-9]{1,3}")) {
                            String[] rgb = color.replaceAll(" ", "").split(",");
                            int r = Integer.parseInt(rgb[0]);
                            int g = Integer.parseInt(rgb[1]);
                            int b = Integer.parseInt(rgb[2]);

                            Color color1 = Color.fromRGB(r, g,b);
                            ((LeatherArmorMeta) meta).setColor(color1);
                        } else {
                            UtilsPlugin.sendMessageToConsole("&7The color &b" + color + "&7 is not a color");
                        }
                    }
                }
            }
        }
        return item;
    }

}
