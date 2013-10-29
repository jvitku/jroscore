package ctu.nengoros.other;

import ctu.nengoros.util.Application;
import ctu.nengoros.util.ShutDownInterceptor;

/**
 * Testing how to gracefully shutdown application.
 * 
 * Based on this: http://twit88.com/blog/2007/09/27/do-a-graceful-shutdown-of-your-java-application-when-ctr-c-kill/
 * 
 * @author Jaroslav Vitku
 *
 */
public class SampleApp /*extends BaseApp*/ implements Application {

	private volatile boolean isRunning=false;
	
	public void start() {
		int poc = 0;
		try {
			while(true){
				this.isRunning = true;
				System.out.println("Sleeping for 0.5 seconds ..."+poc++);
				Thread.sleep(500);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void shutDown() {
		// Do a graceful shutdown here
		System.out.println("Shutdown is called");
		this.isRunning = false;
	}

	public static void main(String args[]) {
		Application app = new SampleApp();
		ShutDownInterceptor shutdownInterceptor = new ShutDownInterceptor(app);

		Runtime.getRuntime().addShutdownHook(shutdownInterceptor);
		app.start();
	}

	@Override
	public void printUsage() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isRunning() {
		return this.isRunning;
	}
}