package ctu.nengoros.network.node.synchedStart;

/**
 * Object that can be in two states: ready/not ready. 
 * 
 * @author Jaroslav Vitku
 *
 */
public interface SynchUtil {

	public boolean isReady();

	public void setReady(boolean ready);

	public String getName();
	
}
