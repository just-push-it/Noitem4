package net.worldoftomorrow.noitem.actions;

import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import net.worldoftomorrow.noitem.Util;
import net.worldoftomorrow.noitem.config.ConfigManager;
import net.worldoftomorrow.noitem.interfaces.INoItemPlayer;

public class ActionHoldPickup extends Action {
	public ActionHoldPickup(ItemStack toCheck) {
		super(ActionType.HOLD, Util.getItemName(toCheck));
	}
	
	public ActionHoldPickup(ItemStack toCheck, int dur) {
		super(ActionType.HOLD, Util.getItemName(toCheck), String.valueOf(toCheck.getDurability()));
	}
	
	@Override
	public void process(INoItemPlayer player, Cancellable event) {
		if(event.isCancelled()) return;
		int firstEmpty = player.getPlayer().getInventory().firstEmpty();
		if(firstEmpty == player.getPlayer().getInventory().getHeldItemSlot()) {
			super.process(player, event);
		}
		// If the event is cancelled after being processed, then set the pickup delay
		if(event.isCancelled()) {
			int delay = Integer.valueOf(ConfigManager.getInstance().getValue("notify.timeout"));
			((PlayerPickupItemEvent) event).getItem().setPickupDelay(delay);
		}
	}
}
