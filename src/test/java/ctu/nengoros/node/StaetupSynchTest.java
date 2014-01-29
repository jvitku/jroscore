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
			d.awaitReady();		// should throw
			fail();
		} catch (StartupDelayException e) {
			//e.printStackTrace();
		}
		
		assertFalse(d.isReady());
		d.setReady(true);
		assertTrue(d.isReady());
		
		d.setReady(false);
		assertTrue(d.isReady());	// cannot discard the ready state
	}
	
	@Test
	public void noChildsAwait(){
		
		Dummy d = new Dummy("nochilds");
		d.setLogginEnabled(true);
		
		assertFalse(d.isReady());
		d.setReady(true);
		assertTrue(d.isReady());
		
		try {
			d.awaitReady();	// should return ok
		} catch (StartupDelayException e) {
			e.printStackTrace();
			fail();
		}
		
		d.setReady(false);
		assertTrue(d.isReady());	// cannot discard the ready state
	}
	
	@Test
	public void oneChild(){
		
		Dummy d = new Dummy("oneChild");
		
		Dummy ch = new Dummy("child1");
		d.addChild(ch);
		
		assertFalse(d.isReady());
		assertFalse(ch.isReady());
		d.setReady(true);			// the parent is ready, but the child is not
		assertFalse(d.isReady());
		assertFalse(ch.isReady());
		
		ch.setReady(true);			// setting child to ready state now sets both to ready
		assertTrue(d.isReady());
		assertTrue(ch.isReady());
		
		d.setReady(false);
		ch.setReady(false);
		assertTrue(d.isReady());	// cannot discard the ready state
		assertTrue(ch.isReady());	// cannot discard the ready state
	}
	
	@Test
	public void twoChilds(){
		
		Dummy d = new Dummy("oneChild");
		
		Dummy ch = new Dummy("child1");
		Dummy chh = new Dummy("child2");
		d.addChild(ch);
		d.addChild(chh);
		
		assertFalse(d.isReady());
		assertFalse(ch.isReady());
		assertFalse(chh.isReady());
		
		d.setReady(true);			// the parent is ready, but the child is not
		assertFalse(d.isReady());
		assertFalse(ch.isReady());
		assertFalse(chh.isReady());
		
		ch.setReady(true);			// all childs have to be ready
		assertFalse(d.isReady());
		assertTrue(ch.isReady());
		assertFalse(chh.isReady());
		
		chh.setReady(true);
		assertTrue(d.isReady());
		assertTrue(ch.isReady());
		assertTrue(chh.isReady());
		
		d.setReady(false);
		chh.setReady(false);
		ch.setReady(false);
		assertTrue(d.isReady());	// cannot discard the ready state
		assertTrue(ch.isReady());	// cannot discard the ready state
		assertTrue(chh.isReady());	// cannot discard the ready state
	}

	@Test
	public void twoChildsDifferentOrder(){
		
		Dummy d = new Dummy("oneChild");
		
		Dummy ch = new Dummy("child1");
		Dummy chh = new Dummy("child2");
		d.addChild(ch);
		d.addChild(chh);
		
		assertFalse(d.isReady());
		assertFalse(ch.isReady());
		assertFalse(chh.isReady());

		assertFalse(d.isReady());
		assertFalse(ch.isReady());
		assertFalse(chh.isReady());
		
		ch.setReady(true);			// all childs have to be ready
		assertFalse(d.isReady());
		assertTrue(ch.isReady());
		assertFalse(chh.isReady());
		
		chh.setReady(true);
		assertFalse(d.isReady());	// here: parent is not ready: setReady not called
		assertTrue(ch.isReady());
		assertTrue(chh.isReady());
		
		d.setReady(true);
		assertTrue(d.isReady());
		assertTrue(ch.isReady());
		assertTrue(chh.isReady());
		
		d.setReady(false);
		chh.setReady(false);
		ch.setReady(false);
		assertTrue(d.isReady());	// cannot discard the ready state
		assertTrue(ch.isReady());	// cannot discard the ready state
		assertTrue(chh.isReady());	// cannot discard the ready state
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
		
		assertFalse(d.isReady());
		assertFalse(ch01.isReady());
		assertFalse(ch02.isReady());
		assertFalse(ch11.isReady());
		assertFalse(ch12.isReady());
		assertFalse(ch13.isReady());
		
		ch01.setReady(true);
		assertFalse(d.isReady());
		assertFalse(ch01.isReady());
		assertFalse(ch02.isReady());
		assertFalse(ch11.isReady());
		assertFalse(ch12.isReady());
		assertFalse(ch13.isReady());
		
		ch11.setReady(true);			// one branch ready
		assertFalse(d.isReady());
		assertTrue(ch01.isReady());	//
		assertFalse(ch02.isReady());
		assertTrue(ch11.isReady());	//
		assertFalse(ch12.isReady());
		assertFalse(ch13.isReady());
		
		d.setReady(true);			// parent ready
		assertFalse(d.isReady());
		assertTrue(ch01.isReady());
		assertFalse(ch02.isReady());
		assertTrue(ch11.isReady());
		assertFalse(ch12.isReady());
		assertFalse(ch13.isReady());
		
		ch13.setReady(true);			// parent ready
		assertFalse(d.isReady());
		assertTrue(ch01.isReady());
		assertFalse(ch02.isReady());
		assertTrue(ch11.isReady());
		assertFalse(ch12.isReady());
		assertTrue(ch13.isReady());	//
		
		ch12.setReady(true);			// parent ready
		assertFalse(d.isReady());
		assertTrue(ch01.isReady());
		assertFalse(ch02.isReady());
		assertTrue(ch11.isReady());
		assertTrue(ch12.isReady());
		assertTrue(ch13.isReady());	//
		
		ch02.setReady(true);			// parent ready
		assertTrue(d.isReady());	//
		assertTrue(ch01.isReady());
		assertTrue(ch02.isReady());//
		assertTrue(ch11.isReady());
		assertTrue(ch12.isReady());
		assertTrue(ch13.isReady());	
		
		d.setLogginEnabled(true);
		try {
			d.awaitReady();
		} catch (StartupDelayException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	
	private class Dummy extends SyncedStart{

		private String name;
		
		public Dummy(String name){ this.name = name;	}
		
		@Override
		public String getName() { return this.name;		}
	}
	
	
}
