package ctu.nengoros.network.node.discreteSync;

/**
 * Exception during attempt of synchronization of the {@link SetOfMessages} 
 * 
 * @author Jaroslav Vitku
 *
 */
public class NengoROSSync extends Exception{
	
	private static final long serialVersionUID = 1L;

	/**
	 * @param message Text explanation of the exception. 
	 */
	public NengoROSSync(String message) {
		super(message);
	}

	/**
	 * @param cause Another throwable that indicates a problem underlying this 
	 * 		exception.  
	 */
	public NengoROSSync(Throwable cause) {
		super(cause); 
	}

	/**
	 * @param message Text explanation of the exception. 
	 * @param cause Another throwable that indicates a problem underlying this 
	 * 		exception.  
	 */
	public NengoROSSync(String message, Throwable cause) {
		super(message, cause);
	}
}
