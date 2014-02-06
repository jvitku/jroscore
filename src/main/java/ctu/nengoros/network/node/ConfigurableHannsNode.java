package ctu.nengoros.network.node;

import org.ros.node.ConnectedNode;

import ctu.nengoros.network.common.Resettable;

/**
 * This HannsNode is able to communicate with the rest of the network 
 * (e.g. Nengoros simulator) by means of predefined messages. 
 * 
 * Especially, the node can be reseted by two main ways.
 * 
 * @see ctu.nengoros.network.common.Resettable
 * 
 * @author Jaroslav Vitku
 *
 */
public interface ConfigurableHannsNode extends HannsNode, Resettable{
	
	/**
	 * Registers subscriber which listens simulator commands. This method
	 * should be called from the {@link AbstractHannsNode#onStart(ConnectedNode)}
	 * method.
	 */
	void registerSimulatorCommunication(ConnectedNode connectedNode);

}
