
As defined on ccs.neu.edu/home/matthias/4500-f19/C.html, a valid color-string
is one of the five following JSON Strings:
  - "white"
  - "black"
  - "red"
  - "green"
  - "blue"


As defined on ccs.neu.edu/home/matthias/4500-f19/C.html, a valid command
is one of:

  - a labyrinth creation request, which takes the form of a JSON list that
    starts with the string "lab", and contains JSON objects that have a
    "from" field, and a "to" field, both of which have values of Strings.
    There can be an arbritrary number of objects sent.
    These Strings are the names of the nodes that are created.
    A node-name is any String that appears in the from or to fields of the
    JSON Objects.
    The labyrinth creation request must come only once. All commands sent 
    before this will be ignored.
  - a place-token request, which takes the form of a JSON list that starts
    with the string "add", and contains a color-string and a node-name.
    This is only valid if the fields are sent in that specific order.
    The command is also only valid if the string in the third slot matches
    the name of one of the nodes on the labrynth, otherwise, it is ignored.  
  - a move token query, which takes the form of a JSON list that starts with
    the string "move" and takes a color-string and a node-name, in that order.
    The move token query is only valid if the specified node-name matches a
    node placed on the labyrinth, and if the color-string has been placed
    at any point. The move token query is the only command that returns
    anything. Move token specifically returns a JSON Boolean.


How to connect to our micro-service:
  - Open a TCP Connection with our server, with an input and output stream.
    Send *VALID* JSON on the stream to our server. The behaviour for
    handling non-valid JSON is undefined. The first command that should be
    sent is a labyrinth creation request. If any valid commands as defined
    above are sent before, they will be ignored. If any other JSON is sent,
    behaviour is undefined. If the labryinth creation request is not valid,
    the server will shut down the connection. 
  - The server will only send JSON booleans as responses to move token
    queries. All other commands will not receive feedback 
