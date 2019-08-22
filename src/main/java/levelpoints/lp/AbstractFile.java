package levelpoints.lp;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class AbstractFile {

    protected  LP lp;
    private File file;
    private FileConfiguration config;

    public AbstractFile(LP lp, String fileName)
    {
        this.file = new File(lp.getDataFolder(), fileName);
        if(!file.exists())
        {
            try{
                file.createNewFile();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);

    }

    public void save()
    {
        try{
            config.save(file);
        } catch (IOException e){
            e.printStackTrace();
        }

    }
    
}
