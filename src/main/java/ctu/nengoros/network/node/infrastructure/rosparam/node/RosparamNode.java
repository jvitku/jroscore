package ctu.nengoros.network.node.infrastructure.rosparam.node;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.apache.commons.logging.Log;

import ctu.nengoros.network.node.infrastructure.rosparam.RosparamInt;
import ctu.nengoros.network.node.infrastructure.rosparam.impl.Rosparam;

/**
 * Implementation (actual ROS node) which is able to provide Rosparam functionality
 * 
 * @author Jaroslav Vitku
 *
 */
public class RosparamNode extends AbstractNodeMain implements RosparamInt{

	private Rosparam r; // my private parameter handler
	protected Log l;

	@Override
	public GraphName getDefaultNodeName() { return GraphName.of("RosparamNode"); }

	@Override
	public void onStart(ConnectedNode connectedNode){

		r = new Rosparam(connectedNode);
		l = connectedNode.getLog();

		// ROS uses these cancellable loops
		connectedNode.executeCancellableLoop(new CancellableLoop() {

			@Override
			protected void setup() {
				l.info("RosparamNode Launched! Waiting for commands..");
			}

			@Override
			protected void loop() throws InterruptedException {
				Thread.sleep(r.sleeptime);
			}
		});
	}

	@Override
	public void set(String key, String value) {
		this.awaitNodeReady();
		r.set(key, value);
	}

	@Override
	public void set(String key, Integer value) {
		this.awaitNodeReady();
		r.set(key, value);
	}

	@Override
	public void set(String key, Double value) {
		this.awaitNodeReady();
		r.set(key, value);		
	}

	@Override
	public void set(String key, boolean value) {
		this.awaitNodeReady();
		r.set(key, value);		
	}

	@Override
	public String getString(String key) throws Exception {
		this.awaitNodeReady();
		return r.getString(key);
	}

	@Override
	public Integer getInteger(String key) throws Exception {
		this.awaitNodeReady();
		return r.getInteger(key);
	}

	@Override
	public Double getDouble(String key) throws Exception {
		this.awaitNodeReady();
		return r.getDouble(key);
	}

	@Override
	public Boolean getBoolean(String key) throws Exception {
		this.awaitNodeReady();
		return r.getBoolean(key);
	}

	@Override
	public String getString(String key, String def) {
		this.awaitNodeReady();
		return r.getString(key, def);
	}

	@Override
	public Integer getInteger(String key, int def) {
		this.awaitNodeReady();
		return r.getInteger(key, def);
	}

	@Override
	public Double getDouble(String key, double def) {
		this.awaitNodeReady();
		return r.getDouble(key, def);
	}

	@Override
	public Boolean getBoolean(String key, boolean def) {
		this.awaitNodeReady();
		return r.getBoolean(key, def);
	}

	@Override
	public String getStringValueofParam(String key) throws Exception {
		this.awaitNodeReady();
		return r.getStringValueofParam(key);
	}

	@Override
	public void delete(String key) {
		this.awaitNodeReady();
		r.delete(key);	
	}

	@Override
	public String printTree() {
		this.awaitNodeReady();
		return r.printTree();
	}

	/**
	 * Wait after the method onStart is called and my Rosparam
	 * is instantiated
	 */
	protected void awaitNodeReady(){

		while(r==null){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean hasParam(String key) {
		return r.hasParam(key);
	}

	@Override
	public GraphName getNamespace() {
		this.awaitNodeReady();
		return r.getNamespace();
	}

	@Override
	public GraphName getAbsoluteName() {
		this.awaitNodeReady();
		return r.getAbsoluteName();
	}

	@Override
	public GraphName getBaseName() {
		this.awaitNodeReady();
		return r.getBaseName();
	}
}
