package ctu.nengoros.rosparam.node;

import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;

import ctu.nengoros.rosparam.PrivateRosparamInt;
import ctu.nengoros.rosparam.impl.PrivateRosparam;

/**
 * Node with simple access to private parameters, uses ProvateRosparam and RosParam classes for this.
 * 
 * @author Jaroslav Vitku
 *
 */
public class PrivateRosparamNode extends RosparamNode implements PrivateRosparamInt{

	public static final String DEFNAME = "PrivateRosparamNode";

	private PrivateRosparam pr; // my private parameter handler

	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of(DEFNAME);
	}

	@Override
	public void onStart(ConnectedNode connectedNode){
		super.onStart(connectedNode);
		pr = new PrivateRosparam(connectedNode);
	}

	@Override
	public String getMyString(String key) throws Exception {
		this.awaitNodeReady();
		return pr.getMyString(key);
	}

	@Override
	public Integer getMyInteger(String key) throws Exception {
		this.awaitNodeReady();
		return pr.getMyInteger(key);
	}

	@Override
	public Double getMyDouble(String key) throws Exception {
		this.awaitNodeReady();
		return pr.getMyDouble(key);
	}

	@Override
	public Boolean getMyBoolean(String key) throws Exception {
		this.awaitNodeReady();
		return pr.getMyBoolean(key);
	}

	@Override
	public String getMyString(String key, String def) {
		this.awaitNodeReady();
		return pr.getMyString(key, def);
	}

	@Override
	public Integer getMyInteger(String key, int def) {
		this.awaitNodeReady();
		return pr.getMyInteger(key, def);
	}

	@Override
	public Double getMyDouble(String key, double def) {
		this.awaitNodeReady();
		return pr.getMyDouble(key, def);
	}

	@Override
	public Boolean getMyBoolean(String key, boolean def) {
		this.awaitNodeReady();
		return pr.getMyBoolean(key, def);
	}

	@Override
	public int getNumPrivateParams() {
		this.awaitNodeReady();
		return pr.getNumPrivateParams();
	}

	@Override
	public boolean hasPrivateParam(String key) {
		this.awaitNodeReady();
		return pr.hasPrivateParam(key);
	}

	protected void awaitNodeReady(){
		super.awaitNodeReady();

		while(pr==null){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}


