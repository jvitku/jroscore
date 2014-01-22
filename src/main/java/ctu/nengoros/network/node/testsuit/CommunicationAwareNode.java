package ctu.nengoros.network.node.testsuit;

import org.ros.node.AbstractNodeMain;

import ctu.nengoros.network.node.testsuit.topicParticipant.TopicParticipants;

/**
 * This is a template for ROS node testers. Subclass this in order to create node which is aware 
 * of TopicParticipants (publishers/subscribers). It can wait for all topic participants to be 
 * registered / connected @see TopicParticipants. 
 *   
 * @author Jaroslav Vitku
 *
 */
public abstract class CommunicationAwareNode extends AbstractNodeMain{

	private volatile boolean everythingPrepared = false;
	
	/**
	 * Add here all publishers/subscribers you want to check.
	 * Add subclasses of TopicParticipant if you want to check whether the 
	 * participant is registered with ROS master.
	 * Add subclasses of ConnectedTopicParticipant if you want to check 
	 * whether the participant is connected also somewhere in the ROS network.
	 */
	protected TopicParticipants participants = new TopicParticipants();

	/**
	 * Wait for the node to be ready (has to call nodeIsPrepared and all 
	 * TopicParticipants have to be registered with the master).
	 */
	public void awaitCommunicationReady(){ participants.awaitAllready(); }

	public boolean communicationReady(){ return participants.allReady(); }

	/**
	 * This is to be called after successful setup in onStart() method.
	 * It indicates that the node is ready (e.g. all TopicParticipants are added).
	 */
	public void nodeIsPrepared(){
		this.everythingPrepared = true;
		participants.allParticipantsAdded();
	}
	
	public boolean isNodePrepared(){ return this.everythingPrepared; }

}
