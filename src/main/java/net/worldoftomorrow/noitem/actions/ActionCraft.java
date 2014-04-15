package net.worldoftomorrow.noitem.actions;

import org.bukkit.inventory.ItemStack;

import net.worldoftomorrow.noitem.Util;

public class ActionCraft extends Action {
	public ActionCraft(ItemStack stack) {
		super(ActionType.CRAFT, Util.getItemName(stack));
	}
	
	public ActionCraft(ItemStack stack, int data) {
		super(ActionType.CRAFT, Util.getItemName(stack), String.valueOf(data));
	}
}
