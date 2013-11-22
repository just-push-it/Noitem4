package net.worldoftomorrow.noitem;

import static org.junit.Assert.*;
import net.worldoftomorrow.noitem.fakes.FakePlayer;

import org.junit.Test;

public class TestNoItemPlayer {

	@Test
	public void canDoActionReturnsProperlyFalse() {
		FakePlayer fakePlayer = new FakePlayer();
		fakePlayer.addPermission("noitem.action.break.stone");
		NoItemPlayer mockPlayer = new NoItemPlayer(fakePlayer);
		assertFalse(mockPlayer.canDoAction(new Action(ActionType.BREAK, "stone")));
	}
	
	@Test
	public void canDoActionReturnsProperlyTrue() {
		FakePlayer fakePlayer = new FakePlayer();
		NoItemPlayer mockPlayer = new NoItemPlayer(fakePlayer);
		assertTrue(mockPlayer.canDoAction(new Action(ActionType.BREAK, "stone")));
	}
	
	@Test
	public void doPermissionsReloadCorrectly() {
		FakePlayer fakePlayer = new FakePlayer();
		NoItemPlayer mockPlayer = new NoItemPlayer(fakePlayer);
		fakePlayer.addPermission("noitem.action.break.stone");
		mockPlayer.reloadPermissions();
		assertFalse(mockPlayer.canDoAction(new Action(ActionType.BREAK, "stone")));
	}
}
