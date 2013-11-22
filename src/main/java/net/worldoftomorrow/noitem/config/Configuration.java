package net.worldoftomorrow.noitem.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.configuration.file.YamlConfiguration;

import net.worldoftomorrow.noitem.interfaces.IConfiguration;

public class Configuration implements IConfiguration {
	
	private YamlConfiguration yamlConfig;
	private final File actualFile;
	private HashMap<String, String> defaultValues = new HashMap<String, String>();
	
	public Configuration(YamlConfiguration config, File file) {
		this.yamlConfig = config;
		this.actualFile = file;
		// Add Default Values Here
		this.defaultValues.put("notify.timeout", "5");
		this.defaultValues.put("notify.donotify", "true");
		this.defaultValues.put("notify.message", "&3You are not allowed to $1 this object.");
		// Then save the configuration
		this.saveConfig();
	}
	
	public boolean hasDefaultValue(String key) {
		return defaultValues.containsKey(key);
	}

	public String getDefaultValue(String key) {
		return defaultValues.get(key);
	}

	public void addDefaultValue(String key, String value, boolean overwrite) {
		if(defaultValues.containsKey(key) && overwrite) {
			defaultValues.put(key, value);
		}
	}

	public void addDefaultValue(String key, String value) {
		addDefaultValue(key, value, false);
	}

	public String getValue(String key) {
		return yamlConfig.getString(key);
	}

	public void setValue(String key, Object value) {
		yamlConfig.set(key, value);
	}
	
	public void saveConfig() {
		for(Entry<String, String> entry : defaultValues.entrySet()) {
			if(!yamlConfig.isSet(entry.getKey())) {
				yamlConfig.set(entry.getKey(), entry.getValue());
			}
		}
		try {
			yamlConfig.save(actualFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
