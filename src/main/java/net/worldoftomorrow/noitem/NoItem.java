package net.worldoftomorrow.noitem;

import java.io.File;

import net.worldoftomorrow.noitem.config.ConfigManager;
import net.worldoftomorrow.noitem.config.Configuration;
import net.worldoftomorrow.noitem.interfaces.IConfigManager;
import net.worldoftomorrow.noitem.interfaces.IConfiguration;
import net.worldoftomorrow.noitem.interfaces.ILang;
import net.worldoftomorrow.noitem.interfaces.ILangFile;
import net.worldoftomorrow.noitem.interfaces.INoItem;
import net.worldoftomorrow.noitem.interfaces.INoItemPlayer;
import net.worldoftomorrow.noitem.lang.Lang;
import net.worldoftomorrow.noitem.lang.LangFile;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class NoItem extends JavaPlugin implements INoItem {

	private IConfigManager configMan;
	private ILang lang;
	private ActionFactory actionFactory = new ActionFactory();

	@Override
	public void onEnable() {
		this.configMan = new ConfigManager(this);
		this.lang = new Lang(this);
		this.getServer().getPluginManager().registerEvents(actionFactory, this);
		this.getCommand("noitem").setExecutor(new CommandProcessor(this));
	}
	
	public IConfiguration getConfigFile() {
		File configFile = new File(this.getDataFolder() + File.separator + "config.yml");
		YamlConfiguration yamlconf = YamlConfiguration.loadConfiguration(configFile);
		return new Configuration(yamlconf, configFile);
	}
	
	public ILangFile getLangFile() {
		File langFile = new File(this.getDataFolder() + File.separator + "lang.yml");
		YamlConfiguration yamlLang = YamlConfiguration.loadConfiguration(langFile);
		return new LangFile(yamlLang, langFile);
	}
	
	public IConfigManager getConfigManager() {
		return this.configMan;
	}
	
	public INoItemPlayer getNoItemPlayer(Player player) {
		return actionFactory.getPlayer(player);
	}
	
	public static NoItem getInstance() {
		return (NoItem) Bukkit.getPluginManager().getPlugin("NoItem");
	}

	public ILang getLang() {
		return this.lang;
	}

}
