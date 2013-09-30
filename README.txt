# author Jaroslav Vítků [vitkujar@fel.cvut.cz]

Helper for launching java-based implementation of ROS core from rosjava_core on unix-based systems. 

=========================================== Usage:
In order to launch ROS core, call:
	./jroscore [help]

	or call the Main method of the class: org.ros.Jroscore.java 

=========================================== Prerequisites:

rosjava_core compiled (e.g .branch hydro_devel)


=========================================== Installation:

To rebuild the app (jroscore script) run:
	./gradlew installApp

To install jroscore into ~/.m2/ repository, run:
	./gradlew install
	
