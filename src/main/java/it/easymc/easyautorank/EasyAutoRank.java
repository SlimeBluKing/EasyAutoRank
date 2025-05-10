package it.easymc.easyautorank;

import com.google.common.collect.Lists;
import it.easymc.easyautorank.commands.EasyAutoRankCommand;
import it.easymc.easyautorank.listeners.CreateTimedPlayerListener;
import it.easymc.easyautorank.listeners.UpdateTimedPlayerListener;
import it.easymc.easyautorank.objects.RankChanger;
import it.easymc.easyautorank.objects.TimedPlayer;
import it.easymc.easyautorank.tasks.UpdateTimedPlayerTask;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.UnknownDependencyException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

@Getter
public final class EasyAutoRank extends JavaPlugin {
    public static EasyAutoRank INS;
    public final File DATA_FOLDER = new File(getDataFolder(), "data");

    private LuckPerms luckPerms;

    private int refreshTime;

    private ArrayList<RankChanger> rankChangerArrayList;
    private HashMap<String, String> rankContextsHashMap;
    private HashMap<UUID, TimedPlayer> timedPlayerHashMap;

    public EasyAutoRank(){
        INS = this;
    }


    @Override
    public void onEnable() {
        saveDefaultConfig();
        start();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void start(){
        getDependencies();
        getCommands();
        getEvents();

        this.refreshTime = getConfig().getInt("update-every-minutes");
        initialRankChangerArrayList(getConfig().getConfigurationSection("ranks"));
        initialRankContextsHashMap(getConfig().getConfigurationSection("rank-contexts"));
        initialTimedPlayerHashMap();

        getTasks();
    }

    public void getCommands(){
        this.getCommand("ar").setExecutor(new EasyAutoRankCommand());
    }

    public void getEvents(){
        Lists.newArrayList(
                new CreateTimedPlayerListener(),
                new UpdateTimedPlayerListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    public void getTasks(){
        Bukkit.getScheduler().runTaskTimer(this, this::updateTimedPlayerHashMap, 0L, this.refreshTime * 20L * 60L);
    }

    public void getDependencies(){
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
        } else {
            throw new UnknownDependencyException("Luckperms does not found");
        }
    }

    public void initialRankChangerArrayList(ConfigurationSection ranksConfig){
        this.rankChangerArrayList = new ArrayList<>();
        for (String rankName: ranksConfig.getKeys(false)){
            RankChanger rankChanger = new RankChanger(
                    rankName,
                    ranksConfig.getInt(rankName + ".after-hours") * 60
            );
            this.rankChangerArrayList.add(rankChanger);
        }

        this.rankChangerArrayList.sort(Comparator.comparing(RankChanger::getMinuteToChange, Collections.reverseOrder()));
    }

    public void initialRankContextsHashMap(ConfigurationSection rankContextsConfig){
        this.rankContextsHashMap = new HashMap<>();
        for (String rankContext: rankContextsConfig.getKeys(false)){
            String key = rankContextsConfig.getString(rankContext + ".key");
            String value = rankContextsConfig.getString(rankContext + ".value");

            this.rankContextsHashMap.put(key, value);
        }
    }

    public void initialTimedPlayerHashMap(){
        if (!this.DATA_FOLDER.exists()){
            if (!this.DATA_FOLDER.mkdirs()){
                throw new RuntimeException("data folder could not be created");
            }
        }
        this.timedPlayerHashMap = new HashMap<>();
        File[] files = this.DATA_FOLDER.listFiles();

        if (files == null)
            return;

        for (File file : files) {
            String path = file.getPath();
            TimedPlayer timedPlayer = TimedPlayer.loadJSON(path);
            if (timedPlayer != null)
                this.timedPlayerHashMap.put(UUID.fromString(timedPlayer.getUUID()), timedPlayer);
        }
    }

    public void saveNewTimedPlayerHashMap(TimedPlayer timedPlayer){
        this.timedPlayerHashMap.put(UUID.fromString(timedPlayer.getUUID()), timedPlayer);
        timedPlayer.dumpJSON(timedPlayer.getDestinationPath(this.DATA_FOLDER));
    }

    public void updateTimedPlayerHashMap(){
        for (Player player : getServer().getOnlinePlayers()){
            TimedPlayer timedPlayer = timedPlayerHashMap.get(player.getUniqueId());
            new UpdateTimedPlayerTask(timedPlayer).runTaskAsynchronously(this);
        }
    }
}
