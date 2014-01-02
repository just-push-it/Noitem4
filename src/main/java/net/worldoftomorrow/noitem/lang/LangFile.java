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
