package net.worldoftomorrow.noitem.actions;

import org.bukkit.inventory.Inventory;

import net.worldoftomorrow.noitem.Util;

public class ActionOpen extends Action {
	public ActionOpen(Inventory inv) {
		super(ActionType.OPEN, Util.getInventoryTypeName(inv));
	}
}
