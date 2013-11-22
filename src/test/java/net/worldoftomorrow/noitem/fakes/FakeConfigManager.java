package net.worldoftomorrow.noitem.fakes;

import java.io.File;

import net.worldoftomorrow.noitem.interfaces.IConfigManager;
import net.worldoftomorrow.noitem.interfaces.IConfiguration;

public class FakeConfigManager implements IConfigManager {

	IConfiguration config;
	
	public void loadConfig(File configFile) {
	}

	public String getDefaultValue(String key) {
		return null;
	}

	public String getValue(String key) {
		return null;
	}

	public void setValue(String key, Object value) {
	}

}
