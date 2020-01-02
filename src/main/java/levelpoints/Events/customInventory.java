package levelpoints.Events;

import levelpoints.lp.LP;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class customInventory implements Listener {
    private Plugin plugin = LP.getPlugin(LP.class);
    private LP lp = LP.getPlugin(LP.class);
    private ItemStack ii2;
    private ItemStack ii3;
    private ItemStack ii4;
    private ItemStack ii5;
    private ItemStack ii6;
    private ItemStack ii7;
    private ItemStack ii8;
    private ItemStack ii9;
    private ItemStack ii10;
    public HashMap<UUID, Long> cooldown = new HashMap<UUID, Long>();
    private int cooldowntimer;

    public int num;
    public int pnum;
    public int ct;
    public int amount;
    public String playerName;


    private int frostsec;
    private int frostmin;
    private int frosthour;
    private int frostday;
    public customInventory(LP lp) {

    }


    public customInventory() {

    }

    public void doCountDown(Player player) throws IOException {
        if (pnum > 0) {
            pnum -= 1;
            //player.sendMessage("hi");
            lp.getPlayersConfig().set(player.getName() + ".EXP.Gen", pnum);
            lp.getPlayersConfig().save(lp.getPlayersFile());
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        doCountDown(player);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.runTaskLater(lp, 20);
        } else if (pnum == 0) {
            try {
                lp.getPlayersConfig().set(player.getName() + ".EXP.Active", 1);
                lp.getPlayersConfig().save(lp.getPlayersFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void boosterInventory(Player player) {

        ii2 = lp.getBoosterConfig().getItemStack("Boosters.2");
        ii3 = lp.getBoosterConfig().getItemStack("Boosters.3");
        ii4 = lp.getBoosterConfig().getItemStack("Boosters.4");
        ii5 = lp.getBoosterConfig().getItemStack("Boosters.5");
        ii6 = lp.getBoosterConfig().getItemStack("Boosters.6");
        ii7 = lp.getBoosterConfig().getItemStack("Boosters.7");
        ii8 = lp.getBoosterConfig().getItemStack("Boosters.8");
        ii9 = lp.getBoosterConfig().getItemStack("Boosters.9");
        ii10 = lp.getBoosterConfig().getItemStack("Boosters.10");

        if(!(ii2 == null)) {
            if(lp.getPlayersConfig().getInt(player.getName() + ".EXP.Boost2x") == 0){
                ii2.setAmount(1);
            }else {
                ii2.setAmount(lp.getPlayersConfig().getInt(player.getName() + ".EXP.Boost2x"));
            }
       }
        if(!(ii3 == null)) {
            if (lp.getPlayersConfig().getInt(player.getName() + ".EXP.Boost3x") == 0) {
                ii3.setAmount(1);
            } else {
                ii3.setAmount(lp.getPlayersConfig().getInt(player.getName() + ".EXP.Boost3x"));
            }
        }
        if(!(ii4 == null)) {
            if (lp.getPlayersConfig().getInt(player.getName() + ".EXP.Boost4x") == 0) {
                ii4.setAmount(1);
            } else {
                ii4.setAmount(lp.getPlayersConfig().getInt(player.getName() + ".EXP.Boost4x"));
            }
        }
        if(!(ii5 == null)) {
            if (lp.getPlayersConfig().getInt(player.getName() + ".EXP.Boost5x") == 0) {
                ii5.setAmount(1);
            } else {
                ii5.setAmount(lp.getPlayersConfig().getInt(player.getName() + ".EXP.Boost5x"));
            }
        }
        if(!(ii6 == null)) {
            if (lp.getPlayersConfig().getInt(player.getName() + ".EXP.Boost6x") == 0) {
                ii6.setAmount(1);
            } else {
                ii6.setAmount(lp.getPlayersConfig().getInt(player.getName() + ".EXP.Boost6x"));
            }
        }
        if(!(ii7 == null)) {
            if (lp.getPlayersConfig().getInt(player.getName() + ".EXP.Boost7x") == 0) {
                ii7.setAmount(1);
            } else {
                ii7.setAmount(lp.getPlayersConfig().getInt(player.getName() + ".EXP.Boost7x"));
            }
        }
        if(!(ii8 == null)) {
            if (lp.getPlayersConfig().getInt(player.getName() + ".EXP.Boost8x") == 0) {
                ii8.setAmount(1);
            } else {
                ii8.setAmount(lp.getPlayersConfig().getInt(player.getName() + ".EXP.Boost8x"));
            }
        }
        if(!(ii9 == null)) {
            if (lp.getPlayersConfig().getInt(player.getName() + ".EXP.Boost9x") == 0) {
                ii9.setAmount(1);
            } else {
                ii9.setAmount(lp.getPlayersConfig().getInt(player.getName() + ".EXP.Boost9x"));
            }
        }
        if(!(ii10 == null)) {
            if (lp.getPlayersConfig().getInt(player.getName() + ".EXP.Boost10x") == 0) {
                ii10.setAmount(1);
            } else {
                ii10.setAmount(lp.getPlayersConfig().getInt(player.getName() + ".EXP.Boost10x"));
            }
        }

        Inventory i = plugin.getServer().createInventory(null, 9, ChatColor.DARK_AQUA + "Backpack");

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemMeta glassm = glass.getItemMeta();
        glassm.setDisplayName(" ");
        glass.setItemMeta(glassm);

        if (ii2 == null) {
            ii2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
            i.setItem(0, ii2);
        } else {
            i.setItem(0, ii2);
        }
        if (ii3 == null) {
            ii3 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
            i.setItem(1, ii3);
        } else {
            i.setItem(1, ii3);
        }
        if (ii4 == null) {
            ii4 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
            i.setItem(2, ii4);
        } else {
            i.setItem(2, ii4);
        }
        if (ii5 == null) {
            ii5 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
            i.setItem(3, ii5);
        } else {
            i.setItem(3, ii5);
        }
        if (ii6 == null) {
            ii6 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
            i.setItem(4, ii6);
        } else {
            i.setItem(4, ii6);
        }
        if (ii7 == null) {
            ii7 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
            i.setItem(5, ii7);
        } else {
            i.setItem(5, ii7);
        }
        if (ii8 == null) {
            ii8 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
            i.setItem(6, ii8);
        } else {
            i.setItem(6, ii8);
        }
        if (ii9 == null) {
            ii9 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
            i.setItem(7, ii9);
        } else {
            i.setItem(7, ii9);
        }
        if (ii10 == null) {
            ii10 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
            i.setItem(8, ii10);
        } else {
            i.setItem(8, ii10);
        }
        player.openInventory(i);
    }



    public void boosteruseclick(Player player, int multiplier) throws IOException {
        cooldowntimer = lp.getConfig().getInt("BoostersTime");

        if (cooldown.containsKey(player.getUniqueId())) {
            long secondsleft = ((cooldown.get(player.getUniqueId()) / 1000) + cooldowntimer) - (System.currentTimeMillis() / 1000);
            if (secondsleft > 0) {
                player.closeInventory();
                player.sendMessage(ChatColor.RED + "You Must Wait Till your first boost has finished, before activating another one");
                frostsec = (int) secondsleft;
                if (frostsec >= 60) {
                    frostmin = frostsec / 60;
                    frostsec = frostsec - (frostmin * 60);
                }
                if (frostmin >= 60) {
                    frosthour = frostmin / 60;
                    frostmin = frostmin - (frosthour * 60);
                }
                if (frosthour >= 24) {
                    frostday = frosthour / 24;
                    frosthour = frosthour - (frostday * 24);
                }
                player.sendMessage(ChatColor.AQUA + "Time: " + frostday + ".days" + frosthour + ".hours " + frostmin + ".minutes " + frostsec + ".seconds");
            } else {
                if (lp.getPlayersConfig().getInt(player.getName() + ".EXP.Boost" + multiplier + "x") >= 1) {
                    int numm = lp.getPlayersConfig().getInt(player.getName() + ".EXP.Boost" + multiplier + "x");
                    lp.getPlayersConfig().set(player.getName() + ".EXP.Boost" + multiplier + "x", numm - 1);
                    lp.getPlayersConfig().set(player.getName() + ".EXP.Active", multiplier);
                    player.closeInventory();

                    lp.getPlayersConfig().save(lp.getPlayersFile());
                    cooldown.put(player.getUniqueId(), System.currentTimeMillis());
                    player.sendMessage(ChatColor.GREEN + "Booster Has Been Activated");
                } else {
                    player.closeInventory();
                    player.sendMessage(ChatColor.RED + "You Need Atleast 1 " + multiplier + "X booster to use it");
                }
            }
        } else {
            if (lp.getPlayersConfig().getInt(player.getName() + ".EXP.Boost" + multiplier + "x") >= 1) {
                int numm = lp.getPlayersConfig().getInt(player.getName() + ".EXP.Boost" + multiplier + "x");
                lp.getPlayersConfig().set(player.getName() + ".EXP.Boost" + multiplier + "x", numm - 1);
                lp.getPlayersConfig().set(player.getName() + ".EXP.Active", multiplier);
                player.closeInventory();

                lp.getPlayersConfig().save(lp.getPlayersFile());
                cooldown.put(player.getUniqueId(), System.currentTimeMillis());
                player.sendMessage(ChatColor.GREEN + "Booster Has Been Activated");
            } else {
                player.closeInventory();
                player.sendMessage(ChatColor.RED + "You Need Atleast 1 " + multiplier + "X booster to use it");
            }
        }
    }


    @EventHandler
    public void InvenClick(InventoryClickEvent event) throws IOException {
        Player player = (Player) event.getWhoClicked();
        Inventory open = event.getClickedInventory();
        ItemStack item = event.getCurrentItem();

        ClickType click = event.getClick();
        if (open == null) {
            return;
        }
        if (open.getTitle().equals(ChatColor.DARK_AQUA + "Backpack")) {
            event.setCancelled(true);

            if (item.equals(null) || !item.hasItemMeta()) {
                return;
            }

            if (item.isSimilar(lp.getBoosterConfig().getItemStack("Boosters.2"))) {
                boosteruseclick(player, 2);
            }

            if (item.isSimilar(lp.getBoosterConfig().getItemStack("Boosters.3"))) {
                boosteruseclick(player, 3);
            }
            if (item.isSimilar(lp.getBoosterConfig().getItemStack("Boosters.4"))) {
                boosteruseclick(player, 4);
            }
            if (item.isSimilar(lp.getBoosterConfig().getItemStack("Boosters.5"))) {
                boosteruseclick(player, 5);
            }
            if (item.isSimilar(lp.getBoosterConfig().getItemStack("Boosters.6"))) {
                boosteruseclick(player, 6);
            }
            if (item.isSimilar(lp.getBoosterConfig().getItemStack("Boosters.7"))) {
                boosteruseclick(player, 7);
            }
            if (item.isSimilar(lp.getBoosterConfig().getItemStack("Boosters.8"))) {
                boosteruseclick(player, 8);
            }
            if (item.isSimilar(lp.getBoosterConfig().getItemStack("Boosters.9"))) {
                boosteruseclick(player, 9);
            }
            if (item.isSimilar(lp.getBoosterConfig().getItemStack("Boosters.10"))) {
                boosteruseclick(player, 10);
            }
        }
    }
    @EventHandler
    public void onJoin(PlayerMoveEvent event) throws IOException {
        Player player = event.getPlayer();

        cooldowntimer = lp.getConfig().getInt("BoostersTime");
        if (cooldown.containsKey(player.getUniqueId())) {
            long secondsleft = ((cooldown.get(player.getUniqueId()) / 1000) + cooldowntimer) - (System.currentTimeMillis() / 1000);
            if (secondsleft > 0) {
                return;
            } else {
                if(lp.getPlayersConfig().getInt(player.getName() + ".EXP.Active") != 1) {
                    lp.getPlayersConfig().set(player.getName() + ".EXP.Active", 1);
                    lp.getPlayersConfig().save(lp.getPlayersFile());
                }
            }
        }else{
            return;
        }
    }
}
