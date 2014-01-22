package ctu.nengoros.communicationAware;


import static org.junit.Assert.assertTrue;

import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;

import ctu.nengoros.network.node.testsuit.CommunicationAwareNode;

/**
 * No communication needs to be established
 * 
 * @author Jaroslav Vitku
 *
 */
public class CANa extends CommunicationAwareNode{

	@Override
	public void onStart(ConnectedNode node){
		System.out.println("onStart called setting up the node config.");

		super.nodeIsPrepared();

		assertTrue(super.isNodePrepared());
	}

	@Override
	public GraphName getDefaultNodeName() { return GraphName.of("CANa"); }
}