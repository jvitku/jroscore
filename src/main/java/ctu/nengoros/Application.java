package ctu.nengoros;
/**
 * This interface is used by the ShutDownInterceptor
 * 
 * @author Jaroslav Vitku
 *
 */
public interface Application {
	
	public void printUsage();
	
	public void start();
	
	public void shutDown();
	
	public boolean isRunning();
}
