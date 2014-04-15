package net.worldoftomorrow.noitem.actions;

import org.bukkit.inventory.ItemStack;

import net.worldoftomorrow.noitem.Util;

public class ActionHoldClick extends Action {
	public ActionHoldClick(ItemStack stack) {
		super(ActionType.HOLD, Util.getItemName(stack));
	}
	
	public ActionHoldClick(ItemStack stack, int dur) {
		super(ActionType.HOLD, Util.getItemName(stack), String.valueOf(dur));
	}
}
