package ctu.nengoros.testsuit.topicParticipant;

/**
 * Components of ROS Java nodes which are meant to communicate across the ROS network.
 * 
 * These have to register with the ROS master (and potentially "connect" somewhere).
 * 
 * @author Jaroslav Vitku
 */
public interface TopicParticipantInterface {
	
	public boolean isReady();
	
	public void awaitReady();

}
