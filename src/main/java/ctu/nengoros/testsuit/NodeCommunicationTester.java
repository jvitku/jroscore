package ctu.nengoros.testsuit;

import org.ros.node.AbstractNodeMain;

import ctu.nengoros.testsuit.topicParticipant.TopicParticipants;

/**
 * This is a template for ROS node testers. Subclass this in order to create node for testing
 * the ROS network communication.
 * The class can wait for all topic participants to be registered / connected @see TopicParticipants. 
 *   
 * @author Jaroslav Vitku
 *
 */
public abstract class NodeCommunicationTester extends AbstractNodeMain{

	/**
	 * Add here all publishers/subscribers you want to check.
	 * Add subclasses of TopicParticipant if you want to check whether the 
	 * participant is registered with ROS master.
	 * Add subclasses of ConnectedTopicParticipant if you want to check 
	 * whether the participant is connected also somewhere in the ROS network.
	 */
	protected TopicParticipants participants = new TopicParticipants();

	public void waitForCommunicationReady(){ participants.awaitAllready(); }

	public boolean communicationReady(){ return participants.allReady(); }

}
