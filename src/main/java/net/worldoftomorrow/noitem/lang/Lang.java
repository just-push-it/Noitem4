package net.worldoftomorrow.noitem.lang;

import net.worldoftomorrow.noitem.exceptions.NoSuchLangValueException;
import net.worldoftomorrow.noitem.interfaces.ILang;
import net.worldoftomorrow.noitem.interfaces.ILangFile;
import net.worldoftomorrow.noitem.interfaces.INoItem;

public class Lang implements ILang {
	
	private ILangFile langFile;
	
	public Lang(INoItem plugin) {
		this.langFile = plugin.getLangFile();
	}

	public String getDefaultValue(String key) {
		return this.langFile.getDefaultValue(key);
	}

	public String getValue(String key) {
		String val = langFile.getValue(key);
		if(val == null && !langFile.hasDefaultValue(key)) {
			// Throw a runtime exception if there is not a lang value
			throw new NoSuchLangValueException(key);
		} else if (val == null && langFile.hasDefaultValue(key)) {
			return langFile.getDefaultValue(key);
		}
		return val;
	}

	public String $(String key) {
		return getValue(key);
	}

}
