package ctu.nengoros.rosparam;

import org.apache.commons.logging.Log;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.parameter.ParameterTree;

/**
 * First, basic implementation of rosparam (@see RosparamNode). The RosparamNode just starts and 
 * waits for commands.
 * Assumed that the RosRunner is used, the RosRunner.start() uses awaitStart method, so 
 * we can start node and immediately call set, get().. methods.
 *  
 * @author Jaroslav Vitku
 *
 */
public abstract class Rosparam extends AbstractNodeMain implements RosparamInt{

	public Log l;
	public ParameterTree pt;
	public ParameterTreeCrawler ptc;
	public final int sleeptime = 50;
	
	// assume that the core is running after onStart is called
	// set this to true in the onStart method
	protected boolean coreRunning = false;	
	
	@Override
	public GraphName getDefaultNodeName() { return GraphName.of("jrosparam"); }
	
	@Override
	public void onStart(ConnectedNode connectedNode){
		this.coreRunning = true;

		pt = connectedNode.getParameterTree();
		ptc = new ParameterTreeCrawler(pt);
	}

	@Override
	public boolean coreRunning() { return this.coreRunning; }

	@Override
	public void set(String key, String value) {
		awaitParamsReady();
		l.debug("Setting key: "+key+" to String value: "+value);
		pt.set(key, value);
	}

	@Override
	public void set(String key, Integer value) {
		awaitParamsReady();
		l.debug("Setting key: "+key+" to Integer value: "+value);
		pt.set(key, value);
	}

	@Override
	public void set(String key, Double value) {
		awaitParamsReady();
		l.debug("Setting key: "+key+" to Double value: "+value);
		pt.set(key, value);
	}

	@Override
	public void set(String key, boolean value) {
		awaitParamsReady();
		l.debug("Setting key: "+key+" to boolean value: "+value);
		pt.set(key, value);
	}
	
	@Override
	public String getString(String key) throws Exception {
		awaitParamsReady();
		l.debug("Getting String by the key: "+key);
		return pt.getString(key);
	}

	@Override
	public Integer getInteger(String key) throws Exception {
		awaitParamsReady();
		l.debug("Getting Integer by the key: "+key);
		return pt.getInteger(key);
	}

	@Override
	public Double getDouble(String key) throws Exception {
		awaitParamsReady();
		l.debug("Getting Double by the key: "+key);
		return pt.getDouble(key);
	}

	@Override
	public Boolean getBoolean(String key) throws Exception {
		awaitParamsReady();
		l.debug("Getting Boolean by the key: "+key);
		return pt.getBoolean(key);
	}

	@Override
	public void delete(String key){
		awaitParamsReady();
		pt.delete(key);
	}

	@Override
	public String printTree() {
		awaitParamsReady();
		return ptc.getAll();
	}

	@Override
	public String getStringValofParam(String key) throws Exception {
		String val = ptc.getParam(GraphName.of(key));
		
		if(val==null){
			return "Not found";
		}
		return val;
	}
	
	private final void awaitParamsReady(){
		while(ptc==null || pt==null){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
