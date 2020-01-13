package levelpoints.otherPluginConnections;

import com.bgsoftware.wildstacker.api.events.SpawnerPlaceEvent;
import com.bgsoftware.wildstacker.api.objects.StackedSpawner;
import levelpoints.lp.LP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class WildStacker implements Listener {
    private Plugin plugin = LP.getPlugin(LP.class);
    private LP lp = LP.getPlugin(LP.class);

    public WildStacker(LP lp) {

    }

    @EventHandler
    public void onSpawnerPlace(SpawnerPlaceEvent event){
        StackedSpawner spawner = event.getSpawner();
        Player player = event.getPlayer();
        File userdata = new File(lp.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        if(lp.WSConfig.getBoolean("DebugName")){
            player.sendMessage(spawner.getSpawner().getCreatureTypeName().toString());
        }

        if(UsersConfig.getInt(player.getName() + ".level") >= lp.WSConfig.getInt(spawner.getSpawner().getCreatureTypeName().toString())){

        }else{
            event.setCancelled(true);
        }

    }
}
