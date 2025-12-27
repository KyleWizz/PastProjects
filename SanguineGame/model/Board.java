package sanguine.model;


/**
 * Boards class to initialize the board.
 */
public class Board {
  //invariant: rows always > 0
  private final int numRows;
  //invariant: numcols must be greater than 1 and odd
  private final int numCols;
  private final Cell[][] grid;
  private InfluenceGrid influenceGrid;

  /**
   * public constructer Board.
   *
   * @param rows rows of grid.
   * @param cols cols of grid.
   */
  public Board(int rows, int cols) {
    if (rows <= 0 || cols <= 1 || cols % 2 == 0) {
      throw new IllegalArgumentException("Board initialized wrong");
    }
    numRows = rows;
    numCols = cols;
    this.grid = new Cell[numRows][numCols];

    initializeBoard();
  }


  private void initializeBoard() {
    for (int row = 0; row < numRows; row++) {
      for (int col = 0; col < numCols; col++) {
        grid[row][col] = new Cell();
      }

    }

    for (int row = 0; row < numRows; row++) {
      grid[row][0].setPawns(1, Player.RED);
    }

    for (int row = 0; row < numRows; row++) {
      grid[row][numCols - 1].setPawns(1, Player.BLUE);
    }
  }

  private int getNumRows() {
    return numRows;
  }

  private int getNumCols() {
    return numCols;
  }

  /**
   * Checks if is valid position.
   *
   * @param row row of grid.
   * @param col col of grid.
   * @return true if it is valid position.
   */
  public boolean isValidPosition(int row, int col) {
    return row >= 0 && row < numRows && col >= 0 && col < numCols;
  }


  /**
   * Gets the Cell.
   *
   * @param row of the grid.
   * @param col of the grid.
   * @return the Cell that we want.
   */
  public Cell getCell(int row, int col) {
    if (!isValidPosition(row, col)) {
      throw new IllegalArgumentException("Invalid:" + row + "," + col);
    }
    return grid[row][col];
  }

  /**
   * Gets the row score.
   *
   * @param row the row of the grid.
   * @param player the player in the game.
   * @return the score.
   */
  public int getRowScore(int row, Player player) {
    int score = 0;
    if (row < 0 || row >= numRows) {
      throw new IllegalArgumentException("Invalid score");
    }
    for (int col = 0; col < numCols; col++) {
      Cell cell = grid[row][col];
      if (cell.hasCard() && cell.isOwnedBy(player)) {
        score += cell.getCard().getValue();
      }
    }
    return score;
  }

  /**
   * Gets the totalScore of the player.
   *
   * @param player the player that we want the total score of.
   * @return the total score of the player.
   */
  public int getTotalScore(Player player) {
    int totalScore = 0;
    Player opponent = player == Player.RED ? Player.BLUE : Player.RED;
    for (int row = 0; row < numRows; row++) {
      int playerScore = getRowScore(row, player);
      int opponentScore = getRowScore(row, opponent);
      //gets us score for only one player
      if (playerScore > opponentScore) {
        totalScore += playerScore;
      }
    }
    return totalScore;
  }


  /**
   * Copys the board and makes a second one.
   *
   * @return the copy of a new Board.
   */
  public Board copy() {
    return new Board(numRows, numCols);
  }

  public int getRowNum() {
    return numRows;
  }

  public int getColNum() {
    return numCols;
  }


  /**
   * Places card if isnt owned, and it doesn't have card.
   *
   * @param card cards player has.
   * @param row row of grid.
   * @param col col of grid
   * @param currentPlayer who is the current player?
   */
  public void placeCard(SanguinePlayingCard card, int row, int col, Player currentPlayer) {
    if (!isValidPosition(row, col)) {
      throw new IllegalArgumentException("Invalid:" + row + "," + col);
    }

    Cell cell = getCell(row, col);

    if (cell.hasCard()) {
      throw new IllegalStateException("You can't place card when player is owned!");
    }
    if (!cell.isOwnedBy(currentPlayer)) {
      throw new IllegalStateException("You can't place card when player is not owned!");
    }
    if (cell.getPawnCount() < card.getCost()) {
      throw new IllegalStateException("Not enough pawns. cost: " + card.getCost()
          + ", have: " + cell.getPawnCount());
    }

    cell.setCard(card);
    cell.setPawns(0, currentPlayer);


    //apply the influence model
    //update cell in model


    //validate move
    //Place the card
    //Apply influence
    //update the cell
  }


}
