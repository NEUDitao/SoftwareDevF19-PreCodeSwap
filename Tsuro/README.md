

# We are Implementing a Game of Tsuro

The specifications and rules for the game of Tsuro can be found [here](https://ccs.neu.edu/home/matthias/4500-f19/tsuro.html). 

# Folders

  - The directory Planning contains all of the documents describing how we
  designed our implementation of Tsuro. Each .md file describes a different initial specification of components

  - The directory Common contains all of the code for our implementation

    - Within Common are the src/, test/, out/, and lib/ directories.

      - metainfs/ contains metadata for building JARs. It can be ignored

      - junit-platform-console-standalone-1.5.2/jar is a Java executable from JUnit that runs JUnit tests in the command line. See the test section for more details.

      - test/ contains all of the Unit Tests for the TsuroBoard implementation. Package structure mimics src/'s

      - lib/ contains external libraries used in our implementation. It can be ignored when browsing source code

      - out/ contains the artifacts created from building our game into Java executables. There should be an easier way to always run programs from the top-level directory, so out/ can be ignored.

      - src/ contains the source code for Tsuro
        - Within src/com/tsuro are the separate packages for a game of Tsuro
          - board/ contains the implementation of the board
          - tile/ contains the implementation of tiles and classes for interacting with them
            - /tile/tiletypes contains files for processing JSON  representation of Tiles, and querying them.
            - /tile/tiletypes/tsuro-tiles-index.json contains all of the indices that we will be referring to Tiles by, as well as their connections

  - The directory .idea includes some defaults for the editor we chose to use, IntelliJ. Feel free
  to ignore it.
  

# Project progress

  - Analyzing tiles to find all 35 possible rotations (done!). Code can be found at src/com/tsuro/tile/Tiles.java
  - An implementation for a Board of a game of Tsuro is now done. Code can be found at src/com/tsuro/board

## Tests

  - To run all tests, do ./Test in the Tsuro Directory. This will run all JUnit
    tests
    
