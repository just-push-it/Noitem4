package net.worldoftomorrow.noitem;

import static org.junit.Assert.*;

import org.junit.Test;

import net.worldoftomorrow.noitem.config.ConfigManager;
import net.worldoftomorrow.noitem.exceptions.NoSuchConfigValueException;
import net.worldoftomorrow.noitem.fakes.FakeNoItem;

public class TestConfigManager {
	
	@Test(expected=NoSuchConfigValueException.class)
	public void Config_ExcepteptionForInvalidValue() {
		final ConfigManager configMan = new ConfigManager(new FakeNoItem());
		configMan.getValue("Value.That.Does.Not.Exist");
	}
	
	@Test
	public void GetAndSetValueIsExpected() {
		final ConfigManager configMan = new ConfigManager(new FakeNoItem());
		configMan.setValue("testvalue.1", "hello");
		assertEquals(configMan.getValue("testvalue.1"), "hello");
	}
	
	@Test
	public void GetDefaultValue_ReturnedProperly() {
		final ConfigManager configMan = new ConfigManager(new FakeNoItem());
		// FakeConfiguration automatically puts default value of "testvalue.2" to "test2"
		assertEquals(configMan.getDefaultValue("testvalue.2"), "test2");
	}
	
	@Test
	public void GetValue_ReturnsDefaultIfValueNotSet() {
		final ConfigManager configMan = new ConfigManager(new FakeNoItem());
		assertEquals(configMan.getValue("testvalue.2"), "test2");
	}
}
