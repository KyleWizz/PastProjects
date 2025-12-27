package sanguine.controller;

/**
 * PlayerMoveListener has all the listeners and notifies what player does.
 */
public interface PlayerMoveListener {

  /**
   * Tells if the card is selected.
   *
   * @param cardIndex The card it is selected at.
   */
  void onCardSelected(int cardIndex);

  /**
   * Tells us which cell is selected.
   *
   * @param row row of the board.
   * @param col cell of the board.
   */
  void onCellSelected(int row, int col);

  /**
   * Notifies when player passes.
   */
  void onPass();

  /**
   * Notifies if the move is confirmed.
   */
  void onConfirmMove();



}
