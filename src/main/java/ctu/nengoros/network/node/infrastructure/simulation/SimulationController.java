package ctu.nengoros.network.node.infrastructure.simulation;

import org.apache.commons.logging.Log;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

/**
 * This can be used in any ROS node that wants to publish 
 * messages for simulation controll. So far, only resetting
 * is implemented. 
 * 
 * @author Jaroslav Vitku
 *
 */
public class SimulationController implements SimulationControls{

	// own publisher for controlling the simulation (e.g. reset)
	protected Publisher<std_msgs.String> commandPublisher;	
	protected Log log;

	private final String me;

	public SimulationController(String name, Log log, 
			ConnectedNode connectedNode){
		this.me = "["+name+"] ";
		this.log = log;
		this.registerRosCommunication(connectedNode);
	}

	@Override
	public void callHardReset(boolean randomize){

		log.info(me+"Publishing HardReset to all subscribed nodes.");
		//System.out.println(me+"Publishing HardReset to all subscribed nodes.");
		std_msgs.String fl = commandPublisher.newMessage();
		if(randomize)
			fl.setData(Messages.HARD_RESETR);
		else
			fl.setData(Messages.HARD_RESET);
		commandPublisher.publish(fl);
	}

	@Override
	public void callSoftReset(boolean randomize){

		log.info(me+"Publishind SoftReset to all subscribed nodes.");
		//System.out.println(me+"Publishing SoftReset to all subscribed nodes.");
		std_msgs.String fl = commandPublisher.newMessage();
		if(randomize)
			fl.setData(Messages.SOFT_RESETR);
		else
			fl.setData(Messages.SOFT_RESET);
		commandPublisher.publish(fl);
	}

	private void registerRosCommunication(ConnectedNode connectedNode){
		commandPublisher =connectedNode.newPublisher(Messages.SIMULATOR_TOPIC,
				std_msgs.String._TYPE);
	}

}
