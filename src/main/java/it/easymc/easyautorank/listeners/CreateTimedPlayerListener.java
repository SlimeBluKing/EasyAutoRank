package it.easymc.easyautorank.listeners;

import it.easymc.easyautorank.EasyAutoRank;
import it.easymc.easyautorank.objects.RankChanger;
import it.easymc.easyautorank.objects.TimedPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class CreateTimedPlayerListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        UUID UUID = e.getPlayer().getUniqueId();
        if (!EasyAutoRank.INS.getTimedPlayerHashMap().containsKey(UUID)){
            RankChanger.resetLuckPermsRanks(UUID);
            EasyAutoRank.INS.saveNewTimedPlayerHashMap(new TimedPlayer(UUID.toString()));
        }
    }
}
