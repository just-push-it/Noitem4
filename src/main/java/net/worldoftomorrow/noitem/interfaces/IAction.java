package net.worldoftomorrow.noitem.interfaces;

import net.worldoftomorrow.noitem.ActionType;

public interface IAction {
	
	public String getObjectPerm();
	
	public String getActionPerm();
	
	public String getAllActionPerm();
	
	public String getAllObjectPerm();
	
	public String[] getAllPerms();
	
	public String getObject();
	
	public ActionType getActionType();
}
