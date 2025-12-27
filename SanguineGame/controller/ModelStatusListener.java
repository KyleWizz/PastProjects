package sanguine.controller;

import sanguine.model.Player;

/**
 * ModelStatusListener listens to the model when something is done.
 */
public interface ModelStatusListener {

  /**
   * Tells if the turn is changed.
   *
   * @param player the current player.
   */
  void onTurnChanged(Player player);

  /**
   * Tells if the game is over.
   *
   * @param winner the winner of the game.
   * @param winningScore the winners score.
   */
  void onGameOver(Player winner, int winningScore);

}
