package net.worldoftomorrow.noitem.interfaces;

import org.bukkit.event.Cancellable;

import net.worldoftomorrow.noitem.actions.ActionType;

public interface IAction {
	
	public String getObjectPerm();
	
	public String getActionPerm();
	
	public String getAllActionPerm();
	
	public String getAllObjectPerm();
	
	public String[] getAllPerms();
	
	public String getObject();
	
	public ActionType getActionType();
	
	public void process(INoItemPlayer player, Cancellable event);
}
