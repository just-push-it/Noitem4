package net.worldoftomorrow.noitem;

import java.util.HashMap;

import net.worldoftomorrow.noitem.config.ConfigManager;
import net.worldoftomorrow.noitem.interfaces.IAction;
import net.worldoftomorrow.noitem.interfaces.INoItemPlayer;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class ActionFactory implements Listener {

	private HashMap<String, INoItemPlayer> players = new HashMap<String, INoItemPlayer>();
	
	// This method is to keep track of players that join
	@EventHandler
	public void registerPlayerJoin(PlayerJoinEvent event) {
		players.put(event.getPlayer().getName(), new NoItemPlayer(event.getPlayer()));
	}
	
	// This method is to keep track of players that quit
	@EventHandler
	public void registerPlayerQuit(PlayerQuitEvent event) {
		players.remove(event.getPlayer().getName());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void blockBreakEvent(BlockBreakEvent event) {
		INoItemPlayer player = getPlayer(event.getPlayer());
		IAction action = new Action(ActionType.BREAK, getBlockName(event.getBlock()));
		IAction actionWithData = new Action(ActionType.BREAK, getBlockName(event.getBlock()), String.valueOf(event.getBlock().getData()));
		if(!player.canDoAction(action) || !player.canDoAction(actionWithData)) {
			event.setCancelled(true);
			player.notifyPlayer(action);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void blockPlaceEvent(BlockPlaceEvent event) {
		INoItemPlayer player = getPlayer(event.getPlayer());
		IAction action = new Action(ActionType.PLACE, getBlockName(event.getBlock()));
		IAction actionWithData = new Action(ActionType.PLACE, getBlockName(event.getBlock()), String.valueOf(event.getBlock().getData()));
		if(!player.canDoAction(action) || !player.canDoAction(actionWithData)) {
			event.setCancelled(true);
			player.notifyPlayer(action);
		}
	}
	
	@EventHandler
	public void playerOpenInventoryEvent(InventoryOpenEvent event) {
		InventoryType invType = event.getInventory().getType();
		// These do not need to be checked for opening
		if(invType == InventoryType.PLAYER || invType == InventoryType.CREATIVE)
			return;
		INoItemPlayer player = getPlayer(event.getPlayer());
		IAction action = new Action(ActionType.OPEN, invType.toString().replaceAll("_", "").toLowerCase());
		if(!player.canDoAction(action)) {
			event.setCancelled(true);
			player.notifyPlayer(action);
		}
	}
	
	// Crafting should only check for the item name with data since it makes a difference when crafting
	@EventHandler
	public void onPlayerCraftItem(CraftItemEvent event) {
		INoItemPlayer player = getPlayer(event.getWhoClicked());;
		ItemStack result = event.getRecipe().getResult();
		short dur = result.getDurability();
		// Only check both permissions if the durability is 0
		if(dur != 0) {
			IAction actionWithData = new Action(ActionType.CRAFT, getItemName(result), String.valueOf(result.getDurability()));
			if(!player.canDoAction(actionWithData)) {
				event.setCancelled(true);
				player.notifyPlayer(actionWithData);
			}
		} else {
			IAction actionWithData = new Action(ActionType.CRAFT, getItemName(result), String.valueOf(result.getDurability()));
			IAction action = new Action(ActionType.CRAFT, getItemName(result));
			if(!player.canDoAction(action) || !player.canDoAction(actionWithData)) {
				event.setCancelled(true);
				player.notifyPlayer(action);
			}
		}
	}
	// Reference - org/bukkit/craftbukkit/inventory/CraftInventoryFurnace.java#L22
	// Cooking also only checks with a second value
	@EventHandler
	public void onPlayerCook(InventoryClickEvent event) {
		InventoryType invType = event.getInventory().getType();
		if(invType != InventoryType.FURNACE) return;
		ItemStack itemToCheck;
		InventoryView inventoryView = event.getView();
		ItemStack onCursor = event.getCursor();
		ItemStack currentIng = inventoryView.getItem(0);
		
		// -- Begin logic to determine what to check -- //
		// If the clicked slot type is fuel and it is empty
		if(event.getSlotType() == SlotType.FUEL && isAir(event.getCurrentItem())) {
			// If the cursor or the ingredient is empty, return.
			if(isAir(onCursor) || isAir(currentIng)) return;
			itemToCheck = currentIng;
		} else {
			int clickedSlot = event.getRawSlot();
			// If click was in the ingredient slot
			if(isSlotTopInventory(clickedSlot, inventoryView) && clickedSlot == 0) {
				// If there is not an ingredient and there is a cursor item
				if(isAir(currentIng) && !isAir(onCursor)) {
					itemToCheck = onCursor;
				} else return;
			} else { // Anything not a click on the fuel slot, or ingredient slot.
				// Shift clicking is the last possibility.
				if(!event.isShiftClick()) return;
				ItemStack clickedItem = event.getCurrentItem();
				// If the clicked slot is empty, return.
				if(isAir(clickedItem)) return;
				itemToCheck = clickedItem;
			}
		}
		// -- End logic to determine what to check -- //
		INoItemPlayer player = getPlayer(event.getWhoClicked());
		short dur = itemToCheck.getDurability();
		IAction actionWithData = new Action(ActionType.COOK, getItemName(itemToCheck), String.valueOf(dur));
		if(dur != 0) {
			if(!player.canDoAction(actionWithData)) {
				event.setCancelled(true);
				player.notifyPlayer(actionWithData);
			}
		} else {
			IAction action = new Action(ActionType.COOK, getItemName(itemToCheck));
			if(!player.canDoAction(action) || !player.canDoAction(actionWithData)) {
				event.setCancelled(true);
				player.notifyPlayer(action);
			}
		}
	}
	
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		INoItemPlayer player = getPlayer(event.getPlayer());
		Item item = event.getItem();
		ItemStack stack = item.getItemStack();
		IAction action = new Action(ActionType.PICKUP, getItemName(stack));
		IAction actionWithData = new Action(ActionType.PICKUP, getItemName(stack), String.valueOf(stack.getDurability()));
		if(!player.canDoAction(action) || !player.canDoAction(actionWithData)) {
			event.setCancelled(true);
			// Set the pickup delay to the same time as the message delay to prevent event spam
			item.setPickupDelay(Integer.valueOf(ConfigManager.getInstance().getValue("notify.timeout")));
			player.notifyPlayer(action);
		}
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		INoItemPlayer player = getPlayer(event.getPlayer());
		Item item = event.getItemDrop();
		ItemStack stack = item.getItemStack();
		IAction action = new Action(ActionType.DROP, getItemName(stack));
		IAction actionWithData = new Action(ActionType.DROP, getItemName(stack), String.valueOf(stack.getDurability()));
		if(!player.canDoAction(action) || !player.canDoAction(actionWithData)) {
			event.setCancelled(true);
			player.notifyPlayer(action);
		}
	}
	
	// Utility Method
	private String getBlockName(Block b) {
		return b.getType().toString().replace("_", "").toLowerCase();
	}
	
	private String getItemName(ItemStack stack) {
		return stack.getType().toString().replace("_", "").toLowerCase();
	}
	
	private boolean isSlotTopInventory(int rawSlot, InventoryView view) {
		return view.convertSlot(rawSlot) == rawSlot;
	}
	
	private boolean isAir(ItemStack stack) {
		return stack.getType().equals(Material.AIR);
	}
	
	public INoItemPlayer getPlayer(HumanEntity player) {
		return players.get(player.getName());
	}
}
