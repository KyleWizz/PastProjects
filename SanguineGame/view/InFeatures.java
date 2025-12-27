package sanguine.view;

import sanguine.model.Player;

/**
 * our game method integration interface.
 */
public interface InFeatures {

  /**
   * clicks a card for user.
   *
   * @param row row
   * @param player player
   */
  void clickCard(int row, Player player);

  /**
   * restarts game unimplemented.
   */
  void restartGame();

  /**
   * gets cell unimplemented.
   *
   * @param row row
   * @param col col
   */
  void cellClicked(int row, int col);

  /**
   * confirms a move on the board when clicked.
   */
  void confirm();

  /**
   * passes a turn.
   */
  void pass();


}
