package net.worldoftomorrow.noitem.actions;

import org.bukkit.block.Block;

import net.worldoftomorrow.noitem.Util;

public class ActionInteractObject extends Action {
	public ActionInteractObject(Block block) {
		super(ActionType.INTERACT_OBJECT, Util.getBlockName(block));
	}
	
	public ActionInteractObject(Block block, int data) {
		super(ActionType.INTERACT_OBJECT, Util.getBlockName(block), String.valueOf(data));
	}
}
