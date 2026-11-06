package com.hoshinoshop.shards;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class AfkZone {
   private boolean enabled;
   private String worldName;
   private double minX;
   private double maxX;
   private double minY;
   private double maxY;
   private double minZ;
   private double maxZ;

   public AfkZone(FileConfiguration config) {
      this.update(config);
   }

   public void update(FileConfiguration config) {
      this.enabled = config.getBoolean("afk-zone-enabled", true);
      if (this.enabled) {
         this.worldName = config.getString("afk-zone.world", "world");
         this.minX = config.getDouble("afk-zone.min-x", (double)-100.0F);
         this.maxX = config.getDouble("afk-zone.max-x", (double)100.0F);
         this.minY = config.getDouble("afk-zone.min-y", (double)0.0F);
         this.maxY = config.getDouble("afk-zone.max-y", (double)256.0F);
         this.minZ = config.getDouble("afk-zone.min-z", (double)-100.0F);
         this.maxZ = config.getDouble("afk-zone.max-z", (double)100.0F);
      }

   }

   public boolean contains(Location location) {
      if (this.enabled && location != null) {
         World world = Bukkit.getWorld(this.worldName);
         if (world != null && location.getWorld().equals(world)) {
            double x = location.getX();
            double y = location.getY();
            double z = location.getZ();
            return x >= this.minX && x <= this.maxX && y >= this.minY && y <= this.maxY && z >= this.minZ && z <= this.maxZ;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
