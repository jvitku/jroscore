package ctu.nengoros.node;

import static org.junit.Assert.*;

import org.junit.Test;

import ctu.nengoros.network.common.exceptions.StartupDelayException;
import ctu.nengoros.network.node.synchedStart.StartupManager;
import ctu.nengoros.network.node.synchedStart.impl.BasicStartupManager;
import ctu.nengoros.network.node.synchedStart.impl.StartedObject;

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
		
		assertFalse(d.allStarted());
		d.setStarted();
		assertTrue(d.allStarted());
		
	}
	
	@Test
	public void noChildsAwait(){
		
		Dummy d = new Dummy("nochilds");
		d.setLogginEnabled(true);
		
		assertFalse(d.allStarted());
		d.setStarted();
		assertTrue(d.allStarted());
		
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
		
		assertFalse(d.allStarted());
		assertFalse(ch.allStarted());
		d.setStarted();			// the parent is ready, but the child is not
		assertFalse(d.allStarted());
		assertFalse(ch.allStarted());
		
		ch.setStarted();			// setting child to ready state now sets both to ready
		assertTrue(d.allStarted());
		assertTrue(ch.allStarted());
		
		assertTrue(d.allStarted());	// cannot discard the ready state
		assertTrue(ch.allStarted());	// cannot discard the ready state
	}
	
	@Test
	public void twoChilds(){
		
		Dummy d = new Dummy("oneChild");
		
		Dummy ch = new Dummy("child1");
		Dummy chh = new Dummy("child2");
		d.addChild(ch);
		d.addChild(chh);
		
		assertFalse(d.allStarted());
		assertFalse(ch.allStarted());
		assertFalse(chh.allStarted());
		
		d.setStarted();			// the parent is ready, but the child is not
		assertFalse(d.allStarted());
		assertFalse(ch.allStarted());
		assertFalse(chh.allStarted());
		
		ch.setStarted();			// all childs have to be ready
		assertFalse(d.allStarted());
		assertTrue(ch.allStarted());
		assertFalse(chh.allStarted());
		
		chh.setStarted();
		assertTrue(d.allStarted());
		assertTrue(ch.allStarted());
		assertTrue(chh.allStarted());
		
	}

	@Test
	public void twoChildsDifferentOrder(){
		
		Dummy d = new Dummy("oneChild");
		
		Dummy ch = new Dummy("child1");
		Dummy chh = new Dummy("child2");
		d.addChild(ch);
		d.addChild(chh);
		
		assertFalse(d.allStarted());
		assertFalse(ch.allStarted());
		assertFalse(chh.allStarted());

		assertFalse(d.allStarted());
		assertFalse(ch.allStarted());
		assertFalse(chh.allStarted());
		
		ch.setStarted();			// all childs have to be ready
		assertFalse(d.allStarted());
		assertTrue(ch.allStarted());
		assertFalse(chh.allStarted());
		
		chh.setStarted();
		assertFalse(d.allStarted());	// here: parent is not ready: setReady not called
		assertTrue(ch.allStarted());
		assertTrue(chh.allStarted());
		
		d.setStarted();
		assertTrue(d.allStarted());
		assertTrue(ch.allStarted());
		assertTrue(chh.allStarted());
		
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
		
		assertFalse(d.allStarted());
		assertFalse(ch01.allStarted());
		assertFalse(ch02.allStarted());
		assertFalse(ch11.allStarted());
		assertFalse(ch12.allStarted());
		assertFalse(ch13.allStarted());
		
		ch01.setStarted();
		assertFalse(d.allStarted());
		assertFalse(ch01.allStarted());
		assertFalse(ch02.allStarted());
		assertFalse(ch11.allStarted());
		assertFalse(ch12.allStarted());
		assertFalse(ch13.allStarted());
		
		ch11.setStarted();			// one branch ready
		assertFalse(d.allStarted());
		assertTrue(ch01.allStarted());	//
		assertFalse(ch02.allStarted());
		assertTrue(ch11.allStarted());	//
		assertFalse(ch12.allStarted());
		assertFalse(ch13.allStarted());
		
		d.setStarted();			// parent ready
		assertFalse(d.allStarted());
		assertTrue(ch01.allStarted());
		assertFalse(ch02.allStarted());
		assertTrue(ch11.allStarted());
		assertFalse(ch12.allStarted());
		assertFalse(ch13.allStarted());
		
		ch13.setStarted();			// parent ready
		assertFalse(d.allStarted());
		assertTrue(ch01.allStarted());
		assertFalse(ch02.allStarted());
		assertTrue(ch11.allStarted());
		assertFalse(ch12.allStarted());
		assertTrue(ch13.allStarted());	//
		
		ch12.setStarted();			// parent ready
		assertFalse(d.allStarted());
		assertTrue(ch01.allStarted());
		assertFalse(ch02.allStarted());
		assertTrue(ch11.allStarted());
		assertTrue(ch12.allStarted());
		assertTrue(ch13.allStarted());	//
		
		ch02.setStarted();			// parent ready
		assertTrue(d.allStarted());	//
		assertTrue(ch01.allStarted());
		assertTrue(ch02.allStarted());//
		assertTrue(ch11.allStarted());
		assertTrue(ch12.allStarted());
		assertTrue(ch13.allStarted());
		
		
		d.setLogginEnabled(true);
		try {
			d.awaitStarted();
		} catch (StartupDelayException e) {
			e.printStackTrace();
			fail();
		}
	}
	
private class Dummy implements StartedObject, StartupManager{
	private String name;
	private String x;
	StartupManager s = new BasicStartupManager(this);
	
	public Dummy(String name){ this.name = name;	}
	
	public void setStarted(){
		this.x = "something";
	}
	
	@Override
	public String getFullName() { return this.name;		}

	@Override
	public boolean isStarted() {
		return x!=null;
	}

	@Override
	public StartupManager getStartupManager() { return this.s; }

	@Override
	public void setFullName(String name) {
		s.setFullName(name);	
	}

	@Override
	public void setLogginEnabled(boolean enabled) {
		s.setLogginEnabled(enabled);
	}

	@Override
	public void awaitStarted() throws StartupDelayException {
		s.awaitStarted();
	}

	@Override
	public boolean allStarted() {
		return s.allStarted();
	}

	@Override
	public int getMaxWaitTime() {
		return s.getMaxWaitTime();
	}

	@Override
	public void setMaxWaitTime(int ms) {
		s.setMaxWaitTime(ms);
	}

	@Override
	public void addChild(StartupManager child) {
		s.addChild(child);
	}

	@Override
	public void removeChild(StartupManager child) {
		s.removeChild(child);
	}
	
}
	/*
	private class Dummy extends BasicStartupManager implements StartedObject{

		private String name;
		
		public Dummy(String name){ this.name = name;	}
		
		@Override
		public String getFullName() { return this.name;		}

		@Override
		public void setFullName(String name) { this.name =name;	}
	}*/
	
	
}
