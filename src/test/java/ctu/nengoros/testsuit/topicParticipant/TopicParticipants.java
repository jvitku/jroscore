package ctu.nengoros.testsuit.topicParticipant;

import java.util.ArrayList;

/**
 * List of topic participants which have to be registered to ROS master (and potentially 
 * connected somewhere).
 * 
 * @author Jaroslav Vitku
 *
 */
public class TopicParticipants {

	private ArrayList<TopicParticipantInterface> participants;
	
	private boolean allReady = false;
	
	public TopicParticipants(){
		participants = new ArrayList<TopicParticipantInterface>();
	}
	
	/**
	 * Wait for all participants to be ready (just registered in the network, or connected somewhere too)
	 */
	public synchronized void awaitAllready(){
		
		for(int i=0; i<participants.size(); i++){
			if(!participants.get(i).isReady())
				participants.get(i).awaitReady();
		}
		allReady = true;
	}
	
	/**
	 * Add new participant to the list. 
	 * 
	 * @param participant By choosing a class you can choose what to wait for on particular participant.  
	 */
	public synchronized void registerParticipant(TopicParticipantInterface participant){
		this.allReady = false;
		participants.add(participant);
	}
	
	public boolean allReady(){ return this.allReady; }
}
