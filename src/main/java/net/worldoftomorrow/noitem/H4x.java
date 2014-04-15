package net.worldoftomorrow.noitem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_7_R3.util.CraftMagicNumbers;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.potion.Potion;

import net.minecraft.server.v1_7_R3.CraftingManager;
import net.minecraft.server.v1_7_R3.InventoryCrafting;
import net.minecraft.server.v1_7_R3.Item;
import net.minecraft.server.v1_7_R3.ItemArmor;
import net.minecraft.server.v1_7_R3.ItemBow;
import net.minecraft.server.v1_7_R3.ItemShears;
import net.minecraft.server.v1_7_R3.ItemStack;
import net.minecraft.server.v1_7_R3.ItemSword;
import net.minecraft.server.v1_7_R3.ItemTool;
import net.minecraft.server.v1_7_R3.PacketPlayOutHeldItemSlot;
import net.minecraft.server.v1_7_R3.PlayerInventory;
import net.minecraft.server.v1_7_R3.TileEntityBrewingStand;
import net.minecraft.server.v1_7_R3.TileEntityFurnace;
import net.minecraft.server.v1_7_R3.World;
import net.worldoftomorrow.noitem.interfaces.INoItemPlayer;

/**
 * This class contains my hacky code, which usually means that it uses
 * reflection, CB/NMS code or both.
 * @author Alan Litz
 *
 */
public class H4x {
	
	// This will likely need to be updated with every new version of the game
	private static final Method getResultMethod;
	// This is here simply to help invoke the method in a timely and cost effective fashion
	private static final TileEntityBrewingStand tebsInstance = new TileEntityBrewingStand();
	
	static {
		/*
		 * Use a temporary method to get the one I want. Then assign it to the getResultMethod.
		 * This allows me to keep getResultMethod as final.
		 */
		Method tempMethod = null;
		try {
			tempMethod = TileEntityBrewingStand.class.getDeclaredMethod("c", int.class, ItemStack.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} finally {
			getResultMethod = tempMethod;
			getResultMethod.setAccessible(true);
		}
	}
	
	/**
	 * This method gets what the damage result of a brewing recipe would be. It is used
	 * to implement restriction of a brewing action. It will return -1 if the initial
	 * static setup encountered a problem. This will (for the most part) cleanly break
	 * the feature instead of spouting errors except on startup.
	 * @param ingredient
	 * @param base
	 * @return
	 */
	public static int getBrewResult(org.bukkit.inventory.ItemStack ingredient, org.bukkit.inventory.ItemStack base) {
		// If initial static setup failed, return -1
		if(getResultMethod == null) return -1;
		if(ingredient == null) return base.getDurability();
		
		/*
		 * The method I invoke here (as of MC1.7.2/4) takes the arguments of an int which
		 * is the damage value of the current potion and an ItemStack which is the ingredient
		 * and returns the damage value of the resulting brew
		 */
		try {
			net.minecraft.server.v1_7_R3.ItemStack ing = CraftItemStack.asNMSCopy(ingredient);
			short durability = base.getDurability();
			int result = (Integer) getResultMethod.invoke(tebsInstance, durability, ing);
			
			return result;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return -2;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return -2;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return -2;
		}
	}

	public static Potion getPotion(org.bukkit.inventory.ItemStack ing, org.bukkit.inventory.ItemStack base) {
		int rawData = getBrewResult(ing, base);
		return getPotion(rawData);
	}

	public static Potion getPotion(int damage) {
		// damage <= 0 ? new Potion(PotionType.WATER) : Potion.fromItemStack(new org.bukkit.inventory.ItemStack(Material.POTION, 1, (short) damage));
		//Potion result = Potion.fromItemStack(new org.bukkit.inventory.ItemStack(Material.POTION, 1, (short) damage));
		@SuppressWarnings("deprecation")
		Potion result = Potion.fromDamage(damage);
		return result;
	}
	
	/**
	 * This method uses the NMS TileEntityFurnace.isFuel method to check if a Bukkit
	 * ItemStack is a valid source of fuel for a furnace
	 * @param fuel
	 * @return
	 */
	public static boolean isFuel(org.bukkit.inventory.ItemStack fuel) {
		return TileEntityFurnace.isFuel(CraftItemStack.asNMSCopy(fuel));
	}
	
	public static void setHeldItemSlot(int i, INoItemPlayer niplayer) {
		if(i > PlayerInventory.getHotbarSize()) {
			NoItem.getInstance().getLogger().severe("A method tried to set an invalid held item slot. I stopped it. Your welcome.");
			return;
		}
		if(niplayer == null) {
			// If this ever happens, at least it might be somewhat humorous.
			NoItem.getInstance().getLogger().severe("Yo dawg, I can't send a packet to a null player. Fix that before I fix your face. With my fist. (If I had one)");
			return;
		}
		((CraftPlayer) niplayer.getPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutHeldItemSlot(i));
	}
	
	public static boolean isTool(org.bukkit.inventory.ItemStack stack) {
		return CraftMagicNumbers.getItem(stack.getType()) instanceof ItemTool;
	}
	
	public static boolean isArmor(org.bukkit.inventory.ItemStack stack) {
		return CraftMagicNumbers.getItem(stack.getType()) instanceof ItemArmor;
	}
	
	public static boolean isWeapon(org.bukkit.inventory.ItemStack stack) {
		Item item = CraftMagicNumbers.getItem(stack.getType());
		return item instanceof ItemSword || item instanceof ItemBow;
	}
	
	public static boolean hasDurability(org.bukkit.inventory.ItemStack stack) {
		Item item = CraftMagicNumbers.getItem(stack.getType());
		return item instanceof ItemTool || item instanceof ItemArmor || item instanceof ItemSword || item instanceof ItemBow || item instanceof ItemShears;
	}
	
	public static org.bukkit.inventory.ItemStack getCraftResult(InventoryCrafting inv, World world) {
		return CraftItemStack.asBukkitCopy(CraftingManager.getInstance().craft(inv, world));
	}
	
	public static SlotType getSlotType(InventoryView view, int slot) {
		return CraftInventoryView.getSlotType(view, slot);
	}
	
}
