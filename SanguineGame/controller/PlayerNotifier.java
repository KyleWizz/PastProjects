package sanguine.controller;

/**
 * represents human or AI that is notified after each turn.
 * This allows us to define any type of class we want
 * with different implementation and publish it to
 * our controller.
 */
public interface PlayerNotifier {

  /**
   * controller notifies the player through this
   * subscriber, and
   * for AI, it notifies moves.
   * For human players we have listeners in panel/view.
   */
  void notifyPlayerTurn();

  /**
   * allows a controller to subscribe to this player's move actions,
   * and when AI computes a move,
   * it notifies all subscribed listeners.
   *
   * @param listener listener for controller
   */
  void addPlayerMoveListener(PlayerMoveListener listener);
}
