package ctu.nengoros.network.node.synchedStart.impl;

import java.util.ArrayList;

import ctu.nengoros.network.common.exceptions.StartupDelayException;
import ctu.nengoros.network.node.synchedStart.StartupManager;

/**
 * <p>This manager could be used by HannsNodes for indicating that they are started.
 * Since the method 
 * {@link ctu.nengoros.network.node.AbstractHannsNode#onStart(org.ros.node.ConnectedNode)}
 * is called asynchronously, this manager is initialized implicitly.</p>
 * 
 *  <p>The corresponding object has to implement the 
 *  {@link ctu.nengoros.network.node.synchedStart.impl.StartedObject} interface.</p>
 * 
 * 
 * @see StartupManager
 * @see StartedObject
 * @see ctu.nengoros.network.node.AbstractHannsNode
 * 
 * @author Jaroslav Vitku
 *
 */
public class BasicStartupManager implements StartupManager{

	public static final String DEF_NAME = "StartupManager";
	public static final boolean DEF_LOG = false;	// log about waiting?
	public static final int DEF_MAXWAIT = 7000;		// default max time to wait
	public static final int WAITTIME = 10;			// sleep for?
	public final boolean DEF_READY = false;			// ready after startup?

	private boolean logging = DEF_LOG;
	private int maxWait = DEF_MAXWAIT;

	private StartedObject o;	// my Object that should be ready 
	private String name;		// my name (specified in the constructor)

	private ArrayList<StartupManager> childs;

	private final String me = "[BasicStartupManager] "; 

	public BasicStartupManager(String fullName,StartedObject o){
		this.childs = new ArrayList<StartupManager>();
		this.name = fullName;
		this.o = o;
	}

	public BasicStartupManager(StartedObject o){
		this.childs = new ArrayList<StartupManager>();
		this.o = o;
	}

	@Override
	public String getFullName() {
		if(this.name != null)
			return this.name;

		if(o.getFullName()!=null)
			return o.getFullName();
		
		return DEF_NAME;
	}

	@Override
	public synchronized boolean allStarted() {
		// if all childs are ready (or there are none) and my Object is ready-> true
		return this.allChildsReady() && o.isStarted();
	}

	@Override
	public synchronized void addChild(StartupManager child) {
		if(childs.contains(child)){
			System.err.println(me+"addChild: child aleary registered!");
			return;
		}
		childs.add(child);
	}

	@Override
	public synchronized void removeChild(StartupManager child) {
		if(!childs.contains(child)){
			System.err.println(me+"removeChild: child not found!");
			return;
		}
		childs.remove(child);
	}

	private boolean allChildsReady(){
		if(this.childs.size()==0)
			return true;
		
		for(int i=0; i<childs.size(); i++)
			if(!childs.get(i).allStarted())
				return false;
		return true;
	}

	@Override
	public void awaitStarted() throws StartupDelayException {
		int waited = 0;
		if(this.logging)
			System.out.print("\n waiting for the ROS message to be received");

		while(!this.allStarted()){
			try {
				Thread.sleep(WAITTIME);
			} catch (InterruptedException e) { e.printStackTrace(); }

			if(this.logging)
				System.out.print(".");

			waited +=WAITTIME;
			if(waited > maxWait){
				throw new StartupDelayException("\n\n"+me+" "+this.getFullName()+
						" not ready fast enough. "
						+ "ROS communication probably broken, giving up!");
			}
		}
		if(this.logging)
			System.out.println("");
	}

	@Override
	public int getMaxWaitTime() { return this.maxWait;	}

	@Override
	public void setMaxWaitTime(int ms) {
		if(ms<=0){
			System.err.println(me+"ERROR: should wait non-negative time value");
			return;
		}
		this.maxWait = ms;
	}

	@Override
	public void setLogginEnabled(boolean enabled){ this.logging = enabled; }


	@Override
	public void setFullName(String name) {
		System.err.println("ERROR: StartupManager uses unique name from "
				+ "its StartedObject (or own if set in the construcotr),"
				+ " so setting name is not supported.");
	}

}
