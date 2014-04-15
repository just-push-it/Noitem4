package net.worldoftomorrow.noitem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import net.worldoftomorrow.noitem.actions.ActionBreak;
import net.worldoftomorrow.noitem.actions.ActionBrew;
import net.worldoftomorrow.noitem.actions.ActionCook;
import net.worldoftomorrow.noitem.actions.ActionCraft;
import net.worldoftomorrow.noitem.actions.ActionDrop;
import net.worldoftomorrow.noitem.actions.ActionHoldClick;
import net.worldoftomorrow.noitem.actions.ActionHoldPickup;
import net.worldoftomorrow.noitem.actions.ActionHoldScroll;
import net.worldoftomorrow.noitem.actions.ActionInteractObject;
import net.worldoftomorrow.noitem.actions.ActionOpen;
import net.worldoftomorrow.noitem.actions.ActionPickup;
import net.worldoftomorrow.noitem.actions.ActionPlace;
import net.worldoftomorrow.noitem.interfaces.IActionFactory;
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
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
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
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;

public class ActionFactory implements Listener, IActionFactory {

	private HashMap<UUID, INoItemPlayer> players = new HashMap<UUID, INoItemPlayer>();

	@EventHandler
	public void registerPlayerJoin(PlayerJoinEvent event) {
		players.put(event.getPlayer().getUniqueId(), new NoItemPlayer(event.getPlayer()));
	}

