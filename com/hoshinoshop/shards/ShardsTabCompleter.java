package com.hoshinoshop.shards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class ShardsTabCompleter implements TabCompleter {
   public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
      List<String> completions = new ArrayList();
      if (args.length == 1) {
         for(String s : Arrays.asList("balance", "give", "set", "afkzone", "reload", "help")) {
            if (s.startsWith(args[0].toLowerCase())) {
               completions.add(s);
            }
         }
      } else if (args.length == 2) {
         if (!args[0].equalsIgnoreCase("give") && !args[0].equalsIgnoreCase("set") && !args[0].equalsIgnoreCase("balance")) {
            if (args[0].equalsIgnoreCase("afkzone")) {
               for(String a : Arrays.asList("set", "remove", "wand", "save", "clear")) {
                  if (a.startsWith(args[1].toLowerCase())) {
                     completions.add(a);
                  }
               }
            }
         } else {
            for(Player p : Bukkit.getOnlinePlayers()) {
               if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                  completions.add(p.getName());
               }
            }
         }
      } else if (args.length >= 3 && args[0].equalsIgnoreCase("afkzone") && args[1].equalsIgnoreCase("set") && args.length == 3) {
         for(World w : Bukkit.getWorlds()) {
            if (w.getName().toLowerCase().startsWith(args[2].toLowerCase())) {
               completions.add(w.getName());
            }
         }
      }

      return completions;
   }
}
