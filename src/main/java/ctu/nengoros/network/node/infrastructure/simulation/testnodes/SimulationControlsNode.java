package ctu.nengoros.network.node.infrastructure.simulation.testnodes;

import org.apache.commons.logging.Log;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import ctu.nengoros.network.common.exceptions.StartupDelayException;
import ctu.nengoros.network.node.infrastructure.simulation.SimulationController;
import ctu.nengoros.network.node.infrastructure.simulation.SimulationControls;
import ctu.nengoros.network.node.synchedStart.StartupManager;
import ctu.nengoros.network.node.synchedStart.impl.BasicStartupManager;
import ctu.nengoros.network.node.synchedStart.impl.StartedObject;

/**
 * This is able to publish simulation commands over the ROS network. Currently request
 * for resetting.
 * 
 * @see ctu.nengoros.network.node.infrastructure.simulation.Messages
 * @see ctu.nengoros.network.common.Resettable
 * 
 * @author Jaroslav Vitku
 *
 */
public class SimulationControlsNode extends AbstractNodeMain implements StartedObject, SimulationControls{

	private boolean started = false;

	public static final String name = "SimulationControlsNode"; // redefine the nodes name
	public final String me = "["+name+"] ";
	// waiting for the node to be ready
	public StartupManager startup = new BasicStartupManager(this);
	public String fullName = name;		// change this to hold your unique name

	protected Log log;
	protected Publisher<std_msgs.String> commandPublisher;

	private SimulationController controller;
	
	private ConnectedNode connectedNode;
	
	@Override
	public void onStart(ConnectedNode connectedNode) {
		log = connectedNode.getLog();
		//this.registerRosCommunication(connectedNode);
		this.connectedNode = connectedNode;
		
		this.controller = new SimulationController(name, log, connectedNode);

		System.out.println(name+" node started!");
		this.started = true;
	}
	
	public String getNamespace(){
		return connectedNode.getResolver().getNamespace().toString();
	}
	
	@Override
	public GraphName getDefaultNodeName() { return GraphName.of(name); }

	@Override
	public boolean isStarted() {return this.started; }

	@Override
	public StartupManager getStartupManager() { return this.startup; }

	@Override
	public String getFullName() { return this.fullName; }
	
	public void awaitStarted() throws StartupDelayException{
		startup.awaitStarted();
	}
	@Override
	public void callHardReset(boolean randomize) {
		try {
			this.startup.awaitStarted();
		} catch (StartupDelayException e) {
			e.printStackTrace();
		}
		//System.out.println("calling hard reset");
		this.controller.callHardReset(randomize);
	}
	@Override
	public void callSoftReset(boolean randomize) {
		try {
			this.startup.awaitStarted();
		} catch (StartupDelayException e) {
			e.printStackTrace();
		}
		//System.out.println("calling soft reset");
		this.controller.callSoftReset(randomize);
	}

}
