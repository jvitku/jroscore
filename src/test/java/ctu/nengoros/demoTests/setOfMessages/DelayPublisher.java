package ctu.nengoros.demoTests.setOfMessages;

import org.apache.commons.logging.Log;
import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import ctu.nengoros.network.node.infrastructure.rosparam.impl.PrivateRosparam;

/**
 * Publishes number of published messages with given period in [ms].
 * 
 * The period is specified by the private parameter passed to the node.
 * 
 * @author Jaroslav Vitku, based on original rosjava_core tutorials
 */
public class DelayPublisher extends AbstractNodeMain {

	public final String periodConf = "period";
	final int DEF_PERIOD = 10;
	private int waittime;

	public final String DEF_TOPIC = "topicA"; 
	final String topicConf = "topic";
	private String topic;

	@Override
	public GraphName getDefaultNodeName() { return GraphName.of("DelayPublisher"); }

	/**
	 * Method called after launching the node. 
	 * After exiting this method, the node will stop working.
	 */
	@Override
	public void onStart(final ConnectedNode connectedNode) {

		System.out.println("Node started, initializing, parsing parameters!");
		PrivateRosparam r = new PrivateRosparam(connectedNode);

		waittime = r.getMyInteger(periodConf, DEF_PERIOD);
		topic = r.getMyString(topicConf,DEF_TOPIC);
		//System.out.println

		System.out.println("Will publish with this period: "+waittime);

		final Log log = connectedNode.getLog();

		// define the publisher
		final Publisher<std_msgs.Float32> publisher = 
				connectedNode.newPublisher(topic, std_msgs.Float32._TYPE);

		log.info("HEY! Node ready now! Starting sending mesages..");

		// ROS uses these cancellable loops
		connectedNode.executeCancellableLoop(new CancellableLoop() {
			private float poc;

			@Override
			protected void setup() {
				poc = 0;
			}

			@Override
			protected void loop() throws InterruptedException {
				std_msgs.Float32 mess = publisher.newMessage();	// init message
				mess.setData(poc);								// set message data
				publisher.publish(mess);						// send message
				
				log.info("Sending message no.: "+poc+", on the topic: "+topic);
				poc++;
				Thread.sleep(waittime);
			}
		});
	}
}
