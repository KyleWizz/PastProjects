package sanguine.model;

import java.util.ArrayList;
import java.util.List;
import sanguine.controller.ModelStatusListener;
import sanguine.controller.SanguineController;


/**
 * Sanguined standard model. This model controls
 * all of our functions and rules that our game
 * runs by.
 */
public class SanguineStandard implements SanguinedModel<SanguinePlayingCard>,
    ReadonlySanguinedModel<SanguinePlayingCard> {


  private boolean gameOver;
  private List<SanguinePlayingCard> redDeck;
  private List<SanguinePlayingCard> blueDeck;
  private List<SanguinePlayingCard> redHand;
  private List<SanguinePlayingCard> blueHand;

  private int turn;
  //our range size will provide us will cells affected by card.
  //board will be our main board initialized by client.
  private Board board; // make board
  //invariant: gameStarted must be true after a game starts
  private boolean gameStarted;
  //invariant: player is always red at start
  private Player currentPlayer;

  private boolean redPassed;
  private boolean bluePassed;

  private List<ModelStatusListener> modelListeners = new ArrayList<>();



  /**
   * Our constructor for our variable init.
   */
  public SanguineStandard() {
    this.redHand = new ArrayList<>();
    this.blueHand = new ArrayList<>();
    this.gameStarted = false;
    this.turn = 0;
    this.redPassed = false;
    this.bluePassed = false;
    this.gameOver = false;
  }

  private void notifyTurnChanged(Player player) {
    for (ModelStatusListener listener : modelListeners) {
      listener.onTurnChanged(player);
    }
  }

  private void notifyGameOver(Player winner, int winningScore) {
    for (ModelStatusListener listener : modelListeners) {
      listener.onGameOver(winner, winningScore);
    }
  }

  @Override
  public void placeCard(SanguinePlayingCard card, int row, int col, Player player) {
    if (board == null) {
      throw new IllegalStateException("Sanguine has no board");
    }
    List<SanguinePlayingCard> hand = getCurrentHand();
    if (!hand.contains(card)) {
      throw new IllegalStateException("Sanguine has no hand");
    }
    if (currentPlayer == Player.RED) {
      redHand.remove(card);
    } else {
      blueHand.remove(card);
    }
    board.placeCard(card, row, col, currentPlayer);

    InfluenceGrid influenceGrid = card.getRangeSize();
    applyInfluence(row, col, currentPlayer, influenceGrid);

    if (currentPlayer == Player.RED) {
      redPassed = false;
    } else {
      bluePassed = false;
    }


    drawCard();
    turn++;
    switchPlayer();
    if (isGameOver()) {
      System.out.println("game over");
    } else {
      notifyTurnChanged(currentPlayer);
    }


  }


  private void switchPlayer() {
    turn = turn % 2;
    if (turn == 0) {
      currentPlayer = Player.RED;
    }
    if (turn == 1) {
      currentPlayer = Player.BLUE;
    }


  }

  @Override
  public void addModelStatusListener(ModelStatusListener listener) {
    if (listener == null) {
      throw new NullPointerException();
    }
    modelListeners.add(listener);
  }

  /**
   * gets us the current turn of the game.
   *
   * @param playerColor our player
   * @return our player
   */
  public Player getTurn(Player playerColor) {
    //use either int / method that changes color each turn\

    return playerColor;

  }

  //make it take in board, deck etc.
  //added more exceptions.
  @Override
  public void startGame(Board board, List<SanguinePlayingCard> deck,
                        List<SanguinePlayingCard> deck2,
                        Player redPlayer, Player bluePlayer) {
    System.out.println(deck.size());
    if (board == null) {
      throw new IllegalStateException("Sanguine has no board");
    }
    //check for same card as board size (ex. 3x5 = 15 cards)
    //|| deck.size() != (board.getRowNum() * board.getColNum())
    if (deck == null) {
      throw new IllegalStateException("Sanguine has no deck");
    }
    if (gameStarted) {
      throw new IllegalStateException("Sanguine has already started");
    }
    this.board = board;
    this.redDeck = new ArrayList<>(deck);
    this.blueDeck = new ArrayList<>(deck2);
    this.turn = 0;
    this.redPassed = false;
    this.bluePassed = false;
    this.gameOver = false;
    this.redHand = new ArrayList<>();
    this.blueHand = new ArrayList<>();
    this.currentPlayer = Player.RED;
    this.gameStarted = true;

    dealInitialHands(5);
    notifyTurnChanged(currentPlayer);
  }


  // helper method to deal hands to both players
  private void dealInitialHands(int handSize) {
    for (int i = 0; i < handSize && !redDeck.isEmpty(); i++) {
      redHand.add(redDeck.remove(0));
    }

    for (int i = 0; i < handSize && !blueDeck.isEmpty(); i++) {
      blueHand.add(blueDeck.remove(0));
    }
  }

  @Override
  public boolean isGameOver() {
    if (!gameStarted) {
      return false;
    }
    if (redPassed && bluePassed || noMovesLeft()) {


      Player winner;
      int winningScore;

      if (board.getTotalScore(Player.RED) > board.getTotalScore(Player.BLUE)) {
        winner = Player.RED;
        winningScore = board.getTotalScore(Player.RED);
      } else if (board.getTotalScore(Player.BLUE) > board.getTotalScore(Player.RED)) {
        winner = Player.BLUE;
        winningScore = board.getTotalScore(Player.BLUE);
      } else {
        // Tie game
        winner = null;
        winningScore = 0;
      }
      notifyGameOver(winner, winningScore);
      return true;
    }

    return false;

    //score check etc -get row calc
  }

  private boolean noMovesLeft() {
    for (int row = 0; row < board.getRowNum(); row++) {
      for (int col = 0; col < board.getColNum(); col++) {
        Cell cell = board.getCell(row, col);
        if (!cell.hasCard()) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public Player getWinner() {
    Player winner;
    int redScore = board.getTotalScore(Player.RED);
    int blueScore = board.getTotalScore(Player.BLUE);

    if (redScore > blueScore) {
      System.out.println("RED wins with score: " + redScore);
      winner = Player.RED;
    } else if (blueScore > redScore) {
      winner = Player.BLUE;
      System.out.println("BLUE wins with score: " + blueScore);
    } else {
      winner = null;
      System.out.println("It's a tie! Both players have score: " + redScore);
    }
    return winner;
  }

  /**
   * gets the score of the player winner.
   * exposed to client.
   *
   * @return board.getTotalScore(winner) winner score
   */
  public int getWinnerScore() {
    if (!isGameOver()) {
      throw new IllegalStateException("Game not over");
    }

    Player winner = getWinner();
    if (winner == null) {
      // Tie - return either score (they're the same)
      return board.getTotalScore(Player.RED);
    }
    return board.getTotalScore(winner);
  }

  @Override
  public void pass() {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }

    if (currentPlayer == Player.RED) {
      redPassed = true;
    } else if (currentPlayer == Player.BLUE) {
      bluePassed = true;
    }

    turn += 1;
    switchPlayer();
    if (isGameOver()) {
      System.out.println("game over");
    } else {
      notifyTurnChanged(currentPlayer);
    }

    //could do isgameover
  }


  @Override
  public Board getBoard() {
    return board;
  }

  /**
   * helper method for each turn.
   *
   * @return currentPlayer boolean (red/blue)
   */
  @Override
  public List<SanguinePlayingCard> getCurrentHand() {
    List<SanguinePlayingCard> hand = currentPlayer == Player.RED ? redHand : blueHand;
    return new ArrayList<>(hand);
    // if red return red, if not return blue.
  }

  //for model/ controller cause
  //we need both views to have red/blue deck present
  //so one view red deck one blue deck

  /**
   * need to get hand for each color player,
   * so we decide to
   * expose this to client and GUI/view.
   *
   * @param player player color
   * @return hand
   */
  public List<SanguinePlayingCard> getHandFor(Player player) {
    if (player == Player.RED) {
      return new ArrayList<>(redHand);
    } else {
      return new ArrayList<>(blueHand);
    }
  }

  /**
   * after every turn a cards drawn.
   */
  @Override
  public void drawCard() {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }

    if (currentPlayer == Player.RED) {
      if (!redDeck.isEmpty()) {
        redHand.add(redDeck.removeFirst());
      }
    } else if (!blueDeck.isEmpty()) {
      blueHand.add(blueDeck.removeFirst()); // do we want randomize
    }

  }


  @Override
  public InfluenceGrid getInfluenceGrid(SanguinePlayingCard card) {
    return card.getRangeSize();
  }

  @Override
  public int getHandSize() {
    return getCurrentHand().size();
  }


  @Override
  public void mulligan(SanguinePlayingCard card) {
    //    if (!gameStarted) {
    //      throw new IllegalStateException("Game Started");
    //    }
    //    if (drawPile.isEmpty()) {
    //      throw new IllegalStateException("No draw found");
    //    }
    //    PlayingCard card = drawPile.removeFirst();
    //    drawPile.add(card); -- adds new card to hand
  }

  /**
   * gets a pawns color from cell.
   *
   * @return currentPlayer color of player this turn
   */
  @Override
  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  @Override
  public boolean isGameStarted() {
    return false;
  }


  private void applyInfluence(int row, int col, Player player, InfluenceGrid grid) {
    System.out.println("APPLYING INFLUENCE");
    //we need to check if opponent color switches (player switch in model)
    //mirror blue
    if (player == Player.BLUE) {
      grid = grid.mirrorInfluenceGrid();
    }
    Player opponent = getOpposingPlayer();
    for (int rowPos = 0; rowPos < 5; rowPos++) {
      for (int colPos = 0; colPos < 5; colPos++) {
        if (!grid.isInfluenced(rowPos, colPos)) {
          continue;
        }
        //gets center
        if (rowPos == 2 && colPos == 2) {
          continue;
        }
        int boardRow = row + (rowPos - 2);
        int boardCol = col + (colPos - 2);
        System.out.println("Checking" + boardRow + ", " + boardCol);
        if (!board.isValidPosition(boardRow, boardCol)) {
          System.out.println("posit" + boardRow + ", " + boardCol);
          continue;
        }

        Cell cell = board.getCell(boardRow, boardCol);
        if (cell.hasCard()) {
          System.out.println("hascard" + boardRow + ", " + boardCol);
          continue; //skips
        }
        if (cell.isOwnedBy(opponent)) {
          cell.convertPawn(player);
          System.out.println("CONVERTED!");
        } else {
          cell.addPawn(player);
          System.out.println("ADDED PAWN!");
        }
      }
    }
  }

  private Player getOpposingPlayer() {
    if (currentPlayer == Player.RED) {
      return Player.BLUE;
    } else {
      return Player.RED;
    }
  }

}
