package ctu.nengoros;

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
	
	private void initNode(String[]argv){
		System.out.println(me+"starting the node named: "+name);
	    CommandLineLoader loader = new CommandLineLoader(Lists.newArrayList(argv));
	    String nodeClassName = loader.getNodeClassName();
	    System.out.println("Loading node class: " + loader.getNodeClassName());
	    nodeConfiguration = loader.build();

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
	    System.out.println(me+"the node named: "+name+" successfully launched");
	}
	
	
	public void start(){
		Preconditions.checkState(nodeMain != null);
	    nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
	    nodeMainExecutor.execute(nodeMain, nodeConfiguration);
	}
	
	public void stop(){
		System.out.println(me+"stopping the node named: "+name);
		nodeMainExecutor.shutdown();
		System.out.println(me+"node named "+name+" successfully down");
	}
	
	
}
