package ctu.nengoros.util;


/**
 * This catches SIGTERM signals and shut-downs the application gracefully.   
 * 
 * @see <a href="http://twit88.com/blog/2007/09/27/do-a-graceful-shutdown-of-your-java-application-when-ctr-c-kill/">source</a>
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