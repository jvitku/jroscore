package ctu.nengoros;

import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeListener;

/**
 * This listener helps the RosRunner to handle launching and killing ROSJava nodes.
 * 
 * @author Jaroslav Vitku
 *
 */
public class RosRunnerNodeListener implements NodeListener{

	private volatile boolean isRunning = false;
	private volatile boolean shutdownComplete = false;
	
	private final String me = "[RosRunner]: ";
	
	public boolean isRunning(){ return this.isRunning; }
	public boolean isShutdowncomplete(){ return this.shutdownComplete; }
	
	@Override
	public void onError(Node arg0, Throwable arg1) {
		this.isRunning = false;
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
	}
	
	private int waittime = 10;
	private int maxwait = 5000;
	
	public void awaitshutdown(){
		int poc = 0;
		while(!this.isShutdowncomplete()){
			if(poc++*waittime > maxwait){
				System.err.println(me+"Giving up waiting for the node to shut down! ");
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
				System.err.println(me+"Giving up waiting for the node to start!");
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
