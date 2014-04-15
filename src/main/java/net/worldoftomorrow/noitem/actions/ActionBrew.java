package net.worldoftomorrow.noitem.actions;

import net.worldoftomorrow.noitem.interfaces.IAction;
import net.worldoftomorrow.noitem.interfaces.INoItemPlayer;

import org.bukkit.event.Cancellable;
import org.bukkit.potion.Potion;

public class ActionBrew implements IAction {
	
	private String objPerm;
	private String actPerm;
	private String objPermAll;
	private String actPermAll;
	
	private final Potion potion;
	
	public ActionBrew(Potion potion) {
		this.potion = potion;
		String type = potion.getType().toString().toLowerCase();
		objPerm = "noitem.object.potion." + type;
		objPermAll = "noitem.object.potion." + type;
		actPerm = "noitem.action.brew." + type;
		actPermAll = "noitem.action.brew.*";
		if(potion.hasExtendedDuration()) {
			objPerm = objPerm + ".extended";
			objPermAll = objPermAll + ".extended";
			actPerm = actPerm + ".extended";
		}
		if(potion.isSplash()) {
			objPerm = objPerm + ".splash";
			objPermAll = objPermAll + ".splash";
			actPerm = actPerm + ".splash";
		}
		objPerm = objPerm + ".brew";
		objPermAll = objPermAll + ".*";
	}

	public String getObjectPerm() {
		return this.objPerm;
	}
	
	public String getActionPerm() {
		return this.actPerm;
	}
	
	public String getAllActionPerm() {
		return this.actPermAll;
	}
	
	public String getAllObjectPerm() {
		return this.objPermAll;
	}
	
	public String[] getAllPerms() {
		return new String[] {
				this.actPerm,
				this.actPermAll,
				this.objPerm,
				this.objPermAll
		};
	}
	
	public String getObject() {
		String object = potion.getType().toString().toLowerCase();
		if(potion.hasExtendedDuration()) {
			object = object + ".extended";
		}
		if(potion.isSplash()) {
			object = object + ".splash";
		}
		return object;
	}
	
	public ActionType getActionType() {
		return ActionType.BREW;
	}

	public void process(INoItemPlayer player, Cancellable event) {
		if(event.isCancelled()) return;
		if(player.canDoAction(this)) return;
		event.setCancelled(true);
		if(player.shouldNotify(this)) player.notifyPlayer(this);
	}
}
