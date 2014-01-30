package ctu.nengoros.network.node.synchedStart.impl;

import java.util.ArrayList;

import ctu.nengoros.network.node.synchedStart.SyncedUnitInterface;

/**
 * Class for synchronization (e.g. Nengo with ROS components) of arbitrary components.
 * The Unit has two states: ready/notReady, Unit is ready if the following is true:
 * <ul>
 * 	<li> has set flag: asynchronous=true (simulation is not waiting for it) </li>
 * 	<li> all its childs are ready </li>
 * 	<li> does not have any childs and is set to state: ready</li>
 * </ul>
 * 
 * @author Jaroslav Vitku
 */
public abstract class SyncedUnit implements SyncedUnitInterface{

	// should we check all conditions, or always presume state: ready (therefore asynchronous communication?)
	protected boolean synchronous;
	private volatile boolean ready = true;
	private final boolean DEFSYNCHRONOUS = true;	// synchronous communication by default
	private final boolean DEFREADY = true; 			// default state after init
	private ArrayList<SyncedUnitInterface> childs;
	
	private final String me = "[SyncedUnit] "; 
	private String name = "";
	
	public SyncedUnit(){
		this.synchronous = this.DEFSYNCHRONOUS;
		this.ready = this.DEFREADY;
		this.childs = new ArrayList<SyncedUnitInterface>();
	}
	
	public SyncedUnit(String name){
		this.name = name;
		this.synchronous = this.DEFSYNCHRONOUS;
		this.ready = this.DEFREADY;
		this.childs = new ArrayList<SyncedUnitInterface>();
	}
	
	public SyncedUnit(boolean synchronous){
		this.synchronous = synchronous;
		this.ready = this.DEFREADY;
		this.childs = new ArrayList<SyncedUnitInterface>();
	}
	
	public SyncedUnit(boolean synchronous, String name){
		this.name = name;
		this.synchronous = synchronous;
		this.ready = this.DEFREADY;
		this.childs = new ArrayList<SyncedUnitInterface>();
	}

	@Override
	public synchronized void setSynchronous(boolean synchronous) {
		this.synchronous = synchronous;
		if(this.synchronous)
			this.ready = true;	// not to halt the simulation
	}
	
	@Override
	public synchronized boolean isReady() {
		if(!this.synchronous)
			return true;
		
		if(childs.size()==0)
			return this.ready;
		
		return this.allChildsReady();
	}

	@Override
	public synchronized void setReady(boolean ready) {
		this.ready = ready;
	}

	@Override
	public synchronized void addChild(SyncedUnitInterface child) {
		if(childs.contains(child)){
			System.err.println(name+" "+me+"addChild: child aleary registered!");
			return;
		}
		childs.add(child);
	}

	@Override
	public synchronized void removeChild(SyncedUnitInterface child) {
		if(!childs.contains(child)){
			System.err.println(name+" "+me+"removeChild: child not found!");
			return;
		}
		childs.remove(child);
		
		if(childs.size()==0)
			this.ready = this.DEFREADY;
	}
	
	@Override
	public synchronized void setName(String name){// throws StructuralException{ 
		this.name = name;
	}
	
	@Override
	public synchronized String getFullName(){ return this.name; }
	
	
	private boolean allChildsReady(){
		for(int i=0; i<childs.size(); i++)
			if(!childs.get(i).isReady())
				return false;
		return true;
	}
	
	@Override
	public synchronized void discardChildsReady(){
		this.ready = false;
		for(int i=0; i<childs.size(); i++)
			childs.get(i).discardChildsReady();
	}
	
	
}
