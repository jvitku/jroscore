package ctu.nengoros.demoTests.setOfMessages;

import org.apache.commons.logging.Log;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import ctu.nengoros.network.node.infrastructure.rosparam.impl.PrivateRosparam;

/**
 * Demo node: subscribe to a given topic and write received array-of-integer messages into the log ROS-console.
 * 
 * @author Jaroslav Vitku , based on original rosjava_core tutorials
 *
 */
public class EventDrivenSubscriber extends AbstractNodeMain{
	
	// topic used for input messages 
	protected final java.lang.String DEF_TOPICA = "topicA";
	protected final java.lang.String DEF_TOPICB = "topicB";
	
	public final java.lang.String topicAConf = "topicA";
	public final java.lang.String topicBConf = "topicB";
	
	private String topicA, topicB;
	
	@Override
	public GraphName getDefaultNodeName() { return GraphName.of("EventDrivenSubscriber"); }

	@Override
	public void onStart(final ConnectedNode connectedNode) {
		
		System.out.println("Node started, initializing!");
		final Log log = connectedNode.getLog();
		
		// read remapping arguments
		PrivateRosparam r = new PrivateRosparam(connectedNode);
		topicA = r.getMyString(topicAConf, 	DEF_TOPICA);
		topicB = r.getMyString(topicBConf,	DEF_TOPICB);

		Subscriber<std_msgs.Float32> subscriberA = 
				connectedNode.newSubscriber(topicA, std_msgs.Float32._TYPE);
		Subscriber<std_msgs.Float32> subscriberB = 
				connectedNode.newSubscriber(topicB, std_msgs.Float32._TYPE);
		
		subscriberA.addMessageListener(new MessageListener<std_msgs.Float32>() {
			@Override
			public void onNewMessage(std_msgs.Float32 message) {
				float data = message.getData();
				log.info("Received these data on INPUT A: "+data);
			}
		});
		
		subscriberB.addMessageListener(new MessageListener<std_msgs.Float32>() {
			@Override
			public void onNewMessage(std_msgs.Float32 message) {
				float data = message.getData();
				log.info("Received these data on INPUT B: "+data);
			}
		});

		log.info("HEY! Node ready now!");
	}

}
