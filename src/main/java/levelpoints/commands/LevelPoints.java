package levelpoints.commands;

import levelpoints.Events.customInventory;
import levelpoints.lp.API;
import levelpoints.lp.LP;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class LevelPoints  implements CommandExecutor {
    private Plugin plugin = LP.getPlugin(LP.class);
    private LP lp = LP.getPlugin(LP.class);
    private String playerName;
    private String cfgName;
    public int hours;
    public int minutes;
    public int seconds;
    public String commandString;
    public int posTop;

    File file = new File("plugins/LP/lang.yml");
    FileConfiguration Lang = YamlConfiguration.loadConfiguration(file);

    public LevelPoints(LP lp) {

    }



    public void doCountDown() {
		
		 
        if (seconds > 0) {
            seconds -= 1;

            new BukkitRunnable() {
                @Override
                public void run() {
                    doCountDown();
                }
            }.runTaskLater(lp, 20);
        }
        else if(seconds == 0){
            if(minutes == 0){
                if(!(hours == 0)){
                    minutes = hours * 60;
                    hours -= 1;
                }
            }else{
                seconds = 60;
                minutes -= 1;
            }
            doCountDown();
        }
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // List<String> translatedMessages = Lang.getStringList("lp.baseCommand");
        //Set<String> sl = Lang.getConfigurationSection("lp").getKeys(false);
        int LEXP = lp.LevelConfig.getInt("LevelingEXP");
        if (args.length == 0) {
            for(String x : Lang.getStringList("lp")) {

                sender.sendMessage(API.format(x));
            }


            return true;
        }

        if(args[0].equalsIgnoreCase("apphasaskedforthis")){
            int max = 1000;
            int min = 0;

            Random r = new Random();
            int re = r.nextInt((max - min) + 1) + min;
            sender.sendMessage(ChatColor.DARK_AQUA + "You Have Consumed " + re + " EXP");
        }
        if (sender.hasPermission("lp.admin.reload")) {
            if (args[0].equalsIgnoreCase("reload")) {
                sender.sendMessage(API.format(Lang.getString("lpreload")));

                lp.playersFile = new File(lp.getDataFolder(), "players.yml");
                lp.boosterFile = new File(lp.getDataFolder(), "Boosters.yml");
                lp.LangFile = new File(lp.getDataFolder(), "Lang.yml");
                lp.EXPFile = new File(lp.getDataFolder(), "/Settings/EXP.yml");
                lp.ESFile = new File(lp.getDataFolder(), "/OtherSettings/EpicSpawners.yml");
                lp.WSFile = new File(lp.getDataFolder(), "/OtherSettings/WildStacker.yml");
                lp.RewardsFile = new File(lp.getDataFolder(), "/Settings/Rewards.yml");
                lp.LevelFile = new File(lp.getDataFolder(), "/Settings/Levels.yml");
                lp.boosterConfig = YamlConfiguration.loadConfiguration(lp.boosterFile);
                lp.playersConfig = YamlConfiguration.loadConfiguration(lp.playersFile);
                lp.LangConfig = YamlConfiguration.loadConfiguration(lp.LangFile);
                lp.EXPConfig = YamlConfiguration.loadConfiguration(lp.EXPFile);
                lp.LevelConfig = YamlConfiguration.loadConfiguration(lp.LevelFile);
                lp.RewardsConfig = YamlConfiguration.loadConfiguration(lp.RewardsFile);
                lp.ESConfig = YamlConfiguration.loadConfiguration(lp.ESFile);
                lp.WSConfig = YamlConfiguration.loadConfiguration(lp.WSFile);
                lp.reloadConfig();
                return true;
            }
        }
        //info command
        //Player player = (Player) sender;
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args[0].equalsIgnoreCase("expgive")){
                if(player.hasPermission("lp.admin.give")) {
                    if (args.length == 1) {
                        sender.sendMessage(ChatColor.RED + "You must pick a player");
                        return true;
                    }

                    if (args.length == 2) {
                        sender.sendMessage(ChatColor.RED + "You Must Pick a Number of EXP to add");
                        return true;
                    }
                    Player target = Bukkit.getPlayer(args[1]);

                    if (target != null) {
                        lp.CustomXP(target, Integer.parseInt(args[2]),0);
                    }
                }else{
                    player.sendMessage(ChatColor.RED + "You Have Insufficient Permission");
                }
            }

            if(args[0].equalsIgnoreCase("expremove")){
                if(player.hasPermission("lp.admin.remove")) {

                    if (args.length == 1) {
                        sender.sendMessage(ChatColor.RED + "You Must Pick a Player");
                        return true;
                    }
                    if (args.length == 2) {
                        sender.sendMessage(ChatColor.RED + "You Must Pick a Number of EXP to remove");
                        return true;
                    }
                    Player target = Bukkit.getPlayer(args[1]);

                    if (target != null) {
                        int expp = lp.getPlayersConfig().getInt(target.getName() + ".EXP.Amount");
                        int t = Integer.parseInt(args[2]);
                        int tep = expp - t;
                        lp.getPlayersConfig().set(target.getName() + ".EXP.Amount", tep);
                        try {
                            lp.getPlayersConfig().save(lp.getPlayersFile());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    player.sendMessage(ChatColor.RED + "You Have Insufficient Permission");
                }
                if(lp.getConfig().getBoolean("UseSQL")) {
                    lp.update_EXP(player.getUniqueId(), player);
                    lp.update_LEVEL(player.getUniqueId(), player);
                    lp.update_PRESTIGE(player.getUniqueId(), player);
                }
            }

            if(args[0].equalsIgnoreCase("setlevel")){
                if(player.hasPermission("lp.admin")) {
                    if (args.length == 1) {
                        sender.sendMessage(ChatColor.RED + "You Must Pick a Player");
                        return true;
                    }
                    if (args.length == 2) {
                        sender.sendMessage(ChatColor.RED + "You Must Pick a Level to set");
                        return true;
                    }
                    Player target = Bukkit.getPlayer(args[1]);

                    if(target != null){
                        int level = Integer.parseInt(args[2]);
                        lp.getPlayersConfig().set(target.getName() + ".level", level);
                        try {
                            lp.getPlayersConfig().save(lp.playersFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    player.sendMessage(ChatColor.RED + "You Have Insufficient Permission");
                }
            }
            playerName = player.getName();
            cfgName = lp.getPlayersConfig().getString(player.getName());


            if (player.hasPermission("lp.player")) {
                if (args[0].equalsIgnoreCase("bp")) {
                    customInventory i = new customInventory();
                    i.boosterInventory(player);
                }
                if(args[0].equalsIgnoreCase("top")){
                    sender.sendMessage(ChatColor.DARK_GRAY + "-----------------------");
                    posTop = 0;

                    ConfigurationSection cf = lp.getPlayersConfig().getConfigurationSection("");
                    cf.getValues(false)
                            .entrySet()
                            .stream()
                            .sorted((a1, a2) -> {
                                int points1 = ((MemorySection) a1.getValue()).getInt("level");
                                int points2 = ((MemorySection) a2.getValue()).getInt("level");
                                return points2 - points1;
                            })
                            .limit(10) // Limit the number of 'results'
                            .forEach(f -> {
                                posTop += 1;

                                int points = ((MemorySection) f.getValue()).getInt("level");
                                sender.sendMessage(ChatColor.AQUA + Integer.toString(posTop) + ". " + f.getKey() + ": " + points);
                                // Here you can send to player or do whatever you wan't.
                            });
                    sender.sendMessage(ChatColor.DARK_GRAY + "-----------------------");


                }
            }

            if (args[0].equalsIgnoreCase("booster")) {
                if(player.hasPermission("lp.booster")) {
                    ItemStack item = player.getItemInHand();
                    item.setAmount(1);
                    String number = args[2];
                    if (args[1].equalsIgnoreCase("set")) {
                        lp.getBoosterConfig().set("Boosters." + number, item);
                        try {
                            lp.getBoosterConfig().save(lp.boosterFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (args[1].equalsIgnoreCase("give")) {
                        Player target = Bukkit.getPlayer(args[2]);
                        if (target == null) {
                            player.sendMessage(ChatColor.DARK_AQUA + "You Must select a player that is online!");
                        } else {
                            target.getInventory().addItem(lp.getBoosterConfig().getItemStack("Boosters." + args[3]));
                        }
                    }
                }else{
                    player.sendMessage(ChatColor.RED + "You Have Insufficient Permission");
                }
            }


            if (args[0].equalsIgnoreCase("creator")) {
                sender.sendMessage(ChatColor.DARK_AQUA + "LevelPoints Created by: " + ChatColor.AQUA + "Zoon20X");
            }

            if (args[0].equalsIgnoreCase("start")) {

                seconds = 65;
                if (seconds >= 60) {
                    minutes = seconds / 60;
                    seconds = seconds - (minutes * 60);
                }
                if (minutes >= 60) {
                    hours = minutes / 60;
                    minutes = minutes - (hours * 60);
                }

                player.sendMessage(ChatColor.RED + "Start Timer");
                player.sendMessage(ChatColor.GREEN + "Hours: " + hours);
                player.sendMessage(ChatColor.GREEN + "Minutes: " + minutes);
                player.sendMessage(ChatColor.GREEN + "Seconds: " + seconds);
                doCountDown();
            }


            if (sender.hasPermission("lp.player")) {
                int needep;
                if (args[0].equalsIgnoreCase("info")) {
                    String levels;
                    if (args.length == 1) {
                        levels = lp.getPlayersConfig().getString(player.getName() + ".level");
                        int pres = lp.getPlayersConfig().getInt(player.getName() + ".Prestige");
                        int nlevel = lp.getPlayersConfig().getInt(player.getName() + ".level");
                        int expss = lp.getPlayersConfig().getInt(player.getName() + ".EXP.Amount");


                        if(lp.LevelConfig.getBoolean("PrestigeLeveling")){
                            needep = lp.LevelConfig.getInt("Prestige-" + lp.getPlayersConfig().getInt(player.getName() + ".Prestige") + ".Level-" + lp.getPlayersConfig().getInt(player.getName() + ".level"));
                        }else if(lp.LevelConfig.getBoolean("CustomLeveling")){
                            needep = lp.LevelConfig.getInt("Level-" + nlevel);
                        }else{
                            needep = nlevel * LEXP;
                        }
                        float percentage = expss * 100;

                        String EXP = Integer.toString(expss) + "/" + Integer.toString(needep);
                        String Percentage = Math.round(percentage/needep) + "%";

                       // player.sendMessage(API.format(Lang.getString("lpinfoName").replace("{lp_player}", player.getName())));
                        //player.sendMessage(ChatColor.AQUA + " ");
                        //player.sendMessage(API.format(Lang.getString("lpinfoLevel").replace("{lp_level}", levels)));
                        //player.sendMessage(API.format(Lang.getString("lpinfoEXP").replace("{lp_xp}", EXP)));
                       // player.sendMessage(API.format(Lang.getString("lpinfoEXPProgress").replace("{lp_xp_required}", Percentage)));
                       // player.sendMessage(ChatColor.DARK_GREEN + "");
                       // player.sendMessage(API.format(Lang.getString("lpinfoPrestige").replace("{lp_prestige}", Integer.toString(pres))));

                        for(String x : Lang.getStringList("lpsInfo")) {

                            sender.sendMessage(API.format(x.replace("{lp_player}", player.getName()).replace("{lp_level}", levels).replace("{lp_xp}", EXP).replace("{lp_progress}", Percentage).replace("{lp_prestige}", Integer.toString(pres))));
                        }
                        return true;
                    }

                    StringBuilder str = new StringBuilder(args[1]);

                    for (int i = 2; i < args.length; i++) {
                        str.append(' ').append(args[i]);
                    }
                    levels = lp.getPlayersConfig().getString(str.toString() + ".level");
                    int pres = lp.getPlayersConfig().getInt(str.toString() + ".Prestige");
                    int nlevel = lp.getPlayersConfig().getInt(str.toString() + ".level");
                    int expss = lp.getPlayersConfig().getInt(str.toString() + ".EXP.Amount");

                    if(lp.LevelConfig.getBoolean("PrestigeLeveling")){
                        needep = lp.LevelConfig.getInt("Prestige-" + lp.getPlayersConfig().getInt(str.toString() + ".Prestige") + ".Level-" + lp.getPlayersConfig().getInt(str.toString() + ".level"));
                    }else if(lp.LevelConfig.getBoolean("CustomLeveling")){
                        needep = lp.LevelConfig.getInt("Level-" + nlevel);
                    }else{
                        needep = nlevel * LEXP;
                    }
                    float percentage = expss * 100;
                    String EXP = Integer.toString(expss) + "/" + Integer.toString(needep);
                    String Percentage = Math.round(percentage/needep) + "%";

                    for(String x : Lang.getStringList("lpsInfo")) {

                        sender.sendMessage(API.format(x.replace("{lp_player}", str.toString()).replace("{lp_level}", levels).replace("{lp_xp}", EXP).replace("{lp_progress}", Percentage).replace("{lp_prestige}", Integer.toString(pres))));
                    }
                    return true;
                }
            }else{
                player.sendMessage(ChatColor.RED + "You Have Insufficient Permission");
            }
            if(args[0].equalsIgnoreCase("prestige")) {
                if (lp.LevelConfig.getBoolean("Prestige")) {
                    if (lp.getPlayersConfig().getInt(playerName + ".level") == lp.LevelConfig.getInt("MaxLevel")) {
                        int CustomMaxEXP = lp.getConfig().getInt("Level-" + lp.LevelConfig.getInt("MaxLevel"));
                        int PrestigeMaxEXP = lp.LevelConfig.getInt("Prestige-" + lp.getPlayersConfig().getInt(player.getName() + ".Prestige") + ".Level-" + lp.LevelConfig.getInt("MaxLevel"));
                        if (lp.LevelConfig.getBoolean("PrestigeLeveling")) {
                            if (lp.getPlayersConfig().getInt(playerName + ".EXP.Amount") >= PrestigeMaxEXP) {
                                if (args[0].equalsIgnoreCase("prestige")) {
                                    sender.sendMessage(ChatColor.DARK_AQUA + "You Prestiged");
                                    lp.getPlayersConfig().set(playerName + ".level", 1);
                                    lp.getPlayersConfig().set(playerName + ".Prestige", lp.getPlayersConfig().getInt(player.getName() + ".Prestige") + 1);
                                    lp.getPlayersConfig().set(playerName + ".EXP.Amount", 0);

                                    try {
                                        lp.getPlayersConfig().save(lp.getPlayersFile());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    return true;
                                }
                            }
                        } else if (lp.LevelConfig.getBoolean("CustomLeveling")) {

                            if (lp.getPlayersConfig().getInt(playerName + ".EXP.Amount") >= CustomMaxEXP) {
                                if (args[0].equalsIgnoreCase("prestige")) {
                                    sender.sendMessage(ChatColor.DARK_AQUA + "You Prestiged");
                                    lp.getPlayersConfig().set(playerName + ".level", 1);
                                    lp.getPlayersConfig().set(playerName + ".Prestige", 1);
                                    lp.getPlayersConfig().set(playerName + ".EXP.Amount", 0);

                                    try {
                                        lp.getPlayersConfig().save(lp.getPlayersFile());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    return true;
                                }
                            }
                        } else {
                            if (lp.getPlayersConfig().getInt(playerName + ".EXP.Amount") >= (lp.getConfig().getInt("LevelingEXP") * lp.getConfig().getInt("MaxLevel"))) {
                                if (args[0].equalsIgnoreCase("prestige")) {
                                    sender.sendMessage(ChatColor.DARK_AQUA + "You Prestiged");
                                    lp.getPlayersConfig().set(playerName + ".level", 1);
                                    lp.getPlayersConfig().set(playerName + ".Prestige", 1);
                                    lp.getPlayersConfig().set(playerName + ".EXP.Amount", 0);

                                    try {
                                        lp.getPlayersConfig().save(lp.getPlayersFile());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    return true;
                                }
                            }
                        }
                    }else{
                        player.sendMessage(ChatColor.RED + "You are Not at Max Level to Prestige");
                    }
                }
            }


            return true;
        }else {
            if (args[0].equalsIgnoreCase("expgive")) {
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "You must pick a player");
                    return true;
                }

                if (args.length == 2) {
                    sender.sendMessage(ChatColor.RED + "You Must Pick a Number of EXP to add");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);

                if (target != null) {
                    lp.CustomXP(target, Integer.parseInt(args[2]),0);
                }
            }

            if (args[0].equalsIgnoreCase("expremove")) {

                if (args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "You Must Pick a Player");
                    return true;
                }
                if (args.length == 2) {
                    sender.sendMessage(ChatColor.RED + "You Must Pick a Number of EXP to remove");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);

                if (target != null) {
                    int expp = lp.getPlayersConfig().getInt(target.getName() + ".EXP.Amount");
                    int t = Integer.parseInt(args[2]);
                    int tep = expp - t;
                    lp.getPlayersConfig().set(target.getName() + ".EXP.Amount", tep);
                    try {
                        lp.getPlayersConfig().save(lp.getPlayersFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(lp.getConfig().getBoolean("UseSQL")) {
                    lp.update_EXP(target.getUniqueId(), target);
                    lp.update_LEVEL(target.getUniqueId(), target);
                    lp.update_PRESTIGE(target.getUniqueId(), target);
                }
            }
        }
        return true;
    }
}