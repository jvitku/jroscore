package ctu.nengoros.node;

import static org.junit.Assert.*;

import org.junit.Test;

import ctu.nengoros.network.node.synchedStart.impl.StartedObject;
import ctu.nengoros.network.node.synchedStart.impl.BasicStartupManager;

public class StartupManagerTest {
	
	@Test
	public void manager(){
		
		Node n = new Node("parent");
		Node c = new Node("child");
		
		n.getStartupManager().addChild(c.getStartupManager());// define the hierarchy
		
		assertFalse(n.isStarted());
		assertFalse(n.getStartupManager().allStarted());
		assertFalse(c.isStarted());
		assertFalse(c.getStartupManager().allStarted());	// nobody ready
		
		c.setup();
		
		assertFalse(n.isStarted());
		assertFalse(n.getStartupManager().allStarted());
		assertTrue(c.isStarted());							// 
		assertTrue(c.getStartupManager().allStarted());		// c and all his childs ready
		
		n.setup();

		assertTrue(n.isStarted());
		assertTrue(n.getStartupManager().allStarted());		// all happy, hierarchy can be used now
		assertTrue(c.isStarted());						 
		assertTrue(c.getStartupManager().allStarted());
	}
	
	
	/**
	 * Equivalent of the AbstractHannsNode
	 * 
	 * @author Jaroslav Vitku
	 */
	private abstract class Obj implements StartedObject{
		
		protected BasicStartupManager s = new BasicStartupManager(this);
		
	}
	
	/**
	 * Equivalent of some implementation of HannsNode (e.g. RL)
	 * 
	 * @author Jaroslav Vitku
	 */
	private class Node extends Obj{

		private String x;
		private String name;
		
		public Node(String name){
			this.name = name;
		}
		
		public void setup(){
			x = "hello";
		}
		
		/**
		 * Provide information that the object is setup
		 */
		@Override
		public boolean isStarted() {
			return this.x !=null;
		}

		@Override
		public BasicStartupManager getStartupManager() { return this.s; }

		@Override
		public String getFullName() {
			return this.name;	
		}
		
	}

}
