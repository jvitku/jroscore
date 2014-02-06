package ctu.nengoros.network.node.infrastructure.simulation;

import org.apache.commons.logging.Log;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import ctu.nengoros.network.common.exceptions.StartupDelayException;
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
public class SimulationMaster extends AbstractNodeMain implements StartedObject{

	private boolean started = false;

	public static final String name = "SimulationMaster"; // redefine the nodes name
	public final String me = "["+name+"] ";
	// waiting for the node to be ready
	public StartupManager startup = new BasicStartupManager(this);
	public String fullName = name;		// change this to hold your unique name

	protected Log log;
	protected Publisher<std_msgs.String> commandPublisher;

	@Override
	public void onStart(ConnectedNode connectedNode) {
		log = connectedNode.getLog();
		this.registerRosCommunication(connectedNode);

		System.out.println(name+" node started!");
		this.started = true;
	}

	/**
	 * Publish ROS message which tells to all (subscribed) nodes to call 
	 * {@link ctu.nengoros.network.common.Resettable#softReset(boolean)}.  
	 * @param randomize the randomization is set to default value for now
	 */
	public void callHardReset(boolean randomize){

		log.info(me+"Publishind SoftReset to all subscribed nodes.");
		System.out.println(me+"Publishind SoftReset to all subscribed nodes.");
		std_msgs.String fl = commandPublisher.newMessage();
		fl.setData(Messages.HARD_RESET);
		commandPublisher.publish(fl);
	}

	/**
	 * Publish ROS message which tells to all (subscribed) nodes to call 
	 * {@link ctu.nengoros.network.common.Resettable#hardReset(boolean)}.  
	 * @param randomize the randomization is set to default value for now
	 */
	public void callSoftReset(boolean randomize){

		log.info(me+"Publishind SoftReset to all subscribed nodes.");
		System.out.println(me+"Publishind SoftReset to all subscribed nodes.");
		std_msgs.String fl = commandPublisher.newMessage();
		fl.setData(Messages.SOFT_RESET);
		commandPublisher.publish(fl);
	}

	private void registerRosCommunication(ConnectedNode connectedNode){
		commandPublisher =connectedNode.newPublisher(Messages.SIMULATOR_TOPIC,
				std_msgs.String._TYPE);
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

}
