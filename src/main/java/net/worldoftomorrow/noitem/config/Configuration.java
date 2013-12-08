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
		//this.defaultValues.put("notify.message", "&3You are not allowed to $1 this object.");
		this.defaultValues.put("notify.message.break", "&3You are not allowed to break $1.");
		this.defaultValues.put("notify.message.place", "&3You can not place $1.");
		this.defaultValues.put("notify.message.craft", "&3You can not craft $1.");
		this.defaultValues.put("notify.message.cook", "&3You are not allowed to cook this $1.");
		this.defaultValues.put("notify.message.open", "&3You are not allowed to open this.");
		this.defaultValues.put("notify.message.pickup", "&3You can not pick that up.");
		this.defaultValues.put("notify.message.drop", "&3You can not drop this.");
		this.defaultValues.put("notify.message.hold", "&3You can not hold that.");
		this.defaultValues.put("notify.message.interact.entity", "&3You can not interact with this entity.");
		this.defaultValues.put("notify.message.interact.object", "&3You can not interact with this object");
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
