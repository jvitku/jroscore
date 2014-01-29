package ctu.nengoros.network.node.synchedStart.impl;

import java.util.ArrayList;

import ctu.nengoros.network.common.exceptions.StartupDelayException;
import ctu.nengoros.network.node.synchedStart.SynchedStartInterface;

/**
 * Basic implementation of the SynchedStartInterface.
 *  
 * @author Jaroslav Vitku
 */
public abstract class SyncedStart implements SynchedStartInterface{

	public static final boolean DEF_LOG = false;	// log about waiting?
	public static final int DEF_MAXWAIT = 7000;		// default max time to wait
	public static final int WAITTIME = 10;			// sleep for?
	public final boolean DEF_READY = false;	// ready after startup?

	private boolean logging = DEF_LOG;
	private int maxWait = DEF_MAXWAIT;
	private volatile boolean readyDefinite = false;	// have to check childs?
	private volatile boolean ready = this.DEF_READY;

	private ArrayList<SynchedStartInterface> childs;

	private final String me = "[SyncedStart] "; 

	public SyncedStart(){
		this.childs = new ArrayList<SynchedStartInterface>();
	}

	@Override
	public synchronized boolean isReady() {

		if(this.ready && this.readyDefinite)
			return true;

		if(childs.size()==0){
			this.readyDefinite = true;
			return this.ready;
		}
		return this.allChildsReady() && this.ready;
	}

	@Override
	public synchronized void setReady(boolean ready) {
		if(this.ready && !ready){
			System.err.println(me+"ERROR: will not disable the "
					+ "current ready state! Staying ready");
			return;
		}
		if(childs.size()==0){
			this.readyDefinite = true;
		}
		this.ready = true;
	}

	@Override
	public synchronized void addChild(SynchedStartInterface child) {
		if(childs.contains(child)){
			System.err.println(me+"addChild: child aleary registered!");
			return;
		}
		childs.add(child);
	}

	@Override
	public synchronized void removeChild(SynchedStartInterface child) {
		if(!childs.contains(child)){
			System.err.println(me+"removeChild: child not found!");
			return;
		}
		childs.remove(child);
	}

	private boolean allChildsReady(){
		for(int i=0; i<childs.size(); i++)
			if(!childs.get(i).isReady())
				return false;
		this.readyDefinite = true;
		return true;
	}

	@Override
	public void awaitReady() throws StartupDelayException {
		int waited = 0;
		if(this.logging)
			System.out.print("\n waiting for the ROS message to be received");

		while(!this.isReady()){
			try {
				Thread.sleep(WAITTIME);
			} catch (InterruptedException e) { e.printStackTrace(); }

			if(this.logging)
				System.out.print(".");

			waited +=WAITTIME;
			if(waited > maxWait){
				throw new StartupDelayException("\n\n"+me+" "+this.getName()+
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

}
