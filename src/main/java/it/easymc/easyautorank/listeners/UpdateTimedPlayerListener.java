package it.easymc.easyautorank.listeners;

import it.easymc.easyautorank.EasyAutoRank;
import it.easymc.easyautorank.events.UpdateTimedPlayerEvent;
import it.easymc.easyautorank.objects.RankChanger;
import it.easymc.easyautorank.objects.TimedPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.io.File;

public class UpdateTimedPlayerListener implements Listener {
    private final File dataFolder = EasyAutoRank.INS.DATA_FOLDER;

    @EventHandler(priority = EventPriority.LOWEST)
    public void updateTimedPlayer(UpdateTimedPlayerEvent e){
        TimedPlayer timedPlayer = e.getTimedPlayer();
        Player player = timedPlayer.getPlayer();
        if (!player.hasPermission("easyautorank.admin.bypass") && player.hasPermission("easyautorank.upgrade"))
            for (RankChanger rankChanger : EasyAutoRank.INS.getRankChangerArrayList()){
                if (rankChanger.changeTimedPlayerRank(timedPlayer)){
                    break;
                }
            }
        timedPlayer.refresh(EasyAutoRank.INS.getRefreshTime(), timedPlayer.getDestinationPath(dataFolder));
    }
}
