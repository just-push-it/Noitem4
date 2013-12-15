package net.worldoftomorrow.noitem;

import java.util.ArrayList;
import java.util.HashMap;

import net.worldoftomorrow.noitem.config.ConfigManager;
import net.worldoftomorrow.noitem.interfaces.IAction;
import net.worldoftomorrow.noitem.interfaces.INoItemPlayer;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class ActionFactory implements Listener {

	private HashMap<String, INoItemPlayer> players = new HashMap<String, INoItemPlayer>();
	private ArrayList<INoItemPlayer> toUpdate = new ArrayList<INoItemPlayer> ();
	
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
		if(player == null) return;
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
		if(player == null) return;
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
		// Apparently this might happen sometimes?
		if(player == null) return;
		IAction action = new Action(ActionType.OPEN, invType.toString().replaceAll("_", "").toLowerCase());
		if(!player.canDoAction(action)) {
			event.setCancelled(true);
			player.notifyPlayer(action);
		}
	}
	
	// Crafting should only check for the item name with data since it makes a difference when crafting
	@EventHandler
	public void onPlayerCraftItem(CraftItemEvent event) {
		INoItemPlayer player = getPlayer(event.getWhoClicked());
		if(player == null) return;
		ItemStack result = event.getRecipe().getResult();
		short dur = result.getDurability();
		// Only check both permissions if the durability is 0
		if(dur != 0) {
			IAction actionWithData = new Action(ActionType.CRAFT, getItemName(result), String.valueOf(result.getDurability()));
			if(!player.canDoAction(actionWithData)) {
				event.setCancelled(true);
				player.notifyPlayer(actionWithData);
				this.toUpdate.add(player);
			}
		} else {
			IAction actionWithData = new Action(ActionType.CRAFT, getItemName(result), String.valueOf(result.getDurability()));
			IAction action = new Action(ActionType.CRAFT, getItemName(result));
			if(!player.canDoAction(action) || !player.canDoAction(actionWithData)) {
				event.setCancelled(true);
				player.notifyPlayer(action);
				this.toUpdate.add(player);
			}
		}
	}
	// Update the players inventory if they are marked for it.
	@SuppressWarnings("deprecation")
	@EventHandler
	public void updateInvOnClose(InventoryCloseEvent event) {
		INoItemPlayer player = getPlayer(event.getPlayer());
		if(player == null) return;
		if(toUpdate.contains(player)) {
			player.getPlayer().updateInventory();
			toUpdate.remove(player);
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
		if(player == null) return;
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
		// Okay seriously, why should this ever happen? Apparently it does though.
		// I should probably find out why this would ever be null..
		if(player == null) return;
		Item item = event.getItemDrop();
		ItemStack stack = item.getItemStack();
		
		IAction action = new Action(ActionType.DROP, getItemName(stack));
		IAction actionWithData = new Action(ActionType.DROP, getItemName(stack), String.valueOf(stack.getDurability()));
		if(!player.canDoAction(action) || !player.canDoAction(actionWithData)) {
			event.setCancelled(true);
			player.notifyPlayer(action);
		}
	}
	
	// Click drop (&drag?) into slot
	@EventHandler
	public void onPlayerHoldItemClick(InventoryClickEvent event) {
		Inventory inventory = event.getInventory();
		if(inventory.getType() != InventoryType.CRAFTING) return;
		if(event.getSlotType() != SlotType.QUICKBAR) return;
		ItemStack toCheck = event.getCursor();
		if(isAir(toCheck)) return;
		INoItemPlayer player = getPlayer(event.getWhoClicked());
		if(player == null) return;
		if(event.getSlot() != player.getPlayer().getInventory().getHeldItemSlot()) return;
		
		IAction action = new Action(ActionType.HOLD, getItemName(toCheck));
		IAction actionWithData = new Action(ActionType.HOLD, getItemName(toCheck), String.valueOf(toCheck.getDurability()));
		if(!player.canDoAction(action) || !player.canDoAction(actionWithData)) {
			event.setCancelled(true);
			player.notifyPlayer(action);
		}
	}
	// Scroll onto slot
	@EventHandler
	public void onPlayerHoldItemSwitch(PlayerItemHeldEvent event) {
		INoItemPlayer player = getPlayer(event.getPlayer());
		ItemStack toCheck = player.getPlayer().getInventory().getItem(event.getNewSlot());
		if(isAir(toCheck)) return;
		
		IAction action = new Action(ActionType.HOLD, getItemName(toCheck));
		IAction actionWithData = new Action(ActionType.HOLD, getItemName(toCheck) ,String.valueOf(toCheck.getDurability()));
		if(!player.canDoAction(action) || !player.canDoAction(actionWithData)) {
			event.setCancelled(true);
			player.notifyPlayer(action);
		}
	}
	// Press number key onto slot
	@EventHandler
	public void onPlayerHoldItemKeyPress(InventoryClickEvent event) {
		if(event.getClick() != ClickType.NUMBER_KEY) return;
		// confirm this is the correct item to check
		ItemStack toCheck = event.getCurrentItem();
		if(isAir(toCheck)) return;
		
		INoItemPlayer player = getPlayer(event.getWhoClicked());
		if(player == null) return;
		IAction action = new Action(ActionType.HOLD, getItemName(toCheck));
		IAction actionWithData = new Action(ActionType.HOLD, getItemName(toCheck) ,String.valueOf(toCheck.getDurability()));
		if(!player.canDoAction(action) || !player.canDoAction(actionWithData)) {
			event.setCancelled(true);
			player.notifyPlayer(action);
		}
	}
	//Shift click
	@EventHandler
	public void onPlayerHoldItemShiftClick(InventoryClickEvent event) {
		if(!event.isShiftClick()) return;
		ItemStack toCheck = event.getCurrentItem();
		if(isAir(toCheck)) return;
		if(event.getSlot() < 9) return;
		INoItemPlayer player = getPlayer(event.getWhoClicked());
		int heldSlot = player.getPlayer().getInventory().getHeldItemSlot();
		// if the first empty quickbar slot is not the held slot, return.
		if(firstEmptyQuickbarSlot(player.getPlayer()) != heldSlot) return;
		
		IAction action = new Action(ActionType.HOLD, getItemName(toCheck));
		IAction actionWithData = new Action(ActionType.HOLD, getItemName(toCheck) ,String.valueOf(toCheck.getDurability()));
		if(!player.canDoAction(action) || !player.canDoAction(actionWithData)) {
			event.setCancelled(true);
			player.notifyPlayer(action);
		}
	}
	//Pickup into slot
	@EventHandler
	public void onPlayerPickupItemHold(PlayerPickupItemEvent event) {
		int firstEmpty = firstEmptyQuickbarSlot(event.getPlayer());
		// If the quickbar is not full (-1) or the first empty slot is not the held slot, return.
		if(firstEmpty == -1 || firstEmpty != event.getPlayer().getInventory().getHeldItemSlot()) return;
		// At this point, the item being picked up SHOULD be going to the held item slot, so lets check it.
		INoItemPlayer player = getPlayer(event.getPlayer());
		ItemStack toCheck = event.getItem().getItemStack();
		
		IAction action = new Action(ActionType.HOLD, getItemName(toCheck));
		IAction actionWithData = new Action(ActionType.HOLD, getItemName(toCheck), String.valueOf(toCheck.getDurability()));
		if(!player.canDoAction(action) || !player.canDoAction(actionWithData)) {
			event.setCancelled(true);
			player.notifyPlayer(action);
			// Should I also set the pickup time for the item?
		}
	}
	
	// Handle a player interacting with an object
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteractObjectEvent(PlayerInteractEvent event) {
		// Shouldn't need to do anything here
		if(event.getAction() == org.bukkit.event.block.Action.LEFT_CLICK_AIR) return;
		Block toCheck = event.getClickedBlock();
		if(isAir(toCheck)) return;
		IAction action = new Action(ActionType.INTERACT_OBJECT, getBlockName(toCheck));
		IAction actionWithData = new Action(ActionType.INTERACT_OBJECT, getBlockName(toCheck), String.valueOf(toCheck.getData()));
		INoItemPlayer player = getPlayer(event.getPlayer());
		if(!player.canDoAction(action) || !player.canDoAction(actionWithData)) {
			event.setCancelled(true);
			event.setUseInteractedBlock(Result.DENY);
			event.setUseItemInHand(Result.DENY);
			player.notifyPlayer(action);
		}
		
	}
	// Handle a player interacting with an entity
	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
		INoItemPlayer player = getPlayer(event.getPlayer());
		Entity clicked = event.getRightClicked();
		IAction action = new Action(ActionType.INTERACT_ENTITY,clicked.getType().toString().toLowerCase().replaceAll("_", ""));
		if(!player.canDoAction(action)) {
			event.setCancelled(true);
			player.notifyPlayer(action);
		}
	}
	
	@EventHandler
	public void onPlayerAttackEntity(EntityDamageByEntityEvent event) {
		//if(event.getCause() != DamageCause.ENTITY_ATTACK) return;
		Entity attacker = event.getDamager();
		if(!(attacker instanceof Player)) return;
		EntityType entityType = event.getEntity().getType();
		// If it is not a living entity
		//if(!entityType.isAlive()) return; Forget this for now, that way minecarts and such are included
		INoItemPlayer player = getPlayer((HumanEntity) attacker);
		IAction action = new Action(ActionType.ATTACK, entityType.toString().replaceAll("_", ""));
		if(!player.canDoAction(action)) {
			event.setCancelled(true);
			player.notifyPlayer(action);
		}
	}

	// Forget this for now, this is no clean way to do it atm.
	/*@EventHandler
	public void onBrewerClick(InventoryClickEvent event) {
		InventoryType invType = event.getInventory().getType();
		if(invType != InventoryType.BREWING) return;
		BrewerInventory brewInv = (BrewerInventory) event.getInventory();
		ItemStack ingredient = brewInv.getIngredient();
		// 0 - 2 look to be items, 3 is ingredient
		// net/minecraft/server/TileEntityBrewingStand.java#L13
		ItemStack[] items = new ItemStack[] {
			brewInv.getItem(0),
			brewInv.getItem(1),
			brewInv.getItem(2)
		};
	}*/

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
		if(stack == null) return true;
		return stack.getType().equals(Material.AIR);
	}
	
	private boolean isAir(Block block) {
		if(block == null) return true;
		return block.getType().equals(Material.AIR);
	}
	
	// Returns -1 if there is no empty quickbar slot
	private int firstEmptyQuickbarSlot(Player player) {
		ItemStack itemInSlot;
		for(int i = 0; i < 9; i++) {
			itemInSlot = player.getInventory().getItem(i);
			if(isAir(itemInSlot)) return i;
		}
		return -1;
	}
	
	public INoItemPlayer getPlayer(HumanEntity player) {
		return players.get(player.getName());
	}
}
