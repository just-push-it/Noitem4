package net.worldoftomorrow.noitem.actions;

import net.worldoftomorrow.noitem.Util;

import org.bukkit.inventory.ItemStack;

public class ActionWear extends Action {

	public ActionWear(ItemStack item) {
		super(ActionType.WEAR, Util.getItemName(item));
	}

}
