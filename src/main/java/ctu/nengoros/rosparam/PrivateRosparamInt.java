package ctu.nengoros.rosparam;


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

	public String getMyString(String key) throws Exception;
	public Integer getMyInteger(String key) throws Exception;
	public Double getMyDouble(String key) throws Exception;
	public Boolean getMyBoolean(String key) throws Exception;
	
	// the same as above, bout catch exception and return default value
	public String getMyString(String key, String def);
	public Integer getMyInteger(String key, int def);
	public Double getMyDouble(String key, double def);
	public Boolean getMyBoolean(String key, boolean def);
	
	public int getNumPrivateParams();
	public boolean hasPrivateParam(String key);
}
