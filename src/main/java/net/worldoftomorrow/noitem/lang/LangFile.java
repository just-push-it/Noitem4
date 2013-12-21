package net.worldoftomorrow.noitem.lang;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import net.worldoftomorrow.noitem.interfaces.ILangFile;

import org.bukkit.configuration.file.YamlConfiguration;

public class LangFile implements ILangFile {
	private YamlConfiguration langFile;
	private File actualFile;
	private HashMap<String, String> defaultValues = new HashMap<String, String>();
	
	public LangFile(YamlConfiguration langFile, File file) {
		this.langFile = langFile;
		this.actualFile = file;
		
		this.defaultValues.put("command.usage", "Usage: /noitem reload <playername> [-q (quiet)]");
		this.defaultValues.put("command.nopermission", "You do not have permission to perform this command.");
		this.defaultValues.put("command.playernotfound", "Player not found.");
		this.defaultValues.put("command.reloadsuccess", "The players permissions are successfully reloaded.");
		
		this.saveLangFile();
	}
	
	public boolean hasDefaultValue(String key) {
		return defaultValues.containsKey(key);
	}

	public String getDefaultValue(String key) {
		return defaultValues.get(key);
	}

	public String getValue(String key) {
		return langFile.getString(key);
	}
	
	public void saveLangFile() {
		for(Entry<String, String> entry : defaultValues.entrySet()) {
			if(!langFile.isSet(entry.getKey())) {
				langFile.set(entry.getKey(), entry.getValue());
			}
		}
		if(this.actualFile == null) return;
		try {
			langFile.save(actualFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
