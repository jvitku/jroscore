package ctu.nengoros.rosparam;

/**
 * Interface for a rosparam class, @see: http://wiki.ros.org/rosparam
 * 
 * Supported data types so far:
 * string: 'foo'
 * integer: 1234
 * double: 1234.5
 * boolean: true
 * 
 * @author Jaroslav Vitku
 *
 */
public interface RosparamInt extends NodeInfoInt{
	
	//public boolean coreRunning();
	
	public void set(String key, String value);
	public void set(String key, Integer value);
	public void set(String key, Double value);
	public void set(String key, boolean value);
	
	public String getString(String key) throws Exception;
	public Integer getInteger(String key) throws Exception;
	public Double getDouble(String key) throws Exception;
	public Boolean getBoolean(String key) throws Exception;
	
	// the same as above, bout catch exception and return default value
	public String getString(String key, String def);
	public Integer getInteger(String key, int def);
	public Double getDouble(String key, double def);
	public Boolean getBoolean(String key, boolean def);
	
	
	public boolean hasParam(String key);

	
	/**
	 * Try to get String, Integer, Double, Boolean and then throw Exception
	 * @param key key
	 * @return String value of the value, null if key not found
	 * @throws Exception if non of that worked (unsupported format) 
	 */
	public String getStringValueofParam(String key) throws Exception;
	
	public void delete(String key);
	
	public String printTree();
}
