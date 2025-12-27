package sanguine.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import sanguine.model.Board;
import sanguine.model.Cell;
import sanguine.model.Player;
import sanguine.model.SanguinePlayingCard;
import sanguine.model.SanguineStandard;


/**
 * Board places down max value cards when it can.
 */
public class MaxRowScore implements StratInterface {

  private static boolean canPlace(Board board, SanguinePlayingCard card,
                                  int row, int col, Player player) {
    Cell cell = board.getCell(row, col);

    return cell.hasOwner() && cell.isOwnedBy(player)
        && !cell.hasCard() && cell.getPawnCount() >= card.getCost();

  }

  //getMaxRow
  @Override
  public Optional<Moves> move(SanguineStandard model, Player anIn) {
    List<SanguinePlayingCard> hand = model.getCurrentHand();
    if (hand.isEmpty()) {
      System.out.println("Hand is empty, no moves possible");
      return Optional.empty();
    }
    Board board = model.getBoard();
    Player opponent;
    if (anIn == Player.RED) {
      opponent = Player.BLUE;
    } else if (anIn == Player.BLUE) {
      opponent = Player.RED;
    } else {
      opponent = (anIn == Player.RED) ? Player.BLUE : Player.RED;
    }
    System.out.println("Current hand: " + hand);
    for (int row = 0; row < board.getRowNum(); row++) {
      System.out.println("Checking row: " + row);
      //we get our score and check row score
      int anInScore = board.getRowScore(row, anIn);
      System.out.println("AIScore: " + anInScore);
      int opponentScore = board.getRowScore(row, opponent);
      if (model.getCurrentPlayer() == anIn) {
        board.getRowScore(row, model.getCurrentPlayer());
        try {
          for (int col = 0; col < board.getColNum(); col++) {
            for (int card = 0; card < hand.size(); card++) {
              SanguinePlayingCard playingCard = hand.get(card);
              System.out.println("  Trying card " + card + ": " + playingCard.getName()
                  + " (cost: " + playingCard.getCost() + ")");
              if (canPlace(board, playingCard, row, col, anIn)) {
                //we place a card down
                System.out.println("Placing card");
                int newScore = anInScore + playingCard.getCost();
                if (newScore > opponentScore) {
                  //model.placeCard(playingCard, row, col, AnIn);
                  System.out.println("Move = maxRow done");
                  return Optional.of(new Moves(card, row, col));
                } //passes if not valid
              }
            }
          }
        } catch (Exception e) {
          return Optional.empty();
        }
      }
    }
    System.out.println("No moves found");
    return Optional.empty();
  }
}
