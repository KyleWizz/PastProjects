package sanguine.strategy;

import java.util.List;
import java.util.Optional;
import sanguine.model.Board;
import sanguine.model.Cell;
import sanguine.model.Player;
import sanguine.model.SanguinePlayingCard;
import sanguine.model.SanguineStandard;



/**
 * minimizes maximum value out of
 * placements.
 */
public class MinMax implements StratInterface {
  private static boolean canPlace(Board board, SanguinePlayingCard card,
                                  int row, int col, Player player) {
    Cell cell = board.getCell(row, col);

    return cell.hasOwner() && cell.isOwnedBy(player)
        && !cell.hasCard() && cell.getPawnCount() >= card.getCost();

  }


  @Override
  public Optional<Moves> move(SanguineStandard model, Player anIn) {
    Board board = model.getBoard();
    List<SanguinePlayingCard> hand = model.getCurrentHand();
    Player opponent = (anIn == Player.RED) ? Player.BLUE : Player.RED;
    int bestScore = Integer.MIN_VALUE;
    Moves move = null;
    System.out.println("Current hand: " + hand);
    for (int row = 0; row < board.getRowNum(); row++) {
      try {
        for (int col = 0; col < board.getColNum(); col++) {
          System.out.println("Checking row: " + row + "," + col);
          for (int card = 0; card < hand.size(); card++) {
            SanguinePlayingCard playingCard = hand.get(card);
            if (canPlace(board, playingCard, row, col, anIn)) {
              int newScore = evaulateMove(model, row, playingCard, anIn, opponent);
              if (newScore > bestScore) {
                bestScore = newScore;
                System.out.println("Move = minMax");
                move = new Moves(card, row, col);
              }
            }
          }
        }
      } catch (Exception e) {
        return Optional.empty();
      }
    }
    if (Optional.ofNullable(move).isPresent()) {
      return Optional.of(move);
    }
    System.out.println("No moves found");
    return Optional.empty();
  }

  private int evaulateMove(SanguineStandard model, int row, SanguinePlayingCard card,
                           Player anIn, Player opponent) {
    Board board = model.getBoard();
    int currentAnInScore = board.getRowScore(row, anIn);
    int currentOpponentScore = board.getRowScore(row, opponent);
    int newAnInScore = currentAnInScore + card.getCost();
    int rowAdvantage = newAnInScore - currentOpponentScore;

    int totalAnInScore = 0;
    int totalOpponentScore = 0;
    for (int rowAnIn = 0; rowAnIn < board.getRowNum(); rowAnIn++) {
      //check row index
      if (rowAnIn == row) {
        totalAnInScore += newAnInScore;
      } else {
        totalAnInScore += board.getRowScore(rowAnIn, anIn);
      }
      totalOpponentScore += board.getRowScore(rowAnIn, opponent);
      System.out.println("opponent score: " + totalOpponentScore);
      System.out.println("oppenent curr score" + currentOpponentScore);
      System.out.println("AI score: " + currentAnInScore);
      System.out.println("AI score total: " + totalAnInScore
          + "opponent: " + opponent);
    }

    int overallAdvantage = totalAnInScore - totalOpponentScore;
    System.out.println("Overall advantage: " + overallAdvantage);
    return rowAdvantage * 2 + overallAdvantage;
  }
}


