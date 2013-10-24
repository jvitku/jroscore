package ctu.nengoros;

import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import org.ros.RosCore;

/**
 * Launcher for RosCore, a Java-based implementation of [roscore](http://wiki.ros.org/roscore).
 * 
 * @author Jaroslav Vitku
 *
 */
public class Jroscore implements Application {

	private static final String defaultUri = "http://localhost:11311/";
	private static final String me = "[Jroscore]: ";

	private RosCore rosCore;
	//private String address;
	private URI myUri;
	private volatile boolean running=false;

	public static void main(String[] args) {

		if(args.length > 1){
			usage();
			return;
		}
		
		Jroscore jr = new Jroscore(args);

		// add the shutdown hook
		ShutDownInterceptor shutdownInterceptor = new ShutDownInterceptor(jr);
		Runtime.getRuntime().addShutdownHook(shutdownInterceptor);
		
		jr.start();
	}
	
	public Jroscore(String[] args){
		this.parseURI(args);
	}
	
	public Jroscore(){
		this.myUri = this.getUri(defaultUri);
	}

	public void parseURI(String[] args){
		String uri;
		if(args.length < 1){
			uri=defaultUri;
			warn();
		}else{
			uri=args[0];
		}
		this.myUri = this.getUri(uri);
	}
	
	/**
	 * Try to parse URI given by String
	 * @param st String uri
	 * @return valid uri or exception
	 */
	private URI getUri(String st){

		try {
			URI ur= new URI(st);
			return ur;
		} catch (URISyntaxException e) {
			// default uri failed?
			if(st.equalsIgnoreCase(defaultUri)){
				System.err.println(me+"Could not parse default URI: "+defaultUri);
				return null;
				// will try the default one
			}else{
				System.err.println(me+"Could not parse your URI: "+st);
				return this.getUri(defaultUri);
			}
		}
	}

	
	public static void usage(){
		System.out.println("=============== Usage ===============");
		System.out.println("Run the main method. One optinal argument specifying "+
				"an URI address of the RosCore, e.g.: "+defaultUri+"\n");
	}

	@Override
	public void printUsage() {
		usage();
	}
	
	@Override
	public void start() {
		// initialize new public roscore with parsed address
		if(myUri==null){
			rosCore = RosCore.newPublic();
		}else{
			rosCore = RosCore.newPublic(myUri.getHost(),myUri.getPort());
		}
		rosCore.start();

		// wait for start..
		try {
			assertTrue(rosCore.awaitStart(1, TimeUnit.SECONDS));
		} catch (InterruptedException e) {
			e.printStackTrace();
			this.running = false;
			System.err.println(me+"Could not start ros core on address: "+
					myUri.getHost()+" "+myUri.getPort()+" exiting..");
			return;
		}
		running=true;
		myUri = rosCore.getUri();
		
		System.out.println(me+"Server successfully launched on address: "+
					myUri.getHost()+" "+myUri.getPort()+".");
	}

	@Override
	public void shutDown() {
		if(isRunning()){
			System.out.println(me+"OK, shutting down the server");
			rosCore.shutdown();
			running = false;
			System.out.println(me+"Shutdown complete, exiting.");	
		}else{
			System.err.println(me+"Core not launched, exiting");
		}
	}

	private void warn(){
		System.out.println(me+"warning: could not parse URI, " +
				"will use default one; this: "+defaultUri);
	}

	@Override
	public boolean isRunning() { return this.running; }

}
