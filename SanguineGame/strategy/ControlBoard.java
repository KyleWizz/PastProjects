package sanguine.strategy;

import java.util.List;
import java.util.Optional;
import sanguine.model.Board;
import sanguine.model.Cell;
import sanguine.model.Player;
import sanguine.model.SanguinePlayingCard;
import sanguine.model.SanguineStandard;

/**
 * Like fill-strategy, this strategy will
 * have the AI try its best based off moves to control
 * the entire board (best points + value)
 * with pawns etc.
 */
public class ControlBoard implements StratInterface {

  private static boolean canPlace(Board board, SanguinePlayingCard card,
                                  int row, int col, Player player) {
    Cell cell = board.getCell(row, col);

    return cell.hasOwner() && cell.isOwnedBy(player)
        && !cell.hasCard() && cell.getPawnCount() >= card.getCost();

  }

  @Override
  public Optional<Moves> move(SanguineStandard sanguine, Player player) {
    List<SanguinePlayingCard> hand = sanguine.getCurrentHand();
    if (sanguine.isGameOver()) {
      return Optional.empty();
    }
    int maxCells = -1;
    Moves bestMove = null;
    System.out.println("Current hand: " + hand);
    for (int row = 0; row < sanguine.getBoard().getRowNum(); row++) {
      for (int col = 0; col < sanguine.getBoard().getColNum(); col++) {
        System.out.println("checking row " + row + " col " + col);
        for (int cardIdx = 0; cardIdx < sanguine.getHandSize();
             cardIdx++) {
          SanguinePlayingCard playingCard = hand.get(cardIdx);
          if (canPlace(sanguine.getBoard(), playingCard, row, col, player)) {
            int cellsControlled = countCellsAfterMove(sanguine.getBoard(),
                sanguine, player, cardIdx, row, col);

            // Update best move if this gives more control
            if (cellsControlled > maxCells) {
              maxCells = cellsControlled;
              System.out.println("Move = control");
              bestMove = new Moves(cardIdx, row, col);
            }
          }
        }
      }
    }
    System.out.println("No moves found");
    return Optional.ofNullable(bestMove);
  }

  private int countCellsAfterMove(Board board, SanguineStandard sanguine,
                                  Player player, int cardIdx, int row, int col) {

    // Create a copy to simulate the move
    int cellCount = 0;
    for (int r = 0; r < board.getRowNum(); r++) {
      for (int c = 0; c < board.getColNum(); c++) {
        if (board.getCell(row, col).getCellOwner() == sanguine.getCurrentPlayer()) {
          cellCount++;
        }
      }
    }

    return cellCount;
  }


}
