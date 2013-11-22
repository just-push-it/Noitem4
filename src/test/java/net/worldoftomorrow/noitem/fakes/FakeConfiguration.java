package net.worldoftomorrow.noitem.fakes;

import java.util.HashMap;

import net.worldoftomorrow.noitem.interfaces.IConfiguration;

public class FakeConfiguration implements IConfiguration {
	
	public HashMap<String, String> values = new HashMap<String, String>();
	public HashMap<String, String> defaults = new HashMap<String, String>();

	public FakeConfiguration() {
		defaults.put("testvalue.2", "test2");
	}
	
	public boolean hasDefaultValue(String key) {
		return defaults.containsKey(key);
	}

	public String getDefaultValue(String key) {
		return defaults.get(key);
	}

	public void addDefaultValue(String key, String value, boolean overwrite) {
		if(defaults.containsKey(key) && overwrite) defaults.put(key, value);
		if(!defaults.containsKey(key)) defaults.put(key, value);
	}

	public void addDefaultValue(String key, String value) {
		addDefaultValue(key, value, false);
	}

	public String getValue(String key) {
		return values.get(key);
	}

	public void setValue(String key, Object value) {
		values.put(key, (String) value);
	}

	public void saveConfig() {
	}

}
