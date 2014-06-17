package ctu.nengoros.network.node.discreteSync.impl;

import java.util.HashMap;
import java.util.LinkedList;

import ctu.nengoros.network.node.discreteSync.NengoROSSync;
import ctu.nengoros.network.node.discreteSync.PartiallyOrderedSet;

/**
 * This SetOfMessages accepts (partial) ordering constraints on the order of messages received
 * during the simulation. Only the correct sequence of data messages is accepted.
 * 
 * @author Jaroslav Vitku
 *
 */
public class PartiallyOrderedSetOfMessages extends BasicSetOfMessages implements PartiallyOrderedSet{

	/**
	 * This maps ancestor to list of all its predecessors. 
	 * If one wants to evaluate a message as a received one, all its predecessors 
	 * have to be received already. 
	 */
	private final HashMap<String, LinkedList<String>> constraints; 

	public PartiallyOrderedSetOfMessages(){
		super();

		constraints = new HashMap<String, LinkedList<String>>(); 
	}

	@Override
	public void registerMessageArival(String topic, String type) {

		if(!this.constraitsFulfilled(topic, type))
			return;

		super.registerMessageArival(topic, type);
	}

	@Override
	public void addConstraint(String firstType, String firstTopic,
			String secondType, String secondTopic) throws NengoROSSync {

		String predecessor = firstType+firstTopic;
		String ancestor = secondType+secondTopic;

		this.checkForCircularConstraints(predecessor, ancestor);

		// check for messages
		if(!noArrived.containsKey(predecessor)){
			throw new NengoROSSync("First Message ("+predecessor+") not added!");			
		}else if(!noArrived.containsKey(ancestor)){
			throw new NengoROSSync("Second Message ("+ancestor+") not added!");
		}

		if(constraints.containsKey(ancestor)){
			LinkedList<String> predecessors = constraints.get(ancestor);
			if(predecessorRegistered(predecessors, predecessor)){
				System.err.println("PartiallyOrderedSetOfMessages: Warning: constraint between: ["
						+ predecessor+"->"+ ancestor+"] already defined, ignoring.");
				return;
			}else{
				predecessors.add(predecessor);
			}
		}else{
			// create new constraint
			constraints.put(ancestor, new LinkedList<String>());
			constraints.get(ancestor).add(predecessor);

			//System.out.println("Constraint added: "+predecessor+"->"+ancestor);
		}
	}

	private boolean predecessorRegistered(LinkedList<String> preds, String pred){
		for(int i=0; i<preds.size(); i++){
			if(preds.get(i).equalsIgnoreCase(pred))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * This check whether this newly created constraint will cause some circular
	 * dependency. The dependencies are checked to the depth of 1.
	 * 
	 *  TODO: check circular dependencies into an arbitrary depth.
	 * 
	 * @param predecessor predecessor to be added
	 * @param ancestor ancestor to be added
	 * @throws NengoROSSync thrown if circular dependency detected 
	 */
	private synchronized void checkForCircularConstraints(String predecessor, String ancestor) 
			throws NengoROSSync{
		if(predecessor.equalsIgnoreCase(ancestor))
			throw new NengoROSSync("Circular constraint detected: "
					+ "ancestor==predecessor=="+predecessor);

		if(!constraints.containsKey(predecessor))
			return;

		// check whether the list of predecessors of this predecessor contains given ancestor
		LinkedList<String> prepre = constraints.get(predecessor);

		for(int i=0; i<prepre.size(); i++)
			if(prepre.get(i).equalsIgnoreCase(ancestor))
				throw new NengoROSSync("Circular constraint detected: "
						+ancestor+"->"+predecessor+"->"+ancestor);	
	}

	@Override
	public synchronized boolean constraitsFulfilled(String type, String topic) {
		// no constraints registered
		if(!constraints.containsKey(type+topic))
			return true;
		
		// check whether all predecessors have been received
		LinkedList<String> predecessors = constraints.get(type+topic);

		for(int i=0; i<predecessors.size(); i++)
			if(!super.messageReceived(predecessors.get(i)))
				return false;

		return true;
	}

}
