package it.easymc.easyautorank.events;

import it.easymc.easyautorank.objects.TimedPlayer;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class UpdateTimedPlayerEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final TimedPlayer timedPlayer;

    public UpdateTimedPlayerEvent(TimedPlayer timedPlayer){
        this.timedPlayer = timedPlayer;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
