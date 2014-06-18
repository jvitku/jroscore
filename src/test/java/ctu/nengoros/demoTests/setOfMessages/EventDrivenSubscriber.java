package ctu.nengoros.demoTests.setOfMessages;

import org.apache.commons.logging.Log;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;

import ctu.nengoros.network.node.discreteSync.PartiallyOrderedSet;
import ctu.nengoros.network.node.discreteSync.impl.PartiallyOrderedSetOfMessages;
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
	public final java.lang.String topicOut = "topicOUT"; 

	private String topicA, topicB;
	private float dataA, dataB;		// last received data for each input

	private Publisher<std_msgs.Float32> publisher;

	private Log log;
	private PartiallyOrderedSet pos = new PartiallyOrderedSetOfMessages();
	private int poc = 0;

	@Override
	public GraphName getDefaultNodeName() { return GraphName.of("EventDrivenSubscriber"); }

	@Override
	public void onStart(final ConnectedNode connectedNode) {

		System.out.println("Node started, initializing!");
		log = connectedNode.getLog();

		// read remapping arguments
		PrivateRosparam r = new PrivateRosparam(connectedNode);
		topicA = r.getMyString(topicAConf, 	DEF_TOPICA);
		topicB = r.getMyString(topicBConf,	DEF_TOPICB);

		Subscriber<std_msgs.Float32> subscriberA = 
				connectedNode.newSubscriber(topicA, std_msgs.Float32._TYPE);
		Subscriber<std_msgs.Float32> subscriberB = 
				connectedNode.newSubscriber(topicB, std_msgs.Float32._TYPE);

		// register messages to the set of messages
		pos.addMessage(topicA, std_msgs.Float32._TYPE);
		pos.addMessage(topicB, std_msgs.Float32._TYPE);

		subscriberA.addMessageListener(new MessageListener<std_msgs.Float32>() {
			@Override
			public void onNewMessage(std_msgs.Float32 message) {
				dataA = message.getData();
				log.info("Received these data on INPUT A: "+dataA);
				pos.registerMessageArival(topicB, std_msgs.Float32._TYPE);
				tryToCompute();
			}
		});

		subscriberB.addMessageListener(new MessageListener<std_msgs.Float32>() {
			@Override
			public void onNewMessage(std_msgs.Float32 message) {
				dataB = message.getData();
				log.info("Received these data on INPUT B: "+dataB);
				pos.registerMessageArival(topicA, std_msgs.Float32._TYPE);
				tryToCompute();
			}
		});

		// define the publisher
		publisher = connectedNode.newPublisher(topicOut, std_msgs.Float32._TYPE);

		log.info("HEY! Node ready now!");
	}


	private void tryToCompute(){
		if(!pos.shouldMakeStep())
			return;
		
		float inA = this.dataA;					// store current data securely
		float inB = this.dataB;
		
		pos.resetStep();						// allow registering new data
		float output = this.compute(inA, inB);
		this.sendMessage(output, inA, inB);
	}

	private void sendMessage(float output, float inA, float inB){

		std_msgs.Float32 mess = publisher.newMessage();	// init message
		mess.setData(poc);								// set message data
		publisher.publish(mess);						// send message

		log.info("Sending message no.: "+poc+", on the topic: "+topicOut+
				" with data: "+inA+"+"+inB+"="+output);
		poc++;
	}

	/**
	 * Implement the node's computation here
	 * @return result to be sent
	 */
	private float compute(float inA, float inB){
		return inA+inB;
	}


}
