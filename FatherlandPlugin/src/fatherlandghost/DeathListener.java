package fatherlandghost;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.awt.*;
import java.util.Objects;

public class DeathListener implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Player killer = p.getKiller();
        Objective obj = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().getObjective("deaths");
        assert obj != null;
        Score score = obj.getScore(p.getName());
        if(killer != null) {
            if(score.getScore() > 1) {
                score.setScore(score.getScore() - 1);
                Main.UnGhost(p);
            } else if (score.getScore() == 1 || score.getScore() <= 0 && score.getScore() != -10) score.setScore(-1);
        }
        if (score.getScore() == -10) {
            score.setScore(5);
            e.setDeathMessage(p.getName() + " has been revived");
            Main.UnGhost(p);
        }
        if (score.getScore() == -9) {
            score.setScore(5);
            e.setDeathMessage(p.getName() + " died from cringe");
        }
        if(score.getScore() == -8) {
            score.setScore(-1);
            e.setDeathMessage(p.getName() + " has been " + ChatColor.of(Color.RED) + "sacrificed");
        }
    }
}
