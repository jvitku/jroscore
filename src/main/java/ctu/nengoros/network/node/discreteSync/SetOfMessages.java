package ctu.nengoros.network.node.discreteSync;

/**
 * Each input to the node has own ID. Set of messages defines set of message IDs 
 * that are necessary to make one discrete step. That is, to execute the node's 
 * computation and publish data (ROS messages) to outputs. 
 * 
 * @author Jaroslav Vitku
 *
 */
public interface SetOfMessages extends ReceivedEvaluator{
	
	/**
	 * Register new message identified by it's topic and type  
	 * @param topic ROS message topic
	 * @param type	ROS message type
	 */
	public void addMessage(String topic, String type);
	
	
	//public void removeMessage(String topic, String type) throws something;
	
	/**
	 * Register that this message has arrived
	 * 
	 * @param topic 
	 * @param type
	 * @throws NengoROSSync throw in case of attempt to register arrival of non-existing message
	 */
	public void registerMessageArival(String topic, String type);// throws NengoROSSync;
	

	/**
	 * Return true if the ROS node should make one simulation step. 
	 * This means that all messages in this set arrived from the last reset.
	 *  
	 * @return true if the owner node should perform computation
	 */
	public boolean shouldMakeStep();
	
	/**
	 * Call this method just before performing the computation.
	 * TODO: potential synchronization problems, but this should not be
	 * a problem in the synchronous simulation mode.
	 */
	public void resetStep();
	
}
