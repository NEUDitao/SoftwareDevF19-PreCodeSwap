

# We are Implementing a Game of Tsuro

The specifications and rules for the game of Tsuro can be found [here](https://ccs.neu.edu/home/matthias/4500-f19/tsuro.html). 

# Folders
```
 _    _  ___  ______ _   _ _____ _   _ _____ 
| |  | |/ _ \ | ___ \ \ | |_   _| \ | |  __ \
| |  | / /_\ \| |_/ /  \| | | | |  \| | |  \/
| |/\| |  _  ||    /| . ` | | | | . ` | | __ 
\  /\  / | | || |\ \| |\  |_| |_| |\  | |_\ \
 \/  \/\_| |_/\_| \_\_| \_/\___/\_| \_/\____/
                                          

```
                                                    

 ### Planning
  - Planning contains all of the documents describing how we
  designed our implementation of Tsuro. Each .md file describes a different initial specification of components. 

### Common
  - Common contains shared code for our implementation. 

  - metainfs/ contains metadata for building JARs. It can be ignored

  - junit-platform-console-standalone-1.5.2/jar is a Java executable from JUnit that runs JUnit tests in the command line. See the test section for more details.

  - test/ contains all of the Unit Tests for the TsuroBoard implementation. Package structure mimics src/'s

  - lib/ contains external libraries used in our implementation. It can be ignored when browsing source code

  - out/ contains the artifacts created from building our game into Java executables. There should be an easier way to always run programs from the top-level directory, so out/ can be ignored.

##### src
  src/ contains the source code for Tsuro

  - Within src/com/tsuro are the separate packages for a game of Tsuro
    - action/ contains the implementation for actions that can be taken on the board 
      - actionpat/ contains JSON deserializers for action-pats
    - board/ contains the implementation of the board
      - statepat/ contains JSON deserializers to construct a board from JSON
    - observer/ contains the interface for observers
      - observer/robserver contains a test harness for observers
    - tile/ contains the implementation of tiles and classes for interacting with them
      - /tile/tiletypes contains files for processing JSON  representation of Tiles, and querying them
      - /tile/tiletypes/tsuro-tiles-index.json contains all of the indices that we will be referring to Tiles by, as well as their connections
    - rulechecker/ contains the implementations for a rules component
      - xrules/ contains a test harness for rulecheckers
    - action/ contains the implementations for an action component
    - player/ contains the classes for players to use
    - referee/ contains a strategy for referees to use when choosing tiles
    - strategy/ contains the interface for players to use when creating strategies for themselves
    - utils/ contains utility classes for different parts of Tsuro to use

  ### Admin
  Admin/ contains code for a referee in Tsuro
  
  ### Player
  Player/ contains code for a player component in Tsuro
  
  ### .idea
  - .idea includes some defaults for the editor we chose to use, IntelliJ. Feel free
  to ignore it.
  
  

# Project progress

  - Analyzing tiles to find all 35 possible rotations. Code can be found at Common/src/com/tsuro/tile/Tiles.java
  - An implementation for a Board of a game of Tsuro is now done. Code can be found at Common/src/com/tsuro/board
  - Rulecheckers have been implemented, check them out at Common/src/com/tsuro/rules
  - Observers for players have been implemented, check them out Common/src/com/tsuro/observer
  - 10/22/19, Referees and Players have been implemented. Referees can be found at Admin/referee.java. Players can be found at Player/player.java.

## Tests

  - To run all tests, do ./Test in the Tsuro Directory. This will run all JUnit
    tests
    
    
# Notes on Libraries
  - For JSON Parsing, we use [GSON](https://sites.google.com/site/gson/gson-user-guide)
  - For shortening boilerplate, we use [Lombok](https://projectlombok.org)
  - To extend Java, we use [Guava](https://opensource.google/projects/guava)