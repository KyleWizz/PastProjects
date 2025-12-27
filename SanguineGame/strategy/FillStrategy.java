package sanguine.strategy;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import sanguine.Sanguine;
import sanguine.model.Board;
import sanguine.model.Cell;
import sanguine.model.Player;
import sanguine.model.SanguinePlayingCard;
import sanguine.model.SanguineStandard;
import sanguine.view.SanguineView;

/**
 * The AI tries to fill as many places as possible on
 * the board.
 */
public class FillStrategy implements StratInterface {

  private static boolean canPlace(Board board, SanguinePlayingCard card,
                                  int row, int col, Player player) {
    Cell cell = board.getCell(row, col);

    return cell.hasOwner() && cell.isOwnedBy(player)
        && !cell.hasCard() && cell.getPawnCount() >= card.getCost();

  }

  //just a basic filling board like in Main with
  //strategy interface
  @Override
  public Optional<Moves> move(SanguineStandard model, Player player) {
    Board board = model.getBoard();
    List<SanguinePlayingCard> hand = model.getCurrentHand();

    System.out.println("Current hand: " + hand);

    try {
      for (int row = 0; row < board.getRowNum(); row++) {
        for (int col = 0; col < board.getColNum(); col++) {
          System.out.println("Checking row: " + row + "," + col);
          for (int card = 0; card < hand.size(); card++) {
            SanguinePlayingCard playingCard = hand.get(card);
            if (canPlace(board, playingCard, row, col, player)) {
              System.out.println(player + " Placed" + card + " at: " + row + " " + col);
              //  model.placeCard(playingCard, row, col, player);
              System.out.println(player + "placed: " + playingCard.getName()
                  + " at: " + row + " " + col);
              System.out.println("Move = fillStrategy");
              return Optional.of(new Moves(card, row, col));
            } //passes if not valid
          }
        }
      }

    } catch (Exception e) {
      return Optional.empty();
    }
    System.out.println("No moves found");
    return Optional.empty();
  }
}
