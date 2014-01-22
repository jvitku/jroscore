package ctu.nengoros.network.node.infrastructure.rosparam;

import org.ros.namespace.GraphName;

public interface NodeInfoInt {
	
	public GraphName getNamespace();
	public GraphName getAbsoluteName();
	public GraphName getBaseName();

}
