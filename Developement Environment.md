This is a quick rundown of how to set up your development environment.

If you are using intelliJ, open the folder that is containing the repository directory as a project.
This will ensure that the configuration files for your IDE do not get pushed up to github and stay between branches.

Next, pointer IDE to use junit 4.12.
JDK v TBD

Maven:


To access Gao's dependencies, you will need to setup Maven. In IntelliJ, you just need to right click pom.xml in the project viewer, select "use as Maven", and then right click again and select "Maven>Reimport".

To add an Agent:
1. Write the subclass
2. Add your new Agent to getAgentList() and parseAgent()

To Run 2 Clients:
1. Run > Edit Configurations
2. Top right corner checkbox "single instance": should be unchecked

To use TextClient:
1. Run the main method in Client
2. Login to Gao's server (using whatever username/password you want)
3. Select the Agent to use with the 'agent' command
4. Select the time to spend per move with the 'timer' command
5. Join a room on Gao's server using the 'join' command
6. Once the room has 2 players in it, the game will immediately start
