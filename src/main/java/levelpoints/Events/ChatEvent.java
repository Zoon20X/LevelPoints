package levelpoints.Events;

import com.nametagedit.plugin.NametagEdit;
import levelpoints.lp.API;
import levelpoints.lp.LP;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

public class ChatEvent implements Listener {
    private Plugin plugin = LP.getPlugin(LP.class);
    private LP lp = LP.getPlugin(LP.class);

    public ChatEvent(LP lp) {

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        File userdata = new File(lp.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        int level = UsersConfig.getInt(player.getName() + ".level");
        int prestige = UsersConfig.getInt(player.getName() + ".Prestige");
        String chat = event.getFormat();
        String message = event.getMessage();
        String levels = String.valueOf(level);
        String prestigess = String.valueOf(prestige);
        String color = lp.getConfig().getString("PrefixColor");
        String symbol = lp.getConfig().getString("Symbol");
        Boolean lpsChat = lp.getConfig().getBoolean("LPSFormat");


        if(lpsChat) {

            for (String key : lp.FormatsConfig.getKeys(false)) {

                ConfigurationSection formats = lp.FormatsConfig.getConfigurationSection("");

                List<String> formatlevels = formats.getStringList(key + ".Levels");



                if (formatlevels.contains(levels)) {
                    chat = chat.replace("%1$s", player.getName()).replace("%2$s", message);

                    String Format = lp.FormatsConfig.getString(key + ".Format");
                    String FormatTags = Format.replace("{level}", levels).replace("{symbol}", symbol).replace("{prestige}", prestigess).replace("{name}", player.getName()).replace("{message}", message).replace("{format}", chat);
                    String Text = PlaceholderAPI.setPlaceholders(player, FormatTags);
                    String hi = "hi";




                    event.setCancelled(true);
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendMessage(Text);
                    }
                }
            }
        }
    }
    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        File userdata = new File(lp.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);

        if(lp.getConfig().getBoolean("namePrefix")){
            String levelss = String.valueOf(UsersConfig.getInt(player.getName() + ".level"));
            String color = lp.getConfig().getString("namePrefixColor");
            String Levels = API.format(color)+levelss + " ";

            NametagEdit.getApi().setPrefix(player, Levels);
        }

    }

}
