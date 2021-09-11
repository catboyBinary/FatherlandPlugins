package fatherlandghost;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.Objects;

public class RespawnEvent implements Listener {
    @EventHandler
    public void onRespawn (PlayerRespawnEvent e) {
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), task -> {
            Player p = e.getPlayer();
            Objective obj = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().getObjective("deaths");
            assert obj != null;
            Score score = obj.getScore(p.getName());
            if (score.getScore() == -1) Main.SetGhost(p);
        }, 5L);
    }
}
