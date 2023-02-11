package cc.stormworth.meetup.util.file;

import cc.stormworth.meetup.util.Colors;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Taken from the aUHC plugin (Created by StrongTino and Ryzeon)
 */

public class ConfigFile extends YamlConfiguration {

    private final File file;

    public ConfigFile(JavaPlugin plugin, String name) {
        this.file = new File(plugin.getDataFolder(), name);
        if (!this.file.exists())
            plugin.saveResource(name, false);
        try {
            load(this.file);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return this.file;
    }

    public void save() {
        try {
            save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public int getInt(String path) {
        return getInt(path, 0);
    }

    public double getDouble(String path) {
        return getDouble(path, 0.0D);
    }

    public boolean getBoolean(String path) {
        return getBoolean(path, false);
    }

    public String getString(String path) {
        return Colors.translate(getString(path, ""));
    }

    public List<String> getStringList(String path) {
        return super.getStringList(path).stream().map(Colors::translate).collect(Collectors.toList());
    }
}
