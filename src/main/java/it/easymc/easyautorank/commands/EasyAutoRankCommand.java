package it.easymc.easyautorank.commands;

import it.easymc.easyautorank.EasyAutoRank;
import it.easymc.easyautorank.objects.TimedPlayer;
import it.easymc.easyautorank.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EasyAutoRankCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("ar")){
            if (args.length < 1) {
                Msg.send(sender, EasyAutoRank.INS.getConfig().getString("msg.wrong-usage"));
                return true;
            }
            if (args[0].equalsIgnoreCase("check")){
                    //ar check
                if(args.length == 1){
                    if (sender instanceof Player){
                        TimedPlayer timedPlayer = EasyAutoRank.INS.getTimedPlayerHashMap().get(((Player) sender).getUniqueId());
                        Msg.send(sender, EasyAutoRank.INS.getConfig().getString("msg.check-self")
                                .replace("{time}", getVerboseTime(timedPlayer.getMinute())));
                    } else {
                        Msg.send(sender, EasyAutoRank.INS.getConfig().getString("msg.only-player"));
                    }
                    return true;
                }
                //ar check {Player}
                if(args.length == 2){
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player != null){
                        TimedPlayer timedPlayer = EasyAutoRank.INS.getTimedPlayerHashMap().get(player.getUniqueId());
                        Msg.send(sender, EasyAutoRank.INS.getConfig().getString("msg.check-other")
                                .replace("{time}", getVerboseTime(timedPlayer.getMinute()))
                                .replace("{player}", timedPlayer.getPlayer().getDisplayName()));
                    } else {
                        Msg.send(sender, EasyAutoRank.INS.getConfig().getString("msg.not-online")
                                .replace("{player}", args[1]));
                    }
                    return true;
                }
            }
        }
        Msg.send(sender, EasyAutoRank.INS.getConfig().getString("msg.wrong-usage"));
        return true;
    }

    private static String getVerboseTime(int minute) {
        long days = 0;
        long hours = 0;
        long minutes = 0;

        long duration = minute;

        if (duration / 3600 > 0) {
            days = duration / 3600;
            duration = duration % 3600;
        }

        if (duration / 60 > 0) {
            hours = duration / 60;
            duration = duration % 60;
        }

        minutes = duration;


        String output = "";

        if (days > 0) {
            output += days + "d ";
        }
        if (hours > 0) {
            output += hours + "h ";
        }
        if (minutes > 0) {
            output += minutes + "m ";
        }

        return output.substring(0, output.length() - 1);
    }
}
