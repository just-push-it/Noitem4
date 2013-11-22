package net.worldoftomorrow.noitem;

import net.worldoftomorrow.noitem.interfaces.IAction;

public class Action implements IAction {
	
	private final String objPerm;
	private final String actPerm;
	private final String objPermAll;
	private final String actPermAll;
	private final String object;
	private final ActionType actionType;
	
	public Action(ActionType actionType, String object) {
		// Checks:  The object for this action
		this.objPerm = "noitem.object." + object + "." + actionType.name;
		// The action for this object
		this.actPerm = "noitem.action." + actionType.name + "." + object;
		// All objects for this action
		this.actPermAll = "noitem.action." + actionType.name + ".*";
		// All actions for this object
		this.objPermAll = "noitem.object." + object + ".*";
		this.object = object;
		this.actionType = actionType;
	}
	
	public Action(ActionType actionType, String object, String secObject) {
		// The Object and second value for this action
		this.objPerm = "noitem.object." + object + "." + secObject + "." + actionType.name;
		// The action for this object with a second value
		this.actPerm = "noitem.action." + actionType.name + "." + object + "." + secObject;
		// All objects for this action
		this.actPermAll = "noitem.action." + actionType.name + ".*";
		// All actions for this object and second object
		this.objPermAll = "noitem.object." + object + "." + secObject + ".*";
		this.object = object;
		this.actionType = actionType;
	}

	public String getObjectPerm() {
		return this.objPerm;
	}
	
	public String getActionPerm() {
		return this.actPerm;
	}
	
	public String getAllActionPerm() {
		return this.actPermAll;
	}
	
	public String getAllObjectPerm() {
		return this.objPermAll;
	}
	
	public String[] getAllPerms() {
		return new String[] {
				this.actPerm,
				this.actPermAll,
				this.objPerm,
				this.objPermAll
		};
	}
	
	public String getObject() {
		return this.object;
	}
	
	public ActionType getActionType() {
		return this.actionType;
	}
}
