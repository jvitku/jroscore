package ctu.nengoros;

import java.io.File;

import org.ros.exception.RosRuntimeException;
import org.ros.internal.loader.CommandLineLoader;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;


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
	
	private final String me = "[RosRunner]: ";
	private final String name;
	private volatile boolean running = false;
	
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
		if(running){
			return nodeMain;
		}
		System.err.println(me+"my node is not running!");
		return null;
	}
	
	private void initNode(String[]argv){
		System.out.println(me+"starting the node named: "+name);
	    CommandLineLoader loader = new CommandLineLoader(Lists.newArrayList(argv));
	    String nodeClassName = loader.getNodeClassName();
	    System.out.println("Loading node class: " + loader.getNodeClassName());
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
	    running = false;
	    System.out.println(me+"the node named: "+name+" successfully launched");
	}
	
	
	public void start(){
		Preconditions.checkState(nodeMain != null);
	    nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
	    nodeMainExecutor.execute(nodeMain, nodeConfiguration);
	    running = true;
	}
	
	public boolean isRunning(){ return running; }
	
	public void stop(){
		System.out.println(me+"stopping the node named: "+name);
		nodeMainExecutor.shutdown();
		running = false;
		System.out.println(me+"node named "+name+" successfully down");
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
	
}
