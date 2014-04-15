package net.worldoftomorrow.noitem.actions.special;

import org.bukkit.Bukkit;

import net.worldoftomorrow.noitem.H4x;
import net.worldoftomorrow.noitem.NoItem;
import net.worldoftomorrow.noitem.interfaces.INoItemPlayer;

public class HoldTask implements Runnable {
	
	private int slot;
	private INoItemPlayer player;
	
	public HoldTask(int slot, INoItemPlayer player) {
		this.slot = slot;
		this.player = player;
	}

	public void run() {
		if(System.currentTimeMillis() - player.getLastHoldCancel() >= 500) {
			H4x.setHeldItemSlot(slot, player);
			player.setTaskScheduled(false);
			return;
		} else {
			Bukkit.getScheduler().scheduleSyncDelayedTask(NoItem.getInstance(), this, 4);
			player.setTaskScheduled(true);
			return;
		}
	}

}
