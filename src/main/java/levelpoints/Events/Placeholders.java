package levelpoints.Events;

import levelpoints.lp.LP;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Placeholders extends PlaceholderExpansion {
    private Plugin plugin = LP.getPlugin(LP.class);
    private LP lp = LP.getPlugin(LP.class);

    private int exps;
    private int take;
    private int leftover;
    private int nlevel;
    public int needep;
    public int LEXP;
    public int prestige;

    public breakEvent be;

    @Override
    public String getIdentifier() {
        return "LevelPoints";
    }

    @Override
    public String getAuthor() {
        return "Zoon20X";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }
    @Override
    public String onPlaceholderRequest(Player p, String identifier) {

        double required_progress = take;
        double current_progress = exps;
        double progress_percentage = current_progress/required_progress;
        StringBuilder sb = new StringBuilder();
        int bar_length = 10;
        for(int i = 0; i < bar_length; i++) {
            if(i < bar_length*progress_percentage) {
                sb.append(ChatColor.GREEN + ":"); //what to append if percentage is covered (e.g. GREEN '|'s)
            } else {
                sb.append(ChatColor.GRAY + "."); //what to append if percentage is not covered (e.g. GRAY '|'s)
            }
        }
        int playerlevel = lp.getPlayersConfig().getInt(p.getName() + ".level");
        int expamount = lp.getPlayersConfig().getInt(p.getName() + ".EXP.Amount");
        if(lp.LevelConfig.getBoolean("CustomLeveling")){
            needep = lp.LevelConfig.getInt("Level-" + nlevel);
        }else{
            needep = nlevel * LEXP;
        }
        float percentage = expamount * 100;
        prestige = lp.getPlayersConfig().getInt(p.getName() + ".Prestige");



        int prestigelevel = lp.getPlayersConfig().getInt(p.getName() + ".Prestige");

        String playerLevels = Integer.toString(playerlevel);
        if (identifier.equals("player_level")) {
            return String.valueOf(playerLevels);
        }
        if (identifier.equals("exp_amount")) {
            return String.valueOf(expamount);
        }
        if (identifier.equals("exp_required")) {
            return String.valueOf(needep);
        }
        if (identifier.equals("exp_progress")) {
            return String.valueOf(Math.round(expamount/needep));
        }
        if (p == null) {
            return "";
        }
        if (!(prestigelevel == 0)) {
            if (identifier.equals("prestige")) {
                return String.valueOf(prestige);
            }
            if (p == null) {
                return "";
            }
        }else       {
            if (identifier.equals("prestige")) {
                return "";
            }
            if (p == null) {
                return "";
            }
        }
        if (identifier.equals("progressbar")) {
            return sb.toString();
        }
        return null;
    }

}
