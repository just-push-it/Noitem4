package net.worldoftomorrow.noitem;

public enum ActionType {
	BREAK("break"),
	PLACE("place"),
	CRAFT("craft"),
	COOK("cook"),
	OPEN("open"),
	PICKUP("pickup"),
	DROP("drop");
	
	String name;
	
	private ActionType(String name) {
		this.name = name;
	}
}
