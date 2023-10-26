package it.easymc.easyautorank.objects;

import it.easymc.easyautorank.EasyAutoRank;
import it.easymc.easyautorank.utils.Msg;
import it.easymc.easyautorank.utils.NodeBuilder;
import lombok.Getter;
import net.luckperms.api.LuckPerms;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class RankChanger {
    private final String rank;
    private final int minuteToChange;

    public RankChanger(String rank, int minToChange) {
        this.rank = rank;
        this.minuteToChange = minToChange;
    }

    public boolean changeTimedPlayerRank(TimedPlayer timedPlayer){
        if (timedPlayer.getMinute() >= this.minuteToChange){
            if(!timedPlayer.getRank().equals(this.rank)){
                timedPlayer.setRank(this.rank);
                this.setLuckPermsRank(timedPlayer.getPlayer().getUniqueId());
                Msg.send(
                        timedPlayer.getPlayer(),
                        EasyAutoRank.INS.getConfig().getString("msg.ranked-up")
                                .replace("{rank}", this.rank)
                );
            }
            return true;
        }
        return false;
    }

    public void setLuckPermsRank(UUID uuid){
        LuckPerms luckPerms = EasyAutoRank.INS.getLuckPerms();
        luckPerms.getUserManager().modifyUser(
            uuid, user -> user.data().add(
                NodeBuilder.createNode(EasyAutoRank.INS.getRankContextsHashMap(), String.format("group.%s", this.rank))
            )
        );
    }

    public static void resetLuckPermsRanks(UUID uuid){
        LuckPerms luckPerms = EasyAutoRank.INS.getLuckPerms();
        for (String rank: EasyAutoRank.INS.getConfig().getStringList("reset-ranks")){
            luckPerms.getUserManager().modifyUser(
                uuid, user -> {
                    user.data().remove(
                            NodeBuilder.createNode(EasyAutoRank.INS.getRankContextsHashMap(), String.format("group.%s", rank))
                    );
                    user.data().remove(
                            NodeBuilder.createNode(new HashMap<>(), String.format("group.%s", rank))
                    );
                }
            );
        }
    }
}
