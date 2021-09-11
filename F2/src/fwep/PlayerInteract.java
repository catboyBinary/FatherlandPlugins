package fwep;
import net.minecraft.server.v1_16_R3.SoundEffectType;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.awt.image.ColorModel;
import java.util.*;
public class PlayerInteract implements Listener {
    HashMap<String, Long> cooldowns = new HashMap<>();
    HashMap<String, Integer> combo = new HashMap<>();
    HashMap<String, Integer> freedomUltra = new HashMap<>();
    HashMap<String, String> prikol = new HashMap<>();
    public void addCD(Player p, int milliseconds, String weapon) {
        cooldowns.put(p.getName()+weapon, System.currentTimeMillis() + milliseconds);
    }
    public void addCustom(Player p, String variable, Long value) {
        cooldowns.put(p.getName()+variable, value);
    }
    public long getCustom(Player p, String variable) {
        return cooldowns.get(p.getName()+variable);
    }
    public boolean isCooling(Player p, String weapon) {
        if(cooldowns.containsKey(p.getName()+weapon)) {
            return cooldowns.get(p.getName()+weapon) > System.currentTimeMillis();
        } else return false;
    }
    public String getTag(ItemStack item) {
        ItemMeta m;
        NamespacedKey k;
        m = item.getItemMeta();
        if (m.getPersistentDataContainer().getKeys().toArray().length != 0) {
            k = (NamespacedKey) m.getPersistentDataContainer().getKeys().toArray()[0];
            return m.getCustomTagContainer().getCustomTag(k, ItemTagType.STRING);
        }
        return "";
    }
    @EventHandler
    public void Click(PlayerInteractEvent e) {
        Action a = e.getAction();
        Player p = e.getPlayer();
        String tag;
        ItemMeta m;
        HashSet<Material> transparent = new HashSet<>();
        transparent.add(Material.GLASS);
        transparent.add(Material.WATER);
        transparent.add(Material.LAVA);
        transparent.add(Material.AIR);
        transparent.add(Material.CAVE_AIR);
        transparent.add(Material.VOID_AIR);
        transparent.add(Material.GLASS_PANE);
        transparent.add(Material.BARRIER);
        ArrayList<Material> restricted = new ArrayList<>();
        restricted.add(Material.CHEST);
        restricted.add(Material.TRAPPED_CHEST);
        restricted.add(Material.FURNACE);
        restricted.add(Material.BLAST_FURNACE);
        restricted.add(Material.BARREL);
        restricted.add(Material.DARK_OAK_DOOR);
        restricted.add(Material.ACACIA_DOOR);
        restricted.add(Material.BIRCH_DOOR);
        restricted.add(Material.CRIMSON_DOOR);
        restricted.add(Material.IRON_DOOR);
        restricted.add(Material.JUNGLE_DOOR);
        restricted.add(Material.OAK_DOOR);
        restricted.add(Material.SPRUCE_DOOR);
        restricted.add(Material.WARPED_DOOR);
        restricted.add(Material.SPAWNER);
        //Main Hand
        if(p.getInventory().getItemInMainHand().hasItemMeta()) {
            m = p.getInventory().getItemInMainHand().getItemMeta();
            tag = getTag(p.getInventory().getItemInMainHand());
            if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
                if (tag.equalsIgnoreCase("Durandal")) {
                    if (!isCooling(p, "durandal")) {
                        addCD(p, 8000, "durandal");
                        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BELL_RESONATE, 1, 2);
                        p.addPotionEffect(PotionEffectType.SLOW.createEffect(18, 255));
                        p.addPotionEffect(PotionEffectType.JUMP.createEffect(18, 252));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Location loc = p.getEyeLocation();
                                loc.add(loc.getDirection().multiply(3.5f));
                                final int[] i = {4};
                                float x;
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        //p.getWorld().spawnParticle(Particle.SWEEP_ATTACK, loc, 1, 0.2f, 0.2f, 0.2f, 0.1f);
                                        Random a = new Random();
                                        float b = (a.nextFloat() - 0.5f) * 3;
                                        float c = (a.nextFloat() - 0.5f) * 3;
                                        float d = (a.nextFloat() - 0.5f) * 3;
                                        float h = (a.nextFloat() - 0.5f) / 40;
                                        Particle.DustOptions j = new Particle.DustOptions(Color.fromRGB(100, 0, 200), 0.5f);
                                        Location first = loc.clone().add(b, c, d);
                                        for (int i = 110; i > 0; i--) {
                                            p.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, first.add(b / -50 + h, c / -50 + h, d / -50 + h), 1, 0, 0, 0, 0);
                                            p.getWorld().spawnParticle(Particle.REDSTONE, first.add(b / -50 + h, c / -50 + h, d / -50 + h), 1, 0, 0, 0, 0, j);
                                        }
                                        p.getWorld().playSound(p.getLocation(), Sound.ITEM_TRIDENT_THROW, 1, 1.5f);
                                        if (i[0] > 0) {
                                            i[0]--;
                                        } else {
                                            for (LivingEntity e : Objects.requireNonNull(loc.getWorld()).getLivingEntities()) {
                                                if (loc.distance(e.getLocation()) <= 3.5f && e != p) {
                                                    e.damage(25, p);
                                                    Vector vel = loc.getDirection().multiply(new Vector(1.25f, 0, 1.25f)).add(new Vector(0, 0.5f, 0));
                                                    e.setVelocity(vel);
                                                    if (e instanceof Player) {
                                                        Player f = (Player) e;
                                                        if (f.getName().equalsIgnoreCase("Манекен")) {
                                                            p.sendTitle("", "25.0 Attack Damage", 5, 10, 5);
                                                        }
                                                    }
                                                }
                                            }
                                            p.getWorld().spawnParticle(Particle.FLASH, loc, 1, 0, 0, 0, 0.1f);
                                            this.cancel();
                                        }
                                    }
                                }.runTaskTimer(Main.getInstance(), 0L, 1L);
                            }
                        }.runTaskLaterAsynchronously(Main.getInstance(), 18L);
                    }
                }
                if (tag.equalsIgnoreCase("Freedom")) {
                    if (!isCooling(p, "freedom")) {
                        p.swingMainHand();
                        addCD(p, 15500, "freedom");
                        freedomUltra.put(p.getName(), 4);
                        Location loc = p.getLocation().add(0, 0.2f, 0);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (freedomUltra.get(p.getName()) > 0) {
                                    p.getWorld().playSound(loc, Sound.BLOCK_BEACON_ACTIVATE, 1, 1.6f);
                                    Particle.DustOptions b = new Particle.DustOptions(Color.fromRGB(13, 255, 177), 1.5f);
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            for (LivingEntity e : Objects.requireNonNull(loc.getWorld()).getLivingEntities()) {
                                                if (loc.distance(e.getLocation()) <= 5f && e != p) {
                                                    e.damage(4, p);
                                                    if (e instanceof Player) {
                                                        Player f = (Player) e;
                                                        if (f.getName().equalsIgnoreCase("Манекен")) {
                                                            p.sendTitle("", "4.0 Attack Damage", 5, 10, 5);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }.runTask(Main.getInstance());
                                    for (int iter = 0; iter < 360; iter++) {
                                        double xCoord = (5 * Math.cos(iter));
                                        double yCoord = (5 * Math.sin(iter));
                                        for (float prikol = 0; prikol < 2; prikol += 0.25f) {
                                            p.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + xCoord, loc.getY() + prikol, loc.getZ() + yCoord, 1, 0, 0, 0, 1f, b);
                                        }
                                    }

                                    final float[] range = {0.4f};
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            Particle.DustOptions b = new Particle.DustOptions(Color.fromRGB(13, 255, 177), 1f);
                                            if (range[0] < 5) {
                                                for (int iter = 0; iter < 360; iter++) {
                                                    double xCoord = (range[0] * Math.cos(iter));
                                                    double yCoord = (range[0] * Math.sin(iter));
                                                    p.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + xCoord, loc.getY(), loc.getZ() + yCoord, 1, 0, 0, 0, 1f, b);
                                                }
                                                range[0] += 0.2f;
                                            } else this.cancel();
                                        }
                                    }.runTaskTimerAsynchronously(Main.getInstance(), 0L, 0L);

                                    p.getWorld().spawnParticle(Particle.REDSTONE, loc, 100, 4, 1, 4, 1f, b);
                                    freedomUltra.put(p.getName(), freedomUltra.get(p.getName()) - 1);
                                } else {
                                    p.getWorld().playSound(loc, Sound.BLOCK_BEACON_ACTIVATE, 1, 2f);
                                    Particle.DustOptions b = new Particle.DustOptions(Color.fromRGB(238, 50, 111), 1.5f);
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            for (LivingEntity e : Objects.requireNonNull(loc.getWorld()).getLivingEntities()) {
                                                if (loc.distance(e.getLocation()) <= 5f && e != p) {
                                                    e.damage(6, p);
                                                    if (e instanceof Player) {
                                                        Player f = (Player) e;
                                                        if (f.getName().equalsIgnoreCase("Манекен")) {
                                                            p.sendTitle("", "6.0 Attack Damage", 5, 10, 5);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }.runTask(Main.getInstance());
                                    for (int iter = 0; iter < 360; iter++) {
                                        double xCoord = (5 * Math.cos(iter));
                                        double yCoord = (5 * Math.sin(iter));
                                        for (float prikol = 0; prikol < 2; prikol += 0.25f) {
                                            p.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + xCoord, loc.getY() + prikol, loc.getZ() + yCoord, 1, 0, 0, 0, 1f, b);
                                        }
                                    }
                                    final float[] range = {0.4f};
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            Particle.DustOptions b = new Particle.DustOptions(Color.fromRGB(238, 50, 111), 1f);
                                            if (range[0] < 5) {
                                                for (int iter = 0; iter < 360; iter++) {

                                                    double xCoord = (range[0] * Math.cos(iter));
                                                    double yCoord = (range[0] * Math.sin(iter));
                                                    p.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + xCoord, loc.getY(), loc.getZ() + yCoord, 1, 0, 0, 0, 1f, b);
                                                }
                                                range[0] += 0.2f;

                                            } else this.cancel();
                                        }
                                    }.runTaskTimerAsynchronously(Main.getInstance(), 0L, 0L);
                                    p.getWorld().spawnParticle(Particle.REDSTONE, loc, 200, 4, 1, 4, 1f, b);
                                    this.cancel();
                                }
                            }
                        }.runTaskTimerAsynchronously(Main.getInstance(), 0L, 30L);
                    }
                }
                if (tag.equalsIgnoreCase("ADivinity")) {
                    if (!isCooling(p, "adivinity")) {
                        addCD(p, 3000, "adivinity");
                        p.swingMainHand();
                        Location loc = p.getEyeLocation().add(p.getLocation().getDirection().multiply(18));
                        if (!p.getTargetBlock(transparent, 15).isEmpty())
                            loc = p.getTargetBlock(transparent, 15).getLocation().add(0.5f, 0.5f, 0.5f);
                        for (Entity e1 : p.getNearbyEntities(14, 14, 14)) {
                            if (e1 instanceof LivingEntity) {
                                if (getLookingAt(p, (LivingEntity) e1)) {
                                    LivingEntity e2 = (LivingEntity) e1;
                                    loc = e2.getEyeLocation();
                                }
                            }
                        }
                        Location finalLoc = loc.clone().add(0, 25, 0);
                        Location finalLoc2 = loc;
                        final int[] i = {8};
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (i[0] > 0) {
                                    i[0]--;
                                    FallingBlock f = p.getWorld().spawnFallingBlock(finalLoc.clone().add(Math.random() * 10 - 5, 0, Math.random() * 10 - 5), p.getServer().createBlockData(Material.END_STONE));
                                    f.setVelocity(new Vector(0, -0.5, 0));
                                    f.setHurtEntities(false);
                                    f.setDropItem(false);
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            if (f.isOnGround()) {
                                                if (f.getLocation().getBlock().getType() == Material.END_STONE) {
                                                    f.getWorld().getBlockAt(f.getLocation()).setType(Material.AIR);
                                                }
                                                f.getWorld().spawnParticle(Particle.END_ROD, f.getLocation(), 100, 0, 0, 0, 0.1f);
                                                f.getWorld().playSound(f.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1, 1);
                                                for (LivingEntity e : Objects.requireNonNull(f.getWorld()).getLivingEntities()) {
                                                    if (f.getLocation().distance(e.getLocation()) <= 3.5f && e != p) {
                                                        e.damage(15, p);
                                                        if (e instanceof Player) {
                                                            Player f = (Player) e;
                                                            if (f.getName().equalsIgnoreCase("Манекен")) {
                                                                p.sendTitle("", "15.0 Attack Damage", 5, 10, 5);
                                                            }
                                                        }
                                                    }
                                                }
                                                this.cancel();
                                            }
                                        }
                                    }.runTaskTimer(Main.getInstance(), 0L, 0L);
                                } else this.cancel();
                            }
                        }.runTaskTimer(Main.getInstance(), 0L, 2L);
                    }
                }
                if (tag.equalsIgnoreCase("EndScythe")) {
                    if (!isCooling(p, "endscythe")) {
                        addCD(p, 4000, "endscythe");
                        Particle.DustOptions b = new Particle.DustOptions(Color.fromRGB(100, 100, 100), 1.25f);
                        p.getWorld().playSound(p.getLocation(), Sound.ITEM_TRIDENT_THROW, 1, 0.8f);
                        Location loc = p.getLocation().add(0, 0.6f, 0);
                        for (int iter = 0; iter < 180; iter++) {
                            double xCoord = (2 * Math.cos(iter));
                            double yCoord = (2 * Math.sin(iter));
                            p.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + xCoord, loc.getY(), loc.getZ() + yCoord, 1, 0, 0, 0, 1f, b);
                        }
                        for (LivingEntity ent : Objects.requireNonNull(p.getWorld()).getLivingEntities()) {
                            if (p.getLocation().distance(ent.getLocation()) <= 2.5f && ent != p) {
                                ent.damage(22.5f, p);
                                if (e instanceof Player) {
                                    Player f = (Player) e;
                                    if (f.getName().equalsIgnoreCase("Манекен")) {
                                        p.sendTitle("", "22.5 Attack Damage", 5, 10, 5);
                                    }
                                }
                            }
                        }
                    }
                }
                if (tag.equalsIgnoreCase("TheShovel")) {
                    if (!isCooling(p, "theshovel")) {
                        addCD(p, 250, "theshovel");
                        Block block = p.getTargetBlock(null, 12);
                        if (block.getType().getHardness() < 50f && !restricted.contains(block.getType()) && block.getType().getHardness() != -1) {
                            FallingBlock f = p.getWorld().spawnFallingBlock(block.getLocation().add(0.5f, 0, 0.5f), block.getBlockData());
                            f.setVelocity(new Vector(0, 0.5f, 0));
                            f.setHurtEntities(false);
                            block.setType(Material.AIR);
                            e.setCancelled(true);
                        }
                    }
                }
                if (tag.equalsIgnoreCase("Banhammer")) {
                    switch (m.getCustomModelData()) {
                        case 8:
                            m.setCustomModelData(9);
                            break;
                        case 9:
                            m.setCustomModelData(10);
                            break;
                        case 10:
                            m.setCustomModelData(8);
                            break;
                    }
                    p.getInventory().getItemInMainHand().setItemMeta(m);
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                }
                if (tag.equalsIgnoreCase("SwordCane")) {
                    switch (m.getCustomModelData()) {
                        case 11:
                            m.setCustomModelData(12);
                            break;
                        case 12:
                            m.setCustomModelData(11);
                            break;
                    }
                    p.getInventory().getItemInMainHand().setItemMeta(m);
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                }
                if (tag.equalsIgnoreCase("ThunderKatana")) {
                    if (!isCooling(p, "thunderkatana")) {
                        addCD(p, 10000, "thunderkatana");
                        p.setGravity(false);
                        final int[] i = {2};
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Location loc = p.getLocation();
                                p.getWorld().playSound(loc, Sound.ENTITY_VEX_HURT, 1, 1);
                                p.getWorld().playSound(loc, Sound.ITEM_TRIDENT_THROW, 0.5F, 1.75F);
                                p.setVelocity(p.getLocation().getDirection().multiply(new Vector(4, 0, 4)));
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        Vector startVelocity = p.getVelocity();
                                        Location loc2 = loc.add(0, 0.2f, 0);
                                        Location loc3 = p.getLocation().add(0, 0.2f, 0);
                                        for (int i = 20; i > 0; i--) {
                                            p.getWorld().spawnParticle(Particle.END_ROD, loc2.add(startVelocity.multiply(new Vector(0.05f, 0, 0.05f))), 1, 0, 0, 0, 0);
                                        }
                                        p.setVelocity(new Vector(0, 0, 0));
                                        for (int i = 20; i > 0; i--) {
                                            p.getWorld().spawnParticle(Particle.END_ROD, loc3.add(startVelocity.multiply(new Vector(-0.05f, 0, -0.05f))), 1, 0, 0, 0, 0);
                                        }
                                    }
                                }.runTaskLater(Main.getInstance(), 2L);
                                for (LivingEntity e : Objects.requireNonNull(p.getWorld()).getLivingEntities()) {
                                    if (p.getLocation().distance(e.getLocation()) <= 3.5f && e != p) {
                                        e.damage(15, p);
                                        if (e instanceof Player) {
                                            Player f = (Player) e;
                                            if (f.getName().equalsIgnoreCase("Манекен")) {
                                                p.sendTitle("", "15.0 Attack Damage", 5, 10, 5);
                                            }
                                        }
                                    }
                                }
                                if (i[0] > 0) {
                                    i[0]--;
                                } else {
                                    this.cancel();
                                    p.setGravity(true);
                                }
                            }
                        }.runTaskTimer(Main.getInstance(), 1L, 5L);
                    }
                }
                if (tag.equalsIgnoreCase("PrototypeRedstone")) {
                    if (!isCooling(p, "redstone")) {
                        addCD(p, 5000, "redstone");
                        Particle.DustOptions j = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1f);
                        for (LivingEntity ent : Objects.requireNonNull(p.getWorld()).getLivingEntities()) {
                            if (p.getLocation().distance(ent.getLocation()) <= 4f && ent != p) {
                                ent.damage(16, p);
                                ent.addPotionEffect(PotionEffectType.GLOWING.createEffect(200, 0));
                                if (ent instanceof Player) {
                                    Player f = (Player) ent;
                                    if (f.getName().equalsIgnoreCase("Манекен")) {
                                        p.sendTitle("", "16.0 Attack Damage", 5, 10, 5);
                                    }
                                }
                            }
                        }
                        for (float i = 8; i > 0; i--) {
                            Location loc = p.getLocation().add(0, 0.1f, 0);
                            Vector direction = new Vector((float) Math.cos(Math.toRadians(i * 45)), 0, (float) Math.sin(Math.toRadians(i * 45))).multiply(0.15f);
                            for (int i2 = 20; i2 > 0; i2--) {
                                p.getWorld().spawnParticle(Particle.REDSTONE, loc.add(direction), 1, 0, 0, 0, 0, j);
                            }
                        }
                    }
                }
            }
            // Left Click
            if(a == Action.LEFT_CLICK_AIR) {
            }
        }
        if(p.getInventory().getItemInOffHand().hasItemMeta()) {
            m = p.getInventory().getItemInOffHand().getItemMeta();
            tag = getTag(p.getInventory().getItemInOffHand());
            if (p.getInventory().getItemInMainHand().getData().getItemType().isAir()) {// Right Click
                if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
                    if (tag.equalsIgnoreCase("Celestial")) {
                        if (!isCooling(p, "celestial")) {
                            addCD(p, 30000, "celestial");
                            Location loc = p.getLocation();
                            p.getWorld().spawnParticle(Particle.CLOUD, loc, 50, 0, 0, 0, 0.1f);
                            p.sendTitle(ChatColor.GREEN + "Access Granted", "p.setAllowFlight(true)", 5, 20, 5);
                            p.setAllowFlight(true);
                            p.setFlying(true);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    p.setAllowFlight(false);
                                    p.setFlying(false);
                                    p.sendTitle(ChatColor.RED + "Access Denied", "p.setAllowFlight(false)", 5, 20, 5);
                                    p.addPotionEffect(PotionEffectType.SLOW_FALLING.createEffect(100, 0));
                                }
                            }.runTaskLater(Main.getInstance(), 200L);
                        }
                    }
                    if (tag.equalsIgnoreCase("Collapse")) {
                        if (!isCooling(p, "collapse")) {
                            addCD(p, 160000, "collapse");
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    p.sendTitle("", "GALAXY COLLAPSE IS READY", 10, 10, 10);
                                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                }
                            }.runTaskLater(Main.getInstance(), 3200L);
                            Particle.DustOptions b = new Particle.DustOptions(Color.fromRGB(0, 0, 0), 1.25f);
                            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1);
                            Location loc = p.getLocation().add(0, 0.2f, 0);
                            final int[] i = {10};
                            ArrayList<Entity> entities = new ArrayList<>();
                            for (LivingEntity ent : loc.getWorld().getLivingEntities()) {
                                if (loc.distance(ent.getLocation()) <= 10f && e != p) {
                                    entities.add(ent);
                                }
                            }
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    for (int iter = 0; iter < 360; iter++) {
                                        double xCoord = (10 * Math.cos(iter));
                                        double yCoord = (10 * Math.sin(iter));
                                        p.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + xCoord, loc.getY(), loc.getZ() + yCoord, 1, 0, 0, 0, 1f, b);
                                    }
                                    if (i[0] > 0) {
                                        i[0]--;
                                    }
                                    if (i[0] == 2) {
                                        for (Entity ent : entities) {
                                            if (ent instanceof Player) {
                                                ((Player) ent).sendTitle("GALAXY", "", 0, 0, 10);
                                            }
                                        }
                                    }
                                    if (i[0] == 1) {
                                        for (Entity ent : entities) {
                                            if (ent instanceof Player) {
                                                ((Player) ent).sendTitle("COLLAPSE", "", 0, 0, 10);
                                            }
                                        }
                                    }
                                    if (i[0] == 0) {
                                        this.cancel();
                                        for (Entity ent : entities) {
                                            ent.teleport(new Location(p.getServer().getWorld("collapse"), 0, 1, 0));
                                        }
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                for (Entity ent : p.getServer().getWorld("collapse").getEntities()) {
                                                    ent.teleport(loc);
                                                }
                                            }
                                        }.runTaskLater(Main.getInstance(), 600L);
                                    }
                                }
                            }.runTaskTimer(Main.getInstance(), 0L, 25L);
                        }
                    }
                }
                float cool = p.getAttackCooldown();
                // Left Click
                if (a == Action.LEFT_CLICK_AIR) {
                    if (tag.equalsIgnoreCase("BookOTales")) {
                        Location loc = p.getEyeLocation().add(p.getLocation().getDirection().multiply(15));
                        if (!p.getTargetBlock(transparent, 15).isEmpty())
                            loc = p.getTargetBlock(transparent, 15).getLocation().add(0.5f, 0.5f, 0.5f);
                        Particle.DustOptions j = new Particle.DustOptions(Color.fromRGB(100, 255, 70), 2f);
                        if (cool > 0.25) {
                            for (Entity e1 : p.getNearbyEntities(14, 14, 14)) {
                                if (e1 instanceof LivingEntity) {
                                    if (getLookingAt(p, (LivingEntity) e1)) {
                                        LivingEntity e2 = (LivingEntity) e1;
                                        loc = e2.getEyeLocation();
                                        e2.damage(5 * cool, p);
                                        if (p.getAttackCooldown() == 1) {
                                            Random rand = new Random();
                                            int test = rand.nextInt(24);
                                            switch (test) {
                                                case 0:
                                                    if (e2 instanceof Player)
                                                        ((Player) e2).sendTitle("Heat", "", 0, 5, 5);
                                                    j = new Particle.DustOptions(Color.fromRGB(255, 100, 0), 2f);
                                                    e2.setFireTicks(60);
                                                    break;
                                                case 1:
                                                    if (e2 instanceof Player)
                                                        ((Player) e2).sendTitle("Death", "", 0, 5, 5);
                                                    j = new Particle.DustOptions(Color.fromRGB(10, 10, 10), 2f);
                                                    e2.addPotionEffect(PotionEffectType.WITHER.createEffect(60, 1));
                                                    break;
                                                case 2:
                                                    if (e2 instanceof Player)
                                                        ((Player) e2).sendTitle("Soul", "", 0, 5, 5);
                                                    j = new Particle.DustOptions(Color.fromRGB(0, 200, 255), 2f);
                                                    e2.addPotionEffect(PotionEffectType.SLOW.createEffect(60, 0));
                                                    break;
                                                case 3:
                                                    if (e2 instanceof Player)
                                                        ((Player) e2).sendTitle("Fear", "", 0, 5, 5);
                                                    j = new Particle.DustOptions(Color.fromRGB(220, 0, 255), 2f);
                                                    e2.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(60, 0));
                                                    break;
                                                case 4:
                                                    if (e2 instanceof Player)
                                                        ((Player) e2).sendTitle("Gold", "", 0, 5, 5);
                                                    j = new Particle.DustOptions(Color.fromRGB(255, 190, 30), 2f);
                                                    p.getInventory().addItem(new ItemStack(Material.GOLD_NUGGET, 1));
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                        if (e1 instanceof Player) {
                                            Player g = (Player) e1;
                                            if (g.getName().equalsIgnoreCase("Манекен")) {
                                                p.sendTitle("", (double) Math.round((5 * cool) * 10d) / 10d + " Attack Damage", 5, 10, 5);
                                            }
                                        }
                                    }
                                }
                            }
                            p.getWorld().playSound(loc, Sound.ITEM_TRIDENT_RETURN, 1, 1.25f);
                            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_VILLAGER_WORK_LIBRARIAN, 1, 1);
                            loc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 100, 0, 0, 0, 1f);
                            loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 25, 0.2f, 0.2f, 0.2f, 0f, j);
                        }
                    }
                    if (tag.equalsIgnoreCase("Celestial")) {
                        Particle.DustOptions j = new Particle.DustOptions(Color.fromRGB(0, 188, 255), 2f);
                        Particle.DustOptions j2 = new Particle.DustOptions(Color.fromRGB(255, 240, 80), 0.75f);
                        if (cool > 0.25) {
                            Location loc = p.getEyeLocation().add(p.getLocation().getDirection().multiply(15));
                            if (!p.getTargetBlock(transparent, 15).isEmpty())
                                loc = p.getTargetBlock(transparent, 15).getLocation().add(0.5f, 0.5f, 0.5f);
                            for (Entity e1 : p.getNearbyEntities(14, 14, 14)) {
                                if (e1 instanceof LivingEntity) {
                                    if (getLookingAt(p, (LivingEntity) e1)) {
                                        LivingEntity e2 = (LivingEntity) e1;
                                        loc = e2.getEyeLocation();
                                        e2.damage(7 * cool, p);
                                        if (e2 instanceof Player) {
                                            Player g = (Player) e1;
                                            if (g.getName().equalsIgnoreCase("Манекен")) {
                                                p.sendTitle("", (double) Math.round((7 * cool) * 10d) / 10d + " Attack Damage", 5, 10, 5);
                                            }
                                        }
                                    }
                                }
                            }
                            Location finalLoc = loc;
                            if (cool == 1) {
                                Random rand = new Random();
                                int rand2 = rand.nextInt(19);
                                if (rand2 == 0) p.getWorld().strikeLightning(loc);
                            }
                            for (int i = 360; i > 0; i--) {
                                double xCoord = Math.cos(i * 0.3f);
                                double yCoord = Math.sin(i * 0.5f);
                                double zCoord = Math.sin(i * 0.4f);
                                p.getWorld().spawnParticle(Particle.REDSTONE, finalLoc.clone().add(xCoord, yCoord, zCoord), 1, 0, 0, 0, 0, j2);
                            }
                            p.getWorld().spawnParticle(Particle.REDSTONE, finalLoc, 30, 0.2, 0.2, 0.2, 0, j);
                            p.getWorld().playSound(finalLoc, Sound.ENTITY_BLAZE_SHOOT, 1, 1);
                        }
                    }
                    if (tag.equalsIgnoreCase("Collapse")) {
                        Particle.DustOptions j = new Particle.DustOptions(Color.fromRGB(0, 0, 0), 2f);
                        Particle.DustOptions j2 = new Particle.DustOptions(Color.fromRGB(220, 0, 255), 0.75f);
                        if (cool > 0.25f) {
                            Location loc = p.getEyeLocation().add(p.getLocation().getDirection().multiply(15));
                            if (!p.getTargetBlock(transparent, 15).isEmpty())
                                loc = p.getTargetBlock(transparent, 15).getLocation().add(0.5f, 0.5f, 0.5f);
                            for (Entity e1 : p.getNearbyEntities(14, 14, 14)) {
                                if (e1 instanceof LivingEntity) {
                                    if (getLookingAt(p, (LivingEntity) e1)) {
                                        LivingEntity e2 = (LivingEntity) e1;
                                        loc = e2.getEyeLocation();
                                        e2.damage(6 * cool, p);
                                    }
                                }
                            }
                            Location finalLoc = loc;
                            for (int i = 360; i > 0; i--) {
                                double xCoord = Math.cos(i * 0.4f);
                                double yCoord = Math.sin(i * 0.5f);
                                double zCoord = Math.sin(i * 0.3f);
                                p.getWorld().spawnParticle(Particle.REDSTONE, finalLoc.clone().add(xCoord, yCoord, zCoord), 1, 0, 0, 0, 0, j2);
                            }
                            p.getWorld().playSound(finalLoc, Sound.BLOCK_BEACON_DEACTIVATE, 1, 1.5f);
                            p.getWorld().spawnParticle(Particle.REDSTONE, finalLoc, 30, 0.2, 0.2, 0.2, 0, j);
                        }
                    }
                }
            }
        }
    }
    public boolean getLookingAt(Player player, LivingEntity player1) {
        Location eye = player.getEyeLocation();
        Vector toEntity = player1.getEyeLocation().toVector().subtract(eye.toVector());
        double dot = toEntity.normalize().dot(eye.getDirection());

        return dot > 0.99D;
    }
    @EventHandler
    public void Damage(EntityDamageByEntityEvent e) {
        ((LivingEntity) e.getEntity()).setNoDamageTicks(10);
        ((LivingEntity) e.getEntity()).setMaximumNoDamageTicks(10);
        ItemMeta m;
        String tag;
        NamespacedKey k;
        if(e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            float cool = p.getAttackCooldown();
            if(e.getEntity() instanceof Player) {
                Player g = (Player) e.getEntity();
                if(g.getName().equalsIgnoreCase("Манекен")){
                    p.sendTitle("", (double) Math.round(e.getDamage() * 10d) / 10d + " Attack Damage", 5, 10, 5);
                }
            }
            if (p.getInventory().getItemInMainHand().hasItemMeta()) {
                m = p.getInventory().getItemInMainHand().getItemMeta();
                assert m != null;
                if (m.getPersistentDataContainer().getKeys().toArray().length != 0) {
                    k = (NamespacedKey) m.getPersistentDataContainer().getKeys().toArray()[0];
                    tag = m.getCustomTagContainer().getCustomTag(k, ItemTagType.STRING);
                    assert tag != null;
                    if (tag.equalsIgnoreCase("Freedom")) {
                        //p.sendMessage(String.valueOf(p.getAttackCooldown()) + " EntityDamageByEntityEvent");
                        if(p.getAttackCooldown() > 0.9f) {
                            if(combo.containsKey(p.getName()+"freedom")) {
                                switch (combo.get(p.getName()+"freedom")) {
                                    case 0:
                                        combo.put(p.getName()+"freedom", 1);
                                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.6f);
                                        changeAttribute(m, p, Attribute.GENERIC_ATTACK_DAMAGE, "generic.attackDamage", 6);
                                        p.getWorld().spawnParticle(Particle.TOTEM, (p.getEyeLocation().toVector().add(p.getLocation().getDirection().multiply(1.25f)).toLocation(p.getWorld())), 7, 0,0,0, 0.01f);
                                        stopCombo(1, "freedom", p);
                                        break;
                                    case 1:
                                        combo.put(p.getName()+"freedom", 2);
                                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.8f);
                                        changeAttribute(m, p, Attribute.GENERIC_ATTACK_DAMAGE, "generic.attackDamage", 11);
                                        p.getWorld().spawnParticle(Particle.TOTEM, (p.getEyeLocation().toVector().add(p.getLocation().getDirection().multiply(1.25f)).toLocation(p.getWorld())), 15, 0,0,0, 0.15f);
                                        stopCombo(2, "freedom", p);
                                        break;
                                    case 2:
                                        combo.put(p.getName()+"freedom", 0);
                                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1.0f);
                                        changeAttribute(m, p, Attribute.GENERIC_ATTACK_DAMAGE, "generic.attackDamage", 7);
                                        p.getWorld().spawnParticle(Particle.TOTEM, (p.getEyeLocation().toVector().add(p.getLocation().getDirection().multiply(1.25f)).toLocation(p.getWorld())), 50, 0,0,0, 0.5f);
                                        p.setVelocity(p.getVelocity().add(p.getLocation().getDirection().multiply(1.3f)));
                                        break;
                                }
                            } else {
                                combo.put(p.getName()+"freedom", 1);
                                changeAttribute(m, p, Attribute.GENERIC_ATTACK_DAMAGE, "generic.attackDamage", 6);
                            }
                        } else {
                            combo.put(p.getName()+"freedom", 0);
                            changeAttribute(m, p, Attribute.GENERIC_ATTACK_DAMAGE, "generic.attackDamage", 7);
                        }
                    }
                    if (tag.equalsIgnoreCase("SwordCane")) {
                        //p.sendMessage(String.valueOf(p.getAttackCooldown()) + " EntityDamageByEntityEvent");
                        Particle.DustOptions j = new Particle.DustOptions(Color.fromRGB(100, 0, 200), 0.5f);
                        if(m.getCustomModelData()==11){
                            if (cool > 0.9f) {
                                {
                                    if (combo.containsKey(p.getName() + "swordcane")) {
                                        switch (combo.get(p.getName() + "swordcane")) {
                                            case 0:
                                                combo.put(p.getName() + "swordcane", 1);
                                                p.getWorld().playSound(p.getLocation(), Sound.ITEM_TRIDENT_THROW, 1, 1.75f);
                                                stopCombo(1, "swordcane", p);
                                                break;
                                            case 1:
                                                combo.put(p.getName() + "swordcane", 2);
                                                p.getWorld().playSound(p.getLocation(), Sound.ITEM_TRIDENT_THROW, 1, 1.75f);
                                                stopCombo(2, "swordcane", p);
                                                break;
                                            case 2:
                                                combo.put(p.getName() + "swordcane", 3);
                                                p.getWorld().playSound(p.getLocation(), Sound.ITEM_TRIDENT_THROW, 1, 1.75f);
                                                stopCombo(3, "swordcane", p);
                                                break;
                                            case 3:
                                                combo.put(p.getName() + "swordcane", 4);
                                                p.getWorld().playSound(p.getLocation(), Sound.ITEM_TRIDENT_THROW, 1, 1.75f);
                                                stopCombo(4, "swordcane", p);
                                                break;
                                            case 4:
                                                combo.put(p.getName() + "swordcane", 0);
                                                Location loc = p.getEyeLocation();
                                                loc.add(loc.getDirection());
                                                final int[] i = {4};
                                                Particle.DustOptions finalJ = j;
                                                new BukkitRunnable() {
                                                    @Override
                                                    public void run() {
                                                        p.getWorld().playSound(p.getLocation(), Sound.ITEM_TRIDENT_THROW, 1, 1.75f);
                                                        Location loc2 = loc.clone().add(Math.random() * 2 - 1, Math.random() * 2 - 1, Math.random() * 2 - 1);
                                                        for (int count = 20; count > 0; count--) {
                                                            p.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, loc2.add(p.getLocation().getDirection().multiply(0.075f)), 1, 0, 0, 0, 0);
                                                            p.getWorld().spawnParticle(Particle.REDSTONE, loc2.add(p.getLocation().getDirection().multiply(0.075f)), 1, 0, 0, 0, 0, finalJ);
                                                        }
                                                        if (i[0] > 0) {
                                                            i[0]--;
                                                        } else {
                                                            e.getEntity().setVelocity(p.getLocation().getDirection().multiply(new Vector(1f, 0f, 1f)));
                                                            p.setVelocity(p.getLocation().getDirection().multiply(new Vector(-1, 0, -1)));
                                                            LivingEntity ent = (LivingEntity) e.getEntity();
                                                            ent.addPotionEffect(PotionEffectType.WITHER.createEffect(60, 2));
                                                            this.cancel();
                                                        }
                                                    }
                                                }.runTaskTimer(Main.getInstance(), 0L, 1L);
                                                break;
                                        }
                                    } else {
                                        combo.put(p.getName() + "swordcane", 1);
                                    }
                                }
                            } else {
                                combo.put(p.getName() + "swordcane", 0);
                            }
                        } else {
                            LivingEntity ent = (LivingEntity) e.getEntity();
                            e.setCancelled(true);
                            j = new Particle.DustOptions(Color.fromRGB(255, 237, 38), 1f);
                            if(cool>0.5f){
                                if(ent.getHealth() + 5 * cool <= ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()) {
                                    ent.setHealth(ent.getHealth() + 5 * cool);
                                } else ent.setHealth(ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                                p.getWorld().playSound(ent.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, 1, 1.5f);
                                p.getWorld().spawnParticle(Particle.REDSTONE, ent.getLocation(), 20, 1, 1, 1, 0, j);
                            }
                        }
                    }
                    if(tag.equalsIgnoreCase("Durandal")) {
                        if(p.getAttackCooldown() > 0.8f) {
                            Location loc = p.getEyeLocation();
                            loc.add(loc.getDirection().multiply(2f));
                            p.getWorld().spawnParticle(Particle.SOUL, loc, 7, 0, 0, 0, 0.025f);
                        }
                    }
                    if(tag.equalsIgnoreCase("EndScythe")) {
                        if(p.getAttackCooldown() == 1f) {
                            if(Math.random()>0.9f) {
                                if(e.getEntity() instanceof LivingEntity) {
                                    ((LivingEntity) e.getEntity()).addPotionEffect(PotionEffectType.WITHER.createEffect(60,1));
                                }
                            }
                        }
                    }
                    if(tag.equalsIgnoreCase("Banhammer")) {
                        if(e.getEntity() instanceof Player) {
                            Player pp = (Player) e.getEntity();
                            switch (m.getCustomModelData()) {
                                case 8:
                                    Bukkit.getBanList(BanList.Type.NAME).addBan(pp.getName(), ChatColor.LIGHT_PURPLE + "Nobody escapes.", null, p.getName());
                                    break;
                                case 9:
                                    pp.kickPlayer(ChatColor.LIGHT_PURPLE + "You have been smashed by a " + ChatColor.RED + "Banhammer");
                                    p.getWorld().playSound(pp.getLocation(), Sound.BLOCK_ANVIL_LAND,1,2);
                                    break;
                                case 10:
                                    Bukkit.getBanList(BanList.Type.NAME).pardon(pp.getName());
                                    break;
                            }
                        } else {
                            if(m.getCustomModelData()==9) {
                                p.getWorld().playSound(e.getEntity().getLocation(), Sound.BLOCK_ANVIL_LAND,1,2);
                                e.getEntity().setVelocity(new Vector(0,7,0));
                                e.setCancelled(true);
                            }
                        }
                    }
                    if(tag.equalsIgnoreCase("PrototypeRedstone")) {
                        Particle.DustOptions j = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1f);
                        if(cool>0.5f) {
                            if(Math.random()<=0.075f) ((LivingEntity) e.getEntity()).addPotionEffect(PotionEffectType.GLOWING.createEffect(200, 0));
                            p.getWorld().spawnParticle(Particle.REDSTONE, ((LivingEntity) e.getEntity()).getEyeLocation(), 10,0.5f,0.5f,0.5f,0, j);
                        }
                    }
                    if(tag.equalsIgnoreCase("FunnyStick")) {
                        ((LivingEntity) e.getEntity()).setNoDamageTicks(1);
                        ((LivingEntity) e.getEntity()).setMaximumNoDamageTicks(1);
                    }
                }
            }
        }
    }
    @EventHandler
    public void Shoot(EntityShootBowEvent e) {
        ItemStack bow = e.getBow();
        LivingEntity l = e.getEntity();
        if(bow.hasItemMeta()) if(bow.getItemMeta().hasCustomModelData()) if(bow.getItemMeta().getCustomModelData()==1) if(bow.getType() == Material.CROSSBOW) {
            Projectile proj = (Projectile) e.getProjectile();
            proj.setVelocity(l.getLocation().getDirection().multiply(15));
            proj.setGravity(false);
            proj.setBounce(false);
            e.setProjectile(proj);
            if(l instanceof Player){
                Player p = (Player) l;
                e.setConsumeItem(false);
                p.updateInventory();
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    proj.remove();
                }
            }.runTaskLater(Main.getInstance(),20L);
        }
        if(bow.hasItemMeta()) if(bow.getItemMeta().hasCustomModelData()) if(bow.getItemMeta().getCustomModelData()==1) if(bow.getType() == Material.BOW) {
            Entity fireball = l.getWorld().spawnEntity(l.getEyeLocation(), EntityType.SMALL_FIREBALL);
            Projectile proj = (Projectile) e.getProjectile();
            fireball.setVelocity(proj.getVelocity());
            e.setProjectile(fireball);
            if(l instanceof Player){
                Player p = (Player) l;
                e.setConsumeItem(false);
                p.updateInventory();
            }
        }
    }
    @EventHandler
    public void Anvil(InventoryClickEvent e){
        HumanEntity p = e.getWhoClicked();
        if(e.getInventory().getType()==InventoryType.ANVIL){
            //p.sendMessage("yep");
            if(e.getCurrentItem().hasItemMeta()) if(e.getCurrentItem().getItemMeta().hasCustomModelData()) if(e.getCurrentItem().getItemMeta().getCustomModelData()==727) {
                e.setCancelled(true);
            }
        }
    }
    public void stopCombo(int i, String weapon, Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(combo.get(p.getName()+weapon) == i) {
                    combo.put(p.getName()+weapon, 0);
                    p.playSound(p.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1, 1.5f);
                    ItemMeta m;
                    String tag;
                    NamespacedKey k;
                    if (p.getInventory().getItemInMainHand().hasItemMeta()) {
                        m = p.getInventory().getItemInMainHand().getItemMeta();
                        assert m != null;
                        if (m.getPersistentDataContainer().getKeys().toArray().length != 0) {
                            k = (NamespacedKey) m.getPersistentDataContainer().getKeys().toArray()[0];
                            tag = m.getCustomTagContainer().getCustomTag(k, ItemTagType.STRING);
                            assert tag != null;
                            if (tag.equalsIgnoreCase("Freedom")) {
                                changeAttribute(m, p, Attribute.GENERIC_ATTACK_DAMAGE, "generic.attackDamage", 7);
                            }
                        }
                    }
                } else this.cancel();
            }
        }.runTaskLaterAsynchronously(Main.getInstance(), 30L);
    }
    public void changeAttribute(ItemMeta m, Player p, Attribute att1, String attribute, float number) {
        m.removeAttributeModifier(att1);
        AttributeModifier h = new AttributeModifier(UUID.randomUUID(), attribute, number, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        m.addAttributeModifier(att1, h);
        p.getInventory().getItemInMainHand().setItemMeta(m);
    }
    @EventHandler
    public void Click2(PlayerInteractEvent e) {
        Action a = e.getAction();
        Player p = e.getPlayer();
        String tag;
        ItemMeta m;
        if(p.getInventory().getItemInMainHand().hasItemMeta()) {
            m = p.getInventory().getItemInMainHand().getItemMeta();
            tag=getTag(p.getInventory().getItemInMainHand());
            if(a!=Action.PHYSICAL&&a!=Action.LEFT_CLICK_BLOCK&&a!=Action.RIGHT_CLICK_BLOCK) {
                if(tag.equalsIgnoreCase("Binary")) {
                    if(!isCooling(p,"binary")){
                        p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK,0.5f,1);
                        if (!combo.containsKey(p.getName() + "count")) {
                            combo.put(p.getName() + "count", 1);
                        } else combo.put(p.getName() + "count", combo.get(p.getName() + "count") + 1);
                        int tempCount = combo.get(p.getName() + "count");
                        String chr = "0";
                        if(a==Action.RIGHT_CLICK_AIR) chr = "1";

                        if(!prikol.containsKey(p.getName()+"code")) {
                            prikol.put(p.getName()+"code", chr);
                        } else prikol.put(p.getName()+"code", prikol.get(p.getName()+"code")+chr);

                        if(tempCount%4==0) prikol.put(p.getName()+"code", prikol.get(p.getName()+"code")+" ");

                        if (!prikol.containsKey(p.getName() + "template")) {
                            prikol.put(p.getName() + "template", "--- ---- ---- ----");
                        } else prikol.put(p.getName() + "template", prikol.get(p.getName() + "template").substring(1).trim());

                        String template = prikol.get(p.getName() + "template");
                        String code = prikol.get(p.getName()+"code");
                        p.sendTitle(code+template, "", 0, 30, 0);

                        if (tempCount == 16) {
                            p.sendTitle(net.md_5.bungee.api.ChatColor.of("#59ff7c")+code.trim(), "", 0, 5, 10);
                            combo.remove(p.getName() + "count");
                            prikol.remove(p.getName() + "template");
                            prikol.remove(p.getName()+"code");
                            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
                            compute(e,code);
                            addCD(p, 750,"binary");
                        }
                        //p.sendTitle(net.md_5.bungee.api.ChatColor.of("#e3e3e3")+"1--- ---- ----", net.md_5.bungee.api.ChatColor.of("#e3e3e3")+"Group 1 -> Group 0/Group 1",0,40,0);
                        new BukkitRunnable() {
                            //Fucking clears everything
                            @Override
                            public void run() {
                                if (combo.containsKey(p.getName() + "count")) {
                                    if (tempCount == combo.get(p.getName() + "count")) {
                                        //STOP POSTING ABOUT AMONG US! I'M TIRED OF SEEING IT! MY FRIENDS ON TIKTOK SEND ME MEMES, ON DISCORD IT'S FUCKING MEMES! I was in a server, right? and ALL OF THE CHANNELS were just among us stuff. I-I showed my champion underwear to my girlfriend and t-the logo I flipped it and I said "hey babe, when the underwear is sus HAHA DING DING DING DING DING DING DING DI DI DING"
                                        if (combo.get(p.getName() + "count") < 16) {
                                            combo.remove(p.getName() + "count");
                                            prikol.remove(p.getName() + "template");
                                            prikol.remove(p.getName() + "code");
                                            p.playSound(p.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1, 2);
                                            p.sendTitle(net.md_5.bungee.api.ChatColor.of("#FF2222") + code + template, "", 0, 0, 10);
                                            addCD(p, 500, "binary");
                                        }
                                    } else this.cancel();
                                }
                            }
                        }.runTaskLater(Main.getInstance(), 30L);
                    }
                }
            }
        }
    }
    public String addChar(String str, char ch, int position) {
        StringBuilder sb = new StringBuilder(str);
        sb.insert(position, ch);
        return sb.toString();
    }
    @EventHandler
    public void Join(PlayerJoinEvent e) {
        e.getPlayer().setGravity(true);
        if(e.getPlayer().getName().equalsIgnoreCase("b1n4ry0")) e.setJoinMessage(net.md_5.bungee.api.ChatColor.of("#ffe13d")+"ʙ1ɴ4ʀʏ ᴊᴏɪɴᴇᴅ ᴛʜᴇ ɢᴀᴍᴇ");
    }
    @EventHandler
    public void Quit(PlayerQuitEvent e) {
        if(e.getPlayer().getName().equalsIgnoreCase("b1n4ry0")) e.setQuitMessage(net.md_5.bungee.api.ChatColor.of("#ffe13d")+"ʙ1ɴ4ʀʏ ʟᴇꜰᴛ ᴛʜᴇ ɢᴀᴍᴇ");
    }

    public void compute(PlayerInteractEvent e, String code) {
        Player p = e.getPlayer();
        int[] intArray = code.replaceAll(" ", "").chars().map(a->a-'0').toArray();
        for(int i=0;i<intArray.length;i++) {
            //e.getPlayer().sendMessage(String.valueOf(intArray[i]));
            int i2 = intArray[i];
            switch (i) {
                //CanHurt
                case 0:
                    if(i2==1) {
                        for (LivingEntity ent : Objects.requireNonNull(p.getWorld()).getLivingEntities()) {
                            if (p.getLocation().distance(ent.getLocation()) <= 5.0f && ent != p) {
                                ent.damage(19, p);
                                if (e instanceof Player) {
                                    Player f = (Player) e;
                                    if (f.getName().equalsIgnoreCase("Манекен")) {
                                        p.sendTitle("", "19.0 Attack Damage", 5, 10, 5);
                                    }
                                }
                            }
                        }
                    }
                    break;
                //PlaySound
                case 1:
                    Sound a = Sound.ITEM_TRIDENT_RETURN;
                    if(i2==1) {
                        a = Sound.ITEM_TRIDENT_THROW;
                    }
                    p.getWorld().playSound(p.getLocation(), a, 1, 1);
                    break;
                //ShowParticles
                case 2:
                    if(i2==1) {
                        Particle.DustOptions j = new Particle.DustOptions(Color.BLACK,1f);
                        p.getWorld().spawnParticle(Particle.REDSTONE, p.getEyeLocation().add(p.getLocation().getDirection()), 20,0.2f,0.2f,0.2f,0f,j);
                    } else {
                        p.getWorld().spawnParticle(Particle.END_ROD, p.getEyeLocation().add(p.getLocation().getDirection()), 20,0.2f,0.2f,0.2f,0.05f);
                    }
                    break;
                //ChangeVelocity
                case 3:
                    Vector velocity = new Vector(0,1.2f,0);
                    if(i2==1) {
                        velocity = p.getLocation().getDirection().multiply(1.2f);
                    }
                    for (LivingEntity ent : Objects.requireNonNull(p.getWorld()).getLivingEntities()) {
                        if (p.getLocation().distance(ent.getLocation()) <= 5.0f && ent != p) {
                            ent.setVelocity(velocity);
                        }
                    }
                    break;
            }
        }
    }
}