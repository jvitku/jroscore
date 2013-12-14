package ctu.nengoros.rosparam.impl;

import org.apache.commons.logging.Log;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;

import ctu.nengoros.rosparam.NodeInfoInt;

/**
 * Note that dynamic remapping of parameters may be unsupported here.
 * 
 * @author Jaroslav Vitku
 *
 */
public class NodeInfo implements NodeInfoInt{

	protected final ConnectedNode cn;
	protected final Log l;
	public final String me = "[NodeInfo] ";
	
	protected final GraphName basename;		// name of the node
	protected final GraphName namespace;	// complete namespace
	protected final GraphName absolutename;	// complete name 
	
	public NodeInfo(ConnectedNode connectedNode){
		cn = connectedNode;
		l = cn.getLog();
		absolutename = cn.getName();
		basename = cn.getName().getBasename();
		namespace = cn.getName().toGlobal().getParent();
	}
	
	@Override
	public GraphName getNamespace() { return namespace; }

	@Override
	public GraphName getAbsoluteName() { return absolutename; }

	@Override
	public GraphName getBaseName() { return basename; }

}
