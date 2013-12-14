package ctu.nengoros.rosparam;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.parameter.ParameterTree;

public abstract class ParamReadingNode extends AbstractNodeMain{

	private int sleeptime = 10, maxsleep=5000;
	
	protected ConnectedNode cn;
	protected ParameterTree pt;
	protected Log l;
	
	public GraphName getNamespace(){ return cn.getName().toGlobal().getParent(); }
	public GraphName getAbsoluteName(){ return cn.getName(); }
	public GraphName getBaseName(){ return cn.getName().getBasename(); }

	public int getNumPrivateParams(){
		this.awaitReady();
		try{
			return pt.getMap(cn.getName()).size();
		}catch(Exception e){
			return 0;
		}
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
		pt = connectedNode.getParameterTree();
		l = connectedNode.getLog();
	}
	
	private void awaitReady(){
		int poc = 0;
		while(cn==null || pt==null || l==null){
			try {
				Thread.sleep(10);
				poc++;
				if(poc*sleeptime>maxsleep){
					System.err.println("[ParamReadingNode] error! waited for ROS node"
							+ "to be ready more than: "+maxsleep+"ms, giving up!");
					return;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
