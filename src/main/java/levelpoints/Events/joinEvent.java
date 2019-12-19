package levelpoints.Events;

import levelpoints.lp.LP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.io.IOException;

public class joinEvent implements Listener {
    private Plugin plugin = LP.getPlugin(LP.class);
    private LP lp = LP.getPlugin(LP.class);
    public int num;
    public int pnum = 10;
    public int ct;
    public String playerName;
    public String commandString;
    public static String VersionJoin;

    public joinEvent(LP lp) {

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) throws IOException {

        Player player = event.getPlayer();
        playerName = player.getName();
        lp.PlayerLevels = lp.getPlayersConfig().getInt(player.getName() + ".level");





        if (lp.getPlayersConfig().contains(player.getName())) {
            if(!lp.getPlayersConfig().contains(player.getName() + ".ActionBar")){
                lp.getPlayersConfig().set(player.getName() + ".ActionBar", true);
            }


            lp.getPlayersConfig().set(player.getName() + ".EXP.GenOn", true);
            lp.getPlayersConfig().save(lp.getPlayersFile());
            if (lp.getConfig().getBoolean("UseSQL")) {
                lp.getEXP(player.getUniqueId(), player);
                lp.getLevel(player.getUniqueId(), player);
                lp.getPrestige(player.getUniqueId(), player);
            }

            //Create a new BossBar



        } else if (!lp.getPlayersConfig().contains(player.getName())) {
            lp.getPlayersConfig().set(player.getName() + ".level", lp.LevelConfig.getInt("Level"));
            lp.getPlayersConfig().set(player.getName() + ".EXP.Amount", lp.LevelConfig.getInt("Exp"));
            lp.getPlayersConfig().set(player.getName() + ".Prestige", 0);
            lp.getPlayersConfig().set(player.getName() + ".BlockPowerUp", 0);
            lp.getPlayersConfig().set(player.getName() + ".EXP.Gen", 0);
            lp.getPlayersConfig().set(player.getName() + ".EXP.Boost2x", 0);
            lp.getPlayersConfig().set(player.getName() + ".EXP.Boost3x", 0);
            lp.getPlayersConfig().set(player.getName() + ".EXP.Boost4x", 0);
            lp.getPlayersConfig().set(player.getName() + ".EXP.Boost5x", 0);
            lp.getPlayersConfig().set(player.getName() + ".EXP.Boost6x", 0);
            lp.getPlayersConfig().set(player.getName() + ".EXP.Boost7x", 0);
            lp.getPlayersConfig().set(player.getName() + ".EXP.Boost8x", 0);
            lp.getPlayersConfig().set(player.getName() + ".EXP.Boost9x", 0);
            lp.getPlayersConfig().set(player.getName() + ".EXP.Boost10x", 0);
            lp.getPlayersConfig().set(player.getName() + ".EXP.Active", 1);
            lp.getPlayersConfig().set(player.getName() + ".ActionBar", true);
            lp.getPlayersConfig().save(lp.getPlayersFile());
        }
    }


}
