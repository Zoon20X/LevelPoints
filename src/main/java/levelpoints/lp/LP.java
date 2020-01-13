package levelpoints.lp;


import levelpoints.Events.*;
import levelpoints.commands.LevelPoints;
import levelpoints.otherPluginConnections.EpicSpawners;
import levelpoints.otherPluginConnections.WildStacker;
import lpsapi.lpsapi.LPSAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

public final class LP extends JavaPlugin implements Listener, LevelPointsData {
    private LPSAPI lpapi = (LPSAPI) Bukkit.getPluginManager().getPlugin("LPSAPI");

    private Connection connection;

    public String host, database, username, password, table;
    public int port;
    public File boosterFile = new File(getDataFolder(), "Boosters.yml");
    public File LangFile = new File("plugins/LP/lang.yml");
    public File TopListFile = new File("plugins/LP/TopList.yml");
    public File ESFile = new File("plugins/LP/OtherSettings/EpicSpawners.yml");
    public File EXPFile = new File("plugins/LP/Settings/EXP.yml");
    public File LevelFile = new File("plugins/LP/Settings/Levels.yml");
    public File RewardsFile = new File("plugins/LP/Settings/Rewards.yml");
    public File WSFile = new File("plugins/LP/OtherSettings/WildStacker.yml");
    public File FileChanceFile = new File("plugins/LP/Settings/FileChance.yml");
    public File FormatsFile = new File("plugins/LP/Settings/Formats.yml");
    public FileConfiguration LangConfig = YamlConfiguration.loadConfiguration(LangFile);
    public FileConfiguration FileChanceConfig = YamlConfiguration.loadConfiguration(FileChanceFile);
    public FileConfiguration FormatsConfig = YamlConfiguration.loadConfiguration(FormatsFile);
    public FileConfiguration TopListConfig = YamlConfiguration.loadConfiguration(TopListFile);
    public FileConfiguration boosterConfig = YamlConfiguration.loadConfiguration(boosterFile);
    public int PlayerLevels;
    public String CommandName;

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
    public File userFolder;
    private HashMap<UUID, YamlConfiguration> usersConfig = new HashMap<>();

    private static LP instance;

    public LP() {
        instance = this;
    }

    public static LP getInstance() {
        return instance;
    }

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
        userFolder = new File(getDataFolder(), "Players");
        userFolder.mkdirs();


