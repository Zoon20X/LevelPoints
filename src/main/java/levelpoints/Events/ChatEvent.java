package levelpoints.Events;

import com.nametagedit.plugin.NametagEdit;
import levelpoints.lp.API;
import levelpoints.lp.LP;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class ChatEvent implements Listener {
    private Plugin plugin = LP.getPlugin(LP.class);
    private LP lp = LP.getPlugin(LP.class);

    public ChatEvent(LP lp) {

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        int level = lp.getPlayersConfig().getInt(player.getName() + ".level");
        String chat = event.getFormat();
        String levels = String.valueOf(level);
        String color = lp.getConfig().getString("PrefixColor");
        if (lp.getConfig().getBoolean("UseSymbol")) {
            if (lp.getConfig().getBoolean("PrefixChat")) {
                String symbol = lp.getConfig().getString("Symbol");
                String symbolColor = lp.getConfig().getString("SymbolColor");

                event.setFormat(API.format(symbolColor)+"✶"+API.format(color) + levels + " " + ChatColor.RESET + chat);
            }
        } else {
            if (lp.getConfig().getBoolean("PrefixChat")) {
                event.setFormat(API.format(color) + levels + " " + ChatColor.RESET + chat);
            }

        }
    }
    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();

        if(lp.getConfig().getBoolean("namePrefix")){
            String levelss = String.valueOf(lp.getPlayersConfig().getInt(player.getName() + ".level"));
            String color = lp.getConfig().getString("namePrefixColor");
            String Levels = API.format(color)+levelss + " ";

            NametagEdit.getApi().setPrefix(player, Levels);
        }

    }

}
