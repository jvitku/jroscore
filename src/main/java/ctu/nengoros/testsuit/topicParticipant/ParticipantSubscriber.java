package ctu.nengoros.testsuit.topicParticipant;

import static org.junit.Assert.fail;

import org.ros.node.topic.CountDownSubscriberListener;
import org.ros.node.topic.Subscriber;

/**
 * This subscriber has to be registered with ROS master in order to be ready.
 * 
 * @author Jaroslav Vitku
 *
 * @param <T>
 */
public class ParticipantSubscriber<T> extends TopicParticipant {

	private Subscriber<T> sub;
	private CountDownSubscriberListener<T> listener;
	
	public ParticipantSubscriber(Subscriber<T> subscriber){
		sub = subscriber;
		
		listener = CountDownSubscriberListener.newDefault();
		sub.addSubscriberListener(listener);
	}
	
	@Override
	public void awaitRegistration() {
		try {
			listener.awaitMasterRegistrationSuccess(waittime, units);
			this.isRegistered = true;
		} catch (InterruptedException e) {
			fail("Could not register publisher to the master!");
			e.printStackTrace();
		}
	}
}
