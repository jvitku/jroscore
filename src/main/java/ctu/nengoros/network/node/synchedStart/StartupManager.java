package ctu.nengoros.network.node.synchedStart;

import ctu.nengoros.network.common.exceptions.StartupDelayException;

/**
 * It takes some time to ROS node to start, the ROS node should be able to indicate
 * that it has been properly initializes (e.g. ROS communication ready, no NPEs will occur etc).
 * Furthermore, a NeuralModule is typically composed of own ROS node(s) and own 
 * Modem (ROS node). These have to be ready in order to be the NeuralModule to be ready.  
 * 
 * Basic idea:
 * <ul>
 * 	<li>each Object can have two states: ready / notReady</li>
 *  <li>each Object can have arbitrary number of children</li>
 *  <li>each children is also an Object</li>
 *  <li>To object to be ready, the {@link #setReady(boolean)} must be called and all children must be
 *  in the ready state.
 *  </ul>
 *  
 * Simulator can easily determine whether all components of simulation are ready.
 * 
 * @author Jaroslav Vitku
 */
public interface StartupManager extends SynchUtil{

	/**
	 * The object is able to log waiting, which is disabled by default.
	 * @param enabled enable logging to console?
	 */
	public void setLogginEnabled(boolean enabled);

	/**
	 * There can be problem with synchronizing various ROS nodes with a rest 
	 * of the infrastructure. This method should be called before any attempt 
	 * to use the HannsNode. It will return after the node is initialized, or throws
	 * an exception if max time exceeded.
	 * 
	 * @throws StartupDelayException is thrown if the predefined maximum
	 * waiting time is exceeded, which probably means that the node could not be 
	 * started 
	 */
	public void awaitStarted() throws StartupDelayException;

	/**
	 * Node with all its children is started and ready?
	 * 
	 * @return true if the Node (including all its children) is started (and therefore ready for usage)
	 */
	public boolean allStarted();
	
	/**
	 * Get maximum wait time for this object to be ready
	 * @return max time in milliseconds which the method {@link #awaitStarted()} will wait
	 */
	public int getMaxWaitTime();

	/**
	 * Set the maximum wait time for the method {@link #awaitStarted()} to wait for node
	 * to be ready
	 * @param ms milliseconds to wait
	 */
	public void setMaxWaitTime(int ms);

	/**
	 * enables to create tree of units
	 * unit is ready if all childs are ready
	 * or if flag "alwaysReady" is on 
	 * @param child
	 */
	public void addChild(StartupManager child);

	public void removeChild(StartupManager child);
}
