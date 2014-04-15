package net.worldoftomorrow.noitem;

import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Util {
	public static String getBlockName(Block b) {
		return b.getType().toString().toLowerCase();
	}
	
	public static String getInventoryTypeName(Inventory inv) {
		return inv.getType().toString().toLowerCase();
	}
	
	public static String getItemName(ItemStack stack) {
		return stack.getType().toString().toLowerCase();
	}
}
