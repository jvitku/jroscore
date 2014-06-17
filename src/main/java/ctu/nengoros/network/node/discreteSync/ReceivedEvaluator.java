package ctu.nengoros.network.node.discreteSync;

/**
 * This is able to evaluate whether the message was received (e.g. no of messages is >0). 
 * 
 * @author Jaroslav Vitku
 */
public interface ReceivedEvaluator{
	
	boolean messageReceived(String key);
	
}
