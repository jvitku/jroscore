package ctu.nengoros.rosparam;

import org.ros.namespace.GraphName;

public interface NodeInfoInt {
	
	public GraphName getNamespace();
	public GraphName getAbsoluteName();
	public GraphName getBaseName();

}
