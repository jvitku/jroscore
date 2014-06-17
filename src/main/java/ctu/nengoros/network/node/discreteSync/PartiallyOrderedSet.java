package ctu.nengoros.network.node.discreteSync;

/**
 * {@link SetOfMessages} which allows to define ordering constraints on messages.
 * 
 * @author Jaroslav Vitku
 *
 */
public interface PartiallyOrderedSet extends SetOfMessages{

	/**
	 * Adds one ordering constraint.
	 * 
	 * @param firstType type of the first message
	 * @param firstTopic
	 * @param secondType type of the ancestor message
	 * @param secondTopic topic of the ancestor message
	 * @throws exception either in case that one of these messages is not found
	 * or in case that there is conflicting exception already 
	 */
	public void addConstraint(String firstType, String firstTopic, 
			String secondType, String secondTopic) throws NengoROSSync;
	
	/**
	 * Check whether all constraints for this message have been fulfilled 
	 * (and therefore the message can be registered as an arrived one).
	 * @param type type of the ROS message
	 * @param topic topic of the ROS message
	 * @return true if all constraints for this message have been fulfileld
	 */
	boolean constraitsFulfilled(String type, String topic); 

}
