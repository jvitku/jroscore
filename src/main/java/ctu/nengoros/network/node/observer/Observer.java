package ctu.nengoros.network.node.observer;

import ctu.nengoros.network.common.Resettable;


/**
 * The observer observes agents (nodes) behavior and creates some statistics
 * about it (e.g. to provide the prosperity value). 
 * 
 * Observer can be also any visualization algorithm.
 * 
 * Add method void observe(params) and call it each simulation step in order to observe.
 *  
 * @author Jaroslav Vitku
 *
 */
public interface Observer extends Resettable {
	
	/**
	 * Turn on/off the visualization (logging)
	 * @param visualize true if the node should visualize
	 */
	public void setShouldVis(boolean visualize);
	
	/**
	 * @return true if the visualization (console logging) is turned on
	 */
	public boolean getShouldVis();
	
	/**
	 * Set how often (in the algorithm steps) the visualization should occur
	 * @param period How often to update visualization, 1 means every simulation step, 
	 * -1 means no visualization, 10 means visualization each 10 steps
	 */
	public void setVisPeriod(int period);
	
	/**
	 * @see #setVisPeriod(int)
	 * @return how often the visualization occurs
	 */
	public int getVisPeriod();
	
	/**
	 * Return the name of the Observer, the name should somehow
	 * reflect the observers purpose.
	 * @return name of the observer
	 */
	public String getName();
}

