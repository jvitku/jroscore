package ctu.nengoros.rosparam;


import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;

/**
 * 
 * Just a dummy node for testing private parameters
 * 
 * @author Jaroslav Vitku vitkujar@fel.cvut.cz
 * 
 */
public class DummyNode extends ParamReadingNode{

	public static final String DEFAULT_NAME = "DefaulNodeName";


	public boolean hasParam(String key){ return pt.has(key); }
	public String getParam(String key){ return pt.getString(key); }

	@Override
	public void onStart(final ConnectedNode connectedNode) {
		super.onStart(connectedNode);

		connectedNode.getLog().info("Hi, here is node: "+connectedNode.getName());
	}

	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of(DEFAULT_NAME);
	}
}