	@EventHandler
	public void registerPlayerQuit(PlayerQuitEvent event) {
		players.remove(event.getPlayer().getUniqueId());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void handleBreak (BlockBreakEvent event) {
		if(event.isCancelled()) return;
		INoItemPlayer player = getPlayer(event.getPlayer());
		if(player == null) return;
		ActionBreak action = new ActionBreak(event.getBlock());
		ActionBreak actionWithData = new ActionBreak(event.getBlock(), event.getBlock().getData());
		action.process(player, event);
		actionWithData.process(player, event);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void handlePlace(BlockPlaceEvent event) {
		if(event.isCancelled()) return;
		INoItemPlayer player = getPlayer(event.getPlayer());
		if(player == null) return;
		ActionPlace action = new ActionPlace(event.getBlock());
		ActionPlace actionWithData = new ActionPlace(event.getBlock(), event.getBlock().getData());
		action.process(player, event);
		actionWithData.process(player, event);
	}
	
	@EventHandler
	public void handleOpen(InventoryOpenEvent event) {
		if(event.isCancelled()) return;
		InventoryType invType = event.getInventory().getType();
		if(invType == InventoryType.PLAYER || invType == InventoryType.CREATIVE) return;
		INoItemPlayer player = getPlayer(event.getPlayer());
		if(player == null) return;
		ActionOpen action = new ActionOpen(event.getInventory());
		action.process(player, event);
	}
	
	// Crafting should only check for the item name with data since it makes a difference when crafting
	@EventHandler
	public void handlePlayerCraftItem(CraftItemEvent event) {
		INoItemPlayer player = getPlayer(event.getWhoClicked());
		if(player == null) return;
		ItemStack result = event.getRecipe().getResult();
		short dur = result.getDurability();
		ActionCraft action = new ActionCraft(result);
		action.process(player, event);
		// Only check both permissions if the durability is 0
		if(!event.isCancelled() && dur != 0) {
			ActionCraft actionWithData = new ActionCraft(result, result.getDurability());
			actionWithData.process(player, event);
		}
	}
	
	// Reference - org/bukkit/craftbukkit/inventory/CraftInventoryFurnace.java#L22
	// Cooking also only checks with a second value
	@EventHandler
	public void handlePlayerCook(InventoryClickEvent event) {
		if(event.isCancelled()) return;
		if(event.getInventory().getType() != InventoryType.FURNACE) return;
		ItemStack itemToCheck;
		ItemStack onCursor = event.getCursor();
		ItemStack currentIng = event.getView().getItem(0);
		ItemStack currentFuel = event.getView().getItem(1);
		
		if(event.getSlotType() == SlotType.FUEL && isAir(event.getCurrentItem())) {
			// Only check if there currently is no ingredient
			if(isAir(onCursor) || isAir(currentIng)) return;
			if(!H4x.isFuel(onCursor)) return;
			itemToCheck = currentIng;
		} else if (event.getRawSlot() == 0){
			if(isAir(currentIng) && !isAir(onCursor) && !isAir(currentFuel)) {
				itemToCheck = onCursor;
			} else return;
		} else if (event.isShiftClick() && event.getRawSlot() > 1) {
			ItemStack clicked = event.getCurrentItem();
			if(isAir(clicked)) return;
			if((H4x.isFuel(clicked) && isAir(currentFuel) && !isAir(currentIng))) {
				itemToCheck = currentIng;
			} else if (!isAir(currentFuel) && isAir(currentIng)) {
				itemToCheck = clicked;
			} else return; // If it is a shift click but item would not go to fuel or ingredient slot, return.
		} else return; // Return if it is not a shift click, or on a fuel or ingredient slot

		INoItemPlayer player = getPlayer(event.getWhoClicked());
		if(player == null) return;
		short dur = itemToCheck.getDurability();
		ActionCook actionWithData = new ActionCook(itemToCheck, dur);
		actionWithData.process(player, event);
		if(!event.isCancelled() && dur == 0) {
			ActionCook action = new ActionCook(itemToCheck);
			action.process(player, event);
		}
	}
	
	@EventHandler
	public void handleItemPickup(PlayerPickupItemEvent event) {
		if(event.isCancelled()) return;
		INoItemPlayer player = getPlayer(event.getPlayer());
		Item item = event.getItem();
		ItemStack stack = item.getItemStack();
		ActionPickup actionWithData = new ActionPickup(stack, stack.getDurability());
		actionWithData.process(player, event);
		if(!event.isCancelled() && (stack.getDurability() == 0 || H4x.hasDurability(stack))) {
			ActionPickup action = new ActionPickup(stack);
			action.process(player, event);
		}
	}
	
	@EventHandler
	public void handleItemDrop(PlayerDropItemEvent event) {
		if(event.isCancelled()) return;
		INoItemPlayer player = getPlayer(event.getPlayer());
		if(player == null) return;
		Item item = event.getItemDrop();
		ItemStack stack = item.getItemStack();
		ActionDrop actionWithData = new ActionDrop(stack, stack.getDurability());
		actionWithData.process(player, event);
		if(!event.isCancelled() && (stack.getDurability() == 0 || H4x.hasDurability(stack))) {
			ActionDrop action = new ActionDrop(stack);
			action.process(player, event);
		}
	}
	/*
	 * Currently this is partly broken due to a long standing CB issue.
	 * I have submitted a PR to fix it: https://github.com/Bukkit/CraftBukkit/pull/1338
	 * This should work flawlessly when the PR is accepted
	 */
	@EventHandler
	public void handlePlayerHoldItemClick(InventoryClickEvent event) {
		this.unifiedHoldItemClickDrag(event, event.getRawSlot(), event.getCursor());
	}
	
	@EventHandler
	public void handlePlayerHoldItemDrag(InventoryDragEvent event) {
		Set<Integer> slots = event.getRawSlots();
		ItemStack cursor = event.getOldCursor(); // Get the old cursor, since it was all dropped immediately on click
		for(int slot : slots) {
			this.unifiedHoldItemClickDrag(event, slot, cursor);
		}
	}
	
	private void unifiedHoldItemClickDrag(InventoryInteractEvent event, int slot, ItemStack cursor) {
		if(event.isCancelled()) return;
		INoItemPlayer player = getPlayer(event.getWhoClicked());
		if(player == null) return;
		ItemStack toCheck;
		int heldSlot = player.getPlayer().getInventory().getHeldItemSlot();
		final boolean isShiftClick = event instanceof InventoryClickEvent && ((InventoryClickEvent) event).isShiftClick();
		SlotType slotType = H4x.getSlotType(event.getView(), slot);
		if(isShiftClick) {
			// Shift clicking in the slot bar only goes to the upper inventory
			InventoryClickEvent clickEvent = (InventoryClickEvent) event;
			if(slotType == SlotType.QUICKBAR) return;
			if(isAir(clickEvent.getCurrentItem())) return;
			int firstEmpty = player.getPlayer().getInventory().firstEmpty();
			if(heldSlot != firstEmpty) return;
			toCheck = clickEvent.getCurrentItem();
		} else {
			if(slotType != SlotType.QUICKBAR) return;
			if(isAir(cursor)) return;
			InventoryView view = event.getView();
			if(heldSlot != view.convertSlot(slot)) return;
			toCheck = cursor;
		}
		ActionHoldClick actionWithData = new ActionHoldClick(toCheck, toCheck.getDurability());
		actionWithData.process(player, event);
		// If the durability is 0 or the item is a tool
		if(!event.isCancelled() && (toCheck.getDurability() == 0 || H4x.hasDurability(toCheck))) {
			ActionHoldClick action = new ActionHoldClick(toCheck);
			action.process(player, event);
		}
	}
	
	// Scroll onto slot
	@EventHandler
	public void handlePlayerHoldItemSwitch(PlayerItemHeldEvent event) {
		if(event.isCancelled()) return;
		INoItemPlayer player = getPlayer(event.getPlayer());
		ItemStack toCheck = player.getPlayer().getInventory().getItem(event.getNewSlot());
		if(isAir(toCheck)) return;
		ActionHoldScroll actionWithData = new ActionHoldScroll(toCheck, toCheck.getDurability());
		actionWithData.process(player, event);
		if(!event.isCancelled() && (toCheck.getDurability() == 0 || H4x.hasDurability(toCheck))) {
			ActionHoldScroll action = new ActionHoldScroll(toCheck);
			action.process(player, event);
		}
	}
	
	@EventHandler
	public void handlePlayerHoldItemPickup(PlayerPickupItemEvent event) {
		if(event.isCancelled()) return;
		ItemStack toCheck = event.getItem().getItemStack();
		if(isAir(toCheck)) return;
		INoItemPlayer player = getPlayer(event.getPlayer());
		ActionHoldPickup actionWithData = new ActionHoldPickup(toCheck, toCheck.getDurability());
		actionWithData.process(player, event);
		if(!event.isCancelled() && (toCheck.getDurability() == 0 || H4x.hasDurability(toCheck))) {
			ActionHoldPickup action = new ActionHoldPickup(toCheck);
			action.process(player, event);
		}
	}
	
	// Handle a player interacting with an object
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteractObjectEvent(PlayerInteractEvent event) {
		if(event.isCancelled()) return;
		if(event.getAction() == org.bukkit.event.block.Action.LEFT_CLICK_AIR) return;
		if(event.getAction() == org.bukkit.event.block.Action.LEFT_CLICK_BLOCK) return;
		Block toCheck = event.getClickedBlock();
		if(isAir(toCheck)) return;
		INoItemPlayer player = getPlayer(event.getPlayer());
		if(player == null) return;
		// If the item is not on the intractable item list
		// This is to prevent blanket permissions from stopping blocks being broken
		if(!ItemChecks.isInteractable(toCheck)) return;
		
		ActionInteractObject actionWithData = new ActionInteractObject(toCheck, toCheck.getData());
		actionWithData.process(player, event);
		if(toCheck.getData() == 0) {
			ActionInteractObject action = new ActionInteractObject(toCheck);
			action.process(player, event);
		}
	}
	
	@EventHandler
	public void handleBrewInventoryDrag(InventoryDragEvent event) {
		Set<Integer> slots = event.getRawSlots();
		ItemStack cursor = event.getOldCursor(); // Get the old cursor, since it was all dropped immediately on click
		for(int slot : slots) {
			this.unifiedBrewHandle(event, slot, cursor);
		}
	}
	
	@EventHandler
	public void handleBrewInventoryClick(InventoryClickEvent event) {
		this.unifiedBrewHandle(event, event.getRawSlot(), event.getCursor());
	}
	
	private void unifiedBrewHandle(InventoryInteractEvent event, int slot, ItemStack cursor) {
		if(event.isCancelled()) return;
		if(event.getInventory().getType() != InventoryType.BREWING) return;
		if(slot < 0) return; // Ignore invalid slot clicks
		INoItemPlayer player = getPlayer(event.getWhoClicked());
		if(player == null) return;
		BrewerInventory inv = (BrewerInventory) event.getInventory();
		ItemStack ingredient;
		ItemStack[] bases;
		final boolean isShiftClick = event instanceof InventoryClickEvent && ((InventoryClickEvent) event).isShiftClick();
		// Return if was a click event & clicked slot was not shift click & not on an ingredient slot
		if(slot > 3 && !isShiftClick) return;
		// This should work to replace event.getCurrentItem
		ItemStack current = event.getView().getItem(slot);

		if(isShiftClick && !isAir(current)) {
			if(current.getType() == Material.POTION && !isAir(inv.getIngredient())) {
				bases = new ItemStack[] { current };
				ingredient = inv.getIngredient();
			} else if(current.getType() != Material.POTION) {
				bases = getBases(inv);
				ingredient = current;
			} else return;
		} else if (slot <= 3 && isAir(current) && !isAir(cursor)) {
			if(isShiftClick) return;
			// If base slot is clicked
			if(slot != 3 && !isAir(inv.getIngredient())) {
				ingredient = inv.getIngredient();
				bases = new ItemStack[] { cursor };
			} else { // Has to be the ingredient slot
				ingredient = cursor;
				bases = getBases(inv);
			}
		} else {
			// This covers items being removed in other cases that do not matter to this.
			return;
		}
		int data;
		Potion potion;
		for(ItemStack base : bases) {
			if(event.isCancelled()) break;
			data = H4x.getBrewResult(ingredient, base);
			if(data == base.getDurability()) continue; // Potion did not change
			potion = H4x.getPotion(data);
			if(potion == null || potion.getType() == null) continue;
			ActionBrew action = new ActionBrew(potion);
			action.process(player, event);
		}
	}

	public ItemStack[] getBases(BrewerInventory inv) {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		ItemStack stack;
		for(int i = 0; i < 3; i++) {
			stack = inv.getItem(i);
			if(isAir(stack)) continue;
			list.add(stack);
		}
		// If the array size is 0 (empty), return null.
		ItemStack[] array= new ItemStack[list.size()];
		return list.toArray(array);
	}
	
	//TODO: Re-implement
	// Handle a player interacting with an entity
	/*
	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
		INoItemPlayer player = getPlayer(event.getPlayer());
		Entity clicked = event.getRightClicked();
		IAction action = new Action(ActionType.INTERACT_ENTITY, getEntityName(clicked));
		if(!player.canDoAction(action)) {
			event.setCancelled(true);
			player.notifyPlayer(action);
		}
	}
	*/
	
	//TODO: Re-implement
	/*
	@EventHandler
	public void onPlayerAttackEntity(EntityDamageByEntityEvent event) {
		//if(event.getCause() != DamageCause.ENTITY_ATTACK) return;
		Entity attacker = event.getDamager();
		if(!(attacker instanceof Player)) return;
		Entity attacked = event.getEntity();
		// If it is not a living entity
		// Forget this for now, that way minecarts and such are included
		//if(!entityType.isAlive()) return;
		INoItemPlayer player = getPlayer((HumanEntity) attacker);
		IAction action = new Action(ActionType.ATTACK, getEntityName(attacked));
		if(!player.canDoAction(action)) {
			event.setCancelled(true);
			player.notifyPlayer(action);
		}
	}
	
	@EventHandler
	public void onPlayerUseEvent(PlayerInteractEvent event) {
		if(event.isCancelled()) return;
		ItemStack toCheck = event.getPlayer().getItemInHand();
		IAction action;
		if(event.getAction() == org.bukkit.event.block.Action.LEFT_CLICK_BLOCK
				&& ItemChecks.isUsableTool(toCheck)) {
			Block clicked = event.getClickedBlock();
			// If shears are used on anything but leaves with a left click block, return.
			Material material = clicked.getType();
			// If it is an item that can be sheared
			if(toCheck.getType().equals(Material.SHEARS) && !(material.equals(Material.LEAVES)
					|| material.equals(Material.LEAVES_2) || material.equals(Material.DEAD_BUSH)
					|| material.equals(Material.LONG_GRASS) || material.equals(Material.DOUBLE_PLANT))) {
				return;
			}
			action = new Action(ActionType.USE, getItemName(toCheck));
			// Use tool or something on block
		} else if(event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK
				&& ItemChecks.isUsableItem(toCheck)) {
			// Shears do nothing when a block is right clicked
			if(toCheck.getType().equals(Material.SHEARS)) return;
			// F+S, Spawn Eggs, Water + Lava Bucket
			action = new Action(ActionType.USE, getItemName(toCheck));
		} else if (event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_AIR
				&& ItemChecks.isUsableItem(toCheck)) {
			// If they right clicked air with a fishing rod, set that as the action
			// otherwise return, because there is nothing else to check.
			if(toCheck.getType().equals(Material.FISHING_ROD)) {
				action = new Action(ActionType.USE, getItemName(toCheck));
			} else  {
				return;
			}
		} else {
			return;
		}
		INoItemPlayer player = getPlayer(event.getPlayer());
		if(player == null) return;
		if(!player.canDoAction(action)) {
			event.setCancelled(true);
			player.notifyPlayer(action);
		}

	}
	*/
	
	@EventHandler
	public void onPlayerUseOnEntityEvent(PlayerInteractEntityEvent event) {
		if(event.isCancelled()) return;
	}

	// Utility Methods
	private boolean isAir(ItemStack stack) {
		if(stack == null) return true;
		return stack.getType().equals(Material.AIR);
	}
	
	private boolean isAir(Block block) {
		if(block == null) return true;
		return block.getType().equals(Material.AIR);
	}
	
	// Returns -1 if there is no empty quickbar slot
	/*private int firstEmptyQuickbarSlot(Player player) {
		ItemStack itemInSlot;
		for(int i = 0; i < 9; i++) {
			itemInSlot = player.getInventory().getItem(i);
			if(isAir(itemInSlot)) return i;
		}
		return -1;
	}*/
	
	public INoItemPlayer getPlayer(HumanEntity player) {
		return players.get(player.getUniqueId());
	}
}
