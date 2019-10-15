import lombok.NonNull;

/**
 * An interface for the Observer Pattern where the Observer implementing the interface can be
 * updated with a parameterized state.
 *
 * @param <T> The value class for the update() method to receive.
 */
interface IObserver<T> {

  /**
   * Updates this based off the state given.
   *
   * @param state some value class that holds the state to update this with.
   */
  void update(@NonNull T state);

}
