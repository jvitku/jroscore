package ctu.nengoros.rosparam;

import static org.junit.Assert.*;

import org.junit.Test;
import org.ros.node.NodeMain;

import ctu.nengoros.RosRunner;
import ctu.nengoros.nodes.RosCommunicationTest;
import ctu.nengoros.rosparam.node.PrivateRosparamNode;

public class PrivateRosParam extends RosCommunicationTest{

	RosRunner r;
	public static String dummy = "ctu.nengoros.rosparam.node.DummyNode";
	public static String pr = "ctu.nengoros.rosparam.node.PrivateRosparamNode";
	
	public static String rp = "ctu.nengoros.rosparam.node.RosparamNode";

	/**
	 * Starts new instance of Jrosparam - commandline version 
	 * @return running instance, or the test failed already
	 */
	private Jrosparam startJrosparam(){

		try {
			r = new RosRunner(new String[]{rp});
			r.start();
			assertTrue(r.isRunning());

			NodeMain node = r.getNode();
			Jrosparam jr = new Jrosparam((RosparamInt)node);
			return jr;
		} catch (Exception e) {
			System.err.println("ERROR: could not start the rosparam node! Roscore launched??");
			fail();
		}
		return null;
	}

	@Test
	public void nosetup(){
		Jrosparam j = startJrosparam();
		assertTrue(r.isRunning());

		RosRunner rr = super.runNode(new String[]{pr});
		assertTrue(rr.isRunning());

		PrivateRosparamNode d = (PrivateRosparamNode)rr.getNode();

		// check running node: no params, default name, no namespace
		assertTrue(d.getNumPrivateParams() == 0);
		assertTrue(d.getAbsoluteName().toString().equalsIgnoreCase("/"+PrivateRosparamNode.DEFNAME));
		assertTrue(d.getBaseName().toString().equalsIgnoreCase(PrivateRosparamNode.DEFNAME));
		assertTrue(d.getNamespace().toString().equalsIgnoreCase("/"));

		// check parameter server for no params
		assertTrue(j.processCommand("list").equalsIgnoreCase(Jrosparam.listEmpty));
		assertTrue(j.processCommand("numparams").equalsIgnoreCase("0"));

		// check default values for public parameters 
		assertTrue(d.getString("nonexistend","default").equalsIgnoreCase("default"));
		assertTrue(d.getInteger("nonexistend",-11213)==-11213);
		assertTrue(d.getBoolean("nonexistend",false)==false);
		assertTrue(d.getDouble("nonexistend",0.01)==0.01);

		// check default private parameters
		System.out.println("noooon :"+d.getMyString("nonexistend","default"));
		assertTrue(d.getMyString("nonexistend","default").equalsIgnoreCase("default"));
		//TODO uncomment:
		//assertTrue(d.getMyInteger("nonexistend",-11213)==-11213);
		//assertTrue(d.getMyBoolean("nonexistend",false)==false);
		//assertTrue(d.getMyDouble("nonexistend",0.01)==0.01);

		rr.stop();
		assertFalse(rr.isRunning());
		r.stop();
		assertFalse(r.isRunning());
	}


	@Test
	public void namespace(){
		Jrosparam j = startJrosparam();
		assertTrue(r.isRunning());

		RosRunner rr = super.runNode(
				new String[]{pr, "__name:=ns/testName","__ns:=namespace"});
		assertTrue(rr.isRunning());

		PrivateRosparamNode d = (PrivateRosparamNode)rr.getNode();

		// no params, default name, no namespace
		assertTrue(d.getNumPrivateParams() == 0);
		assertTrue(d.getAbsoluteName().toString().equalsIgnoreCase("/namespace/ns/testName"));
		assertTrue(d.getBaseName().toString().equalsIgnoreCase("testName"));
		assertTrue(d.getNamespace().toString().equalsIgnoreCase("/namespace/ns"));

		// check parameter server
		assertTrue(j.processCommand("list").equalsIgnoreCase(Jrosparam.listEmpty));
		assertTrue(j.processCommand("numparams").equalsIgnoreCase("0"));
		rr.stop();
		assertFalse(rr.isRunning());
		r.stop();
		assertFalse(r.isRunning());
	}

