package levelpoints.Events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public interface LevelPointsData {


    boolean SQLQuery(Player player);
    void SaveLoadFiles(File file, FileConfiguration config, String Location, String secLoc, String Name);
    void TimedEXP();
    void ActionBar(Player player, String Message);
    void Title(Player player, String Title, String Subtitle);

    void FarmEventTrigger(Player player, String FarmedItem, int expAmount, String Task);



}
