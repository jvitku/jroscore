package ctu.nengoros.testsuit.topicParticipant;

import java.util.concurrent.TimeUnit;

/**
 * Can wait for to be registered with the ROS master. Can hold either Publisher or Subscriber.
 *  
 * @author Jaroslav Vitku
 *
 */
public abstract class TopicParticipant implements TopicParticipantInterface{
	
	public final int waittime = 15000;	// 10 seconds
	public final TimeUnit units = TimeUnit.MILLISECONDS;
	
	protected boolean isRegistered = false;

	public boolean isRegistered(){ return this.isRegistered; }

	public boolean isReady(){ return this.isRegistered(); }
	
	// wait for participant to be ready
	public void awaitReady(){ this.awaitRegistration(); }
	
	public abstract void awaitRegistration();
	
}