        try {
            this.getBoosterConfig().save(boosterFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.saveDefaultConfig();
        if (this.getConfig().getBoolean("EpicSpawners")) {
            getServer().getPluginManager().registerEvents(new EpicSpawners(this), this);
        } else if (this.getConfig().getBoolean("WildStacker")) {
            getServer().getPluginManager().registerEvents(new WildStacker(this), this);
        }



        this.getCommand("levelpoints").setExecutor((CommandExecutor) new LevelPoints(this));
        this.getCommand("lps").setExecutor((CommandExecutor) new LevelPoints(this));

        if (this.getConfig().getBoolean("UseSQL")) {
            mySQLSetup();
            getServer().getPluginManager().registerEvents(new mySQL(this), this);
        }
        MetricsLite metrics = new MetricsLite(this);

        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "=============================");
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "LevelPoints Plugin");
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "Developer: Zoon20X");
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "Version: " + this.getDescription().getVersion());
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "MC-Compatible: 1.8-1.13.2");
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "Config-Version: 1.2.3");
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Enabled");
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "=============================");


        versionChecker();


        boosterFile = new File(getDataFolder(), "Boosters.yml");
        LangFile = new File(getDataFolder(), "lang.yml");
        TopListFile = new File(getDataFolder(), "TopList.yml");
        LangFile = new File("plugins/LP/lang.yml");
        EXPFile = new File(getDataFolder(), "/Settings/EXP.yml");
        ESFile = new File(getDataFolder(), "/OtherSettings/EpicSpawners.yml");
        WSFile = new File(getDataFolder(), "/OtherSettings/WildStacker.yml");
        RewardsFile = new File(getDataFolder(), "/Settings/Rewards.yml");
        LevelFile = new File(getDataFolder(), "/Settings/Levels.yml");
        boosterConfig = YamlConfiguration.loadConfiguration(boosterFile);



        LangConfig = YamlConfiguration.loadConfiguration(LangFile);
        TopListConfig = YamlConfiguration.loadConfiguration(TopListFile);
        EXPConfig = YamlConfiguration.loadConfiguration(EXPFile);
        LevelConfig = YamlConfiguration.loadConfiguration(LevelFile);
        RewardsConfig = YamlConfiguration.loadConfiguration(RewardsFile);
        ESConfig = YamlConfiguration.loadConfiguration(ESFile);
        WSConfig = YamlConfiguration.loadConfiguration(WSFile);

        if(RewardsConfig.getString("Type").equals("FILECHANCE")){

            SaveLoadFiles(FileChanceFile, FileChanceConfig, "plugins/LP/Settings/FileChance.yml", "Settings/FileChance.yml", "FileChance");
            FileChanceFile = new File(getDataFolder(), "/Settings/FileChance.yml");
            FileChanceConfig = YamlConfiguration.loadConfiguration(FileChanceFile);
        }

        if(getConfig().getBoolean("LPSFormat")){

            SaveLoadFiles(FormatsFile, FormatsConfig, "plugins/LP/Settings/Formats.yml", "Settings/Formats.yml", "FileChance");
            FormatsFile = new File(getDataFolder(), "/Settings/Formats.yml");
            FormatsConfig = YamlConfiguration.loadConfiguration(FormatsFile);
        }

        reloadConfig();
        TimedEXP();


    }

    public void mySQLSetup() {
        host = this.getConfig().getString("host");
        port = this.getConfig().getInt("port");
        username = this.getConfig().getString("username");
        database = this.getConfig().getString("database");
        password = this.getConfig().getString("password");
        table = "playerData";

        try {

            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    if (!getConnection().isClosed()) {
                        getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "LevelPoints>> SQLDatabase already Connected :)");
                    }
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password));
                this.getServer().getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "MySQL Connected");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //sql.createTable();
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }



    public FileConfiguration getBoosterConfig() {
        return boosterConfig;
    }


    public File getBoosterFile() {
        return boosterFile;
    }

    private void versionChecker() {
        if (this.getConfig().getBoolean("CheckUpdates")) {
            this.getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "Checking for Updates...");
            String key = "key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=";
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=63996").openConnection();
                String version = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
                connection.disconnect();

                if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
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
                    if (this.getDescription().getVersion().equalsIgnoreCase(VersionJoin)) {
                        this.wait(2, player, ChatColor.GREEN + "No Updates For LevelPoints.");
                    } else {
                        this.wait(2, player, ChatColor.RED + "You do not have the most updated version of LevelPoints.");

                        this.wait(3, player, " ");
                        this.wait(4, player, ChatColor.RED + "Download it from: https://www.spigotmc.org/resources/levelpoints-1-8-1-13-2.63996/updates");
                        this.wait(5, player, ChatColor.DARK_AQUA + "Update version: " + VersionJoin);
                        this.wait(5, player, ChatColor.DARK_RED + "Version Your Running: " + this.getDescription().getVersion());
                    }
                }
            }
        }

    @EventHandler
    public void Leave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (this.getConfig().getBoolean("UseSQL")) {
            getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "Sending " + player.getName() + "Data to SQL");

            this.update_EXP(player.getUniqueId(), player);
            this.update_LEVEL(player.getUniqueId(), player);
            this.update_PRESTIGE(player.getUniqueId(), player);
            getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "Data Successfully send");
        }
    }

    public void saveFiles() {
        SaveLoadFiles(LangFile, LangConfig, "plugins/LP/lang.yml", "lang.yml", "Lang");
        SaveLoadFiles(TopListFile, TopListConfig, "plugins/LP/TopList.yml", "TopList.yml", "TopList");
        SaveLoadFiles(ESFile, ESConfig, "plugins/LP/OtherSettings/EpicSpawners.yml", "OtherSettings/EpicSpawners.yml", "EpicSpawners");
        SaveLoadFiles(EXPFile, EXPConfig, "plugins/LP/Settings/EXP.yml", "Settings/EXP.yml", "EXP");
        SaveLoadFiles(LevelFile, LevelConfig, "plugins/LP/Settings/Levels.yml", "Settings/Levels.yml", "Levels");
        SaveLoadFiles(RewardsFile, RewardsConfig, "plugins/LP/Settings/Rewards.yml", "Settings/Rewards.yml", "Rewards");
        SaveLoadFiles(WSFile, WSConfig, "plugins/LP/OtherSettings/WildStacker.yml", "OtherSettings/WildStacker.yml", "WildStacker");


    }

    public void CustomXP(Player player, int expamount, int left) throws IOException {

        if (expamount >= 1) {
            File userdata = new File(this.userFolder, player.getUniqueId() + ".yml");
            FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);


            int CustomMaxEXP = LevelConfig.getInt("Level-" + LevelConfig.getInt("MaxLevel"));
            int PrestigeMaxEXP = LevelConfig.getInt("Prestige-" + UsersConfig.getInt(player.getName() + ".Prestige") + ".Level-" + LevelConfig.getInt("MaxLevel"));


            level = UsersConfig.getInt(player.getName() + ".level");
            exp = UsersConfig.getInt(player.getName() + ".EXP.Amount");


            int boosteractiive = UsersConfig.getInt(player.getName() + ".EXP.Active");


            if (this.EXPConfig.getBoolean("Use-Booster")) {
                int ps = expamount;
                pts = ps * boosteractiive;
            } else {
                pts = expamount;
            }
            number = pts;
            if (left == 0) {
                exp = UsersConfig.getInt(player.getName() + ".EXP.Amount") + number;
            } else {
                exp = left;
            }
            if (LevelConfig.getBoolean("PrestigeLeveling")) {
                LEXP = LevelConfig.getInt("Prestige-" + UsersConfig.getInt(player.getName() + ".Prestige") + ".Level-" + UsersConfig.getInt(player.getName() + ".level"));
                if (PrestigeMaxEXP == 0) {
                    if (player.hasPermission("lp.admin")) {
                        player.sendMessage(API.format(Lang.getString("lpLevelCustomError")));
                    } else {
                        player.sendMessage(API.format(Lang.getString("lpLevelCustomErrorPlayer")));

                    }
                }
                if (UsersConfig.getInt(player.getName() + ".EXP.Amount") >= PrestigeMaxEXP) {
                    return;
                } else {
                    UsersConfig.set(player.getName() + ".EXP.Amount", exp);

                }

                take = LEXP;
            } else if (LevelConfig.getBoolean("CustomLeveling")) {

                LEXP = LevelConfig.getInt("Level-" + UsersConfig.getInt(player.getName() + ".level"));
                if (CustomMaxEXP == 0) {
                    if (player.hasPermission("lp.admin")) {
                        player.sendMessage(API.format(Lang.getString("lpLevelCustomError")));
                    } else {
                        player.sendMessage(API.format(Lang.getString("lpLevelCustomErrorPlayer")));

                    }
                }
                if (UsersConfig.getInt(player.getName() + ".EXP.Amount") >= CustomMaxEXP) {
                    return;
                } else {
                    UsersConfig.set(player.getName() + ".EXP.Amount", exp);
                }

                take = LEXP;
            } else {

                LEXP = LevelConfig.getInt("LevelingEXP");

                UsersConfig.set(player.getName() + ".EXP.Amount", exp);
                UsersConfig.save(userdata);
                take = level * LEXP;
            }


            UsersConfig.save(userdata);
            leftover = exp - take;
            if (LevelConfig.getBoolean("PrestigeLeveling")) {
                if (UsersConfig.getInt(player.getName() + ".EXP.Amount") >= this.LEXP) {

                    UsersConfig.set(player.getName() + ".level", Integer.valueOf(this.level + 1));
                    UsersConfig.set(player.getName() + ".EXP.Amount", Integer.valueOf(this.leftover));
                    UsersConfig.save(userdata);
                    LevelUp(player, 0, level);
                }
            } else if (LevelConfig.getBoolean("CustomLeveling")) {
                if (UsersConfig.getInt(player.getName() + ".EXP.Amount") >= this.LEXP) {

                    UsersConfig.set(player.getName() + ".level", Integer.valueOf(this.level + 1));
                    UsersConfig.set(player.getName() + ".EXP.Amount", Integer.valueOf(this.leftover));
                    UsersConfig.save(userdata);
                    LevelUp(player, 0, level);
                }
            } else {
                if (UsersConfig.getInt(player.getName() + ".EXP.Amount") >= this.LEXP * this.level) {

                    UsersConfig.set(player.getName() + ".level", Integer.valueOf(this.level + 1));
                    UsersConfig.set(player.getName() + ".EXP.Amount", Integer.valueOf(this.leftover));
                    UsersConfig.save(userdata);
                    LevelUp(player, 0, level);
                }
            }
            if (UsersConfig.getInt(player.getName() + ".level") == LPML) {

                if (UsersConfig.getInt(player.getName() + ".EXP.Amount") >= (LEXP * LPML)) {
                    player.sendMessage(ChatColor.DARK_GREEN + "You Have All The EXP You Need To Prestige.");
                    player.sendMessage(ChatColor.DARK_GREEN + " ");
                    player.sendMessage(ChatColor.DARK_GREEN + "Make Sure All Other Quests Are Completed before Prestiging.");
                }
            }

            int nlevelss = UsersConfig.getInt(player.getName() + ".level");
            String expsss = UsersConfig.getString(player.getName() + ".EXP.Amount");
            exps = UsersConfig.getInt(player.getName() + ".EXP.Amount");
            if (LevelConfig.getBoolean("PrestigeLeveling")) {
                needeps = LEXP;
            } else if (LevelConfig.getBoolean("CustomLeveling")) {
                needeps = LEXP;
            } else {
                needeps = nlevelss * LEXP;
            }
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
            if (UsersConfig.getBoolean(player.getName() + ".ActionBar")) {
                if (this.getConfig().getBoolean("Actionbar")) {
                    ActionBar(player, sb.toString() + " " + ChatColor.AQUA + expsss + "/" + needeps);
                }
            }
            try {
                UsersConfig.save(userdata);
            } catch (IOException e) {
                e.printStackTrace();
            }


            int lee = UsersConfig.getInt(player.getName() + ".EXP.Amount");
            int epse = take;

            if(this.getConfig().getBoolean("UseSQL")) {
                this.update_EXP(player.getUniqueId(), player);
                this.update_LEVEL(player.getUniqueId(), player);
                this.update_PRESTIGE(player.getUniqueId(), player);
            }

            if (lee > take) {
                CustomXP(player, lee, lee);
            } else {
                lee = 0;
            }
        }
    }

    public void update_EXP(UUID uuid, Player player) {
        File userdata = new File(userFolder, uuid + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);

        Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = getConnection().prepareStatement("UPDATE " + table + " SET EXP=? WHERE UUID=?");
                    statement.setString(1, String.valueOf(UsersConfig.getInt(player.getName() + ".EXP.Amount")));
                    statement.setString(2, uuid.toString());
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "Couldn't Update SQL Database");
                }
            }
        });


    }

    public void update_LEVEL(UUID uuid, Player player) {
        File userdata = new File(this.userFolder, uuid + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = getConnection().prepareStatement("UPDATE " + table + " SET LEVEL=? WHERE UUID=?");
                    statement.setString(1, String.valueOf(UsersConfig.getInt(player.getName() + ".level")));
                    statement.setString(2, uuid.toString());
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void getEXP(UUID uuid, String name) {
        File userdata = new File(this.userFolder, uuid + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM " + table + " WHERE UUID=?");
                    statement.setString(1, uuid.toString());
                    ResultSet results = statement.executeQuery();
                    results.next();
                    if (statement != null) {
                        UsersConfig.set(name + ".EXP.Amount", results.getInt("EXP"));

                        UsersConfig.save(userdata);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void getLevel(UUID uuid, String name) {
        File userdata = new File(this.userFolder, uuid + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM " + table + " WHERE UUID=?");
                    statement.setString(1, uuid.toString());
                    ResultSet results = statement.executeQuery();
                    results.next();
                    if (statement != null) {
                        UsersConfig.set(name + ".level", results.getInt("LEVEL"));
                        UsersConfig.save(userdata);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void getPrestige(UUID uuid, String name) {
        File userdata = new File(this.userFolder, uuid + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM " + table + " WHERE UUID=?");
                    statement.setString(1, uuid.toString());
                    ResultSet results = statement.executeQuery();
                    results.next();
                    if (statement != null) {
                        UsersConfig.set(name + ".Prestige", results.getInt("PRESTIGE"));
                        UsersConfig.save(userdata);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void update_PRESTIGE(UUID uuid, Player player) {
        File userdata = new File(this.userFolder, uuid + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = getConnection().prepareStatement("UPDATE " + table + " SET PRESTIGE=? WHERE UUID=?");
                    statement.setString(1, String.valueOf(UsersConfig.getInt(player.getName() + ".Prestige")));
                    statement.setString(2, uuid.toString());
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void LevelUp(Player player, int expamount, int level) throws IOException {
        int levels = level;
        if (RewardsConfig.getString("Type").equals("REGULAR")) {
            List<String> cmds = RewardsConfig.getStringList(API.format("Rewards.Level-" + (level + 1)));
            for (String command : cmds) {
                if (RewardsConfig.getString("RewardsMethod").equals("NONE")) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                } else if (RewardsConfig.getString("RewardsMethod").equals("MESSAGE")) {
                    player.sendMessage(API.format(Lang.getString("lpRewardMessage")));
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                } else if (RewardsConfig.getString("RewardsMethod").equals("TITLE")) {
                    Title(player, API.format(Lang.getString(API.format("lpRewardTitleTop"))), API.format(Lang.getString(API.format("lpRewardTitleBottom")).replace("{lp_level}", String.valueOf(levels))));
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                }
            }
        } else if (RewardsConfig.getString("Type").equals("ONE")) {
            List<String> cmds = RewardsConfig.getStringList("Rewards." + "CMD");
            for (String command : cmds) {
                if (RewardsConfig.getString("RewardsMethod").equals("NONE")) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                } else if (RewardsConfig.getString("RewardsMethod").equals("MESSAGE")) {
                    player.sendMessage(API.format(Lang.getString("lpRewardMessage")));
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                } else if (RewardsConfig.getString("RewardsMethod").equals("TITLE")) {
                    Title(player, API.format(Lang.getString(API.format("lpRewardTitleTop"))), API.format(Lang.getString(API.format("lpRewardTitleBottom")).replace("{lp_level}", String.valueOf(levels))));
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                }
            }
        } else if (RewardsConfig.getString("Type").equals("CHANCE")) {
            int max = RewardsConfig.getInt("Amount");
            int min = 1;

            Random r = new Random();
            int re = r.nextInt((max - min) + 1) + min;
            List<String> cmds = RewardsConfig.getStringList("Rewards.Level-" + (level + 1) + "." + String.valueOf(re));
            for (String command : cmds) {
                if (RewardsConfig.getString("RewardsMethod").equals("NONE")) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                } else if (RewardsConfig.getString("RewardsMethod").equals("MESSAGE")) {
                    player.sendMessage(API.format(Lang.getString("lpRewardMessage")));
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                } else if (RewardsConfig.getString("RewardsMethod").equals("TITLE")) {
                    Title(player, API.format(Lang.getString(API.format("lpRewardTitleTop"))), API.format(Lang.getString(API.format("lpRewardTitleBottom")).replace("{lp_level}", String.valueOf(levels))));
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                }
            }
        }else if (RewardsConfig.getString("Type").equals("FILECHANCE")) {
            List<String> levelrequired = RewardsConfig.getStringList("Rewards.LevelChance");
            for(String levelss : levelrequired) {

                if (levelss.equals(String.valueOf(level))) {
                    List<String> RewardType = FileChanceConfig.getStringList("RewardsType");

                    int max = RewardType.size() - 1;
                    int min = 0;

                    Random r = new Random();
                    int re = r.nextInt((max - min) + 1) + min;
                    String ReType = RewardType.get(re);
                    List<String> Cmds = FileChanceConfig.getStringList("Rewards." + ReType);

                    for (String command : Cmds) {
                        if (RewardsConfig.getString("RewardsMethod").equals("NONE")) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                        } else if (RewardsConfig.getString("RewardsMethod").equals("MESSAGE")) {
                            player.sendMessage(API.format(Lang.getString("lpRewardMessage")));
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                        } else if (RewardsConfig.getString("RewardsMethod").equals("TITLE")) {
                            Title(player, API.format(Lang.getString("lpRewardTitleTop")), API.format(Lang.getString("lpRewardTitleBottom").replace("{lp_level}", String.valueOf(levels))));
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                        }
                    }
                }
            }
        }
        this.TopListConfig.set(player.getName() + ".level", level + 1);
        this.TopListConfig.save(this.TopListFile);

           if(lpapi != null){
             lpapi.LevelUpEventTrigger(player.getPlayer(), levels);
          }
    }


    @Override
    public boolean SQLQuery(Player player) {
        boolean updated;
        if (this.getConfig().getBoolean("UseSQL")) {
            this.update_EXP(player.getUniqueId(), player);
            this.update_LEVEL(player.getUniqueId(), player);
            this.update_PRESTIGE(player.getUniqueId(), player);
            updated = true;
        }else{
            updated = false;
        }
        return updated;
    }

    @Override
    public void SaveLoadFiles(File file, FileConfiguration config, String Location, String secLoc, String Name) {
        if (file == null) {
            file = new File(this.getDataFolder() + Location);
            config = YamlConfiguration.loadConfiguration(file);
            getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "VenturesCraft>> Loading Module File " + Name + ".yml");
        }

        if (!file.exists()) {
            getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "VenturesCraft>> Creating Module File " + Name + ".yml");
            this.saveResource(secLoc, false);
        }
    }

    @Override
    public void TimedEXP() {
        if (EXPConfig.getBoolean("TimedEXP")) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                public void run() {
                    if (Bukkit.getServer().getOnlinePlayers().size() >= 1) {
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                            try {
                                CustomXP(p, EXPConfig.getInt("GiveAmount"), 0);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            double seconds = EXPConfig.getInt("GiveEXP");
                            double minutes = 0;
                            double hours = 0;
                            if (seconds >= 60) {
                                minutes = seconds / 60;
                                seconds = seconds - (minutes * 60);
                            }
                            if (minutes >= 60) {
                                hours = minutes / 60;
                                minutes = minutes - (hours * 60);
                            }
                            String TimeMessage;
                            if (hours >= 1) {
                                TimeMessage = hours + " Hours";
                            } else if (minutes >= 1) {
                                TimeMessage = (minutes + " Minute(s)");
                            } else {
                                TimeMessage = seconds + " Seconds";
                            }


                            p.sendMessage(API.format(LangConfig.getString("lpTimedReward").replace("{EXP_Timed_Amount}", Integer.toString(EXPConfig.getInt("GiveAmount"))).replace("{EXP_Timed_Delay}", TimeMessage)));
                        }

                    }
                }
            }, 0L, EXPConfig.getInt("GiveEXP")*20L);
        }
    }

    @Override
    public void ActionBar(Player player, String Message) {



        if (!Bukkit.getVersion().contains("1.8")) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Message));
        }

    }

    @Override
    public void Title(Player player, String Title, String Subtitle) {
        if (!Bukkit.getVersion().contains("1.8")) {
            player.sendTitle(Title, Subtitle, 10, 20, 10);
        }
    }

    public void PlayerAdd(UUID UUID, String Name) {
        String PlayerFolder = getDataFolder() + "/Players/";




        File userFile = new File(userFolder, UUID + ".yml");
        if (!userFile.exists()) {
            try {
                userFile.createNewFile();
            } catch (IOException ex) {
                getLogger().log(Level.SEVERE, ChatColor.DARK_RED + "Can't create " + Name + " user file");
            }
        }
        usersConfig.put(UUID, YamlConfiguration.loadConfiguration(userFile));
    }

    public void PlayerDataLoad(Player player) throws IOException {


        UUID UUID = player.getUniqueId();


        File userdata = new File(userFolder, UUID + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);

        if (!UsersConfig.contains("Name")) {

            getServer().getConsoleSender().sendMessage(API.format("&3Creating Player Data within file"));
            UsersConfig.set("Name", player.getName());

            UsersConfig.set(player.getName() + ".level", this.LevelConfig.getInt("Level"));
            UsersConfig.set(player.getName() + ".EXP.Amount", this.LevelConfig.getInt("Exp"));
            UsersConfig.set(player.getName() + ".Prestige", 0);;
            UsersConfig.set(player.getName() + ".EXP.Boost2x", 0);
            UsersConfig.set(player.getName() + ".EXP.Boost3x", 0);
            UsersConfig.set(player.getName() + ".EXP.Boost4x", 0);
            UsersConfig.set(player.getName() + ".EXP.Boost5x", 0);
            UsersConfig.set(player.getName() + ".EXP.Boost6x", 0);
            UsersConfig.set(player.getName() + ".EXP.Boost7x", 0);
            UsersConfig.set(player.getName() + ".EXP.Boost8x", 0);
            UsersConfig.set(player.getName() + ".EXP.Boost9x", 0);
            UsersConfig.set(player.getName() + ".EXP.Boost10x", 0);
            UsersConfig.set(player.getName() + ".EXP.Active", 1);
            UsersConfig.set(player.getName() + ".ActionBar", true);
            UsersConfig.save(userdata);

        } else {
            getServer().getConsoleSender().sendMessage(API.format("&3Loading Player Data"));
            if (!UsersConfig.getString("Name").equals(player.getName())) {
                getServer().getConsoleSender().sendMessage(API.format("&4Found a new username"));
                UsersConfig.set("Name", player.getName());

                UsersConfig.save(userdata);
            }

            getServer().getConsoleSender().sendMessage(API.format("&bLoaded Data Successfully"));
        }


    }
}