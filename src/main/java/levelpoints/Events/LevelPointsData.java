package levelpoints.Events;

import org.bukkit.entity.Player;

public interface LevelPointsData {

    boolean reconnectSQL();

    boolean SQLQuery(Player player);

}
