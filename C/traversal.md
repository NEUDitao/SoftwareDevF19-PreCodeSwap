### The following is a specification for an interface in Java 12 describing a Labyrinth.

A `Labyrinth` is a connectivity specification for a `Simple Graph`, which is a `Graph` whose `Node`s each connect to at most one other `Node`.

A `Graph` is collection of `Node`s and the `Edge`s that connect the `Node`s.

A `Node` has a unique name and can have a `Token` placed on it

An `Edge` comes from one `Node` and points to another `Node`.

A `Token` is some item that can be placed on a `Node`.

---

You should create an interface representing a `Labyrinth` which supports the following three methods:

- `createLabyrinth`, which creates a Labyrinth with named `Node`s and returns the `Labyrinth` created.
- `addToken`, which adds a colored `Token` to a `Node` of a specified name within a `Labyrinth` and returns the updated `Labyrinth`.
- `canReach`, which determines if some colored token can reach some named `Node` by following the `Edge`s within a particular `Labyrinth`, returning `true` if possible and `false` otherwise.
