package ctu.nengoros.network.node.infrastructure.rosparam;


/**
 * This is able to simply read private parameters of a ROS node from 
 * the parameter tree. 
 * 
 * This reads private nodes parameters, which can be setup e.g.
 * from commandline during launch, example parameter: _parameterX:=10.4 
 *  
 * @author Jaroslav Vitku
 *
 */
public interface PrivateRosparamInt extends RosparamInt{

	/**
	 * Parses list of integer values separated by comma, example:
	 * _dummyList:=10,11,22,31 will return list of corresponding integers
	 * 
	 * @param key name of the parameter (dummyList here)
	 * @return list of parsed integers
	 * @throws Exception thrown if length of list is 0 or some of integers not parsed ok
	 */
	public int[] getMyIntegerList(String key) throws Exception;
	public String getMyString(String key) throws Exception;
	public Integer getMyInteger(String key) throws Exception;
	public Double getMyDouble(String key) throws Exception;
	public Boolean getMyBoolean(String key) throws Exception;
	
	// the same as above, bout catch exception and return default value
	public int[] getMyIntegerList(String key, int[] def);
	public String getMyString(String key, String def);
	public Integer getMyInteger(String key, int def);
	public Double getMyDouble(String key, double def);
	public Boolean getMyBoolean(String key, boolean def);
	
	public int getNumPrivateParams();
	public boolean hasPrivateParam(String key);
}
