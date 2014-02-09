package ctu.nengoros.network.node.infrastructure.simulation;

/**
 * Able to control the state of the simulation over the ROS network.
 * 
 * @author Jaroslav Vitku
 *
 */
public interface SimulationControls {

	/**
	 * Publish ROS message which tells to all (subscribed) nodes to call 
	 * {@link ctu.nengoros.network.common.Resettable#softReset(boolean)}.  
	 * @param randomize the randomization is set to default value for now
	 */
	public void callHardReset(boolean randomize);

	/**
	 * Publish ROS message which tells to all (subscribed) nodes to call 
	 * {@link ctu.nengoros.network.common.Resettable#hardReset(boolean)}.  
	 * @param randomize the randomization is set to default value for now
	 */
	public void callSoftReset(boolean randomize);
}
