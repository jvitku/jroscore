package ctu.nengoros.util;

import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeListener;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

/**
 * This listener helps the RosRunner to handle launching and killing ROSJava nodes.
 * 
 * @author Jaroslav Vitku
 *
 */
public class RosRunnerNodeListener implements NodeListener{

	private final int waittime = 5;
	private final int maxwait = 5000;
	
	private volatile boolean isRunning = false;
	private volatile boolean shutdownComplete = false;
	
	private final String me = "[RosRunner]: ";
	
	public boolean isRunning(){ return this.isRunning; }
	public boolean isShutdowncomplete(){ return this.shutdownComplete; }
	
	private boolean connectionRefused = false;
	
	public final NodeMainExecutor executor;
	public final NodeMain myNode;
	
	public RosRunnerNodeListener(NodeMainExecutor executor, NodeMain myNode){
		this.executor = executor;
		this.myNode = myNode;
	}
	
	public boolean getConnectionRefused(){
		return this.connectionRefused;
	}
	
	@Override
	public void onError(Node arg0, Throwable arg1) {
		this.isRunning = false;
		if(arg1.getMessage().contains("Connection refused"))
			this.connectionRefused = true;
			
		System.err.println(me+"Node "+arg0.getName()+" encountered error, shutdown will start now!");
	}

	@Override
	public void onShutdown(Node arg0) {
		System.out.println(me+"Node "+arg0.getName()+" is being shut down..");
		this.isRunning = false;
	}

	@Override
	public void onShutdownComplete(Node arg0) {
		
		System.out.println(me+"Node "+arg0.getName()+" is completely down!");
		this.shutdownComplete = true;
	}

	@Override
	public void onStart(ConnectedNode arg0) {
		System.out.println(me+"Node "+arg0.getName()+" is being started!");
		this.isRunning = true;
		this.connectionRefused = false;
	}
		
	public void awaitshutdown(){
		int poc = 0;
		while(!this.isShutdowncomplete()){
			if(poc++*waittime > maxwait){
				System.err.println(me+"Giving up waiting for the node to shut down! ");
				//executor.shutdown();
				//executor.shutdownNodeMain(myNode);
				return;
			}
			try {
				Thread.sleep(waittime);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	public void awaitstart(){
		int poc = 0;
		while(!this.isRunning()){
			if(poc++*waittime > maxwait){
				// TODO: after failing communicating with master, the process is still there, kill it..
				System.err.println(me+"Giving up waiting for the node to start!");
				//executor.shutdown();
				//executor.shutdownNodeMain(myNode);
				return;
			}
			try {
				Thread.sleep(waittime);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		}
	}
}
