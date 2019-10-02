

# We are Implementing a Game of Tsuro

The specifications and rules for the game of Tsuro can be found [here](https://ccs.neu.edu/home/matthias/4500-f19/tsuro.html). 

# Folders

  - The directory Planning contains all of the documents describing how we
  designed our implementation of Tsuro
  - The directory Common contains all of the code for our implementation
  - The directory .idea includes some defaults for the editor we chose to use, IntelliJ. Feel free
  to ignore it.
  

# Project progress

  - analyzing tiles to find all 35 possible rotations (done!)
  - An implementation for a Board of a game of Tsuro is now done. Code can be found at src/com/tsuro/board

## Tests

  - To run all tests, do ./Test in the Tsuro Directory. This will run all JUnit
    tests
    
    
ADD TO FOLDERS
- /tile/tiletypes contains files for processing JSON representation of Tiles, and querying them.
- tsuro-tiles-index.json contains all of the indices that we will be referring to the tile by, as
  well as their connections