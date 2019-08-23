package levelpoints.lp;


import com.Zrips.CMI.CMI;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;
import levelpoints.Events.*;
import levelpoints.commands.LevelPoints;
import levelpoints.otherPluginConnections.EpicSpawners;
import levelpoints.otherPluginConnections.WildStacker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class LP extends JavaPlugin implements Listener {


    private mySQL sql;
    private Connection connection;
    public String host, database, username, password, table;
    public int port;
    public File playersFile = new File(getDataFolder(), "players.yml");
    public File boosterFile = new File(getDataFolder(), "Boosters.yml");
    public File LangFile = new File("plugins/LP/lang.yml");
    public File ESFile = new File("plugins/LP/OtherSettings/EpicSpawners.yml");
    public File EXPFile = new File("plugins/LP/Settings/EXP.yml");
    public File LevelFile = new File("plugins/LP/Settings/Levels.yml");
    public File RewardsFile = new File("plugins/LP/Settings/Rewards.yml");
    public File WSFile = new File("plugins/LP/OtherSettings/WildStacker.yml");
    public FileConfiguration LangConfig = YamlConfiguration.loadConfiguration(LangFile);
    public FileConfiguration playersConfig = YamlConfiguration.loadConfiguration(playersFile);
    public FileConfiguration boosterConfig = YamlConfiguration.loadConfiguration(boosterFile);
    public int PlayerLevels;
    public String CommandName;
    private String key = "key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=63996";
    private String VersionJoin;
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
    File lfile = new File("plugins/LP/lang.yml");
    FileConfiguration Lang = YamlConfiguration.loadConfiguration(lfile);
    File ESfile = new File("plugins/LP/OtherSettings/EpicSpawners.yml");
    public FileConfiguration ESConfig = YamlConfiguration.loadConfiguration(ESfile);
    File WSfile = new File("plugins/LP/OtherSettings/WildStacker.yml");
    public FileConfiguration WSConfig = YamlConfiguration.loadConfiguration(WSfile);
    File EXPfile = new File("plugins/LP/Settings/EXP.yml");
    public FileConfiguration EXPConfig = YamlConfiguration.loadConfiguration(EXPfile);
    File Levelfile = new File("plugins/LP/Settings/Levels.yml");
    public FileConfiguration LevelConfig = YamlConfiguration.loadConfiguration(Levelfile);
    File Rewardsfile = new File("plugins/LP/Settings/Rewards.yml");
    public FileConfiguration RewardsConfig = YamlConfiguration.loadConfiguration(Rewardsfile);
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new joinEvent(this), this);
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new breakEvent(this), this);
        getServer().getPluginManager().registerEvents(new killEvent(this), this);
        getServer().getPluginManager().registerEvents(new customInventory(this), this);
        getServer().getPluginManager().registerEvents(new ItemUse(this), this);
        getServer().getPluginManager().registerEvents(new ChatEvent(this), this);
        saveFiles();
        try {
            this.getPlayersConfig().save(playersFile);
            getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "players.yml Reloaded");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.getBoosterConfig().save(boosterFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.saveDefaultConfig();
        if(this.getConfig().getBoolean("EpicSpawners")) {
            getServer().getPluginManager().registerEvents(new EpicSpawners(this), this);
        }else if(this.getConfig().getBoolean("WildStacker")) {
            getServer().getPluginManager().registerEvents(new WildStacker(this), this);
        }
        this.getCommand("levelpoints").setExecutor((CommandExecutor) new LevelPoints(this));
        this.getCommand("lps").setExecutor((CommandExecutor) new LevelPoints(this));

        if(this.getConfig().getBoolean("UseSQL")) {
            mySQLSetup();
            getServer().getPluginManager().registerEvents(new mySQL(this), this);
        }

        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "=============================");
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "LevelPoints Plugin");
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "Developer: Zoon20X");
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "Version: " + this.getDescription().getVersion());
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "MC-Compatible: 1.8-1.13");
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "Config-Version: 1.2.2");
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "Enabled");
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "=============================");
        versionChecker();



    }

    public void mySQLSetup(){
        host = this.getConfig().getString("host");
        port = this.getConfig().getInt("port");
        username = this.getConfig().getString("username");
        database = this.getConfig().getString("database");
        password = this.getConfig().getString("password");
        table = "playerData";
        try{
            synchronized (this){
                if(getConnection() != null && !getConnection().isClosed()){
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database,this.username, this.password));
                this.getServer().getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "MySQL Connected");

            }
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    public FileConfiguration getPlayersConfig() {
        return playersConfig;
    }

    public FileConfiguration getBoosterConfig() {
        return boosterConfig;
    }

    public File getPlayersFile() {
        return playersFile;
    }

    public File getBoosterFile() {
        return boosterFile;
    }

    private void versionChecker() {
        if (this.getConfig().getBoolean("CheckUpdates")) {
            this.getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "Checking for Updates...");

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=63996").openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.getOutputStream().write((key).getBytes("UTF-8"));
                String version = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();

                if (version.equalsIgnoreCase(this.getDescription().getVersion())) {
                    this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "No New Updates for LevelPoints");
                    this.VersionJoin = version;
                    return;
                } else {
                    this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "You do not have the most updated version of LevelPoints");
                    this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Download it from: https://www.spigotmc.org/resources/levelpoints-1-8-1-13-2.63996/updates");
                    this.VersionJoin = version;
                }
            } catch (IOException e) {
                this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Couldnt connect");
                e.printStackTrace();
            }
        } else {
            this.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "You Are Not Checking For LevelPoints Updates");
            return;
        }

    }

    public void wait(int seconds, Player player, String message) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
                player.sendMessage(message);

            }
        }, (seconds * 10));
    }

    @EventHandler
    public void versionMessage(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (this.getConfig().getBoolean("CheckUpdates")) {
            if (player.hasPermission("lp.admin.version")) {
                if (!this.getDescription().getVersion().contains("Beta")) {
                    if (VersionJoin.equalsIgnoreCase(this.getDescription().getVersion())) {
                        this.wait(2, player, ChatColor.GREEN + "No Updates For LevelPoints.");
                    } else {
                        this.wait(2, player, ChatColor.RED + "You do not have the most updated version of LevelPoints.");

                        this.wait(3, player, " ");
                        this.wait(4, player, ChatColor.RED + "Download it from: https://www.spigotmc.org/resources/levelpoints-1-8-1-13-2.63996/updates");
                        this.wait(5, player, ChatColor.DARK_AQUA + "Update version: " + VersionJoin);
                        this.wait(5, player, ChatColor.DARK_RED + "Version Your Running: " + this.getDescription().getVersion());
                    }
                } else {
                    this.wait(2, player, "your in beta");
                }
            }
        }
    }

    public void saveFiles() {
        if (LangFile == null) {
            LangFile = new File("plugins/LP/lang.yml");
            LangConfig = YamlConfiguration.loadConfiguration(LangFile);
        }
        if (!LangFile.exists()) {
            this.saveResource("lang.yml", false);
        }
        if (ESFile == null) {
            ESFile = new File("plugins/LP/OtherSettings/EpicSpawners.yml");
            ESConfig = YamlConfiguration.loadConfiguration(ESFile);
        }
        if (!ESFile.exists()) {
            this.saveResource("OtherSettings/EpicSpawners.yml", false);
        }
        if (EXPFile == null) {
            EXPFile = new File("plugins/LP/Settings/EXP.yml");
            EXPConfig = YamlConfiguration.loadConfiguration(EXPFile);
        }
        if (!EXPFile.exists()) {
            this.saveResource("Settings/EXP.yml", false);
        }
        if (LevelFile == null) {
            LevelFile = new File("plugins/LP/Settings/Levels.yml");
            LevelConfig = YamlConfiguration.loadConfiguration(LevelFile);
        }
        if (!LevelFile.exists()) {
            this.saveResource("Settings/Levels.yml", false);
        }

        if (RewardsFile == null) {
            RewardsFile = new File("plugins/LP/Settings/Rewards.yml");
            RewardsConfig = YamlConfiguration.loadConfiguration(RewardsFile);
        }
        if (!RewardsFile.exists()) {
            this.saveResource("Settings/Rewards.yml", false);
        }
        if (WSFile == null) {
            WSFile = new File("plugins/LP/OtherSettings/WildStacker.yml");
            WSConfig = YamlConfiguration.loadConfiguration(WSFile);
        }
        if (!WSFile.exists()) {
            this.saveResource("OtherSettings/WildStacker.yml", false);
        }

    }
    public void CustomXP(Player player, int expamount, int left) {
        if (player.hasPermission("levelpoints.xp")) {
            int boosteractiive = this.getPlayersConfig().getInt(player.getName() + ".EXP.Active");
            if (LevelConfig.getBoolean("PrestigeLeveling")) {
                LEXP = LevelConfig.getInt("Prestige-" + this.getPlayersConfig().getInt(player.getName() + ".Prestige") + ".Level-" + this.getPlayersConfig().getInt(player.getName() + ".level"));
            } else {
                if (LevelConfig.getBoolean("CustomLeveling")) {
                    LEXP = LevelConfig.getInt("Level-" + this.getPlayersConfig().getInt(player.getName() + ".level"));

                } else {
                    LEXP = LevelConfig.getInt("LevelingEXP");
                }
            }
            LPML = LevelConfig.getInt("MaxLevel");
            if (this.EXPConfig.getBoolean("Use-Booster")) {
                int ps = expamount;
                pts = ps * boosteractiive;
            } else {
                pts = expamount;
            }
            int CustomMaxEXP = LevelConfig.getInt("Level-" + LevelConfig.getInt("MaxLevel"));
            int PrestigeMaxEXP = LevelConfig.getInt("Prestige-" + this.getPlayersConfig().getInt(player.getName() + ".Prestige") + ".Level-" + LevelConfig.getInt("MaxLevel"));
            number = pts;
            if (left == 0) {
                exp = this.getPlayersConfig().getInt(player.getName() + ".EXP.Amount") + number;
            } else {
                exp = left;
            }
            if (LevelConfig.getBoolean("PrestigeLeveling")) {
                if (PrestigeMaxEXP == 0) {
                    if (player.hasPermission("lp.admin")) {
                        player.sendMessage(API.format(Lang.getString("lpLevelCustomError")));
                    } else {
                        player.sendMessage(API.format(Lang.getString("lpLevelCustomErrorPlayer")));

                    }
                }
                if (this.getPlayersConfig().getInt(player.getName() + ".EXP.Amount") >= PrestigeMaxEXP) {
                    return;
                } else {
                    this.getPlayersConfig().set(player.getName() + ".EXP.Amount", exp);
                }
            } else if (LevelConfig.getBoolean("CustomLeveling")) {
                if (CustomMaxEXP == 0) {
                    if (player.hasPermission("lp.admin")) {
                        player.sendMessage(API.format(Lang.getString("lpLevelCustomError")));
                    } else {
                        player.sendMessage(API.format(Lang.getString("lpLevelCustomErrorPlayer")));

                    }
                }
                if (this.getPlayersConfig().getInt(player.getName() + ".EXP.Amount") >= CustomMaxEXP) {
                    return;
                } else {
                    this.getPlayersConfig().set(player.getName() + ".EXP.Amount", exp);
                }
            } else {
                if (this.getPlayersConfig().getInt(player.getName() + ".EXP.Amount") >= (LEXP * LPML)) {
                    return;
                } else {
                    this.getPlayersConfig().set(player.getName() + ".EXP.Amount", exp);
                }
            }

            level = this.getPlayersConfig().getInt(player.getName() + ".level");
            exp = this.getPlayersConfig().getInt(player.getName() + ".EXP.Amount");
            int levels = level + 1;
            if (LevelConfig.getBoolean("PrestigeLeveling")) {
                take = LEXP;
            } else if (LevelConfig.getBoolean("CustomLeveling")) {
                take = LEXP;
            } else {
                take = level * LEXP;
            }

            leftover = exp - take;
            if (LevelConfig.getBoolean("PrestigeLeveling")) {
                if (getPlayersConfig().getInt(player.getName() + ".EXP.Amount") >= this.LEXP){

                    getPlayersConfig().set(player.getName() + ".level", Integer.valueOf(this.level + 1));
                    getPlayersConfig().set(player.getName() + ".EXP.Amount", Integer.valueOf(this.leftover));
                    LevelUp(player, 0, level);
                } else if (LevelConfig.getBoolean("CustomLeveling")) {
                    if (getPlayersConfig().getInt(player.getName() + ".EXP.Amount") >= this.LEXP) {

                        getPlayersConfig().set(player.getName() + ".level", Integer.valueOf(this.level + 1));
                        getPlayersConfig().set(player.getName() + ".EXP.Amount", Integer.valueOf(this.leftover));
                        LevelUp(player, 0, level);
                    }
                }
            }
            if (this.getPlayersConfig().getInt(player.getName() + ".level") == LPML) {

                if (this.getPlayersConfig().getInt(player.getName() + ".EXP.Amount") >= (LEXP * LPML)) {
                    player.sendMessage(ChatColor.DARK_GREEN + "You Have All The EXP You Need To Prestige.");
                    player.sendMessage(ChatColor.DARK_GREEN + " ");
                    player.sendMessage(ChatColor.DARK_GREEN + "Make Sure All Other Quests Are Completed before Prestiging.");
                }
            } else {
                if (getPlayersConfig().getInt(player.getName() + ".EXP.Amount") >= this.LEXP * this.level) {

                    getPlayersConfig().set(player.getName() + ".level", Integer.valueOf(this.level + 1));
                    getPlayersConfig().set(player.getName() + ".EXP.Amount", Integer.valueOf(this.leftover));
                    LevelUp(player, 0, level);
                }
            }

            int nlevelss = this.getPlayersConfig().getInt(player.getName() + ".level");
            String expsss = this.getPlayersConfig().getString(player.getName() + ".EXP.Amount");
            exps = this.getPlayersConfig().getInt(player.getName() + ".EXP.Amount");
            if (LevelConfig.getBoolean("PrestigeLeveling")) {
                needeps = LEXP;
            } else if (LevelConfig.getBoolean("CustomLeveling")) {
                needeps = LEXP;
            } else {
                needeps = nlevelss * LEXP;
            }

            if (this.getConfig().getBoolean("Use-Actionbar-2.0")) {
                double required_progress = take;
                double current_progress = exps;
                double progress_percentage = current_progress / required_progress;
                StringBuilder sb = new StringBuilder();
                int bar_length = 10;
                for (int i = 0; i < bar_length; i++) {
                    if (i < bar_length * progress_percentage) {
                        sb.append(ChatColor.GREEN + "▄"); //what to append if percentage is covered (e.g. GREEN '|'s)
                    } else {
                        sb.append(ChatColor.GRAY + "▄"); //what to append if percentage is not covered (e.g. GRAY '|'s)
                    }
                }
                if (this.getConfig().getBoolean("ActionBarAPI")) {
                    ActionBarAPI.sendActionBar(player, sb.toString() + " " + ChatColor.AQUA + expsss + "/" + needeps);
                } else if (this.getConfig().getBoolean("CMI")) {
                    CMI.getInstance().getActionBar().send(player, sb.toString() + " " + ChatColor.AQUA + expsss + "/" + needeps);
                }
            } else {
                double required_progress = take;
                double current_progress = exps;
                double progress_percentage = current_progress / required_progress;
                StringBuilder sb = new StringBuilder();
                int bar_length = 20;
                for (int i = 0; i < bar_length; i++) {
                    if (i < bar_length * progress_percentage) {
                        sb.append(ChatColor.GREEN + ":"); //what to append if percentage is covered (e.g. GREEN '|'s)
                    } else {
                        sb.append(ChatColor.GRAY + ":"); //what to append if percentage is not covered (e.g. GRAY '|'s)
                    }
                }
                if (this.getConfig().getBoolean("ActionBarAPI")) {
                    ActionBarAPI.sendActionBar(player, sb.toString() + " " + ChatColor.AQUA + expsss + "/" + needeps, 0);
                } else if (this.getConfig().getBoolean("CMI")) {
                    CMI.getInstance().getActionBar().send(player, sb.toString() + " " + ChatColor.AQUA + expsss + "/" + needeps);
                }
            }
            try {
                this.getPlayersConfig().save(this.getPlayersFile());
            } catch (IOException e) {
                e.printStackTrace();
            }


            int lee = this.getPlayersConfig().getInt(player.getName() + ".EXP.Amount");
            int epse = take;

            if (lee > take) {
                CustomXP(player, lee, lee);
            } else {
                lee = 0;
            }
        }
    }

    public void update_EXP(UUID uuid, Player player){
        try {
            PreparedStatement statement = this.getConnection().prepareStatement("UPDATE " + this.table + " SET EXP=? WHERE UUID=?");
            statement.setString(1, String.valueOf(this.getPlayersConfig().getInt(player.getName() + ".EXP.Amount")));
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "Couldn't Update SQL Database");
        }
    }
    public void update_LEVEL(UUID uuid, Player player){
        try {
            PreparedStatement statement = this.getConnection().prepareStatement("UPDATE " + this.table + " SET LEVEL=? WHERE UUID=?");
            statement.setString(1, String.valueOf(this.getPlayersConfig().getInt(player.getName() + ".level")));
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getEXP(UUID uuid, Player player){
        try{
            PreparedStatement statement = this.getConnection().prepareStatement("SELECT * FROM " + this.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            if(statement != null){
                this.getPlayersConfig().set(player.getName() + ".EXP.Amount", results.getInt("EXP"));
            }
            this.getPlayersConfig().save(playersFile);
        }catch(SQLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getLevel(UUID uuid, Player player){
        try{
            PreparedStatement statement = this.getConnection().prepareStatement("SELECT * FROM " + this.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            if(statement != null){
                this.getPlayersConfig().set(player.getName() + ".level", results.getInt("LEVEL"));
            }

            this.getPlayersConfig().save(playersFile);
        }catch(SQLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getPrestige(UUID uuid, Player player){
        try{
            PreparedStatement statement = this.getConnection().prepareStatement("SELECT * FROM " + this.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            if(statement != null){
                this.getPlayersConfig().set(player.getName() + ".Prestige", results.getInt("PRESTIGE"));
            }

            this.getPlayersConfig().save(playersFile);
        }catch(SQLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void update_PRESTIGE(UUID uuid, Player player){
        try {
            PreparedStatement statement = this.getConnection().prepareStatement("UPDATE " + this.table + " SET PRESTIGE=? WHERE UUID=?");
            statement.setString(1, String.valueOf(this.getPlayersConfig().getInt(player.getName() + ".Prestige")));
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void LevelUp(Player player, int expamount, int level) {

        int levels = level;
        int PrestigeMaxEXP = LevelConfig.getInt("Prestige-" + getPlayersConfig().getInt(player.getName() + ".Prestige") + ".Level-" + LevelConfig.getInt("MaxLevel"));
        int CustomMaxEXP = LevelConfig.getInt("Level-" + LevelConfig.getInt("MaxLevel"));
        if (RewardsConfig.getString("Type").equals("REGULAR")) {
            List<String> cmds = RewardsConfig.getStringList(API.format("Rewards.Level-" + (level + 1)));
            for (String command : cmds) {
                if (RewardsConfig.getString("RewardsMethod").equals("NONE")) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                } else if (RewardsConfig.getString("RewardsMethod").equals("MESSAGE")) {
                    player.sendMessage(API.format(Lang.getString("lpRewardMessage")) + levels);
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                } else if (RewardsConfig.getString("RewardsMethod").equals("TITLE")) {
                    TitleAPI.sendTitle(player, 10, LEXP, 10, Lang.getString(API.format("lpRewardTitleTop")), Lang.getString(API.format("lpRewardTitleBottom")) + " " + levels);
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                }
            }
        } else if (RewardsConfig.getString("Type").equals("ONE")) {
            List<String> cmds = RewardsConfig.getStringList("CMD");
            for (String command : cmds) {
                if (RewardsConfig.getString("RewardsMethod").equals("NONE")) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                } else if (RewardsConfig.getString("RewardsMethod").equals("MESSAGE")) {
                    player.sendMessage(API.format(Lang.getString("lpRewardMessage")) + levels);
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                } else if (RewardsConfig.getString("RewardsMethod").equals("TITLE")) {
                    TitleAPI.sendTitle(player, 10, LEXP, 10, Lang.getString(API.format("lpRewardTitleTop")), Lang.getString(API.format("lpRewardTitleBottom")) + " " + levels);
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                }
            }
        } else if (RewardsConfig.getString("Type").equals("CHANCE")) {
            int max = RewardsConfig.getInt("Amount");
            int min = 1;

            Random r = new Random();
            int re = r.nextInt((max - min) + 1) + min;
            List<String> cmds = RewardsConfig.getStringList(String.valueOf(re));
            for (String command : cmds)
                if (RewardsConfig.getString("RewardsMethod").equals("NONE")) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                } else if (RewardsConfig.getString("RewardsMethod").equals("MESSAGE")) {
                    player.sendMessage(API.format(Lang.getString("lpRewardMessage")) + levels);
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                } else if (RewardsConfig.getString("RewardsMethod").equals("TITLE")) {
                    TitleAPI.sendTitle(player, 10, LEXP, 10, Lang.getString(API.format("lpRewardTitleTop")), Lang.getString(API.format("lpRewardTitleBottom")) + " " + levels);
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                }
        }
    }
}