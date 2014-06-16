package ctu.nengoros.network.node.discreteSync.impl;

import java.util.HashMap;

import ctu.nengoros.network.node.discreteSync.NengoROSSync;
import ctu.nengoros.network.node.discreteSync.SetOfMessages;

public class BasicSetOfMessages implements SetOfMessages{

	/**
	* Number of messages arrived from the last call of the @resetStep();
	*/
	private final HashMap<String,Integer> noArrived;
	
	public BasicSetOfMessages(){
		noArrived = new HashMap<String, Integer>();
	}
	
	@Override
	public void addMessage(String topic, String type) {
		noArrived.put(topic+type, 0);
	}

	@Override
	public void registerMessageArival(String topic, String type) throws NengoROSSync{
		if(!noArrived.containsKey(topic+type)){
			throw new NengoROSSync("BasicSetOfMessages: could not find the key: "+(topic+type));
		}
		// increment no of arrived messages
		noArrived.put(topic+type, noArrived.get(topic+type)+1);
	}

	@Override
	public boolean shouldMakeStep() {
		return this.allArrived();
	}

	@Override
	public void resetStep() {
		// TODO Auto-generated method stub
		
	}
	
	private boolean allArrived(){
		
		// TODO iterate over all map keys and return true if all arrived at least once
		return false;
	}

}
