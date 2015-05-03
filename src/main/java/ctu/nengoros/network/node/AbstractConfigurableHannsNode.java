package ctu.nengoros.network.node;

import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import ctu.nengoros.network.node.infrastructure.simulation.Messages;

/**
 * The method {@link #registerSimulatorCommunication(ConnectedNode)} should be called
 * from the {@link AbstractHannsNode#onStart(ConnectedNode)}. 
 * 
 * This method registers the communication (e.g. with the simulator) which serves
 * for simulation purposes, such as reset. The listener added to the 
 * {@link Messages#SIMULATOR_TOPIC} listens for commands which are able to reset the node data. 
 *  
 * @author Jaroslav Vitku
 *
 */
public abstract class AbstractConfigurableHannsNode extends AbstractHannsNode implements ConfigurableHannsNode{

	@Override
	public void registerSimulatorCommunication(ConnectedNode connectedNode) {
		/**
		 * Simulator commands
		 */
		Subscriber<std_msgs.String> simulatorSub = 
				connectedNode.newSubscriber(Messages.SIMULATOR_TOPIC, std_msgs.String._TYPE);

		simulatorSub.addMessageListener(new MessageListener<std_msgs.String>() {
			@Override
			public void onNewMessage(std_msgs.String message) {
				String data = message.getData();

				if(!Messages.commandIsRecognized(data)){
					System.err.println(me+"Simulator command not recognized, ignoring! "
							+ "Command was: "+data);
					return;
				}

				if(data.equalsIgnoreCase(Messages.HARD_RESET)){
					//System.err.println(me+"Received simulator command hardReset(false), resetting node now.");
					hardReset(false);
				}else if(data.equalsIgnoreCase(Messages.HARD_RESETR)){
					//System.err.println(me+"Received simulator command hardReset(true), resetting node now.");
					hardReset(true);
				}else if(data.equalsIgnoreCase(Messages.SOFT_RESET)){
					//System.err.println(me+"Received simulator command softReset(false), resetting node now.");
					softReset(false);
				}else if(data.equalsIgnoreCase(Messages.SOFT_RESETR)){
					//System.err.println(me+"Received simulator command softReset(true), resetting node now.");
					softReset(true);
				}
			}
		});
	}
}
