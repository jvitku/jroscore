package ctu.nengoros.network.common.exceptions;

/**
 * <p>Typically, if a ROS Node class can be found, but there is another problem. 
 * Typical problem can be that the method 
 * {@link org.ros.node.AbstractNodeMain#onStart(org.ros.node.ConnectedNode)}
 * is not called fast enough, e.g. because of waiting for the TimeProvider.</p>
 * 
 * <p>The method {@link ctu.nengoros.network.node.HannsNode#awaitReady()} should return
 * in some reasonable time (e.g. 7 seconds), if the predefined max wait time 
 * is exceeded, this exception can be thrown.</p> 
 * 
 * @author Jaroslav Vitku
 *
 */
public class StartupDelayException  extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message Text explanation of the exception. 
	 */
	public StartupDelayException(String message) {
		super(message);
	}

	/**
	 * @param cause Another throwable that indicates a problem underlying this 
	 * 		exception.  
	 */
	public StartupDelayException(Throwable cause) {
		super(cause); 
	}

	/**
	 * @param message Text explanation of the exception. 
	 * @param cause Another throwable that indicates a problem underlying this 
	 * 		exception.  
	 */
	public StartupDelayException(String message, Throwable cause) {
		super(message, cause);
	}

}
