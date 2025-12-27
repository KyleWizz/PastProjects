package sanguine.view;

import java.util.List;
import sanguine.model.Cell;
import sanguine.model.Player;
import sanguine.model.SanguinePlayingCard;
import sanguine.model.SanguinedModel;


/**
 * Our sanguined features class, which initializes all
 * the features of our board and also includes
 * selection types, -1 is unselected and 1 is selected.
 * This applies for cells on our board and cards.
 */
public class SanguinedFeatures implements InFeatures {

  private final SanguinedModel<SanguinePlayingCard> model;
  private final InFrame view;
  private Player myPlayer;

  private int selectedCardIndex = -1;
  private int selectedRow = -1;
  private int selectedCol = -1;

  /**
   * constructor method for features.
   *
   * @param model model
   * @param view view
   */
  public SanguinedFeatures(SanguinedModel<SanguinePlayingCard> model,
                           InFrame view, Player myPlayer) {
    this.model = model;
    this.myPlayer = myPlayer;
    this.view = view;
  }

  @Override
  public void cellClicked(int row, int col) {

    System.out.println("cellClicked: " + row + ", " + col);
    try {
      this.selectedRow = row;
      this.selectedCol = col;
      view.refresh();
    } catch (Exception e) {
      System.err.println(e);
    }
  }


  @Override
  public void clickCard(int cardIndex, Player player) {

    System.out.println("Clicked card " + cardIndex + " of player " + player);
    try {
      this.selectedCardIndex = cardIndex;
      view.refresh();
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  @Override
  public void restartGame() {
    //an unimplemented method: ideally restarts the game b/t
    //player and AI. mulligan too is still unimp.
  }


  @Override
  public void confirm() {
    System.out.println("Confirmation click");
    if (!model.getCurrentPlayer().equals(myPlayer)) {
      System.out.println("Not " + myPlayer + "'s turn, blocking!");
      return;
    }
    if (selectedCardIndex < 0) {
      System.out.println("No card selected!");
      return;
    }
    if (selectedRow < 0 || selectedCol < 0) {
      System.out.println("Must select both card and cell!");
      return;
    }
    try {
      Cell cell = model.getBoard().getCell(selectedRow, selectedCol);
      List<SanguinePlayingCard> hand = model.getCurrentHand();
      SanguinePlayingCard card = hand.get(selectedCardIndex);
      if (!cell.isOwnedBy(model.getCurrentPlayer())) {
        System.out.println("Cell not owned by " + model.getCurrentPlayer());
        return;
      }
      if (cell.hasCard()) {
        System.out.println("Cell already has a card!");
        return;
      }
      if (cell.getPawnCount() < card.getCost()) {
        System.out.println("Not enough pawns! Need " + card.getCost()
            +
            ", have " + cell.getPawnCount());
        return;
      }
      model.placeCard(card, selectedRow, selectedCol, model.getCurrentPlayer());
      selectedCardIndex = -1;
      selectedRow = -1;
      selectedCol = -1;
      //hand.remove(card);
      view.refresh();

    } catch (Exception e) {
      System.out.println("Cannot palce card, error: " + e.getMessage());
    }
  }


  @Override
  public void pass() {
    try {
      if (!model.getCurrentPlayer().equals(myPlayer)) {
        System.out.println("Not " + myPlayer + "'s turn, blocking!");
        return;
      }
      selectedRow = -1;
      selectedCol = -1;
      selectedCardIndex = -1;
      System.out.println("Button clicked");
      model.pass();
      view.refresh();


    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public int getSelectedCardIndex() {
    return selectedCardIndex;
  }

  public int getSelectedRow() {
    return selectedRow;
  }

  public int getSelectedCol() {
    return selectedCol;
  }

  public void setSelectedCard(int cardIndex) {
    this.selectedCardIndex = cardIndex;
  }

  /**
   * gets selected cell.
   *
   * @param row row
   * @param col col
   */
  public void setSelectedCell(int row, int col) {
    this.selectedRow = row;
    this.selectedCol = col;
  }
}