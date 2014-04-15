package net.worldoftomorrow.noitem;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.worldoftomorrow.noitem.config.ConfigManager;
import net.worldoftomorrow.noitem.interfaces.IAction;
import net.worldoftomorrow.noitem.interfaces.INoItemPlayer;

public class NoItemPlayer implements INoItemPlayer  {

	public final UUID playerUUID;
	//private HashMap<String, Boolean> permissions;
	private HashMap<String, Long> notifyTimes = new HashMap<String, Long>();
	private long holdLastCancel = 0L;
	private boolean taskScheduled = false;

	public NoItemPlayer(Player p) {
		this.playerUUID = p.getUniqueId();
		this.reloadPermissions();
	}

	/**
	 * Checks to see if they have any permissions
	 * that would make this false.
	 */
	public boolean canDoAction(IAction a) {
		boolean whitelist = NoItem.getInstance().inWhitelistMode();
		for(String perm : a.getAllPerms()) {
			//Boolean has = permissions.get(perm);
			boolean has = getPlayer().hasPermission(perm);
			if(has) {
				return whitelist; // false -> whitelist
			}
		}
		return !whitelist; // true -> !whitelist
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
	
	public void reloadPermissions() {
		/*
		// Blank the permissions
		this.permissions = new HashMap<String, Boolean>();
		
		// Set load them from the effective permissions
		Iterator<PermissionAttachmentInfo> perms = Bukkit.getPlayer(playerUUID).getEffectivePermissions().iterator();
		while(perms.hasNext()) {
			PermissionAttachmentInfo pai = perms.next();
			this.permissions.put(pai.getPermission(), pai.getValue());
		}
		*/
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(playerUUID);
	}

	public void notifyPlayer(IAction action) {
		if(this.shouldNotify(action)) {
			String msg = NoItem.getInstance().getLang().$("notify.message." + action.getActionType().name);
			msg = ChatColor.translateAlternateColorCodes('&', msg);
			// Send the message
			Bukkit.getPlayer(playerUUID).sendMessage(msg.replaceAll("\\$1", action.getObject()));
			// Update the last notification time
			this.notifyTimes.put(action.getActionPerm(), System.currentTimeMillis());
		}
	}
	
	public long getLastHoldCancel() {
		return this.holdLastCancel;
	}
	
	public void setLastHoldCancel(long time) {
		this.holdLastCancel = time;
	}
	
	public boolean isTaskScheduled() {
		return this.taskScheduled;
	}
	
	public void setTaskScheduled(boolean value) {
		this.taskScheduled = value;
	}

}
