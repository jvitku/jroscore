package ctu.nengoros.rosparam.node;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.parameter.ParameterTree;

import ctu.nengoros.rosparam.impl.PrivateRosparam;

/**
 * Just a dymmy node for testing.
 * 
 * @author Jaroslav Vitku
 *
 */
public class DummyNode extends AbstractNodeMain{

	private int sleeptime = 10, maxsleep=5000;
	public static final String DEFAULT_NAME = "DefaulNodeName";

	protected ConnectedNode cn;
	protected ParameterTree pt;
	protected Log l;

	protected PrivateRosparam r;

	public boolean hasParam(String key){ return r.hasParam(key); }
	
	public String getParam(String key){
		try {
			return r.getString(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public GraphName getNamespace(){
		this.awaitReady();
		return r.getNamespace();
	}

	public GraphName getAbsoluteName(){
		this.awaitReady();
		return r.getAbsoluteName();
	}

	public GraphName getBaseName(){ 
		this.awaitReady();
		return r.getBaseName(); 
	}

	public int getNumPrivateParams(){
		this.awaitReady();
		return r.getNumPrivateParams();
	}

	public Map<?,?> getPrivateParamsMap(){
		this.awaitReady();
		try{
			return pt.getMap(cn.getName());
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public void onStart(final ConnectedNode connectedNode) {
		cn = connectedNode;
		l = cn.getLog();
		pt = cn.getParameterTree();
		r = new PrivateRosparam(connectedNode);
	}

	private void awaitReady(){
		int poc = 0;
		while(r==null){
			try {
				Thread.sleep(10);
				poc++;
				if(poc*sleeptime>maxsleep){
					System.err.println("[DummyNode] error! waited for ROS node"
							+ "to be ready more than: "+maxsleep+"ms, giving up!");
					return;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of(DEFAULT_NAME);
	}
}
