package ctu.nengoros.network.node.infrastructure.rosparam.impl;

import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.parameter.ParameterTree;

import ctu.nengoros.network.node.infrastructure.rosparam.ParameterTreeCrawler;
import ctu.nengoros.network.node.infrastructure.rosparam.RosparamInt;

/**
 * 
 * Class for simple reading parameters of a connected node.
 *  
 * @author Jaroslav Vitku
 *
 */
public class Rosparam extends NodeInfo implements RosparamInt{

	public final String me = "[Rosparam] ";
	public static final String notFound = "Parameter not found";
	public final int sleeptime = 50;
	
	protected final ParameterTree pt;
	protected ParameterTreeCrawler ptc;
	
	public Rosparam(ConnectedNode connectedNode){
		super(connectedNode);
		
		pt = connectedNode.getParameterTree();
		ptc = new ParameterTreeCrawler(pt);
	}
	
	@Override
	public void set(String key, String value) {
		l.debug(me+"Setting key: "+key+" to String value: "+value);
		pt.set(key, value);
	}

	@Override
	public void set(String key, Integer value) {
		l.debug(me+"Setting key: "+key+" to Integer value: "+value);
		pt.set(key, value);
	}

	@Override
	public void set(String key, Double value) {
		l.debug(me+"Setting key: "+key+" to Double value: "+value);
		pt.set(key, value);
	}

	@Override
	public void set(String key, boolean value) {
		l.debug(me+"Setting key: "+key+" to boolean value: "+value);
		pt.set(key, value);
	}

	@Override
	public String getString(String key) throws Exception {
		l.debug(me+"Getting String by the key: "+key);
		return pt.getString(key);
	}

	@Override
	public Integer getInteger(String key) throws Exception {
		l.debug(me+"Getting Integer by the key: "+key);
		return pt.getInteger(key);
	}

	@Override
	public Double getDouble(String key) throws Exception {
		l.debug(me+"Getting Double by the key: "+key);
		return pt.getDouble(key);
	}

	@Override
	public Boolean getBoolean(String key) throws Exception {
		l.debug(me+"Getting Boolean by the key: "+key);
		return pt.getBoolean(key);
	}

	@Override
	public String getString(String key, String def){
		l.debug(me+"Getting String by the key: "+key);
		try{
			return pt.getString(key);
		}catch(Exception e){
			l.info(me+"Could not find the parameter: "+key+", returning default value!");
			return def;
		}
	}

	@Override
	public Integer getInteger(String key, int def){
		l.debug(me+"Getting Integer by the key: "+key);
		try{
			return pt.getInteger(key);
		}catch(Exception e){
			l.info(me+"Could not find the parameter: "+key+", returning default value!");
			return def;
		}
	}

	@Override
	public Double getDouble(String key, double def){
		l.debug(me+"Getting Double by the key: "+key);
		try{
			return pt.getDouble(key);
		}catch(Exception e){
			l.info(me+"Could not find the parameter: "+key+", returning default value!");
			return def;
		}
	}

	@Override
	public Boolean getBoolean(String key, boolean def){
		l.debug(me+"Getting Boolean by the key: "+key);
		try{
			return pt.getBoolean(key);
		}catch(Exception e){
			l.info(me+"Could not find the parameter: "+key+", returning default value!");
			return def;
		}
	}

	@Override
	public void delete(String key){
		pt.delete(key);
	}

	@Override
	public String printTree() {
		return ptc.getAllS();
	}

	@Override
	public String getStringValueofParam(String key) throws Exception {
		String val = ptc.getParam(GraphName.of(key));

		if(val==null)
			return notFound;
		return val;
	}

	@Override
	public boolean hasParam(String key) {
		return pt.has(key);
	}

}
