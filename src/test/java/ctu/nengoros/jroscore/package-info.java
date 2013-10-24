/**
 * Test launching the Jroscore and how to write Unite tests for ROSjava Nodes. 
 * 
 * Turns out that it is possible to launch one Jroscore for one Unit Test Class.
 * 
 * If the user needs to test two conflicting configurations, these can be 
 * divided into multiple JUnit Test Classes (executed in sequence with own Jroscores).
 * 
 * @author Jaroslav Vitku
 *
 */
package ctu.nengoros.jroscore;