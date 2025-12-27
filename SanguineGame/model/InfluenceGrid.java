package sanguine.model;

//change to from file?
//read file once and store it!


/**
 * gets a grid in range 5x5 and returns
 * certain cells true/false for pawns.
 */
public class InfluenceGrid {
  //invariant: influencedCells are always 5x5
  private final boolean[][] influencedCells; //5x5


  /**
   * Public constructor for influence grid that takes in
   * the boolean to see if the cell is influenced.
   *
   * @param pattern pattern is the influenced pattern to see what is influenced.
   */
  public InfluenceGrid(boolean[][] pattern) {
    if (pattern == null || pattern.length != 5 || pattern[0].length != 5) {
      throw new IllegalArgumentException("Invalid pattern");
    }
    this.influencedCells = pattern;
  }


  /**
   * checks if row is influenced.
   *
   * @param row of board
   * @param col of board
   * @return true;
   */
  public boolean isInfluenced(int row, int col) {
    if (!isValid(row, col)) {
      return false;
    }
    return influencedCells[row][col];
  }

  /**
   * checks for a valid position on board.
   *
   * @param row row
   * @param col col
   * @return true/false
   */
  private boolean isValid(int row, int col) {
    int numRows = 5;
    int numCols = 5;
    return row >= 0 && row < numRows && col >= 0 && col < numCols;
  }


  /**
   * gets the influenced row.
   *
   * @param row row
   * @return int
   */
  public int getInfluencedCellsRow(int row) {
    //how do we get the influenced positions of the array
    return influencedCells[row].length;
  }


  /**
   * gets our influenced columns.
   *
   * @param col column of influence
   * @return the number of influenced cells in a column.
   */
  public int getInfluencedCellsCol(int col) {
    return influencedCells[col].length;
  }

  /**
   * Makes mirror as columns are flipped.
   *
   * @return the new influenced Grid that is mirrored.
   */
  public InfluenceGrid mirrorInfluenceGrid() {
    boolean[][] mirror = new boolean[5][5];
    for (int row = 0; row < influencedCells.length; row++) {
      for (int col = 0; col < influencedCells[row].length; col++) {
        //makes mirror as col are flipped
        mirror[row][col] = influencedCells[row][4 - col];
      }
    }
    return new InfluenceGrid(mirror);
  }
}

