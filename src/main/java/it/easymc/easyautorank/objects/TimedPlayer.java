package it.easymc.easyautorank.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.easymc.easyautorank.EasyAutoRank;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;

@Getter
@Setter
public class TimedPlayer implements Serializable {
    private final String UUID;
    private int minute;
    private String rank;

    public TimedPlayer(String UUID) {
        this.UUID = UUID;
        minute = 0;
        rank = EasyAutoRank.INS.getConfig().getString("first-rank");
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(java.util.UUID.fromString(UUID));
    }

    public void addMinute(int minute){
        this.minute += minute;
    }

    public String getDestinationPath(File baseDir){
        File json = new File(baseDir, String.format("%s.json", this.UUID));
        return json.getPath();
    }

    public void dumpJSON(String path){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);

        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static TimedPlayer loadJSON(String path){
        TimedPlayer timedPlayer;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                jsonContent.append(line);
            }

            Gson gson = new GsonBuilder().create();
            timedPlayer = gson.fromJson(jsonContent.toString(), TimedPlayer.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return timedPlayer;
    }

    public void refresh(int minutes, String path){
        this.addMinute(minutes);
        this.dumpJSON(path);
    }
}
