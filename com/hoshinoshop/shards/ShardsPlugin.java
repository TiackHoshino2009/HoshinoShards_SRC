package com.hoshinoshop.shards;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ShardsPlugin extends JavaPlugin implements Listener {
   private ShardsManager shardsManager;
   private AfkZone afkZone;
   private AfkWandListener afkWandListener;
   private final Map<UUID, Long> lastMoveTime = new HashMap();
   private final Map<UUID, Long> lastAfkRewardTime = new HashMap();
   private BukkitTask afkTask;

   public void onEnable() {
      this.saveDefaultConfig();
      this.reloadConfig();
      this.shardsManager = new ShardsManager(this);
      this.shardsManager.load();
      this.afkZone = new AfkZone(this.getConfig());
      this.getServer().getPluginManager().registerEvents(this, this);
      this.afkWandListener = new AfkWandListener(this);
      this.getServer().getPluginManager().registerEvents(this.afkWandListener, this);
      this.getCommand("shards").setExecutor(new ShardsCommand(this));
      this.getCommand("shards").setTabCompleter(new ShardsTabCompleter());
      if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
         boolean registered = (new PlaceholderHook(this)).register();
         if (registered) {
            this.getLogger().info("Đã đăng ký placeholder thành công với PlaceholderAPI!");
         } else {
            this.getLogger().warning("Không thể đăng ký placeholder với PlaceholderAPI.");
         }
      } else {
         this.getLogger().warning("PlaceholderAPI không tìm thấy! Placeholder sẽ không hoạt động.");
      }

      this.startAfkTask();
      this.getLogger().info("HoshinoShards plugin đã bật.");
   }

   public void onDisable() {
      this.shardsManager.save();
      if (this.afkTask != null) {
         this.afkTask.cancel();
      }

      this.getLogger().info("HoshinoShards plugin đã tắt.");
   }

   private void startAfkTask() {
      this.afkTask = (new BukkitRunnable() {
         public void run() {
            if (ShardsPlugin.this.getConfig().getBoolean("afk-zone-enabled", true)) {
               for(Player player : Bukkit.getOnlinePlayers()) {
                  if (ShardsPlugin.this.afkZone.contains(player.getLocation())) {
                     long lastMove = (Long)ShardsPlugin.this.lastMoveTime.getOrDefault(player.getUniqueId(), 0L);
                     long idleThreshold = (long)ShardsPlugin.this.getConfig().getInt("afk-idle-threshold", 30) * 1000L;
                     if (System.currentTimeMillis() - lastMove >= idleThreshold) {
                        long lastReward = (Long)ShardsPlugin.this.lastAfkRewardTime.getOrDefault(player.getUniqueId(), 0L);
                        long rewardInterval = (long)ShardsPlugin.this.getConfig().getInt("afk-reward-interval", 60) * 1000L;
                        if (System.currentTimeMillis() - lastReward >= rewardInterval) {
                           int reward = ShardsPlugin.this.getConfig().getInt("afk-reward-amount", 1);
                           ShardsPlugin.this.shardsManager.add(player.getUniqueId(), reward);
                           ShardsPlugin.this.lastAfkRewardTime.put(player.getUniqueId(), System.currentTimeMillis());
                        }
                     }
                  }
               }

            }
         }
      }).runTaskTimer(this, 0L, 20L);
   }

   @EventHandler
   public void onPlayerMove(PlayerMoveEvent event) {
      if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
         this.lastMoveTime.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
      }
   }

   @EventHandler
   public void onPlayerDeath(PlayerDeathEvent event) {
      Player victim = event.getEntity();
      Player killer = victim.getKiller();
      if (killer != null && !killer.equals(victim)) {
         int reward = this.getConfig().getInt("kill-reward-amount", 5);
         this.shardsManager.add(killer.getUniqueId(), reward);
      }

   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent event) {
      this.shardsManager.savePlayer(event.getPlayer().getUniqueId());
   }

   public ShardsManager getShardsManager() {
      return this.shardsManager;
   }

   public AfkZone getAfkZone() {
      return this.afkZone;
   }

   public AfkWandListener getAfkWandListener() {
      return this.afkWandListener;
   }
}
