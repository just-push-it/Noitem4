package net.worldoftomorrow.noitem;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class DebugListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR)
	public void debugItemHeldEvent(PlayerItemHeldEvent event) {
		StringBuilder sb = new StringBuilder("[NoItem Debug] Item Held Fired: Prev=");
		sb.append(event.getPreviousSlot());
		sb.append(", New=");
		sb.append(event.getNewSlot());
		sb.append(", NewItem=");
		ItemStack newItem = event.getPlayer().getInventory().getItem(event.getNewSlot());
		if(newItem == null) {
			sb.append("null");
		} else {
			sb.append(newItem.getType().toString());
		}
		sb.append(", InHand=");
		sb.append(event.getPlayer().getItemInHand().getType().toString());
		sb.append(", Cancel=");
		sb.append(event.isCancelled());
		Bukkit.getLogger().info(sb.toString());
	}
	
	@EventHandler
	public void debugInventoryClick(InventoryClickEvent event) {
		StringBuilder sb = new StringBuilder("[NoItem Debug] Inventory Click Fired: RawSlot=");
		sb.append(event.getRawSlot());
		sb.append(", Current=");
		ItemStack curr = event.getCurrentItem();
		if(curr == null) {
			sb.append("null");
		} else {
			sb.append(event.getCurrentItem().getType().toString());
		}
		sb.append(", Cursor=");
		sb.append(event.getCursor().getType().toString());
		sb.append(", Cancel=");
		sb.append(event.isCancelled());
		sb.append("\nSlotType= " + event.getSlotType().toString());
		sb.append(", Top Slot Count= " + event.getView().getTopInventory().getSize());
		sb.append(", Bottom Slot Count= " + event.getView().getBottomInventory().getSize());
		sb.append(", Inv Type= " + event.getView().getType().toString());
		Bukkit.getLogger().info(sb.toString());
	}
	
}
