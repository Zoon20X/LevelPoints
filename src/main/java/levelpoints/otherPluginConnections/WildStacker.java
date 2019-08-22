package levelpoints.otherPluginConnections;

import com.bgsoftware.wildstacker.api.events.SpawnerPlaceEvent;
import com.bgsoftware.wildstacker.api.objects.StackedSpawner;
import levelpoints.lp.LP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class WildStacker implements Listener {
    private Plugin plugin = LP.getPlugin(LP.class);
    private LP lp = LP.getPlugin(LP.class);

    public WildStacker(LP lp) {

    }

    @EventHandler
    public void onSpawnerPlace(SpawnerPlaceEvent event){
        StackedSpawner spawner = event.getSpawner();
        Player player = event.getPlayer();

        if(lp.WSConfig.getBoolean("DebugName")){
            player.sendMessage(spawner.getSpawner().getCreatureTypeName().toString());
        }

        if(lp.getPlayersConfig().getInt(player.getName() + ".level") >= lp.WSConfig.getInt(spawner.getSpawner().getCreatureTypeName().toString())){

        }else{
            event.setCancelled(true);
        }

    }
}
