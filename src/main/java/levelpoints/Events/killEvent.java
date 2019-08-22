package levelpoints.Events;

import com.connorlinfoot.titleapi.TitleAPI;
import levelpoints.lp.LP;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class killEvent implements Listener {
    private Plugin plugin = LP.getPlugin(LP.class);
    private LP lp = LP.getPlugin(LP.class);
    private int number;
    private int exp;
    private int exps;
    private int take;
    private int leftover;
    private int level;
    public int LPML;
    public int LEXP;
    private int needeps;
    public int pts;
    File file = new File("plugins/LP/lang.yml");
    FileConfiguration Lang = YamlConfiguration.loadConfiguration(file);

    public killEvent(LP lp) {

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (lp.LevelConfig.getBoolean("PvpLeveluse")) {
            int levelpvp = lp.LevelConfig.getInt("PvpLevel");
            if (!(event.getDamager() instanceof Player)) {
                return;
            } else {
                if (event.getEntity() instanceof Player) {
                    Player Attacker = (Player) event.getDamager();
                    Player player = (Player) event.getEntity();
                    if ((lp.getPlayersConfig().getInt(player.getName() + ".level") < levelpvp)) {
                        event.setCancelled(true);
                        TitleAPI.sendTitle(player, 20, 50, 20, ChatColor.DARK_RED + "You Must BE", ChatColor.RED + "Level 5 to PVP");
                        Attacker.sendMessage(ChatColor.RED + player.getName() + " Must to Level 5 to allow pvp");
                        Attacker.playSound(Attacker.getLocation(), Sound.ENDERDRAGON_GROWL, 10F, 15);
                    }
                    if ((lp.getPlayersConfig().getInt(Attacker.getName() + ".level") < levelpvp)) {
                        event.setCancelled(true);
                        TitleAPI.sendTitle(Attacker, 20, 50, 20, ChatColor.DARK_RED + "You Must BE", ChatColor.RED + "Level 5 to PVP");
                        //player.sendMessage(ChatColor.RED + Attacker.getName() + " Must to Level 5 to allow pvp");
                        player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 10F, 15);
                    }
                } else {
                    return;
                }
            }

            if (event.getDamager() instanceof Arrow) {
                final Arrow arrow = (Arrow) event.getDamager();

                Player Attacker = (Player) arrow.getShooter();
                Player player = (Player) event.getEntity();
                if ((lp.getPlayersConfig().getInt(player.getName() + ".level") < levelpvp)) {
                    event.setCancelled(true);
                    TitleAPI.sendTitle(player, 20, 50, 20, ChatColor.DARK_RED + "You Must BE", ChatColor.RED + "Level 5 to PVP");
                    Attacker.sendMessage(ChatColor.RED + player.getName() + " Must to Level 5 to allow pvp");
                    Attacker.playSound(Attacker.getLocation(), Sound.ENDERDRAGON_GROWL, 10F, 15);
                }
            }
        }
    }


    @EventHandler
    public void onKill(PlayerDeathEvent d) throws IOException {
        Player player = d.getEntity();

        Player Killer = d.getEntity().getKiller();
        if (Killer instanceof Player) {
            if (lp.EXPConfig.getBoolean("Exp-Kill-players")) {
                lp.CustomXP(Killer, lp.EXPConfig.getInt("Kill-Player-Amount"), 0);
                if (lp.EXPConfig.getBoolean("EXP-Lost-On-Death-Player")) {
                    int expp = lp.getPlayersConfig().getInt(player.getName() + ".EXP.Amount");
                    int t = lp.EXPConfig.getInt("EXP-Lost-Amount");
                    if (t <= expp) {
                        int tep = expp - t;
                        lp.getPlayersConfig().set(player.getName() + ".EXP.Amount", tep);
                        lp.getPlayersConfig().save(lp.getPlayersFile());
                    }
                }
            }
        }

    }

    @EventHandler
    public void onEntityDeath(final EntityDeathEvent event) {

        if (event.getEntity() instanceof Monster) {
            Monster monsterEnt = (Monster) event.getEntity();
            Object mcPlayer = monsterEnt.getKiller();
            Player player = ((Player) mcPlayer);
            if (lp.EXPConfig.getBoolean("Debug")) {
                player.sendMessage(monsterEnt.getType().toString());
            }

            if (lp.EXPConfig.getBoolean("PerWorld")) {

                List<String> worlds = lp.EXPConfig.getStringList("Worlds");
                for (String world : worlds)
                    if (player.getLocation().getWorld().getName().equalsIgnoreCase(world)) {
                        if (mcPlayer == null)
                            return;


                        if (lp.EXPConfig.getBoolean("Exp-Kill-Mob")) {
                            if (lp.EXPConfig.getBoolean("RandomEXP")) {

                                int max = lp.EXPConfig.getInt(monsterEnt.getType().toString());
                                int min = 0;

                                Random r = new Random();
                                int re = r.nextInt((max - min) + 1) + min;
                                lp.CustomXP(player, re, 0);
                            } else {
                                lp.CustomXP(player, lp.EXPConfig.getInt(monsterEnt.getType().toString()), 0);
                            }
                        }
                    }
            } else {
                if (mcPlayer == null)
                    return;


                if (lp.EXPConfig.getBoolean("Exp-Kill-Mob")) {
                    if (lp.EXPConfig.getBoolean("RandomEXP")) {

                        int max = lp.EXPConfig.getInt(monsterEnt.getType().toString());
                        int min = 0;

                        Random r = new Random();
                        int re = r.nextInt((max - min) + 1) + min;
                        lp.CustomXP(player, re, 0);
                    } else {
                        lp.CustomXP(player, lp.EXPConfig.getInt(monsterEnt.getType().toString()), 0);
                    }
                }

            }
        } else {
            if (event.getEntity() instanceof Animals) {
                Animals ani = (Animals) event.getEntity();
                Object mcplayer = ani.getKiller();
                Player player = (Player) mcplayer;

                if (lp.EXPConfig.getBoolean("Debug")) {
                    player.sendMessage(ani.getType().toString());
                }


                if (lp.EXPConfig.getBoolean("PerWorld")) {

                    List<String> worlds = lp.EXPConfig.getStringList("Worlds");
                    for (String world : worlds)
                        if (player.getLocation().getWorld().getName().equalsIgnoreCase(world)) {
                            if (mcplayer == null)
                                return;

                            if (lp.EXPConfig.getBoolean("Passive-Mobs")) {
                                if (lp.EXPConfig.getBoolean("RandomEXP")) {

                                    int max = lp.EXPConfig.getInt(ani.getType().toString());
                                    int min = 0;

                                    Random r = new Random();
                                    int re = r.nextInt((max - min) + 1) + min;
                                    lp.CustomXP(player, re, 0);
                                } else {
                                    lp.CustomXP(player, lp.EXPConfig.getInt(ani.getType().toString()), 0);
                                }

                            }
                        }
                } else {
                    if (mcplayer == null)
                        return;
                    if (lp.EXPConfig.getBoolean("RandomEXP")) {

                        int max = lp.EXPConfig.getInt(ani.getType().toString());
                        int min = 0;

                        Random r = new Random();
                        int re = r.nextInt((max - min) + 1) + min;
                        lp.CustomXP(player, re, 0);
                    } else {
                        lp.CustomXP(player, lp.EXPConfig.getInt(ani.getType().toString()), 0);
                    }

                }

            }
        }
    }
}
