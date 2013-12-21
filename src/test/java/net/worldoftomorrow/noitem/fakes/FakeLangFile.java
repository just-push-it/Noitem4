package net.worldoftomorrow.noitem.fakes;

import java.util.HashMap;

import net.worldoftomorrow.noitem.interfaces.ILangFile;

public class FakeLangFile implements ILangFile {

	public HashMap<String, String> values = new HashMap<String, String>();
	public HashMap<String, String> defaults = new HashMap<String, String>();
	
	public FakeLangFile() {
		this.defaults.put("command.test.test", "test value");
		this.defaults.put("command.test.customval", "default custom value");
		this.values.put("command.test.customval", "changed custom value");
		
		//Actual default values because they are needed in other tests
		this.defaults.put("command.usage", "Usage: /noitem reload <playername> [-q (quiet)]");
		this.defaults.put("command.nopermission", "You do not have permission to perform this command.");
		this.defaults.put("command.playernotfound", "Player not found.");
		this.defaults.put("command.reloadsuccess", "The players permissions are successfully reloaded.");
		
	}
	
	public boolean hasDefaultValue(String key) {
		return defaults.containsKey(key);
	}

	public String getDefaultValue(String key) {
		return defaults.get(key);
	}

	public String getValue(String key) {
		return values.get(key);
	}

	public void saveLangFile() {
	}

}
