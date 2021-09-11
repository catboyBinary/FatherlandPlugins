package fwep;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private static Main instance;
    public Main() {
        instance = this;
    }
    public static Main getInstance(){
        return instance;
    }
    @Override
    public void onEnable() {
        getLogger().info("FWEP enabled");
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
    }

    @Override
    public void onDisable() {
    }
}
