package net.worldoftomorrow.noitem.actions;

import org.bukkit.inventory.ItemStack;

import net.worldoftomorrow.noitem.Util;

public class ActionDrop extends Action {
	public ActionDrop(ItemStack item) {
		super(ActionType.DROP, Util.getItemName(item));
	}
	
	public ActionDrop(ItemStack item, int dur) {
		super(ActionType.DROP, Util.getItemName(item), String.valueOf(dur));
	}
}
