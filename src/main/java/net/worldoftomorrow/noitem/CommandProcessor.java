package net.worldoftomorrow.noitem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandProcessor implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String cmdString, String[] args) {
		if(sender instanceof Player && !((Player) sender).hasPermission("noitem.admin")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to perform this command.");
			return true;
		}
		// /noitem reload <player> -q
		final String usage = ChatColor.RED + "Usage: /noitem reload <playername> [-q (quiet)]";	
		if (args.length >= 1 && args.length <= 3) {
			if(args[0].equalsIgnoreCase("reload") && args.length > 1) {
				Player player = Bukkit.getPlayer(args[1]);
				if(player == null) {
					sender.sendMessage(ChatColor.RED + "Player not found.");
					return true;
				}
				NoItem.getInstance().getNoItemPlayer(player).reloadPermissions();
				if(args.length == 2) {
					sender.sendMessage(ChatColor.RED + "The players permissions are successfully reloaded.");
					return true;
										// Case matters with switch arguments. duh.
				} else if(args.length == 3) {
					if (args[2].equals("-q")) {
						return true;
					} else {
						sender.sendMessage(ChatColor.BLUE + "Who do you think you are?! You can't just put things willy-nilly at the end of commands!");
						sender.sendMessage(ChatColor.RED + "I reloaded your stinking permissions this time but don't let this happen again. ");
						return true;
					}
				}
			} else {
				sender.sendMessage(usage);
				return true;
			}
		} else {
			sender.sendMessage(usage);
			return true;
		}
		return false;
	}
}
