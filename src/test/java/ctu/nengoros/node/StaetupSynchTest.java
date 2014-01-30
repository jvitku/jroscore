package ctu.nengoros.node;

import static org.junit.Assert.*;

import org.junit.Test;

import ctu.nengoros.network.common.exceptions.StartupDelayException;
import ctu.nengoros.network.node.synchedStart.impl.SyncedStart;

/**
 * Test the startup synchronization.
 *  
 * @author Jaroslav Vitku
 *
 */
public class StaetupSynchTest {

	
	@Test
	public void noChilds(){
		
		Dummy d = new Dummy("nochilds");
		d.setLogginEnabled(true);
		d.setMaxWaitTime(100);
		
		try {
			d.awaitStarted();		// should throw
			fail();
		} catch (StartupDelayException e) {
			//e.printStackTrace();
		}
		
		assertFalse(d.isStarted());
		d.setStarted();
		assertTrue(d.isStarted());
		
	}
	
	@Test
	public void noChildsAwait(){
		
		Dummy d = new Dummy("nochilds");
		d.setLogginEnabled(true);
		
		assertFalse(d.isStarted());
		d.setStarted();
		assertTrue(d.isStarted());
		
		try {
			d.awaitStarted();	// should return ok
		} catch (StartupDelayException e) {
			e.printStackTrace();
			fail();
		}
		
	}
	
	@Test
	public void oneChild(){
		
		Dummy d = new Dummy("oneChild");
		
		Dummy ch = new Dummy("child1");
		d.addChild(ch);
		
		assertFalse(d.isStarted());
		assertFalse(ch.isStarted());
		d.setStarted();			// the parent is ready, but the child is not
		assertFalse(d.isStarted());
		assertFalse(ch.isStarted());
		
		ch.setStarted();			// setting child to ready state now sets both to ready
		assertTrue(d.isStarted());
		assertTrue(ch.isStarted());
		
		assertTrue(d.isStarted());	// cannot discard the ready state
		assertTrue(ch.isStarted());	// cannot discard the ready state
	}
	
	@Test
	public void twoChilds(){
		
		Dummy d = new Dummy("oneChild");
		
		Dummy ch = new Dummy("child1");
		Dummy chh = new Dummy("child2");
		d.addChild(ch);
		d.addChild(chh);
		
		assertFalse(d.isStarted());
		assertFalse(ch.isStarted());
		assertFalse(chh.isStarted());
		
		d.setStarted();			// the parent is ready, but the child is not
		assertFalse(d.isStarted());
		assertFalse(ch.isStarted());
		assertFalse(chh.isStarted());
		
		ch.setStarted();			// all childs have to be ready
		assertFalse(d.isStarted());
		assertTrue(ch.isStarted());
		assertFalse(chh.isStarted());
		
		chh.setStarted();
		assertTrue(d.isStarted());
		assertTrue(ch.isStarted());
		assertTrue(chh.isStarted());
		
	}

	@Test
	public void twoChildsDifferentOrder(){
		
		Dummy d = new Dummy("oneChild");
		
		Dummy ch = new Dummy("child1");
		Dummy chh = new Dummy("child2");
		d.addChild(ch);
		d.addChild(chh);
		
		assertFalse(d.isStarted());
		assertFalse(ch.isStarted());
		assertFalse(chh.isStarted());

		assertFalse(d.isStarted());
		assertFalse(ch.isStarted());
		assertFalse(chh.isStarted());
		
		ch.setStarted();			// all childs have to be ready
		assertFalse(d.isStarted());
		assertTrue(ch.isStarted());
		assertFalse(chh.isStarted());
		
		chh.setStarted();
		assertFalse(d.isStarted());	// here: parent is not ready: setReady not called
		assertTrue(ch.isStarted());
		assertTrue(chh.isStarted());
		
		d.setStarted();
		assertTrue(d.isStarted());
		assertTrue(ch.isStarted());
		assertTrue(chh.isStarted());
		
	}
	
	@Test
	public void hierarchy(){
		
		Dummy d = new Dummy("parent");
		
		Dummy ch01 = new Dummy("child01");
		Dummy ch02 = new Dummy("child02");
		
		Dummy ch11 = new Dummy("child11");
		Dummy ch12 = new Dummy("child12");
		Dummy ch13 = new Dummy("child13");
		
		d.addChild(ch01);
		d.addChild(ch02);
		
		ch01.addChild(ch11);
		ch02.addChild(ch12);
		ch02.addChild(ch13);
		
		assertFalse(d.isStarted());
		assertFalse(ch01.isStarted());
		assertFalse(ch02.isStarted());
		assertFalse(ch11.isStarted());
		assertFalse(ch12.isStarted());
		assertFalse(ch13.isStarted());
		
		ch01.setStarted();
		assertFalse(d.isStarted());
		assertFalse(ch01.isStarted());
		assertFalse(ch02.isStarted());
		assertFalse(ch11.isStarted());
		assertFalse(ch12.isStarted());
		assertFalse(ch13.isStarted());
		
		ch11.setStarted();			// one branch ready
		assertFalse(d.isStarted());
		assertTrue(ch01.isStarted());	//
		assertFalse(ch02.isStarted());
		assertTrue(ch11.isStarted());	//
		assertFalse(ch12.isStarted());
		assertFalse(ch13.isStarted());
		
		d.setStarted();			// parent ready
		assertFalse(d.isStarted());
		assertTrue(ch01.isStarted());
		assertFalse(ch02.isStarted());
		assertTrue(ch11.isStarted());
		assertFalse(ch12.isStarted());
		assertFalse(ch13.isStarted());
		
		ch13.setStarted();			// parent ready
		assertFalse(d.isStarted());
		assertTrue(ch01.isStarted());
		assertFalse(ch02.isStarted());
		assertTrue(ch11.isStarted());
		assertFalse(ch12.isStarted());
		assertTrue(ch13.isStarted());	//
		
		ch12.setStarted();			// parent ready
		assertFalse(d.isStarted());
		assertTrue(ch01.isStarted());
		assertFalse(ch02.isStarted());
		assertTrue(ch11.isStarted());
		assertTrue(ch12.isStarted());
		assertTrue(ch13.isStarted());	//
		
		ch02.setStarted();			// parent ready
		assertTrue(d.isStarted());	//
		assertTrue(ch01.isStarted());
		assertTrue(ch02.isStarted());//
		assertTrue(ch11.isStarted());
		assertTrue(ch12.isStarted());
		assertTrue(ch13.isStarted());	
		
		d.setLogginEnabled(true);
		try {
			d.awaitStarted();
		} catch (StartupDelayException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	
	private class Dummy extends SyncedStart{

		private String name;
		
		public Dummy(String name){ this.name = name;	}
		
		@Override
		public String getFullName() { return this.name;		}
	}
	
	
}
