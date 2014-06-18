/**
 * Demo/test of synchronization (event-driven computation) of ROS nodes.
 */
/**
 * This demo serves as simple tutorial showing how to use the {@link PartiallyOrderedSetOfMessages}. 
 * Two ROS nodes send input data, each on own topic with own frequency. But the receiving node needs
 * two data samples in order to implement a computation and publish the result. 
 *  
 * In order to run the synchronization demo, run the following from the jroscore folder:
 * <ul>
 * <li>jroscore</li>
 * <li>./runner ctu.nengoros.demoTests.setOfMessages.EventDrivenSubscriber</li>
 * <li>./runner ctu.nengoros.demoTests.setOfMessages.DelayPublisher _period:=20 _topic:=topicA __name:=nodeA</li>
 * <li>./runner ctu.nengoros.demoTests.setOfMessages.DelayPublisher _period:=2000 _topic:=topicB __name:=nodeB</li>
 * </ul>
 * 
 * @author Jaroslav Vitku
 */
package ctu.nengoros.demoTests.setOfMessages;