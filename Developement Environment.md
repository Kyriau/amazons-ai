This is a quick rundown of how to set up your development environment.

If you are using intelliJ, open the folder that is containing the repository directory as a project.
This will ensure that the configuration files for your IDE do not get pushed up to github and stay between branches.

Next, pointer IDE to use junit 4.12.
JDK v TBD

To access Gao's dependencies, you will need to setup Maven. In IntelliJ, you just need to right click pom.xml in the project viewer, select "use as Maven", and then right click again and select "Maven>Reimport".