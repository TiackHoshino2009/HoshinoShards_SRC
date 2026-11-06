package com.hoshinoshop.shards;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ShardsManager {
   private final JavaPlugin plugin;
   private final Map<UUID, Integer> balances = new HashMap();
   private final File dataFile;
   private final FileConfiguration dataConfig;

   public ShardsManager(JavaPlugin plugin) {
      this.plugin = plugin;
      this.dataFile = new File(plugin.getDataFolder(), "data.yml");
      if (!this.dataFile.exists()) {
         try {
            this.dataFile.createNewFile();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }

      this.dataConfig = YamlConfiguration.loadConfiguration(this.dataFile);
   }

   public void load() {
      for(String key : this.dataConfig.getKeys(false)) {
         try {
            UUID uuid = UUID.fromString(key);
            int balance = this.dataConfig.getInt(key);
            this.balances.put(uuid, balance);
         } catch (IllegalArgumentException var5) {
            this.plugin.getLogger().warning("UUID không hợp lệ trong data.yml: " + key);
         }
      }

      this.plugin.getLogger().info("Đã tải " + this.balances.size() + " dữ liệu người chơi.");
   }

   public void save() {
      for(Map.Entry<UUID, Integer> entry : this.balances.entrySet()) {
         this.dataConfig.set(((UUID)entry.getKey()).toString(), entry.getValue());
      }

      try {
         this.dataConfig.save(this.dataFile);
      } catch (IOException e) {
         e.printStackTrace();
      }

   }

   public void savePlayer(UUID uuid) {
      if (this.balances.containsKey(uuid)) {
         this.dataConfig.set(uuid.toString(), this.balances.get(uuid));

         try {
            this.dataConfig.save(this.dataFile);
         } catch (IOException e) {
            e.printStackTrace();
         }
      }

   }

   public int getBalance(UUID uuid) {
      return (Integer)this.balances.getOrDefault(uuid, 0);
   }

   public void setBalance(UUID uuid, int amount) {
      if (amount < 0) {
         amount = 0;
      }

      this.balances.put(uuid, amount);
      this.savePlayer(uuid);
   }

   public void add(UUID uuid, int amount) {
      this.setBalance(uuid, this.getBalance(uuid) + amount);
   }

   public void remove(UUID uuid, int amount) {
      this.setBalance(uuid, this.getBalance(uuid) - amount);
   }
}
