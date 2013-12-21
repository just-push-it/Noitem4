package net.worldoftomorrow.noitem.interfaces;

public interface IConfiguration {
	
	/**
	 * Checks if the configuration has this default value
	 * @param key
	 * @return
	 */
	public boolean hasDefaultValue(String key);
	
	/**
	 * Gets the specified default value, returns null if it
	 * does not exist
	 * @param key
	 * @return
	 */
	public String getDefaultValue(String key);
	
	/**
	 * Adds the default value with an boolean option to specify
	 * whether to overwrite if the value already exists
	 * @param key
	 * @param value
	 * @param overwrite
	 */
	public void addDefaultValue(String key, String value, boolean overwrite);
	
	/**
	 * Adds the default value but does not overwrite an existing value
	 * @param key
	 * @param value
	 */
	public void addDefaultValue(String key, String value);
	
	/**
	 * Gets the current actual value of the key
	 * @param key
	 * @return
	 */
	public String getValue(String key);
	
	/**
	 * Sets the actual value of the key
	 * @param key
	 * @param value
	 */
	public void setValue(String key, Object value);
	
	/**
	 * Saves the configuration with the current values
	 * and adds any values that are not yet written.
	 */
	public void saveConfig();
}
