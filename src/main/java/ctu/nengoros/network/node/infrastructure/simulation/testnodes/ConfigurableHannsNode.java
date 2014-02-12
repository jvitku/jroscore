package ctu.nengoros.network.node.infrastructure.simulation.testnodes;

import java.util.LinkedList;

import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;

import ctu.nengoros.network.node.AbstractConfigurableHannsNode;
import ctu.nengoros.network.node.observer.Observer;
import ctu.nengoros.network.node.observer.stats.ProsperityObserver;

/**
 * Simple ROS node that can be reseted over the ROS network.
 * 
 * @author Jaroslav Vitku
 *
 */
public class ConfigurableHannsNode extends AbstractConfigurableHannsNode{

	private String name = "name";
	private boolean started = false;
	
	public boolean softResetted = false;
	public boolean hardResetted = false;
	public boolean randomized = false;
	
	private ConnectedNode connectedNode;

	@Override
	public void onStart(ConnectedNode connectedNode) {
		this.connectedNode = connectedNode;
		
		this.registerSimulatorCommunication(connectedNode);
		
		System.out.println(name+" node started!");
		this.started = true;
	}
	
	@Override
	public String listParams() {
		return null;
	}

	@Override
	public boolean isStarted() { return started; }

	@Override
	public String getFullName() { return name;	}

	@Override
	public void softReset(boolean randomize) {
		System.out.println("soft reseet called!");
		this.softResetted = true;
		this.randomized = randomize;
	}


	public String getNamespace(){
		return connectedNode.getResolver().getNamespace().toString();
	}
	
	@Override
	public void hardReset(boolean randomize) {
		System.out.println("hard reset called!");
		this.hardResetted = true;
		this.randomized = randomize;
	}

	@Override
	public float getProsperity() {
		return 0;
	}

	@Override
	public void publishProsperity() {
	}

	@Override
	public GraphName getDefaultNodeName() { return GraphName.of(name); }

	@Override
	protected void parseParameters(ConnectedNode connectedNode) {
	}

	@Override
	protected void registerParameters() {
	}

	@Override
	protected void buildDataIO(ConnectedNode connectedNode) {
	}

	@Override
	protected void buildConfigSubscribers(ConnectedNode connectedNode) {
	}

	@Override
	public ProsperityObserver getProsperityObserver() {
		return null;
	}

	@Override
	public LinkedList<Observer> getObservers() { return null; }

}
