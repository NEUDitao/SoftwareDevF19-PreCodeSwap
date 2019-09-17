We could not implement the specification given to us fully because their
definitions of "Edges" and how to create them was not well-formed.

We added a constructor for SimpleGraph, even though it wasn't in the 
specification, because without it, createLabyrinth could not be called, thus
making it impossible to create a labyrinth.

The specification also said createLabyrinth shouldn't take Edges. Since we
don't know how to create the edges from the Nodes, we added the ability to
add edges in the constructor for SimpleGraph.

The definition of a labyrinth.ColoredToken vs a Token was ambiguous, and we did not
know how to proceed. 

The code lives in ./Other/server-for-given-traversal/src
