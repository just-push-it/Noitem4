package net.worldoftomorrow.noitem;

import java.io.File;

import net.worldoftomorrow.noitem.config.ConfigManager;
import net.worldoftomorrow.noitem.config.Configuration;
import net.worldoftomorrow.noitem.interfaces.IConfiguration;
import net.worldoftomorrow.noitem.interfaces.INoItem;
import net.worldoftomorrow.noitem.interfaces.INoItemPlayer;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class NoItem extends JavaPlugin implements INoItem {

	private ConfigManager configMan;
	private ActionFactory actionFactory = new ActionFactory();

	@Override
	public void onEnable() {
		this.configMan = new ConfigManager(this);
		this.getServer().getPluginManager().registerEvents(actionFactory, this);
		this.getCommand("noitem").setExecutor(new CommandProcessor());
	}
	
	public IConfiguration getConfigFile() {
		File configFile = new File(this.getDataFolder() + File.separator + "config.yml");
		YamlConfiguration yamlconf = YamlConfiguration.loadConfiguration(configFile);
		return new Configuration(yamlconf, configFile);
	}
	
	public ConfigManager getConfigManager() {
		return this.configMan;
	}
	
	public INoItemPlayer getNoItemPlayer(Player player) {
		return actionFactory.getPlayer(player);
	}
	
	public static NoItem getInstance() {
		return (NoItem) Bukkit.getPluginManager().getPlugin("NoItem");
	}

}
