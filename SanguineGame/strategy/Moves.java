package sanguine.strategy;

import sanguine.view.SanguinePanel;

/**
 * our moves class we will be using in our
 * strategy interface. Does simple getters,
 * equals and placement selections for our
 * "AI".
 */
public class Moves {
  private final int cardIndex;
  private final int row;
  private final int col;

  /**
   * constructor for moves.
   *
   * @param cardIndex index of card
   * @param row       row of card
   * @param col       col of card
   */
  public Moves(int cardIndex, int row, int col) {
    this.cardIndex = cardIndex;
    this.row = row;
    this.col = col;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public int getCardIndex() {
    return cardIndex;
  }

  @Override
  public String toString() {
    return "Moves [cardIndex=" + cardIndex + ", row=" + row + ", col=" + col + "]";
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Moves)) {
      return false;
    }
    Moves move = (Moves) obj;
    return (move.row == row && move.col == col && move.cardIndex == cardIndex);
  }

  @Override
  public int hashCode() {
    return cardIndex * 31 + row * 31 + col;
  }
}
