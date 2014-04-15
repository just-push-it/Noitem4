package net.worldoftomorrow.noitem.actions;

public enum ActionType {
	TEST("test"), // This should only be used for test actions
	BREAK("break"),
	PLACE("place"),
	CRAFT("craft"),
	COOK("cook"),
	OPEN("open"),
	PICKUP("pickup"),
	DROP("drop"),
	BREW("brew"),
	HOLD("hold"),
	INTERACT_ENTITY("interact.entity"),
	INTERACT_OBJECT("interact.object"),
	ATTACK("attack"),
	USE("use");
	// Wear
	// Enchant
	// Repair
	// Interact (click on object?) & Use (click with object in hand?)
	
	public String name;
	
	private ActionType(String name) {
		this.name = name;
	}
}
