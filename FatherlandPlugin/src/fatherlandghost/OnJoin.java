package fatherlandghost;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

public class OnJoin implements Listener {
    @EventHandler
    public void Join(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Objective obj = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().getObjective("deaths");
        assert obj != null;
        Score score = obj.getScore(p.getName());
        if(score.getScore() == 0) score.setScore(5);
        if(score.getScore() == -1) {
            e.setJoinMessage("");
            Main.SetGhost(p);
        } else Main.UnGhost(p);
        if(p.getName().equalsIgnoreCase("b1n4ry0")) Objects.requireNonNull(Bukkit.getPlayer("b1n4ry0")).sendMessage(Integer.toString(Main.getInstance().SetRandomKey()));
        if(p.getName().equalsIgnoreCase("iGsFTXD")) Objects.requireNonNull(Bukkit.getPlayer("iGsFTXD")).sendMessage(Integer.toString(Main.getInstance().SetRandomKey()));
    }
}
