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
		assertTrue(action.getObjectPerm().equals("noitem.object.stone.break"));
	}
	
	@Test
	public void actPermissionIsExpected() {
		assertTrue(action.getActionPerm().equals("noitem.action.break.stone"));
	}
	
	@Test
	public void actPermissionAllIsExpected() {
		assertTrue(action.getAllActionPerm().equals("noitem.action.break.*"));
	}
	
	@Test
	public void objPermissionAllIsExpeceted() {
		assertTrue(action.getAllObjectPerm().equals("noitem.object.stone.*"));
	}
	
	@Test
	public void actPermissionIsExpected2Obj() {
		assertTrue(actionWithSecObj.getObjectPerm().equals("noitem.object.stone.1.break"));
	}
	
	@Test
	public void objPermissionIsExpected2Obj() {
		assertTrue(actionWithSecObj.getActionPerm().equals("noitem.action.break.stone.1"));
	}
	
	@Test
	public void actPermissionAllIsExpected2Obj() {
		assertTrue(actionWithSecObj.getAllActionPerm().equals("noitem.action.break.*"));
	}
	
	@Test
	public void objPermissionAllIsExpected2Obj() {
		assertTrue(actionWithSecObj.getAllObjectPerm().equals("noitem.object.stone.1.*"));
	}
}
