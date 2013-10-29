package ctu.nengoros.demoTest;

import static org.junit.Assert.*;

import org.junit.Test;

import ctu.nengoros.RosRunner;
import ctu.nengoros.demoTest.nodeTesters.SubscriberTestNode;
import ctu.nengoros.testsuit.RosCommunicationTester;

public class SubscriberTests extends RosCommunicationTester{

	
	@Test
	public void testDemoSubscriberConnections() {
		
		RosRunner subscriberRunner = 
				super.runNode("ctu.nengoros.demoTest.nodes.DemoSubscriber");		
		RosRunner subscriberTestRunner = 
				super.runNode("ctu.nengoros.demoTest.nodeTesters.SubscriberTestNode");
		
		SubscriberTestNode st = null;
		
		if(!(subscriberTestRunner.getNode() instanceof SubscriberTestNode))
			fail("Wrong class launched");
		else
			st = (SubscriberTestNode)subscriberTestRunner.getNode();
		if(st==null)
			fail("Could not load DemoSubscriberTester node");
		
		st.waitForCommunicationReady();
		System.out.println("Nodes registered, communication between nodes established.");
		
		super.sleep(500);//ms
		
		subscriberRunner.stop();
		subscriberTestRunner.stop();
		
		assertFalse(subscriberRunner.isRunning());
		assertFalse(subscriberTestRunner.isRunning());
	}
}
