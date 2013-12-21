package net.worldoftomorrow.noitem;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestAction {
	
	public final Action action = new Action(ActionType.BREAK, "stone");
	public final Action actionWithSecObj = new Action(ActionType.BREAK, "stone", "1");
	
	@Test
	public void testActionCreationTime() {
		// This test is simply to make sure action creation is not costly
		@SuppressWarnings("unused")
		final Action action = new Action(ActionType.BREAK, "stone");
	}
	
	@Test
	public void test2ObjActionCreationTime() {
		// This test is simply to make sure action creation is not costly
		@SuppressWarnings("unused")
		final Action actionWithSecObj = new Action(ActionType.BREAK, "stone", "1");
	}
	
	@Test
	public void objPermissionIsExpected() {
		assertEquals(action.getObjectPerm(), "noitem.object.stone.break");
	}
	
	@Test
	public void actPermissionIsExpected() {
		assertEquals(action.getActionPerm(), "noitem.action.break.stone");
	}
	
	@Test
	public void actPermissionAllIsExpected() {
		assertEquals(action.getAllActionPerm(), "noitem.action.break.*");
	}
	
	@Test
	public void objPermissionAllIsExpeceted() {
		assertEquals(action.getAllObjectPerm(), "noitem.object.stone.*");
	}
	
	@Test
	public void actPermissionIsExpected2Obj() {
		assertEquals(actionWithSecObj.getObjectPerm(), "noitem.object.stone.1.break");
	}
	
	@Test
	public void objPermissionIsExpected2Obj() {
		assertEquals(actionWithSecObj.getActionPerm(), "noitem.action.break.stone.1");
	}
	
	@Test
	public void actPermissionAllIsExpected2Obj() {
		assertEquals(actionWithSecObj.getAllActionPerm(), "noitem.action.break.*");
	}
	
	@Test
	public void objPermissionAllIsExpected2Obj() {
		assertEquals(actionWithSecObj.getAllObjectPerm(), "noitem.object.stone.1.*");
	}
}
