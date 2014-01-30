package ctu.nengoros.network.node.synchedStart.impl;

/**
 * This manager could be used by HannsNodes for indicating that they are started.
 * Since the method 
 * {@link ctu.nengoros.network.node.AbstractHannsNode#onStart(org.ros.node.ConnectedNode)}
 * is called asynchronously, this manager is initialized implicitly
 * with default name. Therefore, the method {@link #setFullName(String)} should
 * be called as soon as possible in the method onStart().
 * 
 * @see SyncedStart
 * @see ctu.nengoros.network.node.AbstractHannsNode
 * 
 * @author Jaroslav Vitku
 *
 */
public class StartupManager extends SyncedStart {

	public static final String DEF_NAME = "[StartupManager] ";
	
	private String name;
	
	public StartupManager(String fullName){
		this.name = fullName;
	}
	
	public StartupManager(){
		this.name = DEF_NAME;
	}
	
	@Override
	public String getFullName() { return this.name; }

	@Override
	public void setFullName(String name) { this.name = name; }

}
