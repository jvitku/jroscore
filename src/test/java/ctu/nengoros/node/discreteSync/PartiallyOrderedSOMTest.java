package ctu.nengoros.node.discreteSync;

import static org.junit.Assert.*;

import org.junit.Test;

import ctu.nengoros.network.node.discreteSync.NengoROSSync;
import ctu.nengoros.network.node.discreteSync.PartiallyOrderedSet;
import ctu.nengoros.network.node.discreteSync.SetOfMessages;
import ctu.nengoros.network.node.discreteSync.impl.PartiallyOrderedSetOfMessages;

/**
 * Test ordering constraints in the set of messages.
 * 
 * @author Jaroslav Vitku
 */
public class PartiallyOrderedSOMTest {

	/**
	 * Should be capable of the same functions as the SetOfMessages
	 */
	@Test
	public void multipleMessages(){
		SetOfMessages som = new PartiallyOrderedSetOfMessages();

		assertTrue(som.shouldMakeStep());
		som.resetStep();
		assertTrue(som.shouldMakeStep());

		som.addMessage("a", "  n");
		assertFalse(som.shouldMakeStep());
		som.registerMessageArival("a", "  n");
		assertTrue(som.shouldMakeStep());

		som.addMessage("a", "  nn");
		assertFalse(som.shouldMakeStep());
		som.registerMessageArival("a", "  nn");
		assertTrue(som.shouldMakeStep());

		som.resetStep();
		assertFalse(som.shouldMakeStep());
		som.registerMessageArival("a", "  nn");
		assertFalse(som.shouldMakeStep());
		som.registerMessageArival("a", "  n");
		assertTrue(som.shouldMakeStep());
	}


	@Test
	public void oneConstraint(){
		PartiallyOrderedSet som = new PartiallyOrderedSetOfMessages();

		som.addMessage("a", "a");
		som.addMessage("a", "b");

		// try to create constraint to non-existing message 
		try {
			som.addConstraint("a", "a", "a", "bb");
			fail();
		} catch (NengoROSSync e) {}

		// add a valid constraint
		try {
			som.addConstraint("a", "a", "a", "b");
		} catch (NengoROSSync e) {
			e.printStackTrace();
			fail();
		}

		// should not make step now and after reset
		assertFalse(som.shouldMakeStep());
		som.resetStep();
		assertFalse(som.shouldMakeStep());

		// messages received in correct order 
		som.registerMessageArival("a", "a");
		assertFalse(som.shouldMakeStep());
		som.registerMessageArival("a", "b");
		assertTrue(som.shouldMakeStep());

		som.resetStep();
		assertFalse(som.shouldMakeStep());

		// incorrect order
		som.registerMessageArival("a", "b");
		assertFalse(som.shouldMakeStep());
		som.registerMessageArival("a", "a");
		assertFalse(som.shouldMakeStep());

		// correct order..
		som.registerMessageArival("a", "b");
		assertTrue(som.shouldMakeStep());

		som.resetStep();
		assertFalse(som.shouldMakeStep());
	}

	@Test
	public void simpleCircularConstraint(){
		PartiallyOrderedSet som = new PartiallyOrderedSetOfMessages();

		som.addMessage("a", "a");
		som.addMessage("a", "b");
		som.addMessage("a", "x");
		som.addMessage("a", "c");

		try {
			som.addConstraint("a", "a", "a", "a");
			fail();
		} catch (NengoROSSync e1){
			System.out.println(e1);
		}
	}
	

	@Test
	public void circularConstraintDepthOne(){
		PartiallyOrderedSet som = new PartiallyOrderedSetOfMessages();

		som.addMessage("a", "a");
		som.addMessage("b", "b");

		try {
			som.addConstraint("a", "a", "b", "b");
		} catch (NengoROSSync e1){
			fail();
		}
		
		try {
			som.addConstraint("b", "b", "a", "a");
			fail();
		} catch (NengoROSSync e1){
			System.out.println(e1);
		}
	}

}
