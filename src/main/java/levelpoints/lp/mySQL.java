package levelpoints.lp;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class mySQL implements Listener {
    private Plugin plugin = LP.getPlugin(LP.class);
    private LP lp = LP.getPlugin(LP.class);

    public mySQL(LP lp) {
    }


    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent event){

        createTable();
        createPlayer(event.getUniqueId(), event.getName());


    }
    public boolean playerExists(UUID uuid){
        try {
            PreparedStatement statement = lp.getConnection().prepareStatement("SELECT * FROM " + lp.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            if(results.next()){
                return true;
            }
            plugin.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "Player Not Found");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createPlayer(final UUID uuid, String name){
        File userdata = new File(lp.userFolder, uuid + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        try{
            PreparedStatement statement = lp.getConnection().prepareStatement("SELECT * FROM " + lp.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            if(playerExists(uuid) != true){
                PreparedStatement insert = lp.getConnection().prepareStatement("INSERT INTO " + lp.table + " (UUID,NAME,LEVEL,EXP,PRESTIGE) VALUE (?,?,?,?,?)");
                insert.setString(1, uuid.toString());
                insert.setString(2, name);
                insert.setString(3, String.valueOf(UsersConfig.getInt(name + ".level")));
                insert.setString(4, String.valueOf(UsersConfig.getInt(name + ".EXP.Amount")));
                insert.setString(5, String.valueOf(UsersConfig.getInt(name + ".Prestige")));
                insert.executeUpdate();

                lp.getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "Player Added to Database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        try {


            Statement statement = lp.getConnection().createStatement();

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `playerData` (`UUID` varchar(200), `NAME` varchar(200), `LEVEL` INT(10), EXP INT(10), PRESTIGE INT(10))");

            //ResultSet res = statement.executeQuery("");
            //res.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





}
