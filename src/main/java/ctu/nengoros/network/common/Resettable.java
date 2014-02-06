package ctu.nengoros.network.common;

/**
 * Reset can be called from inside the ROS node, or e.g. potentially  externally from 
 * within the ROS network. 
 * 
 * @author Jaroslav Vitku
 *
 */
public interface Resettable {

	/**
	 * <p>Should re-initialize main components of the simulation, 
	 * but preserve learned data (models)</p>
	 * 
	 *  <p>Typically, the soft reset could be called between particular episodes 
	 * in episodical learning.</p>
	 * 
	 * @param randomize whether to randomize the initial state
	 */
	public void softReset(boolean randomize);

	/**
	 * <p>Hard reset should restart all running components and
	 * delete all data collected so far (e.g. erase models learned).</p>
	 * 
	 * <p>Typically, the hard reset could be called between different
	 * simulations, where all knowledge learned so far should be discarded.</p>  
	 * 
	 * @param randomize randomize new values?
	 */
	public void hardReset(boolean randomize);

}