	@Test
	public void privateParams(){
		Jrosparam j = startJrosparam();
		assertTrue(r.isRunning());

		RosRunner rr = super.runNode(
				new String[]{pr, 
						"__name:=testName",
						"__ns:=namespace",
						"_integerA:=10",
						"_stringB:=hello",
						"_doubleC:=10.0357",
				"_booleanD:=false"});

		assertTrue(rr.isRunning());

		PrivateRosparamNode d = (PrivateRosparamNode)rr.getNode();

		assertTrue(d.getNumPrivateParams() == 4);

		// string
		try {
			assertTrue(d.getMyString("stringB").equalsIgnoreCase("hello"));
		} catch (Exception e) { e.printStackTrace(); fail(); }
		
		assertTrue(d.getMyString("stringB", "default").equalsIgnoreCase("hello"));
		assertTrue(d.getMyString("stringBB", "default").equalsIgnoreCase("default"));
		
		// get the string value of my integer (stored as string from cmdline)
		try {
			assertTrue(d.getMyString("integerA").equalsIgnoreCase("10"));
		} catch (Exception e1) {
			e1.printStackTrace();
			fail();
		}
		
		// get the integer value
		try {
			assertTrue(d.getMyInteger("integerA") == 10);
		} catch (Exception e) { e.printStackTrace(); fail(); }
		
		assertTrue(d.getMyInteger("integerA", 111) == 10);
		assertTrue(d.getMyInteger("stringBB", 111) == 111);
		
		// boolean
		try {
			assertTrue(d.getMyBoolean("booleanD") == false);
		} catch (Exception e) { e.printStackTrace(); fail(); }
		
		assertTrue(d.getMyBoolean("booleanD", true) == false);
		assertTrue(d.getMyBoolean("stringBB", true) == true);
		
		// double
		try {
			assertTrue(d.getMyDouble("doubleC") == 10.0357);
		} catch (Exception e) { e.printStackTrace(); fail(); }
		
		assertTrue(d.getMyDouble("doubleC", 11.001) == 10.0357);
		assertTrue(d.getMyDouble("doubleCC", 11.001) == 11.001);
		
		
		assertTrue(d.getAbsoluteName().toString().equalsIgnoreCase("/namespace/testName"));
		assertTrue(d.getBaseName().toString().equalsIgnoreCase("testName"));
		// not sure whether this should work like this, but it does:
		assertTrue(d.getNamespace().toString().equalsIgnoreCase("/namespace"));

		// check parameter server
		assertTrue(j.processCommand("numparams").equalsIgnoreCase("4"));
		assertTrue(j.processCommand(new String[]{"get","/namespace/testName/integerA"}).equalsIgnoreCase("10"));
		assertTrue(j.processCommand(new String[]{"get","/namespace/testName/stringB"}).equalsIgnoreCase("hello"));
		assertTrue(j.processCommand(new String[]{"get","/namespace/testName/doubleC"}).equalsIgnoreCase("10.0357"));
		assertTrue(j.processCommand(new String[]{"get","/namespace/testName/booleanD"}).equalsIgnoreCase("false"));

		// cannot find them like this:
		assertTrue(j.processCommand(new String[]{"get","booleanD"}).equalsIgnoreCase(Jrosparam.notFound));

		// parameters can be deleted all at once!
		j.processCommand(new String[]{"delete","/namespace/testName/"});
		assertTrue(j.processCommand("list").equalsIgnoreCase(Jrosparam.listEmpty));


		rr.stop();
		assertFalse(rr.isRunning());
		r.stop();
		assertFalse(r.isRunning());
	}
	
	/**
	 * The same with different namespace
	 */
	@Test
	public void nameSpaceDifferent(){
		Jrosparam j = startJrosparam();
		assertTrue(r.isRunning());

		RosRunner rr = super.runNode(
				new String[]{pr, 
						"__name:=namespace/ns/fffffff/testName",
						"_integerA:=10",
						"_stringB:=hello",
						"_doubleC:=10.0357",
				"_booleanD:=false"});

		assertTrue(rr.isRunning());

		PrivateRosparamNode d = (PrivateRosparamNode)rr.getNode();

		assertTrue(d.getNumPrivateParams() == 4);

		// string
		try {
			assertTrue(d.getMyString("stringB").equalsIgnoreCase("hello"));
		} catch (Exception e) { e.printStackTrace(); fail(); }
		
		assertTrue(d.getMyString("stringB", "default").equalsIgnoreCase("hello"));
		assertTrue(d.getMyString("stringBB", "default").equalsIgnoreCase("default"));
		
		// get the string value of my integer (stored as string from cmdline)
		try {
			assertTrue(d.getMyString("integerA").equalsIgnoreCase("10"));
		} catch (Exception e1) {
			e1.printStackTrace();
			fail();
		}
		
		// get the integer value
		try {
			assertTrue(d.getMyInteger("integerA") == 10);
		} catch (Exception e) { e.printStackTrace(); fail(); }
		
		assertTrue(d.getMyInteger("integerA", 111) == 10);
		assertTrue(d.getMyInteger("stringBB", 111) == 111);
		
		// boolean
		try {
			assertTrue(d.getMyBoolean("booleanD") == false);
		} catch (Exception e) { e.printStackTrace(); fail(); }
		
		assertTrue(d.getMyBoolean("booleanD", true) == false);
		assertTrue(d.getMyBoolean("stringBB", true) == true);
		
		// double
		try {
			assertTrue(d.getMyDouble("doubleC") == 10.0357);
		} catch (Exception e) { e.printStackTrace(); fail(); }
		
		assertTrue(d.getMyDouble("doubleC", 11.001) == 10.0357);
		assertTrue(d.getMyDouble("doubleCC", 11.001) == 11.001);
		
		
		assertTrue(d.getAbsoluteName().toString().equalsIgnoreCase("/namespace/ns/fffffff/testName"));
		assertTrue(d.getBaseName().toString().equalsIgnoreCase("testName"));
		// not sure whether this should work like this, but it does:
		assertTrue(d.getNamespace().toString().equalsIgnoreCase("/namespace/ns/fffffff"));

		// check parameter server
		assertTrue(j.processCommand("numparams").equalsIgnoreCase("4"));
		assertTrue(j.processCommand(new String[]{"get","/namespace/ns/fffffff/testName/integerA"}).equalsIgnoreCase("10"));
		assertTrue(j.processCommand(new String[]{"get","/namespace/ns/fffffff/testName/stringB"}).equalsIgnoreCase("hello"));
		assertTrue(j.processCommand(new String[]{"get","/namespace/ns/fffffff/testName/doubleC"}).equalsIgnoreCase("10.0357"));
		assertTrue(j.processCommand(new String[]{"get","/namespace/ns/fffffff/testName/booleanD"}).equalsIgnoreCase("false"));

		// cannot find them like this:
		assertTrue(j.processCommand(new String[]{"get","booleanD"}).equalsIgnoreCase(Jrosparam.notFound));

		// parameters can be deleted all at once!
		j.processCommand(new String[]{"delete","/namespace/ns/fffffff/testName/"});
		assertTrue(j.processCommand("list").equalsIgnoreCase(Jrosparam.listEmpty));


		rr.stop();
		assertFalse(rr.isRunning());
		r.stop();
		assertFalse(r.isRunning());
	}
	
