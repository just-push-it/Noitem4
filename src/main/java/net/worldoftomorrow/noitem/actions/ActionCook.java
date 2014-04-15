package net.worldoftomorrow.noitem.actions;

import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

import net.worldoftomorrow.noitem.Util;
import net.worldoftomorrow.noitem.interfaces.INoItemPlayer;

public class ActionCook extends Action {

	public ActionCook(ItemStack stack) {
		super(ActionType.COOK, Util.getItemName(stack));
	}
	
	public ActionCook(ItemStack stack, int data) {
		super(ActionType.COOK, Util.getItemName(stack), String.valueOf(data));
	}
	
	@Override
	public void process(INoItemPlayer player, Cancellable event) {
		super.process(player, event);
		ItemStack toReplace = player.getPlayer().getItemOnCursor().clone();
		player.getPlayer().setItemOnCursor(null);
		player.getPlayer().getInventory().addItem(toReplace);
	}

}
