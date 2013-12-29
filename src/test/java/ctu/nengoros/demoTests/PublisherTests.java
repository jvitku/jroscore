package ctu.nengoros.demoTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import ctu.nengoros.RosRunner;
import ctu.nengoros.demoTests.nodeTesters.PublisherTestNode;
import ctu.nengoros.nodes.RosCommunicationTest;

public class PublisherTests extends RosCommunicationTest {
	
	/**
	 * Testing the subscriber:
	 * <ul>
	 * 	<li>start tested publisher and tester node</li>
	 * 	<li>obtain the tester node<li>
	 * 	<li>wait for tester to be registered and to establish the communication between nodes</li>
	 * 	<li>give them 500ms for communication</li>
	 * 	<li>shutdown nodes</li>
	 * </ul>
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

		pt.awaitCommunicationReady();
		System.out.println("Nodes registered, communication between nodes established.");

		super.sleep(500);	// wait for some communication

		assertTrue(pt.somethingReceived());

		publisherRunner.stop();
		publisherTestRunner.stop();

		assertFalse(publisherRunner.isRunning());
		assertFalse(publisherTestRunner.isRunning());
	}

}
