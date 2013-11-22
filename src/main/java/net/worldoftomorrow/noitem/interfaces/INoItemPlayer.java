package net.worldoftomorrow.noitem.interfaces;

import org.bukkit.entity.Player;

public interface INoItemPlayer {
	
	/**
	 * Check if the player do the given action
	 */
	public boolean canDoAction(IAction a);
	
	/**
	 * Check if the player should be notified about
	 * not being able to do an action at this time
	 * @return
	 */
	public boolean shouldNotify(IAction a);
	
	/**
	 * Reloads the effective permissions of the player
	 */
	public void reloadPermissions();
	
	/**
	 * Get the bukkit player associated with this object
	 * @return
	 */
	public Player getPlayer();
	
	public void notifyPlayer(IAction action);
}
