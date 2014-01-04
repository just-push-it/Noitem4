package net.worldoftomorrow.noitem;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class ItemChecks {
	private static ArrayList<Material> interactable = new ArrayList<Material>();
	private static ArrayList<Material> usableItem = new ArrayList<Material>();
	private static ArrayList<Material> usableTool = new ArrayList<Material>();
	
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
		interactable.add(Material.MELON_STEM);
		interactable.add(Material.PUMPKIN_STEM);
		interactable.add(Material.CARROT_STICK);
		interactable.add(Material.POTATO);
		
		usableItem.add(Material.BOOK_AND_QUILL);
		usableItem.add(Material.BUCKET);
		usableItem.add(Material.EYE_OF_ENDER);
		usableItem.add(Material.FISHING_ROD);
		usableItem.add(Material.FLINT_AND_STEEL);
		usableItem.add(Material.LAVA_BUCKET);
		usableItem.add(Material.NAME_TAG);
		usableItem.add(Material.SHEARS);
		usableItem.add(Material.WATER_BUCKET);
		
		usableTool.add(Material.DIAMOND_AXE);
		usableTool.add(Material.DIAMOND_HOE);
		usableTool.add(Material.DIAMOND_PICKAXE);
		usableTool.add(Material.DIAMOND_SPADE);
		usableTool.add(Material.DIAMOND_SWORD);
		usableTool.add(Material.GOLD_AXE);
		usableTool.add(Material.GOLD_HOE);
		usableTool.add(Material.GOLD_PICKAXE);
		usableTool.add(Material.GOLD_SPADE);
		usableTool.add(Material.GOLD_SWORD);
		usableTool.add(Material.IRON_AXE);
		usableTool.add(Material.IRON_HOE);
		usableTool.add(Material.IRON_PICKAXE);
		usableTool.add(Material.IRON_SPADE);
		usableTool.add(Material.IRON_SWORD);
		usableTool.add(Material.STONE_AXE);
		usableTool.add(Material.STONE_HOE);
		usableTool.add(Material.STONE_PICKAXE);
		usableTool.add(Material.STONE_SPADE);
		usableTool.add(Material.STONE_SWORD);
		usableTool.add(Material.WOOD_AXE);
		usableTool.add(Material.WOOD_HOE);
		usableTool.add(Material.WOOD_PICKAXE);
		usableTool.add(Material.WOOD_SPADE);
		usableTool.add(Material.WOOD_SWORD);
		usableTool.add(Material.SHEARS);
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
	
	public static boolean isUsableItem(ItemStack item) {
		return isUsableItem(item.getType());
	}
	
	public static boolean isUsableItem(Material mat) {
		return usableItem.contains(mat);
	}
	
	public static boolean isUsableTool(ItemStack item) {
		return isUsableTool(item.getType());
	}
	
	public static boolean isUsableTool(Material mat) {
		return usableTool.contains(mat);
	}
}
