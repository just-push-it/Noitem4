package net.worldoftomorrow.noitem.actions;

import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import net.worldoftomorrow.noitem.NoItem;
import net.worldoftomorrow.noitem.Util;
import net.worldoftomorrow.noitem.actions.special.HoldTask;
import net.worldoftomorrow.noitem.interfaces.INoItemPlayer;

public class ActionHoldScroll extends Action {
	public ActionHoldScroll(ItemStack stack) {
		super(ActionType.HOLD, Util.getItemName(stack));
	}
	
	public ActionHoldScroll(ItemStack stack, int dur) {
		super(ActionType.HOLD, Util.getItemName(stack), String.valueOf(dur));
	}
	
	@Override
	public void process(INoItemPlayer player, Cancellable event) {
		super.process(player, event);
		// Send another packet to set this just to make it harder, if not impossible to glitch the client into holding
		// the banned item by not sending a packet.
		if(event.isCancelled()) {
			PlayerItemHeldEvent e = (PlayerItemHeldEvent) event;

			// For if a player logs in and they are holding a (now) illegal item
			if(e.getPreviousSlot() == e.getNewSlot()) {
				int firstEmpty = e.getPlayer().getInventory().firstEmpty();
				ItemStack toMove = e.getPlayer().getItemInHand().clone();
				e.getPlayer().setItemInHand(null);
				// First empty returns -1 if no space available
				if(firstEmpty == -1) {
					e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), toMove);
				} else {
					e.getPlayer().getInventory().setItem(firstEmpty, toMove);
				}
				return;
			}
			/*
			 * This is a very hacky workaround and should be removed as soon as possible.
			 * It was needed because the client can sometimes switch the held item slot without
			 * the server knowing if it is done quickly enough. This simply resends the packet
			 * that tells the client to set the item held slot correctly.
			 */
			player.setLastHoldCancel(System.currentTimeMillis());
			if(!player.isTaskScheduled()) {
				int slot = e.getPreviousSlot();
				Bukkit.getScheduler().scheduleSyncDelayedTask(NoItem.getInstance(), new HoldTask(slot, player), 4);
			}
		}
	}
}
