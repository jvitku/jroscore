package ctu.nengoros.infrastructure;

import static org.junit.Assert.*;

import org.junit.Test;

import ctu.nengoros.RosRunner;
import ctu.nengoros.network.common.exceptions.StartupDelayException;
import ctu.nengoros.network.node.infrastructure.simulation.testnodes.ConfigurableHannsNode;
import ctu.nengoros.network.node.infrastructure.simulation.testnodes.SimulationControlsNode;
import ctu.nengoros.network.node.testsuit.RosCommunicationTest;

public class SImulationMasterReset extends RosCommunicationTest{

	/**
	 * Start master, slave, master requests hard and soft reset, check slave. 
	 */
	@Test
	public void testReset(){
		RosRunner masterNode =super.runNode(
				"ctu.nengoros.network.node.infrastructure.simulation.testnodes.SimulationControlsNode");		
		RosRunner slaveNode =super.runNode(
				"ctu.nengoros.network.node.infrastructure.simulation.testnodes.ConfigurableHannsNode");

		SimulationControlsNode master = null;
		ConfigurableHannsNode node = null;

		if(!(masterNode.getNode() instanceof SimulationControlsNode))
			fail("Wrong class launched");
		master = (SimulationControlsNode)masterNode.getNode();

		if(!(slaveNode.getNode() instanceof ConfigurableHannsNode))
			fail("Wrong class launched");
		node = (ConfigurableHannsNode)slaveNode.getNode();

		try {
			node.awaitStarted();
			master.awaitStarted();
		} catch (StartupDelayException e1) {
			e1.printStackTrace();
			System.out.println("(At least one of) nodes not started fast enough!");
			fail();
		}
		System.out.println("namesace of slave node is: "+node.getNamespace());
		
		System.out.println("ready");
		assertTrue(node.isStarted());
		assertTrue(master.isStarted());

		assertFalse(node.hardResetted);
		assertFalse(node.softResetted);

		// wait for publishers/subscribers to be operational
		sleep(100);					// TODO communicationAware startup 

		master.callHardReset(false);
		// wait for message to be delivered
		sleep(100);					// TODO use services instead of pub/sub
		assertTrue(node.hardResetted);
		assertFalse(node.softResetted);

		master.callSoftReset(false);
		sleep(100);
		assertTrue(node.hardResetted);
		assertTrue(node.softResetted);

		masterNode.stop();
		slaveNode.stop();
	}
	
	/**
	 * Reset message should be passed in relative namespace 
	 * (so ROS nodes in other namespaces remain not-reseted) 
	 */
	@Test
	public void testResetNamespaceShielding(){
		RosRunner masterNode =super.runNode(
				"ctu.nengoros.network.node.infrastructure.simulation.testnodes.SimulationControlsNode");		
		RosRunner slaveNode =super.runNode(
				new String[]{"ctu.nengoros.network.node.infrastructure.simulation.testnodes.ConfigurableHannsNode",
						"__ns:=namespace"
				});

		SimulationControlsNode master = null;
		ConfigurableHannsNode node = null;

		if(!(masterNode.getNode() instanceof SimulationControlsNode))
			fail("Wrong class launched");
		master = (SimulationControlsNode)masterNode.getNode();

		if(!(slaveNode.getNode() instanceof ConfigurableHannsNode))
			fail("Wrong class launched");
		node = (ConfigurableHannsNode)slaveNode.getNode();

		try {
			node.awaitStarted();
			master.awaitStarted();
		} catch (StartupDelayException e1) {
			e1.printStackTrace();
			System.out.println("(At least one of) nodes not started fast enough!");
			fail();
		}
		System.out.println("namesace of slave node is: "+node.getNamespace());
		
		System.out.println("ready");
		assertTrue(node.isStarted());
		assertTrue(master.isStarted());

		assertFalse(node.hardResetted);
		assertFalse(node.softResetted);

		// wait for publishers/subscribers to be operational
		sleep(100);					// TODO communicationAware startup 

		master.callHardReset(false);
		// wait for message to be delivered
		sleep(100);					// TODO use services instead of pub/sub
		assertFalse(node.hardResetted);
		assertFalse(node.softResetted);

		master.callSoftReset(false);
		sleep(100);
		assertFalse(node.hardResetted);
		assertFalse(node.softResetted);	// just check that reset has no effect here 

		masterNode.stop();
		slaveNode.stop();
	}
}
