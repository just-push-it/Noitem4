package net.worldoftomorrow.noitem;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class ItemChecks {
	private static ArrayList<Material> interactable = new ArrayList<Material>();
	
	static {
		interactable.add(Material.ANVIL);
		interactable.add(Material.BED);
		interactable.add(Material.BEACON);
		interactable.add(Material.BED_BLOCK);
		interactable.add(Material.BOAT);
		interactable.add(Material.BREWING_STAND);
		interactable.add(Material.BURNING_FURNACE);
		interactable.add(Material.CAKE_BLOCK);
		interactable.add(Material.CARROT); // Harvesting?
		interactable.add(Material.CAULDRON);
		interactable.add(Material.CHEST);
		interactable.add(Material.COMMAND_MINECART);
		interactable.add(Material.CROPS);
		interactable.add(Material.DIODE_BLOCK_OFF);
		interactable.add(Material.DIODE_BLOCK_ON);
		interactable.add(Material.DISPENSER);
		interactable.add(Material.DROPPER);
		interactable.add(Material.ENCHANTMENT_TABLE);
		interactable.add(Material.ENDER_CHEST);
		interactable.add(Material.ENDER_PORTAL_FRAME);
		interactable.add(Material.EXPLOSIVE_MINECART);
		interactable.add(Material.FENCE_GATE);
		interactable.add(Material.FURNACE);
		interactable.add(Material.GOLD_PLATE);
		interactable.add(Material.HOPPER_MINECART);
		interactable.add(Material.HOPPER);
		interactable.add(Material.ITEM_FRAME);
		interactable.add(Material.IRON_PLATE);
		interactable.add(Material.JUKEBOX);
		interactable.add(Material.LEVER);
		interactable.add(Material.MINECART);
		interactable.add(Material.NETHER_STALK);
		interactable.add(Material.NETHER_WARTS);
		interactable.add(Material.NOTE_BLOCK);
		interactable.add(Material.POWERED_MINECART);
		interactable.add(Material.REDSTONE_COMPARATOR_OFF);
		interactable.add(Material.REDSTONE_COMPARATOR_ON);
		interactable.add(Material.SOIL); // Stop soil destruction?
		interactable.add(Material.STONE_BUTTON);
		interactable.add(Material.STORAGE_MINECART);
		interactable.add(Material.STONE_PLATE);
		interactable.add(Material.TNT);
		interactable.add(Material.TRAP_DOOR);
		interactable.add(Material.TRAPPED_CHEST);
		interactable.add(Material.TRIPWIRE);
		interactable.add(Material.WOOD_BUTTON);
		interactable.add(Material.WOOD_DOOR);
		interactable.add(Material.WOOD_PLATE);
		interactable.add(Material.WOODEN_DOOR);
		interactable.add(Material.WORKBENCH);
	}
	
	public static boolean isInteractable(ItemStack item) {
		return isInteractable(item.getType());
	}
	
	public static boolean isInteractable(Block block) {
		return isInteractable(block.getType());
	}
	
	public static boolean isInteractable(Material mat) {
		return interactable.contains(mat);
	}
}
