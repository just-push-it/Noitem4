package net.worldoftomorrow.noitem;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import net.worldoftomorrow.noitem.config.ConfigManager;
import net.worldoftomorrow.noitem.interfaces.IAction;
import net.worldoftomorrow.noitem.interfaces.INoItemPlayer;

public class NoItemPlayer implements INoItemPlayer  {
	
	public final Player player;
	private HashMap<String, Boolean> permissions;
	private HashMap<String, Long> notifyTimes = new HashMap<String, Long>();

	public NoItemPlayer(Player p) {
		this.player = p;
		this.reloadPermissions();
	}

	/**
	 * Checks to see if they have any permissions
	 * that would make this false.
	 */
	public boolean canDoAction(IAction a) {
		for(String perm : a.getAllPerms()) {
			Boolean has = permissions.get(perm);
			if(has != null && has) {
				return false;
			}
		}
		return true;
	}

	public boolean shouldNotify(IAction action) {
		// If notify is set to false in the configuration
		if(!Boolean.valueOf(ConfigManager.getInstance().getValue("notify.donotify")))
			return false;
		
		// If they have never been notified for this action
		if(!notifyTimes.containsKey(action.getActionPerm()))
			return true;
		
		Long last = notifyTimes.get(action.getActionPerm());
		int timeout = Integer.parseInt(ConfigManager.getInstance().getValue("notify.timeout"));
		//If the time since the last message is greater than or = to the timout period
		if((System.currentTimeMillis() - last) / 1000 >= timeout) {
			return true;
		} else return false;
	}
	
	//TODO: UNIT TESTS
	public void reloadPermissions() {
		// Blank the permissions
		this.permissions = new HashMap<String, Boolean>();
		
		// Set load them from the effective permissions
		Iterator<PermissionAttachmentInfo> perms = player.getEffectivePermissions().iterator();
		while(perms.hasNext()) {
			PermissionAttachmentInfo pai = perms.next();
			this.permissions.put(pai.getPermission(), pai.getValue());
		}
	}
	
	public Player getPlayer() {
		return this.player;
	}

	public void notifyPlayer(IAction action) {
		if(this.shouldNotify(action)) {
			String msg = ConfigManager.getInstance().getValue("notify.message");
			msg = ChatColor.translateAlternateColorCodes('&', msg);
			// Send the message
			this.player.sendMessage(msg.replaceAll("\\$1", action.getActionType().name));
			// Update the last notification time
			this.notifyTimes.put(action.getActionPerm(), System.currentTimeMillis());
		}
	}

}
