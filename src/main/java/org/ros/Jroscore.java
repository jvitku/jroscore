package org.ros;

import static org.junit.Assert.assertTrue;
import java.util.concurrent.TimeUnit;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * I did not find any ros_core launcher, so I made this one
 * 
 * Default master server uri (address) is different from the variable:
 * $ROS_MASTER_URI (see: http://www.ros.org/wiki/ROS/EnvironmentVariables )
 * 
 * This means that nodes launched without explicitly given uri will not find roscore
 *
 * So I will try to solve this by reading this variable and setting the correct uri
 * 
 * Note: there are still some problems (e.g. rosout is not visible in rxgraph)
 * 
 * @author j
 *
 */
public class Jroscore {
	/*
	protected NodeConfiguration nodeConfiguration;
	protected NodeMainExecutor nodeMainExecutor;
	*/
	private static final String s = "http://localhost:11311/";
	private static RosCore rosCore;
	private static URI u;
	private static final String me = "jroscore: ";
	private static boolean running=false;
	/**
	 * Tries to read the URI address of the roscore from command line
	 * Launches the master server (roscore) and waits 
	 * @param args
	 */
	public static void main(String[] args) {
		String uri;
		
		if(args.length > 1){
			pu();
			return;
		}else if(args.length < 1){
			uri=s;
			warn();
			pu();
		}else{
			uri=args[0];
		}
		start(uri);
	}
	
	public static void start(String uri){
		u=getUri(uri);
		
		if(u!= null)
			System.out.println(me+"Launching master server now, uri: "+u);
		else
			System.out.print(me+"Launching master server now, uri will be generated");
		
		u = launchCore(u);
		if(u!=null)
			System.out.println(me+"Server successfully launched on address: "+u);
	}
	
	public static void start(){
		start(s);
	}
	
	public static void stop(){ 
		System.out.println("jroscore: OK, shutting down the server");
		tearDown();
	}
	
	public static boolean running(){ return running; }
	
	private static void pu(){
		System.out.println("=============== Usage ===============");
		System.out.println("run this Class with one argument, which specifies "+
				"the URI address of the roscore, e.g.: "+s);
	}
	
	private static void warn(){
		System.err.println(me+"warning: could not parse URI, " +
		"will use default one, other nodes may not see the master\n" +
		"Correct format of URI is e.g.: "+s);
	}

	/**
	 * try to parse URI from the command line
	 * @param st
	 * @return valid uri or null in case of fail
	 */
	private static URI getUri(String st){
		try {
			URI ur= new URI(st);
		return ur;
		} catch (URISyntaxException e) {
			//e.printStackTrace();
			warn();
			return null;
		}
	}
  
	/**
	 * Launch the roscore on the specified uri
	 * @param ur new address of the roscore, if null, some will be generated
	 * @return uri of the new roscore
	 */
	private static URI launchCore(URI ur){
			
		// initialize new public roscore with parsed address
		if(ur==null)
			rosCore = RosCore.newPublic();	
		else
			rosCore = RosCore.newPublic(ur.getHost(),ur.getPort());
		
		rosCore.start();
		// wait for start..
		try {
			assertTrue(rosCore.awaitStart(1, TimeUnit.SECONDS));
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
		running=true;
		/*
		// some potentially interesting example of rosCore use?
		nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
		nodeConfiguration = NodeConfiguration.newPublic(rosCore.getUri().getHost(), rosCore.getUri());
		 */

		/*
		// here looking for possibility of listing of all nodes and their communication...
		// topic
		// getSubscribers
		rosCore.getMasterServer().getPublishedTopics(arg0, arg1)
		*/
		return rosCore.getUri();
	}
	
	/**
	 * should be used for shutting down the server.. but is not
	 */
	private static void tearDown() {
		//nodeMainExecutor.shutdown();
		running = false;
		rosCore.shutdown();
	}
	
	

}
