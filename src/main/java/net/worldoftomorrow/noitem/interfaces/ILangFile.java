package net.worldoftomorrow.noitem.interfaces;

public interface ILangFile {
	
	/**
	 * Checks if the lang file has this default value
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
	 * Gets the current actual value of the key
	 * @param key
	 * @return
	 */
	public String getValue(String key);
	
	/**
	 * Saves the lang file with the current values
	 * and adds any values that are not yet written.
	 */
	public void saveLangFile();
}
