package net.worldoftomorrow.noitem;

import static org.junit.Assert.assertEquals;
import net.worldoftomorrow.noitem.exceptions.NoSuchLangValueException;
import net.worldoftomorrow.noitem.fakes.FakeNoItem;
import net.worldoftomorrow.noitem.lang.Lang;

import org.junit.Test;

public class TestLang {
	
	@Test
	public void getDefaultValueReturnsExpected() {
		final Lang lang = new Lang(new FakeNoItem());
		assertEquals(lang.getDefaultValue("command.test.test"), "test value");
	}
	
	@Test
	public void customValuesOverrideDefaults() {
		final Lang lang = new Lang(new FakeNoItem());
		assertEquals(lang.getValue("command.test.customval"), "changed custom value");
	}
	
	@Test
	public void getValueReturnsDefaultIfNotSet() {
		final Lang lang = new Lang(new FakeNoItem());
		assertEquals(lang.getValue("command.test.test"), "test value");
	}
	
	@Test(expected=NoSuchLangValueException.class)
	public void getValueThrowsExceptionIfNoDefault() {
		final Lang lang = new Lang(new FakeNoItem());
		lang.getValue("Some.Value.That.Does.Not.Exist");
	}
}
