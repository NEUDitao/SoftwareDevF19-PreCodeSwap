package cs4500.hw2;

/**
 * The exception thrown whenever the given Json is valid but not valid for the labyrinth
 */
public class NotARequestException extends RuntimeException {

  String message;

  public NotARequestException(String message) {
    this.message = message;
  }

}
