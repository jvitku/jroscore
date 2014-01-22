package ctu.nengoros.network.node.testsuit.topicParticipant;

import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 * List of topic participants which have to be registered to ROS master (and potentially 
 * connected somewhere). 
 * 
 * If allAdded is false, it cannot be said that all are ready.
 * 
 * @author Jaroslav Vitku
 *
 */
public class TopicParticipants {

	private ArrayList<TopicParticipantInterface> participants;

	private volatile boolean allReady = false;
	private volatile boolean allAdded = false;	

	private int maxwait = 4000, wait=155;	//ms

	public TopicParticipants(){
		participants = new ArrayList<TopicParticipantInterface>();
	}

	/**
	 * This has to be called after adding the last participant indicating
	 * that we can wait for all to be ready (asynchronous access).
	 */
	public void allParticipantsAdded(){ allAdded = true; }	

	/**
	 * Wait for all participants to be ready (just registered in the network, or connected somewhere too)
	 */
	public void awaitAllready(){

		if(allAdded && participants.size() == 0){
			allReady = true;
			return;
		}
		
		if(allReady)
			return;

		this.awaitAllAdded();

		for(int i=0; i<participants.size(); i++){
			if(!participants.get(i).isReady()){
				participants.get(i).awaitReady();
			}
		}
		allReady = true;
	}

	private void awaitAllAdded(){
		int poc = 0;
		while(!allAdded){
			if(poc++*wait > maxwait){
				System.err.println("Waited too long for all participants added! nodeIsPrepared() was called?");
				fail("Waited too long for all participants added! nodeIsPrepared() was called?");
				return;
			}
			try {
				Thread.sleep(wait);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.err.println("could not sleep");
				fail("could not sleep");
			}
		}
	}


	/**
	 * Add new participant to the list. 
	 * 
	 * @param participant By choosing a class you can choose what to wait for on particular participant.  
	 */
	public void registerParticipant(TopicParticipantInterface participant){
		this.allReady = false;
		participants.add(participant);
	}

	public boolean allReady(){ return this.allReady; }
}
