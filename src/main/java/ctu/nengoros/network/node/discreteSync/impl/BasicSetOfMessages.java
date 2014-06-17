package ctu.nengoros.network.node.discreteSync.impl;

import java.util.HashMap;
import java.util.Map;

import ctu.nengoros.network.node.discreteSync.SetOfMessages;

/**
 * <p>Used for synchronization of discrete operations in the (Nengo)ROS network.
 * Each of messages in this list has to be received AT LEAST ONCE after call of the {@link #resetStep()}
 * method.</p>
 * 
 * <p>Example usage e.g.: Node has three inputs, after receiving at least one data sample on each 
 * of the inputs the Node resets this SetOfMessages, performs it's computation and publishes new data.</p>
 * 
 * @author Jaroslav Vitku
 *
 */
public class BasicSetOfMessages implements SetOfMessages{

	/**
	 * Number of messages arrived from the last call of the @resetStep();
	 */
	final HashMap<String, Integer> noArrived;

	public BasicSetOfMessages(){
		noArrived = new HashMap<String, Integer>();
	}

	@Override
	public void addMessage(String topic, String type) {
		if(noArrived.containsKey(topic+type))
			System.err.println("BasicSetOfMessages: Warning: this message type already registered: "
					+topic+" "+type);

		noArrived.put(topic+type, 0);
	}

	@Override
	public void registerMessageArival(String topic, String type){
		if(!noArrived.containsKey(topic+type)){
			//throw new NengoROSSync("BasicSetOfMessages: could not find the key: "+(topic+type));
			System.err.println("BasicSetOfMessages: could not find the key: "+(topic+type));
			return;
		}
		noArrived.put(topic+type, noArrived.get(topic+type)+1);
	}

	/**
	 * The step should be made if all messages have been received. 
	 */
	@Override
	public synchronized boolean shouldMakeStep() {
		for (Map.Entry<String, Integer> entry : noArrived.entrySet()) {
			if(!this.messageReceived(entry.getKey()))
				return false;
		}
		return true;
		/*
			if(entry.getValue()==0)
				return false;
			else if(entry.getValue()<0){
				System.err.println("BasicSetOfMessages: error, this should not happen!");
				return false;
			}
		}
		return true;
		 */
	}

	@Override
	public void resetStep() {
		// based on http://stackoverflow.com/questions/1066589/java-iterate-through-hashmap
		for (Map.Entry<String, Integer> entry : noArrived.entrySet()) {
			//String key = entry.getKey();
			//Integer value = entry.getValue();
			entry.setValue(0);
		}
	}

	/**
	 * Here, the message is said to be received if at least one message has arrived. 
	 */
	@Override
	public boolean messageReceived(String key) {
		if(noArrived.get(key)==0)
			return false;
		else if(noArrived.get(key) < 0){
			System.err.println("BasicSetOfMessages: error, this should not happen!");
			return false;
		}	
		return true;
	}

}