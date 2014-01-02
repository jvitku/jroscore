package ctu.nengoros.communicationAware;

import static org.junit.Assert.*;

import org.junit.Test;

import ctu.nengoros.RosRunner;
import ctu.nengoros.nodes.RosCommunicationTest;

public class CommunicationAwareNodeTest extends RosCommunicationTest{

	/**
	 * Node which is 100% correct.
	 */
	@Test
	public void demonode() {

		RosRunner rr = super.runNode("ctu.nengoros.demoTests.nodes.DemoPublisher");
		assertTrue(rr.isRunning());

		sleep(100);

		rr.stop();
		assertFalse(rr.isRunning());
	}

	/**/
	@Test
	public void testNoTopicParticipants() {

		RosRunner rr = super.runNode("ctu.nengoros.communicationAware.CANa");
		assertTrue(rr.isRunning());

		CANa c = (CANa)rr.getNode();

		c.awaitCommunicationReady();
		assertTrue(c.isNodePrepared());

		sleep(300);

		rr.stop();
		assertFalse(rr.isRunning());
	}
	/**/
	@Test
	public void testOnePublisher() {

			RosRunner rr = super.runNode("ctu.nengoros.communicationAware.CANb");
			assertTrue(rr.isRunning());

			CANb c = (CANb)rr.getNode();

			c.awaitCommunicationReady();
			assertTrue(c.isNodePrepared());

			sleep(50);

			rr.stop();
			assertFalse(rr.isRunning());
	}
	
	
	@Test
	public void runMultipleNodesSequentially() {

		for(int i=0; i<10; i++){

			RosRunner rr = super.runNode("ctu.nengoros.communicationAware.CANb");
			assertTrue(rr.isRunning());

			CANb c = (CANb)rr.getNode();

			c.awaitCommunicationReady();
			assertTrue(c.isNodePrepared());

			sleep(50);

			rr.stop();
			assertFalse(rr.isRunning());
		}
	}
	/**/
	@Test
	public void testPublisherSubscriber() {

		RosRunner rr = super.runNode("ctu.nengoros.communicationAware.CANc");
		assertTrue(rr.isRunning());
		
		RosRunner sub = super.runNode("ctu.nengoros.demoTests.nodes.DemoSubscriber");
		assertTrue(sub.isRunning());
		
		CANc c = (CANc)rr.getNode();
		
		
		// wait for registration AND connection to the other node
		c.awaitCommunicationReady();	
		assertTrue(c.isNodePrepared());
		
		sleep(100);

		rr.stop();
		sub.stop();
		assertFalse(rr.isRunning());
		assertFalse(sub.isRunning());
	}

}
