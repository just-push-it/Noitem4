package net.worldoftomorrow.noitem.actions;

import org.bukkit.block.Block;

import net.worldoftomorrow.noitem.Util;

public class ActionPlace extends Action {
	
	public ActionPlace(Block block) {
		super(ActionType.PLACE, Util.getBlockName(block));
	}
	
	public ActionPlace(Block block, byte data) {
		super(ActionType.PLACE, Util.getBlockName(block), String.valueOf(data));
	}
	
}
