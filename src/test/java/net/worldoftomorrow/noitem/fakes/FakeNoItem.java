package net.worldoftomorrow.noitem.fakes;

import java.lang.reflect.Field;

import org.bukkit.plugin.java.JavaPlugin;

import net.worldoftomorrow.noitem.interfaces.IConfigManager;
import net.worldoftomorrow.noitem.interfaces.IConfiguration;
import net.worldoftomorrow.noitem.interfaces.ILang;
import net.worldoftomorrow.noitem.interfaces.ILangFile;
import net.worldoftomorrow.noitem.interfaces.INoItem;
import net.worldoftomorrow.noitem.lang.Lang;

public class FakeNoItem extends JavaPlugin implements INoItem {
	
	public FakeNoItem() {
		// Set enabled with uber 1337 hacks.
		try {
			Field isEnabledField = this.getClass().getSuperclass().getDeclaredField("isEnabled");
			isEnabledField.setAccessible(true);
			isEnabledField.set(this, true);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}

	public IConfiguration getConfigFile() {
		return new FakeConfiguration();
	}

	public IConfigManager getConfigManager() {
		return null;
	}

	public ILangFile getLangFile() {
		return new FakeLangFile();
	}

	public ILang getLang() {
		return new Lang(this);
	}

}
