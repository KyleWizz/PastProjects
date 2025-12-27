package sanguine.controller;

import java.util.List;
import javax.swing.JOptionPane;
import sanguine.model.Board;
import sanguine.model.Player;
import sanguine.model.SanguinePlayingCard;
import sanguine.model.SanguinedModel;
import sanguine.view.SanguineFrame;
import sanguine.view.SanguinePanel;
import sanguine.view.SanguineView;

/**
 * unused class.
 */
public class SanguineController implements PlayerMoveListener,
    ModelStatusListener {
  private final SanguinedModel<SanguinePlayingCard> model;
  private final Player player;
  private final PlayerNotifier playerType;
  private final SanguineFrame view;
  private PlayerMoveListener listener;


  private Integer selectedCardIndex;
  private Integer selectedRow;
  private Integer selectedCol;
  private boolean turn = false;

  /**
   * contstuctor for sanguine controller.
   *
   * @param model      of the game.
   * @param player     current player.
   * @param view       the board of the game
   * @param playerType AI strategies or human.
   */
  public SanguineController(SanguinedModel<SanguinePlayingCard> model, Player player,
                            SanguineFrame view, PlayerNotifier playerType) {
    if (model == null || player == null || view == null) {
      throw new IllegalStateException("Exception - null parameter");
    }
    this.playerType = playerType;
    this.model = model;
    this.player = player;
    this.view = view;
    this.turn = false;
    //    this.selectedCol = null;
    if (playerType != null) {
      playerType.addPlayerMoveListener(this);
    }
    view.addPlayerMoveListener(this);
    model.addModelStatusListener(this);

  }


  @Override
  public void onCardSelected(int cardIndex) {
    if (!turn) {
      return;
    }
    try {
      if (cardIndex < 0 || cardIndex >= model.getHandSize()) {
        showError("Card index out of bounds");
      }
      selectedCardIndex = cardIndex;
      view.refresh();
    } catch (Exception e) {
      showError(e.getMessage());
    }
  }

  @Override
  public void onCellSelected(int row, int col) {
    Board board = model.getBoard();
    if (!turn) {
      return;
    }
    try {
      if (row < 0 || row >= board.getRowNum()
          || col < 0 || col >= board.getColNum()) {
        showError("Cell index out of bounds");
      }

      selectedRow = row;
      selectedCol = col;

      view.refresh();
    } catch (Exception e) {
      showError(e.getMessage());
    }
  }

  @Override
  public void onPass() {
    if (!turn) {
      return;
    }

    try {
      clearSections();
      model.pass();
      view.refresh();

    } catch (Exception e) {
      showError(e.getMessage());
    }
  }

  @Override
  public void onConfirmMove() {
    if (!turn) {
      return;
    }
    try {
      List<SanguinePlayingCard> hand = model.getCurrentHand();
      SanguinePlayingCard card = hand.get(selectedCardIndex);
      if (card == null) {
        showError("Card index out of bounds");
      }
      if (selectedCardIndex <= -1) {
        showError("Card index out of bounds");
      }
      if (selectedRow <= -1) {
        showError("Card index out of bounds");
      }
      if (selectedCol <= -1) {
        showError("Card index out of bounds");
      }
      if (turn) {
        model.placeCard(card, selectedRow, selectedCol, model.getCurrentPlayer());
        view.refresh();
        //clear all index
        clearSections();
      }

    } catch (Exception e) {
      showError(e.getMessage());
    }
  }

  @Override
  public void onTurnChanged(Player currentPlayer) {
    turn = this.player.equals(currentPlayer);
    //notify
    try {
      //refresh
      if (!turn) {
        clearSections();
      }

      view.refresh();
      //clear all index


      if (turn && playerType != null) {
        playerType.notifyPlayerTurn();
      }
      //strings to be added
      view.refresh();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, e.getMessage());
    }
  }

  @Override
  public void onGameOver(Player winner, int winningScore) {
    turn = false;
    winner = model.getWinner();

    JOptionPane.showMessageDialog(null, "Game over. Winner:"
        + winner + " Score: " + winningScore);
    view.refresh();
    //show score player etc string
  }

  /**
   * Clear the sections and make everything null.
   */
  public void clearSections() {
    selectedCardIndex = null;
    selectedRow = null;
    selectedCol = null;
  }

  private void showError(String message) {
    JOptionPane.showMessageDialog(view, message, "Error", JOptionPane.ERROR_MESSAGE);
  }
}
