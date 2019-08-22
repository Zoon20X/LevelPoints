package levelpoints.Events;

import java.io.IOException;
import levelpoints.lp.LP;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

public class ItemUse implements Listener {
    private Plugin plugin = LP.getPlugin(LP.class);
    private LP lp = LP.getPlugin(LP.class);

    public ItemUse(LP lp) {

    }

    public void boosteruse(Player player, int multiplier)
            throws IOException {
        if (player.getItemInHand().getAmount() > 1) {
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
        } else {
            player.getInventory().setItemInHand(new ItemStack(Material.AIR));
        }
        int numm = this.lp.getPlayersConfig().getInt(player.getName() + ".EXP.Boost" + multiplier + "x");
        this.lp.getPlayersConfig().set(player.getName() + ".EXP.Boost" + multiplier + "x", Integer.valueOf(numm + 1));
        this.lp.getPlayersConfig().save(this.lp.getPlayersFile());
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) throws IOException {
        Player player = event.getPlayer();
        ItemStack item2 = lp.getBoosterConfig().getItemStack("Boosters.2");
        ItemStack item3 = lp.getBoosterConfig().getItemStack("Boosters.3");
        ItemStack item4 = lp.getBoosterConfig().getItemStack("Boosters.4");
        ItemStack item5 = lp.getBoosterConfig().getItemStack("Boosters.5");
        ItemStack item6 = lp.getBoosterConfig().getItemStack("Boosters.6");
        ItemStack item7 = lp.getBoosterConfig().getItemStack("Boosters.7");
        ItemStack item8 = lp.getBoosterConfig().getItemStack("Boosters.8");
        ItemStack item9 = lp.getBoosterConfig().getItemStack("Boosters.9");
        ItemStack item10 = lp.getBoosterConfig().getItemStack("Boosters.10");
        if (player.getItemInHand().equals(null)) {
            return;
        } else {
            if (player.getItemInHand().isSimilar(item2)) {
                boosteruse(player, 2);
            }
            if (player.getItemInHand().isSimilar(item3)) {
                boosteruse(player, 3);
            }
            if (player.getItemInHand().isSimilar(item4)) {
                boosteruse(player, 4);
            }
            if (player.getItemInHand().isSimilar(item5)) {
                boosteruse(player, 5);
            }
            if (player.getItemInHand().isSimilar(item6)) {
                boosteruse(player, 6);
            }
            if (player.getItemInHand().isSimilar(item7)) {
                boosteruse(player, 7);
            }
            if (player.getItemInHand().isSimilar(item8)) {
                boosteruse(player, 8);
            }
            if (player.getItemInHand().isSimilar(item9)) {
                boosteruse(player, 9);
            }
            if (player.getItemInHand().isSimilar(item10)) {
                boosteruse(player, 10);
            }
        }
    }
}
