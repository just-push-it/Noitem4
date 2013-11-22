package net.worldoftomorrow.noitem.interfaces;

public interface IConfigManager {
	
	/**
	 * Get the default value of the given key
	 * @param key
	 * @return default value
	 */
	public String getDefaultValue(String key);
	
	/**
	 * Get the current value of the given key or the
	 * default value if none is set
	 * @param key
	 * @return current value
	 */
	public String getValue(String key);
	
	/**
	 * Set the value of the given key
	 * @param key
	 * @param value
	 */
	public void setValue(String key, Object value);
	
	
}
