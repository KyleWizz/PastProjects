package sanguine.model;

import java.util.List;

/**
 * rushed time so trying to implement in sanguine standard og code.
 *
 * @param <C> gen type
 */
public interface ReadonlySanguinedModel<C extends SanguinedCard> {

  /**
   * Tells if the game is over.
   *
   * @return if the game is over true and false if not.
   */
  boolean isGameOver();

  /**
   * Gets the board.
   *
   * @return the board.
   */
  Board getBoard();

  /**
   * Gets the current hand.
   *
   * @return the players current hand.
   */
  List<SanguinePlayingCard> getCurrentHand();

  /**
   * Gets the range of the influence of the grid.
   *
   * @param card the cards that are being placed.
   * @return the range of influence
   */
  InfluenceGrid getInfluenceGrid(SanguinePlayingCard card);

  /**
   * Gets the size of the players hand.
   *
   * @return the hand size of player.
   */
  int getHandSize();

  /**
   * Gets the current player that has the turn.
   *
   * @return the current player playing
   */
  Player getCurrentPlayer();

  /**
   * Tells if the game is started.
   *
   * @return true if game is started and false if not.
   */
  boolean isGameStarted();

}
