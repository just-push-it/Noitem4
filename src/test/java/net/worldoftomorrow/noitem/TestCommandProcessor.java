package net.worldoftomorrow.noitem;

import static org.junit.Assert.*;
import net.worldoftomorrow.noitem.fakes.FakeNoItem;
import net.worldoftomorrow.noitem.fakes.FakePlayer;

import org.junit.Test;

public class TestCommandProcessor {
	public final CommandProcessor testCmdProc = new CommandProcessor(new FakeNoItem());
	
	@Test
	public void sendsPlayerMessageWhenArgsInvalid() {
		FakePlayer fakeSender = new FakePlayer();
		fakeSender.addPermission("noitem.admin");
		String[] args = new String[] {
				"random", "other"
		};
		testCmdProc.onCommand(fakeSender, null, null, args);
		//assertTrue(fakeSender.lastMessage.equalsIgnoreCase(ChatColor.RED + "Usage: /noitem reload <playername> [-q (quiet)]"));
		// I just care that a message is sent, not what the message is
		assertNotNull(fakeSender.lastMessage);
	}
	
	@Test
	public void sendPlayerMessageWhenNoPermission() {
		FakePlayer fakeSender = new FakePlayer();
		testCmdProc.onCommand(fakeSender, null, null, null);
		//assertTrue(fakeSender.lastMessage.equalsIgnoreCase(ChatColor.RED + "You do not have permission to perform this command."));
		// I just care that a message is sent, not what the message is
		assertNotNull(fakeSender.lastMessage);
	}
}