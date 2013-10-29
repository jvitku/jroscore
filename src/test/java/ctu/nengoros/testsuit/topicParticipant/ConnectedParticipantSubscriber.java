package ctu.nengoros.testsuit.topicParticipant;

import static org.junit.Assert.fail;

import org.ros.node.topic.CountDownSubscriberListener;
import org.ros.node.topic.Subscriber;


/**
 * 
 * This TopicParticipant is ready if is both registered and connected somewhere.
 * 
 * @author Jaroslav Vitku
 *
 * @param <T>
 */
public class ConnectedParticipantSubscriber<T> extends ConnectedTopicParticipant {

	private Subscriber<T> sub;
	private CountDownSubscriberListener<T> listener;
	
	public ConnectedParticipantSubscriber(Subscriber<T> subscriber){
		sub = subscriber;
		
		listener = CountDownSubscriberListener.newDefault();
		sub.addSubscriberListener(listener);
	}
	
	@Override
	public void awaitConnection() {
		try {
			listener.awaitNewPublisher(waittime, units);
			this.isConnectedSomewhere = true;
		} catch (InterruptedException e) {
			fail("Could not find any subsscriber for this publisher");
			e.printStackTrace();
		}	
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

