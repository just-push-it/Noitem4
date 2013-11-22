package net.worldoftomorrow.noitem.fakes;

import org.bukkit.entity.Player;

import net.worldoftomorrow.noitem.interfaces.IAction;
import net.worldoftomorrow.noitem.interfaces.INoItemPlayer;

public class FakeNoItemPlayer implements INoItemPlayer {
	
	public FakeNoItemPlayer() {
		
	}

	public boolean canDoAction(IAction a) {
		return false;
	}

	public boolean shouldNotify(IAction a) {
		return false;
	}

	public void reloadPermissions() {
	}

	public Player getPlayer() {
		return null;
	}

	public void notifyPlayer(IAction action) {
	}

}
