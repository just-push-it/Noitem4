package net.worldoftomorrow.noitem.exceptions;

public class NoSuchLangValueException extends RuntimeException {
	private static final long serialVersionUID = -7780415162701238682L;
	
	String msg;
	
	public NoSuchLangValueException(String badKey) {
		msg = "There is real or default value set for the key \"" + badKey + "\"";
	}
	
	public String getMessage() {
		return msg;
	}

}
