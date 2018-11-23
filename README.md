# quoridor
The game Quoridor that I built in Java using Maven
See: https://en.wikipedia.org/wiki/Quoridor for the game's rules
The project was built on Ubuntu 18.04.1 LTS in Eclipse v4.9.0 using JDK v10.0.2 and bult using Maven v3.5.4.

AS OF OCTOBER 23, 2018:
The game can be played by 2 or 4 human players by interacting with the command line.  
See: https://en.wikipedia.org/wiki/Quoridor#Notation for information on how move strings are expected
to be formed.

POSSIBLE EXTENSIONS:

1: Adding each player's remaining walls to the UI

2: Ability to play the game by clicking on the UI instead of only through the command line

3: An AI that can play against any number of human players or other AI players

TO RUN:

1: To build the project, enter the directory containing the pom.xml file and type: 'mvn package'

2: To play the game, type: 

'java -cp target/santanjm-quoridor-1.0-SNAPSHOT.jar santanjm.quoridor.Quoridor \<number of players\>'
  
where '\<number of players\>' is either 2 or 4
  
3: Follow the instructions on the command line to play
