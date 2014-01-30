package ctu.nengoros.network.node.synchedStart;

/**
 * Parent for SynchedUnit and SynchedStart 
 * 
 * @author Jaroslav Vitku
 *
 */
public interface SynchUtil {

	public String getFullName();

	/**
	 * Full name is used mainly for identification of error
	 * messages. Unique name should be set as soon as this is known.
	 * @param name unique name for easier identification of error
	 */
	public void setFullName(String name);
	
}
