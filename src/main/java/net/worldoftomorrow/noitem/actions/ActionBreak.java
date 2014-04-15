package net.worldoftomorrow.noitem.actions;

import org.bukkit.block.Block;

import net.worldoftomorrow.noitem.Util;

public class ActionBreak extends Action {

	public ActionBreak(Block block) {
		super(ActionType.BREAK, Util.getBlockName(block));
	}
	
	public ActionBreak(Block block, byte data) {
		super(ActionType.BREAK, Util.getBlockName(block), String.valueOf(data));
	}

}
