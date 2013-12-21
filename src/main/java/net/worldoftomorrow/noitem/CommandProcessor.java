package net.worldoftomorrow.noitem;

import net.worldoftomorrow.noitem.interfaces.INoItem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandProcessor implements CommandExecutor {
	
	private final INoItem plugin;
	
	public CommandProcessor(INoItem plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String cmdString, String[] args) {
		if(sender instanceof Player && !((Player) sender).hasPermission("noitem.admin")) {
			sender.sendMessage(ChatColor.RED + plugin.getLang().$("command.nopermission"));
			return true;
		}
		// /noitem reload <player> -q
		final String usage = ChatColor.RED + plugin.getLang().$("command.usage");	
		if (args.length >= 1 && args.length <= 3) {
			if(args[0].equalsIgnoreCase("reload") && args.length > 1) {
				Player player = Bukkit.getPlayer(args[1]);
				if(player == null) {
					sender.sendMessage(ChatColor.RED + plugin.getLang().$("command.playernotfound"));
					return true;
				}
				NoItem.getInstance().getNoItemPlayer(player).reloadPermissions();
				if(args.length == 2) {
					sender.sendMessage(ChatColor.RED + plugin.getLang().$("command.reloadsuccess"));
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
