package ctu.nengoros.network.node;

import org.ros.node.ConnectedNode;

/**
 * Hanns node with support of prosperity publishing.
 * 
 * The prosperity should be one value representing some objective how well
 * the HannsNode prospers in a given architecture. 
 * 
 * @author Jaroslav Vitku
 *
 */
public interface ProsperityHannsNode extends HannsNode{

	/**
	 * Return the average value of prosperity of the algorithm
	 * in the current architecture. This value defines how successful 
	 * the algorithm is (according to some chosen measure). 
	 * @return value defining how successful the algorithm is
	 * according to some selected measure
	 */
	public float getProsperity();
	
	/**
	 * This method should use the {@link #prospPublisher} initialized by the
	 * {@link #buildProsperityPublisher(ConnectedNode)} to publish 
	 * its value(s) of prosperity over the ROS network.
	 */
	public void publishProsperity();

}
