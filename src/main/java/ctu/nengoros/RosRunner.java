package ctu.nengoros;

import java.io.File;
import java.util.ArrayList;

import org.ros.exception.RosRuntimeException;
import org.ros.internal.loader.CommandLineLoader;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeListener;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import ctu.nengoros.util.RosRunnerNodeListener;


/**
 *	Small Class for running and stopping the ROS java nodes, based on org.ros.RosRun class.
 * 
 *  This enables tester to have exact control over starting/stopping particular ROS nodes.
 * 
 * @author Jaroslav Vitku
 *
 */
public class RosRunner {

	NodeMain nodeMain;
	NodeConfiguration nodeConfiguration;
	NodeMainExecutor nodeMainExecutor;
	
	RosRunnerNodeListener nodeListener;
	
	private final String me = "[RosRunner]: ";
	private final String name;
	
	public RosRunner() throws Exception{
		System.err.println(me+"Call the constructor with a name of node!");
		throw new Exception("Incorrect instantiation of RosRunner!");
	}
	
	public RosRunner(String[] argv) throws Exception{
		if(argv.length <1){
			System.err.println(me+"You have not specified Node Name!");
			throw new Exception("No node name specified!");
		}
		name = argv[0];
		this.initNode(argv);
	}
	
	public RosRunner(String name) throws Exception{
		if(name == null){
			System.err.println(me+"You have not specified Node Name!");
			throw new Exception("No node name specified!");
		}
		this.name = name;
		this.initNode(new String[]{name});
	}
	
	public NodeMain getNode(){
		
		if(nodeListener !=null && nodeListener.isRunning()){
			return nodeMain;
		}
		System.err.println(me+"my node is not running!");
		return null;
	}
	
	private void initNode(String[]argv){
		System.out.println(me+"starting the node named: "+name);
	    CommandLineLoader loader = new CommandLineLoader(Lists.newArrayList(argv));
	    String nodeClassName = loader.getNodeClassName();
	    
	    nodeConfiguration = loader.build();

	    //this.printClaspath();
	    
	    nodeMain = null;
	    try {
	      nodeMain = loader.loadClass(nodeClassName);
	    } catch (ClassNotFoundException e) {
	      throw new RosRuntimeException("Unable to locate node: " + nodeClassName, e);
	    } catch (InstantiationException e) {
	      throw new RosRuntimeException("Unable to instantiate node: " + nodeClassName, e);
	    } catch (IllegalAccessException e) {
	      throw new RosRuntimeException("Unable to instantiate node: " + nodeClassName, e);
	    }
	    nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
	    nodeListener = new RosRunnerNodeListener(nodeMainExecutor, nodeMain);
	}
	
	
	public void start(){
		Preconditions.checkState(nodeMain != null);
	    
	    // add a custom listener for checking if the node is running/down etc..
	    ArrayList<NodeListener> l = new ArrayList<NodeListener>();
	    l.add(nodeListener);
	    
	    nodeMainExecutor.execute(nodeMain, nodeConfiguration, l);
	    
	    nodeListener.awaitstart();
	}
	
	public boolean isRunning(){
		return nodeListener.isRunning();
	}
	
	public void stop(){
		nodeMainExecutor.shutdown();
		nodeListener.awaitshutdown();
	}
	
	protected void printClaspath(){
		String s = System.getProperty("java.class.path", ".");
	    String[] files = s.split(File.pathSeparator);
	    
	    System.out.println("--------------------------------------------------"+
	    		" Java classpath now includes:");
	    for( int i=0; i<files.length; i++)
	    	System.out.println(files[i]);
	    System.out.println("--------------------------------------------------"+
	    		"----------------------------");
	}
	
	public void stopAll(){
		nodeMainExecutor.shutdown();
		
		//nodeMainExecutor.
		
	}
	
}
