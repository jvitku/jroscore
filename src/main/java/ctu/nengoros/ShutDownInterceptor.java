package ctu.nengoros;

/**
 * This catches SIGTERM signals and shut-downs the application gracefully.   
 * @see http://twit88.com/blog/2007/09/27/do-a-graceful-shutdown-of-your-java-application-when-ctr-c-kill/
 * 
 * @author Jaroslav Vitku
 *
 */
public class ShutDownInterceptor extends Thread {

	private Application app;

	public ShutDownInterceptor(Application app) {
		this.app = app;
	}

	/**
	 * this is called when SIGTERM is intercepted
	 */
	public void run() {
		app.shutDown();
	}
}