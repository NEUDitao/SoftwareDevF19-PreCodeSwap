To: Matthias Felleisen

From: Michael Ballantyne

Date: September 16 2019

Subject: labyrinth.Labyrinth Specification Feedback

These are the ambiguities we have found in our given spec:

>...that accepts a Color and returns a labyrinth.ColoredToken of that color...

The Color data type in this spec was never explicitly specified. We propose,
and have implemented, using Java's Color class to represent the color. 

>...that accepts a List of unique Strings...

There is ambiguity in the labyrinth.Labyrinth constructor around how to ensure that the 
given List of Node Names (as Strings) is unique. We propose, and have 
implemented a simple O(n) algorithm using a hashtable to check if each String 
in the list is unique.

>'...returns a labyrinth.Labyrinth...'

We do not know what data type(s) the labyrinth.Labyrinth holds or uses as it was never 
specified. We chose to implement it as a simple List of Strings, as this is 
the data type we are given to create a labyrinth with.

> ...add the token to that node... 

and

>...connected sequentially...

There is no specification for how a node in the labyrinth.Labyrinth is being considered
or stored, or whether or not there is a token attached to a node, so we are
unable to implement these two features in the spec. We suggest creating a
Node class that holds the characteristics specific to a Node, and to specify
the definition of a labyrinth.Labyrinth to hold a list of Nodes, and a Map of 
labyrinth.ColoredToken Names to Nodes. We suggest these data types as we are being
asked to see if a labyrinth.Labyrinth there is a labyrinth.ColoredToken on a Node, not whether
or not a Node has a labyrinth.ColoredToken. 

For the "addColoredToken" method,

There should be specification on catching the case where if there is a 
certain labyrinth.ColoredToken that you are prompting to add to a Node that is already 
on the Node, then you would throw an exception, or ignore the method call.

Furthermore, there should also be specification on a Main method for a user
to interact with a labyrinth.Labyrinth. We suggest implementing a main method that
parses input through the command line, initializing a labyrinth.Labyrinth upon starting
the program.
