package fatherlandghost;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.Objects;

public class OnQuit implements Listener {
    @EventHandler
    public void Quit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Objective obj = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().getObjective("deaths");
        assert obj != null;
        Score score = obj.getScore(p.getName());
        if(score.getScore() == -1) e.setQuitMessage("");
    }
}
