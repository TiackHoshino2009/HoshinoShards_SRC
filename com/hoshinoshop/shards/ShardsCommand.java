package com.hoshinoshop.shards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShardsCommand implements CommandExecutor {
   private final ShardsPlugin plugin;

   public ShardsCommand(ShardsPlugin plugin) {
      this.plugin = plugin;
   }

   private void sendHelp(CommandSender sender) {
      sender.sendMessage(String.valueOf(ChatColor.GOLD) + "===== HoshinoShards Help =====");
      String var10001 = String.valueOf(ChatColor.YELLOW);
      sender.sendMessage(var10001 + "/shards balance [player]" + String.valueOf(ChatColor.WHITE) + " - Xem số Shards của bạn hoặc người khác");
      var10001 = String.valueOf(ChatColor.YELLOW);
      sender.sendMessage(var10001 + "/shards give <player> <amount>" + String.valueOf(ChatColor.WHITE) + " - Cho người chơi Shards (admin)");
      var10001 = String.valueOf(ChatColor.YELLOW);
      sender.sendMessage(var10001 + "/shards set <player> <amount>" + String.valueOf(ChatColor.WHITE) + " - Đặt số Shards cho người chơi (admin)");
      var10001 = String.valueOf(ChatColor.YELLOW);
      sender.sendMessage(var10001 + "/shards afkzone wand" + String.valueOf(ChatColor.WHITE) + " - Nhận gậy chọn vùng AFK (admin)");
      var10001 = String.valueOf(ChatColor.YELLOW);
      sender.sendMessage(var10001 + "/shards afkzone save" + String.valueOf(ChatColor.WHITE) + " - Lưu vùng AFK đã chọn (admin)");
      var10001 = String.valueOf(ChatColor.YELLOW);
      sender.sendMessage(var10001 + "/shards afkzone clear" + String.valueOf(ChatColor.WHITE) + " - Xóa vùng chọn (admin)");
      var10001 = String.valueOf(ChatColor.YELLOW);
      sender.sendMessage(var10001 + "/shards afkzone set <world> <x1> <y1> <z1> <x2> <y2> <z2>" + String.valueOf(ChatColor.WHITE) + " - Thiết lập vùng AFK bằng tọa độ (admin)");
      var10001 = String.valueOf(ChatColor.YELLOW);
      sender.sendMessage(var10001 + "/shards afkzone remove" + String.valueOf(ChatColor.WHITE) + " - Xóa vùng AFK (admin)");
      var10001 = String.valueOf(ChatColor.YELLOW);
      sender.sendMessage(var10001 + "/shards reload" + String.valueOf(ChatColor.WHITE) + " - Tải lại cấu hình (admin)");
      var10001 = String.valueOf(ChatColor.YELLOW);
      sender.sendMessage(var10001 + "/shards help" + String.valueOf(ChatColor.WHITE) + " - Hiển thị trợ giúp này");
      sender.sendMessage(String.valueOf(ChatColor.GRAY) + "Placeholder: %hoshinoshard_shards% hiển thị số Shards của bạn.");
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (args.length == 0) {
         this.sendHelp(sender);
         return true;
      } else if (args[0].equalsIgnoreCase("help")) {
         this.sendHelp(sender);
         return true;
      } else {
         String sub = args[0].toLowerCase();
         if (!sub.equals("balance") && !sub.equals("bal")) {
            if (sub.equals("give")) {
               if (!sender.hasPermission("shards.admin")) {
                  sender.sendMessage(String.valueOf(ChatColor.RED) + "Bạn không có quyền.");
                  return true;
               } else if (args.length != 3) {
                  sender.sendMessage(String.valueOf(ChatColor.RED) + "Sử dụng: /shards give <player> <amount>");
                  return true;
               } else {
                  Player target = Bukkit.getPlayer(args[1]);
                  if (target == null) {
                     sender.sendMessage(String.valueOf(ChatColor.RED) + "Người chơi không tồn tại.");
                     return true;
                  } else {
                     int amount;
                     try {
                        amount = Integer.parseInt(args[2]);
                     } catch (NumberFormatException var21) {
                        sender.sendMessage(String.valueOf(ChatColor.RED) + "Số lượng không hợp lệ.");
                        return true;
                     }

                     if (amount <= 0) {
                        sender.sendMessage(String.valueOf(ChatColor.RED) + "Số lượng phải lớn hơn 0.");
                        return true;
                     } else {
                        this.plugin.getShardsManager().add(target.getUniqueId(), amount);
                        String var34 = String.valueOf(ChatColor.GREEN);
                        sender.sendMessage(var34 + "Đã cho " + target.getName() + " " + amount + " Shards.");
                        var34 = String.valueOf(ChatColor.GREEN);
                        target.sendMessage(var34 + "Bạn nhận được " + amount + " Shards từ admin.");
                        return true;
                     }
                  }
               }
            } else if (sub.equals("set")) {
               if (!sender.hasPermission("shards.admin")) {
                  sender.sendMessage(String.valueOf(ChatColor.RED) + "Bạn không có quyền.");
                  return true;
               } else if (args.length != 3) {
                  sender.sendMessage(String.valueOf(ChatColor.RED) + "Sử dụng: /shards set <player> <amount>");
                  return true;
               } else {
                  Player target = Bukkit.getPlayer(args[1]);
                  if (target == null) {
                     sender.sendMessage(String.valueOf(ChatColor.RED) + "Người chơi không tồn tại.");
                     return true;
                  } else {
                     int amount;
                     try {
                        amount = Integer.parseInt(args[2]);
                     } catch (NumberFormatException var22) {
                        sender.sendMessage(String.valueOf(ChatColor.RED) + "Số lượng không hợp lệ.");
                        return true;
                     }

                     if (amount < 0) {
                        amount = 0;
                     }

                     this.plugin.getShardsManager().setBalance(target.getUniqueId(), amount);
                     String var33 = String.valueOf(ChatColor.GREEN);
                     sender.sendMessage(var33 + "Đã đặt Shards của " + target.getName() + " thành " + amount);
                     return true;
                  }
               }
            } else if (!sub.equals("afkzone") && !sub.equals("zone")) {
               if (sub.equals("reload")) {
                  if (!sender.hasPermission("shards.admin")) {
                     sender.sendMessage(String.valueOf(ChatColor.RED) + "Bạn không có quyền.");
                     return true;
                  } else {
                     this.plugin.reloadConfig();
                     this.plugin.getAfkZone().update(this.plugin.getConfig());
                     sender.sendMessage(String.valueOf(ChatColor.GREEN) + "Đã tải lại cấu hình.");
                     return true;
                  }
               } else {
                  sender.sendMessage(String.valueOf(ChatColor.RED) + "Lệnh không hợp lệ. Gõ /shards help để xem hướng dẫn.");
                  return true;
               }
            } else if (!sender.hasPermission("shards.admin")) {
               sender.sendMessage(String.valueOf(ChatColor.RED) + "Bạn không có quyền.");
               return true;
            } else if (args.length < 2) {
               sender.sendMessage(String.valueOf(ChatColor.RED) + "Sử dụng: /shards afkzone <wand|save|clear|set|remove>");
               return true;
            } else {
               String action = args[1].toLowerCase();
               if (action.equals("wand")) {
                  if (!(sender instanceof Player)) {
                     sender.sendMessage(String.valueOf(ChatColor.RED) + "Chỉ người chơi mới có thể dùng lệnh này.");
                     return true;
                  } else if (this.plugin.getAfkWandListener() == null) {
                     sender.sendMessage(String.valueOf(ChatColor.RED) + "Lỗi: chưa khởi tạo wand listener.");
                     return true;
                  } else {
                     this.plugin.getAfkWandListener().giveWand((Player)sender);
                     return true;
                  }
               } else if (action.equals("save")) {
                  if (!(sender instanceof Player)) {
                     sender.sendMessage(String.valueOf(ChatColor.RED) + "Chỉ người chơi mới có thể dùng lệnh này.");
                     return true;
                  } else if (this.plugin.getAfkWandListener() == null) {
                     sender.sendMessage(String.valueOf(ChatColor.RED) + "Lỗi: chưa khởi tạo wand listener.");
                     return true;
                  } else {
                     this.plugin.getAfkWandListener().saveSelection((Player)sender);
                     return true;
                  }
               } else if (action.equals("clear")) {
                  if (!(sender instanceof Player)) {
                     sender.sendMessage(String.valueOf(ChatColor.RED) + "Chỉ người chơi mới có thể dùng lệnh này.");
                     return true;
                  } else if (this.plugin.getAfkWandListener() == null) {
                     sender.sendMessage(String.valueOf(ChatColor.RED) + "Lỗi: chưa khởi tạo wand listener.");
                     return true;
                  } else {
                     this.plugin.getAfkWandListener().clearSelection((Player)sender);
                     return true;
                  }
               } else if (action.equals("set")) {
                  if (args.length != 9) {
                     sender.sendMessage(String.valueOf(ChatColor.RED) + "Sử dụng: /shards afkzone set <world> <x1> <y1> <z1> <x2> <y2> <z2>");
                     return true;
                  } else {
                     String worldName = args[2];

                     double x1;
                     double y1;
                     double z1;
                     double x2;
                     double y2;
                     double z2;
                     try {
                        x1 = Double.parseDouble(args[3]);
                        y1 = Double.parseDouble(args[4]);
                        z1 = Double.parseDouble(args[5]);
                        x2 = Double.parseDouble(args[6]);
                        y2 = Double.parseDouble(args[7]);
                        z2 = Double.parseDouble(args[8]);
                     } catch (NumberFormatException var23) {
                        sender.sendMessage(String.valueOf(ChatColor.RED) + "Tọa độ không hợp lệ.");
                        return true;
                     }

                     this.plugin.getConfig().set("afk-zone-enabled", true);
                     this.plugin.getConfig().set("afk-zone.world", worldName);
                     this.plugin.getConfig().set("afk-zone.min-x", Math.min(x1, x2));
                     this.plugin.getConfig().set("afk-zone.max-x", Math.max(x1, x2));
                     this.plugin.getConfig().set("afk-zone.min-y", Math.min(y1, y2));
                     this.plugin.getConfig().set("afk-zone.max-y", Math.max(y1, y2));
                     this.plugin.getConfig().set("afk-zone.min-z", Math.min(z1, z2));
                     this.plugin.getConfig().set("afk-zone.max-z", Math.max(z1, z2));
                     this.plugin.saveConfig();
                     this.plugin.getAfkZone().update(this.plugin.getConfig());
                     sender.sendMessage(String.valueOf(ChatColor.GREEN) + "Đã cập nhật khu AFK.");
                     return true;
                  }
               } else if (action.equals("remove")) {
                  this.plugin.getConfig().set("afk-zone-enabled", false);
                  this.plugin.saveConfig();
                  this.plugin.getAfkZone().update(this.plugin.getConfig());
                  sender.sendMessage(String.valueOf(ChatColor.GREEN) + "Đã xóa khu AFK.");
                  return true;
               } else {
                  sender.sendMessage(String.valueOf(ChatColor.RED) + "Hành động không hợp lệ. Dùng wand, save, clear, set hoặc remove.");
                  return true;
               }
            }
         } else if (!sender.hasPermission("shards.balance")) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + "Bạn không có quyền.");
            return true;
         } else {
            if (args.length == 1 && sender instanceof Player) {
               Player p = (Player)sender;
               int bal = this.plugin.getShardsManager().getBalance(p.getUniqueId());
               String var32 = String.valueOf(ChatColor.GREEN);
               sender.sendMessage(var32 + "Số Shards của bạn: " + String.valueOf(ChatColor.GOLD) + bal);
            } else if (args.length == 2) {
               Player target = Bukkit.getPlayer(args[1]);
               if (target == null) {
                  sender.sendMessage(String.valueOf(ChatColor.RED) + "Người chơi không tồn tại.");
                  return true;
               }

               int bal = this.plugin.getShardsManager().getBalance(target.getUniqueId());
               String var10001 = String.valueOf(ChatColor.GREEN);
               sender.sendMessage(var10001 + target.getName() + " có " + String.valueOf(ChatColor.GOLD) + bal + " Shards.");
            } else {
               sender.sendMessage(String.valueOf(ChatColor.RED) + "Sử dụng: /shards balance [player]");
            }

            return true;
         }
      }
   }
}
