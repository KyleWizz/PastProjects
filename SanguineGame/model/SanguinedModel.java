package sanguine.model;

import java.util.List;
import sanguine.controller.ModelStatusListener;
import sanguine.controller.SanguineController;


/**
 * our sanguine model. this model is where
 * most of the games functions occur.
 *
 * @param <C> Generic.
 */
public interface SanguinedModel<C extends SanguinedCard> {

  /**
   * our method to place a card. Requires
   * rows, columns, player color, and card.
   *
   * @param card   card from hand
   * @param row    row placed on
   * @param col    column placed on
   * @param player color
   */
  public void placeCard(SanguinePlayingCard card, int row, int col, Player player);


  /**
   * our method here starts our game.
   * it sets up all the initial parameters and calls
   * the methods required for the game to run
   * correctly.
   *
   * @param board our board object from model/main
   * @param deck our deck initially from file
   * @param redPlayer our red player who goes first
   * @param bluePlayer our blue player who goes second always
   */
  public void startGame(Board board, List<SanguinePlayingCard>
                            deck, List<SanguinePlayingCard> deck2,
                        Player redPlayer, Player bluePlayer);

  //we can make a helper method in model

  /**
    checks if game is over so we can see who wins/total score.
   */
  public boolean isGameOver();



  /**
   * Gets the winner.
   */
  public Player getWinner();

  /**
   * Passes.
   */
  public void pass();

  /**
   * Gets the board.
   */
  public Board getBoard();

  /**
   * Draws Card.
   */
  public void drawCard();

  /**
   * gets the influenced grid.
   *
   * @param card card of SanguinePlayingCard.
   * @return InfluenceGrid.
   */
  public InfluenceGrid getInfluenceGrid(SanguinePlayingCard card);

  /**
   * Gets Hand size of deck.
   *
   * @return the size of your hand (amount of cards you have in your hand).
   */
  public int getHandSize();


  /**
   * Allows to discard a card.
   *
   * @param card SanguinePlaying Card.
   */
  public void mulligan(SanguinePlayingCard card);


  /**
   * Gets the current Player.
   *
   * @return the current player.
   */
  public Player getCurrentPlayer();

  /**
   * gets the current hand of player.
   *
   * @return list of sanguinedplayingcard
   */
  List<C> getCurrentHand();

  /**
   * Listener for sanguine model.
   *
   * @param listener Listener variable
   */
  void addModelStatusListener(ModelStatusListener listener);

  /**
   * need to get hand for each color player,
   * so we decide to
   * expose this to client and GUI/view.
   *
   * @param player player color
   * @return hand
   */
  public List<SanguinePlayingCard> getHandFor(Player player);
}






