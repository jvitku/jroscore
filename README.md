Part of the NengoROS project - Launcher for RosCore and Rosparam Java implementations
===================================================================================

Author Jaroslav Vitku [vitkujar@fel.cvut.cz]


**Jroscore** is a helper for launching Java-based implementation of [ROS core](http://wiki.ros.org/roscore) from [rosjava_core](https://github.com/rosjava/rosjava_core) on Unix-based systems. 

**Jrospara** is a java implementation of [Rosparam](http://wiki.ros.org/rosparam).


About Nengoros
---------------

This repository is a part of Hybrid Artificial Neural Network Systems (HANNS) project (see: [Nengoros pages](http://nengoros.wordpress.com) or [our research](http://artificiallife.co.nf/) ). 

Each node can be connected into a potentially heterogeneous network of nodes communicating via the [ROS](http://wiki.ros.org/), potentially [Nengoros](http://nengoros.wordpress.com). Jrosparam does not support all functions of the original rosparam (mainly handling files) so far.


Jroscore Usage 
--------------

1. To compile and install the project, run the script:
	
		./gradlew installApp
	
	which builds the project and copies all libraries into the `build/install/jroscore/lib/` directory. 

2. To run the RosCore, run the script:
		
		./jroscore

It is recommended to add this script to the `$PATH`.


Jrosparam Usage
---------------

1. To compile and install the project, run the script:
	
		./gradlew installApp
	
	which builds the project and copies all libraries into the `build/install/jrosparam/lib/` directory. 

2. To run the Jrosparam, run the script:
		
		./jrosparam

It is recommended to add this script to the `$PATH`.


Requirements
------------------

The best way how to install the Jroscore is to use them as a part of the [Nengoros](https://github.com/jvitku/nengoros) project. 

But, since this package depends only on **OUR VERSION** of [rosjava_core](https://github.com/jvitku/rosjava_core), it can be used without Nengoros.


Changelog
------------------


### V0.0.4

* Adds to the SynchronousService the support of resending the request if not processed in a given time. This can be turned on, but it is unused by default, as the Vivae simulator tends to load map several times on some computers.


Troubleshooting
------------------

If the `gradlew` script is unable to link all dependencies at time, it may be necessary to rerun the script with these flags:

	./gradlew installApp --refresh dependencies
	