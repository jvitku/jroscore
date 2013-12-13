package ctu.nengoros.rosparam;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;

/**
 * Implementation (actual ROS node) of abstract RosParam class
 * 
 * @author Jaroslav Vitku
 *
 */
public class RosparamNode extends Rosparam{

	@Override
	public GraphName getDefaultNodeName() { return GraphName.of("RosparamNode"); }
	
	@Override
	public void onStart(ConnectedNode connectedNode){
		
		super.onStart(connectedNode);
		l = connectedNode.getLog();
		
		// ROS uses these cancellable loops
		connectedNode.executeCancellableLoop(new CancellableLoop() {

			@Override
			protected void setup() {
				l.info("RosparamNode Launched! Waiting for commands..");
			}

			@Override
			protected void loop() throws InterruptedException {
				Thread.sleep(sleeptime);
			}
		});
	}
}
