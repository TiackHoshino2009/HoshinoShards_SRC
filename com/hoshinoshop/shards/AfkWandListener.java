package com.hoshinoshop.shards;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AfkWandListener implements Listener {
   private final ShardsPlugin plugin;
   private final Map<UUID, Location> pos1 = new HashMap();
   private final Map<UUID, Location> pos2 = new HashMap();

   public AfkWandListener(ShardsPlugin plugin) {
      this.plugin = plugin;
   }

   public void giveWand(Player player) {
      ItemStack wand = new ItemStack(Material.WOODEN_AXE);
      ItemMeta meta = wand.getItemMeta();
      meta.setDisplayName(String.valueOf(ChatColor.LIGHT_PURPLE) + "AFK Zone Wand");
      meta.setLore(Arrays.asList(String.valueOf(ChatColor.GRAY) + "Click trái vào block để chọn điểm #1", String.valueOf(ChatColor.GRAY) + "Click phải vào block để chọn điểm #2", String.valueOf(ChatColor.GRAY) + "Sau đó dùng /shards afkzone save để lưu"));
      wand.setItemMeta(meta);
      player.getInventory().addItem(new ItemStack[]{wand});
      player.sendMessage(String.valueOf(ChatColor.GREEN) + "Bạn đã nhận gậy chọn vùng AFK. Dùng nó để chọn hai điểm.");
   }

   public void clearSelection(Player player) {
      this.pos1.remove(player.getUniqueId());
      this.pos2.remove(player.getUniqueId());
      player.sendMessage(String.valueOf(ChatColor.YELLOW) + "Đã xóa vùng chọn.");
   }

   @EventHandler
   public void onPlayerInteract(PlayerInteractEvent event) {
      Player player = event.getPlayer();
      ItemStack item = event.getItem();
      if (item != null && item.getType() == Material.WOODEN_AXE) {
         if (item.hasItemMeta()) {
            if (item.getItemMeta().hasDisplayName()) {
               if (item.getItemMeta().getDisplayName().equals(String.valueOf(ChatColor.LIGHT_PURPLE) + "AFK Zone Wand")) {
                  event.setCancelled(true);
                  if (event.getClickedBlock() != null) {
                     Location loc = event.getClickedBlock().getLocation();
                     if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                        this.pos1.put(player.getUniqueId(), loc.clone());
                        String var10001 = String.valueOf(ChatColor.GREEN);
                        player.sendMessage(var10001 + "Điểm #1 đã đặt tại " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + " trong world " + loc.getWorld().getName());
                     } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        this.pos2.put(player.getUniqueId(), loc.clone());
                        String var5 = String.valueOf(ChatColor.GREEN);
                        player.sendMessage(var5 + "Điểm #2 đã đặt tại " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + " trong world " + loc.getWorld().getName());
                     }

                  }
               }
            }
         }
      }
   }

   public Location getPos1(Player player) {
      return (Location)this.pos1.get(player.getUniqueId());
   }

   public Location getPos2(Player player) {
      return (Location)this.pos2.get(player.getUniqueId());
   }

   public void saveSelection(Player player) {
      Location p1 = (Location)this.pos1.get(player.getUniqueId());
      Location p2 = (Location)this.pos2.get(player.getUniqueId());
      if (p1 != null && p2 != null) {
         if (!p1.getWorld().equals(p2.getWorld())) {
            player.sendMessage(String.valueOf(ChatColor.RED) + "Hai điểm phải cùng world.");
         } else {
            double minX = Math.min(p1.getX(), p2.getX());
            double maxX = Math.max(p1.getX(), p2.getX());
            double minY = Math.min(p1.getY(), p2.getY());
            double maxY = Math.max(p1.getY(), p2.getY());
            double minZ = Math.min(p1.getZ(), p2.getZ());
            double maxZ = Math.max(p1.getZ(), p2.getZ());
            String worldName = p1.getWorld().getName();
            this.plugin.getConfig().set("afk-zone-enabled", true);
            this.plugin.getConfig().set("afk-zone.world", worldName);
            this.plugin.getConfig().set("afk-zone.min-x", minX);
            this.plugin.getConfig().set("afk-zone.max-x", maxX);
            this.plugin.getConfig().set("afk-zone.min-y", minY);
            this.plugin.getConfig().set("afk-zone.max-y", maxY);
            this.plugin.getConfig().set("afk-zone.min-z", minZ);
            this.plugin.getConfig().set("afk-zone.max-z", maxZ);
            this.plugin.saveConfig();
            this.plugin.getAfkZone().update(this.plugin.getConfig());
            player.sendMessage(String.valueOf(ChatColor.GREEN) + "Đã lưu khu AFK thành công!");
            this.clearSelection(player);
         }
      } else {
         player.sendMessage(String.valueOf(ChatColor.RED) + "Bạn cần chọn cả hai điểm trước khi lưu.");
      }
   }
}
