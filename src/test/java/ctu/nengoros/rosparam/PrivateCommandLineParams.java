package ctu.nengoros.rosparam;

import static org.junit.Assert.*;

import org.junit.Test;
import org.ros.node.NodeMain;

import ctu.nengoros.RosRunner;
import ctu.nengoros.nodes.RosCommunicationTest;
import ctu.nengoros.rosparam.node.DummyNode;

public class PrivateCommandLineParams extends RosCommunicationTest{

	RosRunner r;
	public static String dummy = "ctu.nengoros.rosparam.node.DummyNode";
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

		RosRunner rr = super.runNode(new String[]{dummy});
		assertTrue(rr.isRunning());

		DummyNode d = (DummyNode)rr.getNode();

		// check running node: no params, default name, no namespace
		assertTrue(d.getNumPrivateParams() == 0);
		assertTrue(d.getAbsoluteName().toString().equalsIgnoreCase("/"+DummyNode.DEFAULT_NAME));
		assertTrue(d.getBaseName().toString().equalsIgnoreCase(DummyNode.DEFAULT_NAME));
		assertTrue(d.getNamespace().toString().equalsIgnoreCase("/"));

		// check parameter server for no params
		assertTrue(j.processCommand("list").equalsIgnoreCase(Jrosparam.listEmpty));
		assertTrue(j.processCommand("numparams").equalsIgnoreCase("0"));

