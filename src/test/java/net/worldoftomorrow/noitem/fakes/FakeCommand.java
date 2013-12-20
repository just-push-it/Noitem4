package net.worldoftomorrow.noitem.fakes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class FakeCommand extends Command {

	public FakeCommand(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender arg0, String arg1, String[] arg2) {
		return false;
	}

}
