package ctu.nengoros.node.discreteSync;

import static org.junit.Assert.*;

import org.junit.Test;

import ctu.nengoros.network.node.discreteSync.SetOfMessages;
import ctu.nengoros.network.node.discreteSync.impl.BasicSetOfMessages;

public class BasicSetOfMessagesTest{
	
	@Test
	public void singleMessage(){
		SetOfMessages som = new BasicSetOfMessages();
		
		assertTrue(som.shouldMakeStep());
		som.resetStep();
		assertTrue(som.shouldMakeStep());
		
		// add message, after registering the message arrival, it should be ready
		som.addMessage("a", "  n");
		assertFalse(som.shouldMakeStep());
		som.registerMessageArival("a", "  n");
		assertTrue(som.shouldMakeStep());
		
		som.registerMessageArival("a", "  n");
		assertTrue(som.shouldMakeStep());
		
		som.addMessage("a", "  n");
		assertFalse(som.shouldMakeStep());
		som.registerMessageArival("a", "  n");
		assertTrue(som.shouldMakeStep());

		// test reset working?
		som.resetStep();
		assertFalse(som.shouldMakeStep());
		som.registerMessageArival("a", "  n");
		assertTrue(som.shouldMakeStep());
		
	}
	
	@Test
	public void multipleMessages(){
		SetOfMessages som = new BasicSetOfMessages();
		
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
	
	
	
}
