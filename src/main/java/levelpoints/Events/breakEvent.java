package levelpoints.Events;

import levelpoints.lp.API;
import levelpoints.lp.LP;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class breakEvent implements Listener {
    private Plugin plugin = LP.getPlugin(LP.class);
    private LP lp = LP.getPlugin(LP.class);
    private int number;
    private int exp;
    private int exps;
    private int take;
    private int leftover;
    private int level;
    private int needeps;
    public int LPML;
    public int LEXP;
    public StringBuilder sb;
    public int pts;
    File file = new File("plugins/LP/lang.yml");
    FileConfiguration Lang = YamlConfiguration.loadConfiguration(file);

    public breakEvent(LP lp) {

    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) throws IOException {


        Block block = event.getBlock();
        Player player = event.getPlayer();

        ItemStack item = player.getItemInHand();
        ItemMeta itemm = item.getItemMeta();



        File userdata = new File(lp.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);

        if (lp.EXPConfig.getBoolean("Resources")) {
            if (lp.EXPConfig.getBoolean("Debug")) {
                player.sendMessage(block.getType().toString());
            }

            if (lp.EXPConfig.getBoolean("PerWorld")) {

                List<String> worlds = lp.EXPConfig.getStringList("Worlds");
                for (String world : worlds)
                    if (player.getLocation().getWorld().getName().equalsIgnoreCase(world)) {
                        if (UsersConfig.getInt(player.getName() + ".level") < lp.EXPConfig.getInt("o" + block.getType().toString())) {
                            event.setCancelled(true);
                        } else if (lp.EXPConfig.getBoolean("RandomEXP")) {

                            int max = lp.EXPConfig.getInt(block.getType().toString());
                            int min = 0;

                            Random r = new Random();
                            int re = r.nextInt((max - min) + 1) + min;
                            if(item.hasItemMeta()) {
                                if(!itemm.hasEnchants() && !item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                                    lp.CustomXP(player, re, 0);
                                }
                            }
                        } else {
                            if(!itemm.hasEnchants() && !item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                                lp.CustomXP(player, lp.EXPConfig.getInt(block.getType().toString()), 0);
                            }
                        }
                    }
            } else {

                if (UsersConfig.getInt(player.getName() + ".level") < lp.EXPConfig.getInt("o" + block.getType().toString())) {
                    int level =  lp.EXPConfig.getInt("o" + block.getType().toString());
                    player.sendMessage(API.format(lp.LangConfig.getString("lpPerLevelOre").replace("{lp_required_level}", String.valueOf(level))));
                    event.setCancelled(true);
                } else if (lp.EXPConfig.getBoolean("RandomEXP")) {

                    int max = lp.EXPConfig.getInt(block.getType().toString());
                    int min = 0;

                    Random r = new Random();
                    int re = r.nextInt((max - min) + 1) + min;
                    if (lp.EXPConfig.getBoolean("Anti-Silk-EXP")) {
                        if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                            lp.CustomXP(player, re, 0);
                        }
                    }else{
                        lp.CustomXP(player, re, 0);
                    }
                } else {
                    if (lp.EXPConfig.getBoolean("Anti-Silk-EXP")) {
                        if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                            lp.CustomXP(player, lp.EXPConfig.getInt(block.getType().toString()), 0);
                        }
                    }else{
                        lp.CustomXP(player, lp.EXPConfig.getInt(block.getType().toString()), 0);
                    }
                }
            }
        }
    }
    @EventHandler
    public void place(BlockPlaceEvent event) throws IOException {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        File userdata = new File(lp.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        if(lp.EXPConfig.getBoolean("Anti-EXP-Dupe")) {
            if (block.getType().equals(Material.COAL_ORE) || block.getType().equals(Material.REDSTONE_ORE) || block.getType().equals(Material.QUARTZ_ORE) || block.getType().equals(Material.IRON_ORE) || block.getType().equals(Material.GOLD_ORE) || block.getType().equals(Material.EMERALD_ORE) || block.getType().equals(Material.DIAMOND_ORE) || block.getType().equals(Material.LAPIS_ORE)) {


                int expp = UsersConfig.getInt(player.getName() + ".EXP.Amount");
                int t = lp.EXPConfig.getInt(block.getType().toString());
                if (t <= expp) {
                    int tep = expp - t;
                    UsersConfig.set(player.getName() + ".EXP.Amount", tep);
                    UsersConfig.save(userdata);
                }
            }
        }

    }
}