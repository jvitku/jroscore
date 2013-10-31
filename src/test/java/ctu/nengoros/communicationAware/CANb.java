package ctu.nengoros.communicationAware;


import static org.junit.Assert.assertTrue;

import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import ctu.nengoros.nodes.CommunicationAwareNode;
import ctu.nengoros.nodes.topicParticipant.ParticipantPublisher;

/**
 * Publisher, which does not have to be connected somewhere.
 * 
 * @author Jaroslav Vitku
 *
 */
public class CANb extends CommunicationAwareNode{

	private final String topicOut = "topic"; 

	@Override
	public void onStart(ConnectedNode node){
		System.out.println("onStart called setting up the node config.");


		// define the publisher
		final Publisher<std_msgs.Float32MultiArray> publisher = 
				node.newPublisher(topicOut, std_msgs.Float32MultiArray._TYPE);

		// do not require connecting
		super.participants.registerParticipant(
					new ParticipantPublisher<std_msgs.Float32MultiArray>(publisher));
		
		
		// do some tasks...
		try {
			Thread.sleep(150);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		super.nodeIsPrepared();

		assertTrue(super.isNodePrepared());
	}

	@Override
	public GraphName getDefaultNodeName() { return GraphName.of("CANb"); }
}