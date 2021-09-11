package fatherlandghost;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import java.util.Objects;
import java.util.Random;

public class Main extends JavaPlugin {
    private static Main instance;
    public Main() {
        instance = this;
    }
    public static Main getInstance(){
        return instance;
    }
    public int secretKey;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new OnJoin(), this);
        getServer().getPluginManager().registerEvents(new OnQuit(), this);
        getServer().getPluginManager().registerEvents(new DamageTaken(), this);
        getServer().getPluginManager().registerEvents(new IDGAF_Event(), this);
        getServer().getPluginManager().registerEvents(new RespawnEvent(), this);
        Objects.requireNonNull(getCommand("canonlives")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("revive")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("reviveall")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("cringe")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("hug")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("hugs")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("kiss")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("sk")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("sacrifice")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("rotatemirror")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("test")).setExecutor(new Commands());
        getLogger().info("Fatherland Ghost Plugin has enabled!");
    }

    @Override
    public void onDisable() {
    }


    public static void SetGhost(Player p) {
        p.setAllowFlight(true);
        p.setFlying(true);
        p.setSleepingIgnored(true);
        p.setFlySpeed(0.04f);
        p.setWalkSpeed(0.15f);
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 1000000, 0, false, false));
        p.setExhaustion(-1f);
        p.setCollidable(false);
        p.setInvisible(true);
        Team ghosts = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().getTeam("ghosts");
        assert ghosts != null;
        ghosts.addEntry(p.getName());
    }
    
    public static void UnGhost(Player p){
        if(p.getGameMode()== GameMode.SURVIVAL){
            p.setAllowFlight(false);
            p.setFlying(false);
        }
        p.setSleepingIgnored(false);
        p.setFlySpeed(0.1f);
        p.setWalkSpeed(0.2f);
        p.setExhaustion(0.1f);
        p.setCollidable(true);
        p.setInvisible(false);
        p.removePotionEffect(PotionEffectType.SLOW_FALLING);
        Team ghosts = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("ghosts");
        ghosts.removeEntry(p.getName());
    }

    public int SetRandomKey(){
        this.secretKey = new Random().nextInt(2147483647);
        return this.secretKey;
    }
}
