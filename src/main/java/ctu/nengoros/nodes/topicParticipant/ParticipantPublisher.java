package ctu.nengoros.nodes.topicParticipant;

import static org.junit.Assert.fail;

import org.ros.node.topic.CountDownPublisherListener;
import org.ros.node.topic.Publisher;

/**
 * This publisher has to be registered with master in order to be ready.
 * 
 * @author Jaroslav Vitku
 *
 * @param <T>
 */
public class ParticipantPublisher <T> extends RegisteredTopicParticipant {

		private Publisher<T> pub;
		private CountDownPublisherListener<T> listener;
		
		public ParticipantPublisher(Publisher<T> publisher){
			pub = publisher;
			
			listener = CountDownPublisherListener.newDefault();
			pub.addListener(listener);
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

