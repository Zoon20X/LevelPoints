package levelpoints.Events;

import levelpoints.lp.LP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
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

    @EventHandler
    public void JoinEventPre(AsyncPlayerPreLoginEvent event){
        lp.PlayerAdd(event.getUniqueId(), event.getName());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws IOException {

        Player player = event.getPlayer();
        playerName = player.getName();

        lp.PlayerDataLoad(player);

        if (lp.getConfig().getBoolean("UseSQL")) {
            lp.getEXP(player.getUniqueId(), player.getName());
            lp.getLevel(player.getUniqueId(), player.getName());
            lp.getPrestige(player.getUniqueId(), player.getName());
        }
    }


}
