The package labyrinth contains the following:

ColoredToken is an interface that supports the following operations:
 - A method, getColor, that returns the Color of the token 

Additionally, there must be a function that accepts a Color and returns
a ColoredToken of that color. This may be a constructor for a class that
implements ColoredToken.

Labyrinth is an interface that supports the following operations:
 - A method, addColoredToken that accepts a ColoredToken and a node name.
   If a node exists with that name, add the token to that node.
   Otherwise, throw a NoSuchElementException.
 - A method, reaches that accepts a ColoredToken and a node name.
   If that ColoredToken is not on any node, return false.
   If the given ColoredToken is on any nodes, return whether the node
   with the given name can be reached from a node that contains the given
   ColoredToken. 

Additionally, there must be a function that accepts a List of unique Strings, 
where each String is the name of a node, and returns a Labyrinth. 
Each node should be connected 
sequentially in the order that its name appears in the given List. 
The last node is not connected to any other nodes.
This function may be a constructor for a class that implements Labyrinth.
