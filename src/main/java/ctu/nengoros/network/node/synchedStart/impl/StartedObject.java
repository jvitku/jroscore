package ctu.nengoros.network.node.synchedStart.impl;

import ctu.nengoros.network.node.synchedStart.StartupManager;

/**
 * Implement this interface in order to use the {@linkStartupManager} for hierarchical
 * waiting for asynchronous startup of all objects needed for correct usage of this Object. 
 * 
 * @author Jaroslav Vitku
 *
 */
public interface StartedObject {
	
	/**
	 * StartedObject should be able to indicate whether is ready to be used. 
	 * This method can return true or false at any time. The object is said to be 
	 * prepared for usage if the {@link #isStarted()} returns true for this object
	 * and for all its children. This value is returned by the method 
	 * {@link StartupManager#allStarted()} or used by the method 
	 * {@link StartupManager#awaitStarted()}.
	 * 
	 * @return true if all things are initialized OK and this object can be used
	 * @see StartupManager#awaitStarted()
	 */
	public boolean isStarted();
	
	/**
	 * Return the owned manager (for usage of e.g. 
	 * {@link ctu.nengoros.network.node.synchedStart.impl.StartupManager#addChild(ctu.nengoros.network.node.synchedStart.StartupManager)}).
	 * 
	 * @return StartupManager of this StartedObject, which specifies whether the object and its childs
	 * has been setup
	 */
	public StartupManager getStartupManager();
	
	/**
	 * Return preferably unique name of this object (used by the 
	 * {@link ctu.nengoros.network.node.synchedStart.impl.StartupManager}
	 * @return preferably unique name in the network
	 */
	public String getFullName();
}
