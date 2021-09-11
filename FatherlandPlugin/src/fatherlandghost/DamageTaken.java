package fatherlandghost;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.Objects;

public class DamageTaken implements Listener {
    @EventHandler
    public void onTake(EntityDamageEvent e) {
        Objective obj = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().getObjective("deaths");
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            assert obj != null;
            Score score = obj.getScore(p.getName());
            if(score.getScore() == -1) e.setCancelled(true);
        }
        if(e instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent d = (EntityDamageByEntityEvent) e;
            if (d.getDamager() instanceof Player) {
                Player p = (Player) d.getDamager();
                assert obj != null;
                Score score = obj.getScore(p.getName());
                if(score.getScore() == -1) d.setCancelled(true);
            } else if (d.getDamager() instanceof Arrow) {
                Arrow a = (Arrow) d.getDamager();
                if (a.getShooter() instanceof Player) {
                    Player p = (Player) a.getShooter();
                    assert obj != null;
                    Score score = obj.getScore(p.getName());
                    if(score.getScore() == -1) d.setCancelled(true);
                }
            } else if (d.getDamager() instanceof TNTPrimed) {
                TNTPrimed t = (TNTPrimed) d.getDamager();
                if (t.getSource() instanceof Player) {
                    Player p = (Player) t.getSource();
                    assert obj != null;
                    Score score = obj.getScore(p.getName());
                    if(score.getScore() == -1) d.setCancelled(true);
                }
            } else if (d.getDamager() instanceof Trident) {
                Trident t = (Trident) d.getDamager();
                if (t.getShooter() instanceof Player) {
                    Player p = (Player) t.getShooter();
                    assert obj != null;
                    Score score = obj.getScore(p.getName());
                    if(score.getScore() == -1) d.setCancelled(true);
                }
            } else if (d.getDamager() instanceof Firework) {
                Firework f = (Firework) d.getDamager();
                if (f.getShooter() instanceof Player) {
                    Player p = (Player) f.getShooter();
                    assert obj != null;
                    Score score = obj.getScore(p.getName());
                    if(score.getScore() == -1) d.setCancelled(true);
                }
            }
        }
    }
}
