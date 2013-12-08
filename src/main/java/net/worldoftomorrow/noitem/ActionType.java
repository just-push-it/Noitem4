package net.worldoftomorrow.noitem;

public enum ActionType {
	BREAK("break"),
	PLACE("place"),
	CRAFT("craft"),
	COOK("cook"),
	OPEN("open"),
	PICKUP("pickup"),
	DROP("drop"),
	BREW("brew"), //
	HOLD("hold"),
	INTERACT_ENTITY("interact.entity"),
	INTERACT_OBJECT("interact.object"),
	ATTACK("attack");
	// Wear
	// Enchant
	// Repair
	// Interact (click on object?) & Use (click with object in hand?)
	
	String name;
	
	private ActionType(String name) {
		this.name = name;
	}
}
