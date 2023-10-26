package it.easymc.easyautorank.tasks;

import it.easymc.easyautorank.events.UpdateTimedPlayerEvent;
import it.easymc.easyautorank.objects.TimedPlayer;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;


public class UpdateTimedPlayerTask extends BukkitRunnable {
    private final TimedPlayer timedPlayer;

    public UpdateTimedPlayerTask(TimedPlayer timedPlayer) {
        this.timedPlayer = timedPlayer;
    }

    @Override
    public void run() {
        UpdateTimedPlayerEvent event = new UpdateTimedPlayerEvent(timedPlayer);
        Bukkit.getPluginManager().callEvent(event);
    }
}
