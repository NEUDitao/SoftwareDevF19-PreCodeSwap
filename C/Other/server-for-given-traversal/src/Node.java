/**
 * Has a unique name and can have a Token placed on it.
 */
public class Node {

  Token token;
  String name;

  Node(String name, Token token) {
    this.name = name;
    this.token = token;
  }

}
