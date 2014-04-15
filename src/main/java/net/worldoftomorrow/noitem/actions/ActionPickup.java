package net.worldoftomorrow.noitem.actions;

import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import net.worldoftomorrow.noitem.Util;
import net.worldoftomorrow.noitem.config.ConfigManager;
import net.worldoftomorrow.noitem.interfaces.INoItemPlayer;

public class ActionPickup extends Action {
	public ActionPickup(ItemStack stack) {
		super(ActionType.PICKUP, Util.getItemName(stack));
	}
	
	public ActionPickup(ItemStack stack, int dur) {
		super(ActionType.PICKUP, Util.getItemName(stack), String.valueOf(dur));
	}
	
	@Override
	public void process(INoItemPlayer player, Cancellable event) {
		super.process(player, event);
		if(event.isCancelled()) {
			int delay = Integer.valueOf(ConfigManager.getInstance().getValue("notify.timeout"));
			((PlayerPickupItemEvent) event).getItem().setPickupDelay(delay);
		}
	}
}
