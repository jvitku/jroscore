package ctu.nengoros.rosparam.impl;

import org.ros.node.ConnectedNode;

import ctu.nengoros.rosparam.PrivateRosparamInt;

public class PrivateRosparam extends Rosparam implements PrivateRosparamInt{

	public final String me = "[PrivateRosparam] ";

	public PrivateRosparam(ConnectedNode connectedNode){
		super(connectedNode);
	}

	@Override
	public String getMyString(String key) throws Exception {
		return pt.getString(absolutename.join(key));
	}

	@Override
	public Integer getMyInteger(String key) throws Exception {
		// try to get integer
		try{
			return pt.getInteger(absolutename.join(key));
		}catch(Exception e){
			// but this might be stored also as a String value
			String val = pt.getString(absolutename.join(key));
			try{
				return Integer.parseInt(val);
			}catch(Exception e1){
				throw e1;
			}
		}
	}

	@Override
	public Double getMyDouble(String key) throws Exception {
		try{
			return pt.getDouble(absolutename.join(key));
		}catch(Exception e){
			// but this might be stored also as a String value
			String val = pt.getString(absolutename.join(key));
			// but this might be stored also as a String value
			try{
				return Double.parseDouble(val);
			}catch(Exception e1){
				throw e1;
			}
		}
	}

	@Override
	public Boolean getMyBoolean(String key) throws Exception {
		try{
			return pt.getBoolean(absolutename.join(key));
		}catch(Exception e){
			// but this might be stored also as a String value
			String val = pt.getString(absolutename.join(key));
			if(val.equalsIgnoreCase("true"))
				return true;
			if(val.equalsIgnoreCase("false"))
				return false;

			throw new Exception("could not parse boolean value!");
		}
	}

	@Override
	public String getMyString(String key, String def) {
		try{
			return pt.getString(absolutename.join(key));
		}catch(Exception e){
			return def;
		}
	}

	@Override
	public Integer getMyInteger(String key, int def) {

		if(!pt.has(absolutename.join(key))){
			return def;
		}
		// try to get integer
		try{
			return pt.getInteger(absolutename.join(key));
		}catch(Exception e){

			// but this might be stored also as a String value
			String val = pt.getString(absolutename.join(key));
			try{
				return Integer.parseInt(val);
			}catch(NumberFormatException e1){
				return def;
			}
		}
	}

	@Override
	public Double getMyDouble(String key, double def) {
		if(!pt.has(absolutename.join(key))){
			return def;
		}
		try{
			return pt.getDouble(absolutename.join(key));
		}catch(Exception e){
			// but this might be stored also as a String value
			
			String val = pt.getString(absolutename.join(key));
			// but this might be stored also as a String value
			try{
				return Double.parseDouble(val);
			}catch(Exception e1){
				return def;
			}
		}
	}

	@Override
	public Boolean getMyBoolean(String key, boolean def) {
		if(!pt.has(absolutename.join(key)))
			return def;
		try{
			return pt.getBoolean(absolutename.join(key));
		}catch(Exception e){
			// but this might be stored also as a String value
			String val = pt.getString(absolutename.join(key));
			if(val.equalsIgnoreCase("true"))
				return true;
			if(val.equalsIgnoreCase("false"))
				return false;
			return def;
		}			
	}

	public int getNumPrivateParams(){
		try{
			return pt.getMap(absolutename).size();
		}catch(Exception e){
			return 0;
		}
	}

	@Override
	public boolean hasPrivateParam(String key) {
		return pt.has(absolutename.join(key));
	}

}
