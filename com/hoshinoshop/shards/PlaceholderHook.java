package com.hoshinoshop.shards;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderHook extends PlaceholderExpansion {
   private final ShardsPlugin plugin;

   public PlaceholderHook(ShardsPlugin plugin) {
      this.plugin = plugin;
   }

   public @NotNull String getIdentifier() {
      return "hoshinoshard";
   }

   public @NotNull String getAuthor() {
      return "HoshinoShop";
   }

   public @NotNull String getVersion() {
      return "1.0.0";
   }

   public String onPlaceholderRequest(Player player, @NotNull String identifier) {
      if (player == null) {
         return "0";
      } else {
         return !identifier.equalsIgnoreCase("shards") && !identifier.equalsIgnoreCase("balance") ? null : String.valueOf(this.plugin.getShardsManager().getBalance(player.getUniqueId()));
      }
   }

   public boolean persist() {
      return true;
   }
}
