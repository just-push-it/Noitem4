package net.worldoftomorrow.noitem.exceptions;

public class NoSuchConfigValueException extends RuntimeException {
	private static final long serialVersionUID = 7059934497873420837L;
	
	String msg;
	
	public NoSuchConfigValueException(String badKey) {
		msg = "There is real or default value set for the key \"" + badKey + "\"";
	}
	
	public String getMessage() {
		return msg;
	}

}
