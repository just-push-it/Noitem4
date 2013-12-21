package net.worldoftomorrow.noitem.interfaces;

public interface ILang {
	
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
	 * Shorthand for getValue(String key);
	 * @param key
	 * @return
	 */
	public String $(String key);

}
