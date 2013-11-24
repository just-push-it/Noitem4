package net.worldoftomorrow.noitem.config;

import net.worldoftomorrow.noitem.NoItem;
import net.worldoftomorrow.noitem.exceptions.NoSuchConfigValueException;
import net.worldoftomorrow.noitem.interfaces.IConfigManager;
import net.worldoftomorrow.noitem.interfaces.IConfiguration;
import net.worldoftomorrow.noitem.interfaces.INoItem;

public class ConfigManager implements IConfigManager {

	private IConfiguration config;
	
	public ConfigManager(INoItem plugin) {
		this.config = plugin.getConfigFile();
	}

	public String getDefaultValue(String key) {
		return config.getDefaultValue(key);
	}

	public String getValue(String key) {
		String val = config.getValue(key);
		if(val == null && !config.hasDefaultValue(key)) {
			// Throw a runtime exception if there is no such config value
			throw new NoSuchConfigValueException(key);
		} else if (val == null && config.hasDefaultValue(key)) {
			return config.getDefaultValue(key);
		}
		return val;
	}

	public void setValue(String key, Object value) {
		config.setValue(key, value.toString());
	}
	
	public IConfiguration getConfig() {
		return config;
	}
	
	public static IConfigManager getInstance() {
		return NoItem.getInstance().getConfigManager();
	}

}
