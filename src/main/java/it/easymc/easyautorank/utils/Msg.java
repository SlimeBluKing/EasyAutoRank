package it.easymc.easyautorank.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class Msg {
    public static void send(CommandSender player, String string){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', string));
    }

    public static ArrayList<String> translateList(ArrayList<String> arrayList){
        arrayList.replaceAll(textToTranslate -> ChatColor.translateAlternateColorCodes('&', textToTranslate));
        return arrayList;
    }
}
