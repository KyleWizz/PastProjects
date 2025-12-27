package sanguine.view;

import java.awt.Dimension;
import java.awt.Toolkit;
import sanguine.model.Board;

/**
 * class for dimensions of our board, handles
 * calculations and widths/heights.
 */
public class DimensionClass {

  public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  public static final int CELL_SIZE = screenSize.width / 7; // need to do by board
  public static final int CARD_HEIGHT = screenSize.height / 3;
  public static final int CARD_WIDTH = CELL_SIZE;
  public static final int PADDING_WIDTH = 50;
  public static final int HAND_HEIGHT = 150;

  /**
   * calculates cellsize.
   *
   * @param numRows r
   * @param numCols c
   * @return r
   */
  public static int calculateCellSize(int numRows, int numCols) {
    int width = screenSize.width - (2 * PADDING_WIDTH);
    int height = screenSize.height - HAND_HEIGHT - (2 * PADDING_WIDTH) - numRows * 28;

    int sizeWidth = width / numCols;
    int sizeHeight = height / numRows;

    return Math.min(sizeWidth, sizeHeight);
  }

  /**
   * calcs card size.
   *
   * @param numRows rows
   * @param numCols cols
   * @return calculatedsize
   */
  public static int calculateCardSize(int numRows, int numCols) {
    int width = screenSize.width - (2 * PADDING_WIDTH);
    int height = screenSize.height - HAND_HEIGHT - (2 * PADDING_WIDTH);

    int sizeWidth = width / numCols;
    int sizeHeight = height / numRows;

    int calculatedSize = Math.max(sizeWidth, sizeHeight);

    return Math.min(calculatedSize, CELL_SIZE);
  }

  /**
   * calcs height.
   *
   * @param numRows num of row
   * @param numCols num of col
   * @return int
   */
  public static int calculateCardHeight(int numRows, int numCols) {
    int height = HAND_HEIGHT - (2 * PADDING_WIDTH);
    return Math.min(height, CARD_HEIGHT);
  }

  /**
   * calcs width.
   *
   * @param numCards c
   * @return c
   */
  public static int calculateCardWidth(int numCards) {
    if (numCards <= 0) {
      return CARD_WIDTH;
    }

    int width = screenSize.width - (2 * PADDING_WIDTH);
    int maxWidth = width / numCards;

    if (maxWidth > CARD_WIDTH) {
      return CARD_WIDTH;
    } else {
      return maxWidth;
    }
  }
}
