package fatherlandghost;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.Objects;

public class IDGAF_Event implements Listener {
    @EventHandler
    public void IDK(EntityTargetLivingEntityEvent e) {
        Objective obj = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().getObjective("deaths");
        if(e.getTarget() instanceof Player) {
            Player p = (Player) e.getTarget();
            assert obj != null;
            Score score = obj.getScore(p.getName());
            if(score.getScore() == -1) e.setCancelled(true);
        }
    }
}
