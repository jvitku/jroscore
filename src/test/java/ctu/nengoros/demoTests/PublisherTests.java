package ctu.nengoros.demoTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import ctu.nengoros.RosRunner;
import ctu.nengoros.demoTests.nodeTesters.PublisherTestNode;
import ctu.nengoros.testsuit.RosCommunicationTester;

public class PublisherTests extends RosCommunicationTester {
	
	/**
	 * Testing the subscriber:
	 * 	-start tested publisher and tester node
	 * 	-obtain the tester node
	 * 	-wait for tester to be registered and to establish the communication between nodes
	 * 	-give them 500ms for communication
	 * 	-check if some messages were received
	 * 	-shutdown nodes
	 */
	@Test
	public void testDemoSubscriberConnections() {
		
		RosRunner publisherRunner = 
				super.runNode("ctu.nengoros.demoTests.nodes.DemoPublisher");		
		RosRunner publisherTestRunner = 
				super.runNode("ctu.nengoros.demoTests.nodeTesters.PublisherTestNode");
		
		PublisherTestNode pt = null;
		
		if(!(publisherTestRunner.getNode() instanceof PublisherTestNode))
			fail("Wrong class launched");
		else
			pt = (PublisherTestNode)publisherTestRunner.getNode();
		if(pt==null)
			fail("Could not load DemoSubscriberTester node");
		
		
		pt.waitForCommunicationReady();
		System.out.println("Nodes registered, communication between nodes established.");
		
		super.sleep(500);//ms
		
		assertTrue(pt.somethingReceived());
		
		publisherRunner.stop();
		publisherTestRunner.stop();
		
		assertFalse(publisherRunner.isRunning());
		assertFalse(publisherTestRunner.isRunning());
	}

}
