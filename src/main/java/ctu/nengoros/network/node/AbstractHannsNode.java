package ctu.nengoros.network.node;

import org.apache.commons.logging.Log;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import ctu.nengoros.network.Topic;
import ctu.nengoros.network.common.exceptions.StartupDelayException;
import ctu.nengoros.network.node.infrastructure.rosparam.impl.PrivateRosparam;
import ctu.nengoros.network.node.infrastructure.rosparam.manager.ParamList;
import ctu.nengoros.network.node.observer.stats.ProsperityObserver;
import ctu.nengoros.network.node.synchedStart.StartupManager;
import ctu.nengoros.network.node.synchedStart.impl.BasicStartupManager;
import ctu.nengoros.util.SL;

/**
 * <p>Defines ROS node with inputs and outputs with main purpose of use in the Hybrid 
 * Artificial Neural Network Systems (HANNS) framework.</p>
 * 
 * <p>Note that this serves just as a helper for maintaining algorithms as ROS nodes, arbitrary
 * ROS node should be still supported by the NengoROS simulator.</p> 
 *  
 * @author Jaroslav Vitku
 *
 */
public abstract class AbstractHannsNode extends AbstractNodeMain implements ProsperityHannsNode{
	
	/**
	 * ROS node configuration
	 */
	public static final String name = "AbstractHannsNode"; // redefine the nodes name
	public final String me = "["+name+"] ";
	public String fullName;					// this name should be unique in the ROS network and set in the onStart()
	public static final String s = "/";
	public static final String conf = "conf"+s;
	public static final String io = "io"+s;

	protected Log log;
	protected Publisher<std_msgs.Float32MultiArray> dataPublisher;
	
	// actions
	public static final String actionPrefix = "a";	// action names: a0, a1,a2,..
	public static final String topicDataOut = io+Topic.baseOut+"Actions"; 	// outActions
	
	// states
	public static final String statePrefix = "s"; 	// state var. names: s0,s1,..
	public static final String topicDataIn  = io+Topic.baseIn+"States"; 	// inStates

	// ROS node configurable parameters
	protected PrivateRosparam r;			// parameter (command-line) reader
	protected ParamList paramList;			// parameter storage

	// waiting for the node to be ready
	public StartupManager startup = new BasicStartupManager(this);
	
	/**
	 * Logging
	 */
	// whether to log into the file, if not (default) the console is used
	//public static final String shouldLog = "shouldLog";
	public static final String logToFileConf = "logToFile";
	public static final boolean DEF_LTF = false;
	protected boolean logToFile = DEF_LTF;
	protected SL logger;	// logger which logs data to file if chosen, if not, the ROS logger is used
	
	// how often to log data (into the console), -1 means no logging
	public static final int DEF_LOGPERIOD = 100; // how often to log? 
	public static final String logPeriodConf = "logPeriod";
	protected int logPeriod = DEF_LOGPERIOD;
	
	// each node should be able to publish its prosperity (real time visible in the Nengoros)
	protected Publisher<std_msgs.Float32MultiArray> prospPublisher;
	public static final String topicProsperity = conf+"prosperity";

	/**
	 * IO configuration
	 */
	// Number of state variables considered by the RL (predefined sampling)
	public static final String noInputsConf = "noInputs";
	public static final int DEF_NOINPUTS = 2;
	// Number of actions that can be performed by the RL ASM (coding 1ofN)
	public static final String noOutputsConf = "noOutputs";
	public static final int DEF_NOOUTPUTS = 4;

	/**
	 * Main entry point to the ROS node. The initialization is made here, 
	 * the functionality of the node is called asynchronously by incoming 
	 * ROS messages.
	 * 
	 * Note: the {@link BasicStartupManager#setFullName(String)} should be called 
	 * from here.
	 * 
	 * @param connectedNode publisher/subscriber/log factory
	 */
	@Override
	public abstract void onStart(final ConnectedNode connectedNode);

	/**
	 * Read private parameters potentially passed to the node.
	 * These can be passed either from the command-line, or can be already
	 * set in the parameter server. 
	 * 
	 * Parse available parameters (or read default values) and instantiate
	 * all classes used here. 
	 * 
	 * @param connectedNode publisher/subscriber/log factory
	 */
	protected abstract void parseParameters(ConnectedNode connectedNode);
	
	/**
	 * Register parameters that are available for this node. 
	 * TOTO: the {@link ctu.nengoros.network.node.infrastructure.rosparam.manager.ParamList} should be able
	 * to parse the Nodes parameters from the XML file.
	 */
	protected abstract void registerParameters();
	
	/**
	 * Register some data publisher for publishing nodes data (actions),  
	 * and some data subscribers for receiving data expected by this node.
	 * 
	 * @param connectedNode ROS factory for publishers/subscribers
	 */
	protected abstract void buildDataIO(ConnectedNode connectedNode);
	
	/**
	 * The same as {@link #buildDataIO(ConnectedNode)}, but here, the 
	 * configuration subscribers (used for online node configuration) 
	 * can be registered.
	 * @param connectedNode ROS factory for registering publishers/subscribers
	 */
	protected abstract void buildConfigSubscribers(ConnectedNode connectedNode);

	/**
	 * Log only if allowed, and if the value is changed
	 * 
	 * @param message message to show value change
	 * @param oldVal old value
	 * @param newVal new one
	 */
	protected void logParamChange(String message, double oldVal, double newVal){
		if(oldVal==newVal)
			return;
		if(this.logPeriod>=0)
			log.info(message+" Value is being changed from: "+oldVal+" to "+newVal);
	}
	
	/**
	 * Each HannsNode should be able to provide (at least one-dimensional)
	 * information about its prosperity (in ideal case from the interval [0,1]). 
	 * 
	 * @return {@link ProsperityObserver} which defines how good a node performs, 
	 * observer has one value of prosperity, but can have also children observers.
	 */
	public abstract ProsperityObserver getProsperityObserver();
	
	/**
	 * By default, each HANNS node publishes one value of prosperity
	 * on the topic named prosperity. Node can publish more values, but
	 * the first one should be the combined (entire) prosperity by convention.
	 * @param connectedNode ROS factory for publishers/subscribers
	 */
	public void buildProsperityPublisher(ConnectedNode connectedNode){
		prospPublisher =connectedNode.newPublisher(topicProsperity, 
				std_msgs.Float32MultiArray._TYPE);
	}
	
	@Override
	public void awaitStarted() throws StartupDelayException{
		startup.awaitStarted();
	}
	

}