		rr.stop();
		assertFalse(rr.isRunning());
		r.stop();
		assertFalse(r.isRunning());
	}

	@Test
	public void name(){
		Jrosparam j = startJrosparam();
		assertTrue(r.isRunning());

		RosRunner rr = super.runNode(new String[]{dummy, "__name:=testName"});
		assertTrue(rr.isRunning());

		DummyNode d = (DummyNode)rr.getNode();

		// no params, default name, no namespace
		assertTrue(d.getNumPrivateParams() == 0);
		assertTrue(d.getAbsoluteName().toString().equalsIgnoreCase("/testName"));
		assertTrue(d.getBaseName().toString().equalsIgnoreCase("testName"));
		assertTrue(d.getNamespace().toString().equalsIgnoreCase("/"));

		// check parameter server
		assertTrue(j.processCommand("list").equalsIgnoreCase(Jrosparam.listEmpty));
		assertTrue(j.processCommand("numparams").equalsIgnoreCase("0"));

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
				new String[]{dummy, "__name:=ns/testName","__ns:=namespace"});
		assertTrue(rr.isRunning());

		DummyNode d = (DummyNode)rr.getNode();

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
				new String[]{dummy, 
						"__name:=testName",
						"__ns:=namespace",
						"_integerA:=10",
						"_stringB:=hello",
						"_floatC:=10.0357",
				"_booleanD:=false"});

		assertTrue(rr.isRunning());

		DummyNode d = (DummyNode)rr.getNode();

		// no params, default name, no namespace
		assertTrue(d.getNumPrivateParams() == 4);
		assertTrue(d.getAbsoluteName().toString().equalsIgnoreCase("/namespace/testName"));
		assertTrue(d.getBaseName().toString().equalsIgnoreCase("testName"));
		// not sure whether this should work like this, but it does:
		assertTrue(d.getNamespace().toString().equalsIgnoreCase("/namespace"));

		// check parameter server
		assertTrue(j.processCommand("numparams").equalsIgnoreCase("4"));
		assertTrue(j.processCommand(new String[]{"get","/namespace/testName/integerA"}).equalsIgnoreCase("10"));
		assertTrue(j.processCommand(new String[]{"get","/namespace/testName/stringB"}).equalsIgnoreCase("hello"));
		assertTrue(j.processCommand(new String[]{"get","/namespace/testName/floatC"}).equalsIgnoreCase("10.0357"));
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
	 * Pass the namespace in the node name (also works)
	 */
	@Test
	public void nameSpaceDifferent(){
		Jrosparam j = startJrosparam();
		assertTrue(r.isRunning());

		RosRunner rr = super.runNode(
				new String[]{dummy, 
						"__name:=namespace/ns/fffffff/testName",
						"_integerA:=10",
						"_stringB:=hello",
						"_floatC:=10.0357",
				"_booleanD:=false"});

		assertTrue(rr.isRunning());

		DummyNode d = (DummyNode)rr.getNode();

		// no params, default name, no namespace
		assertTrue(d.getNumPrivateParams() == 4);
		assertTrue(d.getAbsoluteName().toString().equalsIgnoreCase("/namespace/ns/fffffff/testName"));
		assertTrue(d.getBaseName().toString().equalsIgnoreCase("testName"));
		assertTrue(d.getNamespace().toString().equalsIgnoreCase("/namespace/ns/fffffff"));

		// check parameter server
		assertTrue(j.processCommand("numparams").equalsIgnoreCase("4"));

		assertTrue(j.processCommand(new String[]{"get","/namespace/ns/fffffff/testName/integerA"}).equalsIgnoreCase("10"));
		assertTrue(j.processCommand(new String[]{"get","/namespace/ns/fffffff/testName/stringB"}).equalsIgnoreCase("hello"));
		assertTrue(j.processCommand(new String[]{"get","/namespace/ns/fffffff/testName/floatC"}).equalsIgnoreCase("10.0357"));
		assertTrue(j.processCommand(new String[]{"get","/namespace/ns/fffffff/testName/booleanD"}).equalsIgnoreCase("false"));

		// cannot find them like this:
		assertTrue(j.processCommand(new String[]{"get","booleanD"}).equalsIgnoreCase(Jrosparam.notFound));

		j.processCommand(new String[]{"set","/namespace/xyz","10"});

		// also, all parameters from the namespace can be deleted!
		j.processCommand(new String[]{"delete","/namespace/"});
		assertTrue(j.processCommand("list").equalsIgnoreCase(Jrosparam.listEmpty));

		rr.stop();
		assertFalse(rr.isRunning());
		r.stop();
		assertFalse(r.isRunning());
	}

	@Test
	public void getParamsByNode(){
		Jrosparam j = startJrosparam();
		assertTrue(r.isRunning());

		RosRunner rr = super.runNode(
				new String[]{dummy, 
						"__name:=namespace/ns/testName",
						"_integerA:=10",
						"_stringB:=hello",
						"_floatC:=10.0357",
				"_booleanD:=false"});

		assertTrue(rr.isRunning());

		DummyNode d = (DummyNode)rr.getNode();

		// no params, default name, no namespace
		assertTrue(d.getNumPrivateParams() == 4);
		assertTrue(d.getAbsoluteName().toString().equalsIgnoreCase("/namespace/ns/testName"));
		assertTrue(d.getBaseName().toString().equalsIgnoreCase("testName"));
		assertTrue(d.getNamespace().toString().equalsIgnoreCase("/namespace/ns"));

		// check parameter server
		assertTrue(j.processCommand("numparams").equalsIgnoreCase("4"));

		assertTrue(j.processCommand(new String[]{"get","/namespace/ns/testName/integerA"}).equalsIgnoreCase("10"));
		assertTrue(j.processCommand(new String[]{"get","/namespace/ns/testName/stringB"}).equalsIgnoreCase("hello"));
		assertTrue(j.processCommand(new String[]{"get","/namespace/ns/testName/floatC"}).equalsIgnoreCase("10.0357"));
		assertTrue(j.processCommand(new String[]{"get","/namespace/ns/testName/booleanD"}).equalsIgnoreCase("false"));

		// cannot find them like this:
		assertTrue(j.processCommand(new String[]{"get","booleanD"}).equalsIgnoreCase(Jrosparam.notFound));

		j.processCommand(new String[]{"set","/namespace/xyz","10"});

		// also, all parameters from the namespace can be deleted!
		j.processCommand(new String[]{"delete","/namespace/"});
		assertTrue(j.processCommand("list").equalsIgnoreCase(Jrosparam.listEmpty));

		rr.stop();
		assertFalse(rr.isRunning());
		r.stop();
		assertFalse(r.isRunning());
	}


}
