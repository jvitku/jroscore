package ctu.nengoros.nodes.topicParticipant;

import static org.junit.Assert.fail;

import org.ros.node.topic.CountDownPublisherListener;
import org.ros.node.topic.Publisher;


/**
 * 
 * This TopicParticipant is ready if is both registered and connected somewhere.
 * 
 * @author Jaroslav Vitku
 *
 * @param <T>
 */
public class ConnectedParticipantPublisher<T> extends ConnectedTopicParticipant {

	private Publisher<T> pub;
	private CountDownPublisherListener<T> listener;
	
	public ConnectedParticipantPublisher(Publisher<T> publisher){
		pub = publisher;
		
		listener = CountDownPublisherListener.newDefault();
		pub.addListener(listener);
	}
	
	@Override
	public void awaitConnection() {
		try {
			listener.awaitNewSubscriber(waittime, units);
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

