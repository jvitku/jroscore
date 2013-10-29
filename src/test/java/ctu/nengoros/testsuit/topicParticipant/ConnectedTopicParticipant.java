package ctu.nengoros.testsuit.topicParticipant;

/**
 * This Participant has to be connected somewhere (e.g. in case of publisher, 
 * there has to be at least one subscriber in the ROS network).
 *     
 * @author Jaroslav Vitku
 *
 */
public abstract class ConnectedTopicParticipant extends TopicParticipant {
	
	protected boolean isConnectedSomewhere = false;
	
	public boolean isConnected(){ return this.isConnectedSomewhere; }
	
	public abstract void awaitConnection();

	@Override
	public void awaitReady(){
		this.awaitRegistration();
		this.awaitConnection();
	}
	
	@Override
	public boolean isReady(){ return this.isConnected() && this.isRegistered(); }
	
}
