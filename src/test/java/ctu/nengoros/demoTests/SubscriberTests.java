package ctu.nengoros.demoTests;

import static org.junit.Assert.*;

import org.junit.Test;

import ctu.nengoros.RosRunner;
import ctu.nengoros.demoTests.nodeTesters.SubscriberTestNode;
import ctu.nengoros.testsuit.RosCommunicationTester;

public class SubscriberTests extends RosCommunicationTester{


	/**
	 * Testing the subscriber:
	 * 	-start tested subscriber and tester node
	 * 	-obtain the tester node
	 * 	-wait for tester to be registered and to establish the communication between nodes
	 * 	-give them 500ms for communication
	 * 	-shutdown nodes
	 */
	@Test
	public void testDemoSubscriberConnections() {
		
		RosRunner subscriberRunner = 
				super.runNode("ctu.nengoros.demoTests.nodes.DemoSubscriber");		
		RosRunner subscriberTestRunner = 
				super.runNode("ctu.nengoros.demoTests.nodeTesters.SubscriberTestNode");
		
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