	/**
	 * The same with no namespace
	 */
	@Test
	public void noNameSpace(){
		Jrosparam j = startJrosparam();
		assertTrue(r.isRunning());

		RosRunner rr = super.runNode(
				new String[]{pr, 
						"__name:=testName",
						"_integerA:=10",
						"_stringB:=hello",
						"_doubleC:=10.0357",
				"_booleanD:=false"});

		assertTrue(rr.isRunning());

		PrivateRosparamNode d = (PrivateRosparamNode)rr.getNode();

		assertTrue(d.getNumPrivateParams() == 4);

		// string
		try {
			assertTrue(d.getMyString("stringB").equalsIgnoreCase("hello"));
		} catch (Exception e) { e.printStackTrace(); fail(); }
		
		assertTrue(d.getMyString("stringB", "default").equalsIgnoreCase("hello"));
		assertTrue(d.getMyString("stringBB", "default").equalsIgnoreCase("default"));
		
		// get the string value of my integer (stored as string from cmdline)
		try {
			assertTrue(d.getMyString("integerA").equalsIgnoreCase("10"));
		} catch (Exception e1) {
			e1.printStackTrace();
			fail();
		}
		
		// get the integer value
		try {
			assertTrue(d.getMyInteger("integerA") == 10);
		} catch (Exception e) { e.printStackTrace(); fail(); }
		
		assertTrue(d.getMyInteger("integerA", 111) == 10);
		assertTrue(d.getMyInteger("stringBB", 111) == 111);
		
		// boolean
		try {
			assertTrue(d.getMyBoolean("booleanD") == false);
		} catch (Exception e) { e.printStackTrace(); fail(); }
		
		assertTrue(d.getMyBoolean("booleanD", true) == false);
		assertTrue(d.getMyBoolean("stringBB", true) == true);
		
		// double
		try {
			assertTrue(d.getMyDouble("doubleC") == 10.0357);
		} catch (Exception e) { e.printStackTrace(); fail(); }
		
		assertTrue(d.getMyDouble("doubleC", 11.001) == 10.0357);
		assertTrue(d.getMyDouble("doubleCC", 11.001) == 11.001);
		
		
		assertTrue(d.getAbsoluteName().toString().equalsIgnoreCase("/testName"));
		assertTrue(d.getBaseName().toString().equalsIgnoreCase("testName"));
		// not sure whether this should work like this, but it does:
		assertTrue(d.getNamespace().toString().equalsIgnoreCase("/"));

		// check parameter server
		assertTrue(j.processCommand("numparams").equalsIgnoreCase("4"));
		assertTrue(j.processCommand(new String[]{"get","/testName/integerA"}).equalsIgnoreCase("10"));
		assertTrue(j.processCommand(new String[]{"get","/testName/stringB"}).equalsIgnoreCase("hello"));
		assertTrue(j.processCommand(new String[]{"get","/testName/doubleC"}).equalsIgnoreCase("10.0357"));
		assertTrue(j.processCommand(new String[]{"get","/testName/booleanD"}).equalsIgnoreCase("false"));

		// cannot find them like this:
		assertTrue(j.processCommand(new String[]{"get","booleanD"}).equalsIgnoreCase(Jrosparam.notFound));

		// parameters can be deleted all at once!
		j.processCommand(new String[]{"delete","/testName"});
		assertTrue(j.processCommand("list").equalsIgnoreCase(Jrosparam.listEmpty));


		rr.stop();
		assertFalse(rr.isRunning());
		r.stop();
		assertFalse(r.isRunning());
	}
	
}
