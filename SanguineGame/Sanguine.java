package sanguine;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import sanguine.controller.FileReader;
import sanguine.model.Board;
import sanguine.model.Cell;
import sanguine.model.Player;
import sanguine.model.SanguinePlayingCard;
import sanguine.model.SanguineStandard;
import sanguine.view.SanguineView;


/**
 * our sanguine main class.
 * We can put in different deck files,
 * both decks share the same configuration file
 * and it is unshuffled as per instructions.
 * I have made two config files, deckone and decktwo.
 * Both should play out the same each time.
 * Automatically placed cards made.
 */
public class Sanguine {

  /**
   * Main method to run everything.
   *
   * @param args how the user inputs.
   */
  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("Usage: Sanguine <deck>");
      return;
    }

    String deckPath = args[0];
    File deck = new File(deckPath);
    System.out.println("Deck path: " + deckPath);
    try {
      FileReader fr = new FileReader(deck);
      SanguineStandard model = new SanguineStandard();
      Board board = new Board(3, 5);
      List<SanguinePlayingCard> playingCards = fr.readConfig();
      model.startGame(board, playingCards, playingCards, Player.RED, Player.BLUE);
      SanguineView view = new SanguineView(model);
      view.render();

      playGame(model, view);


      //fixing

    } catch (Exception e) {
      System.out.println("Error starting game: " + e.getMessage());
    }

  }


  // instructions say to make game play automatically
  private static void playGame(SanguineStandard model, SanguineView view) throws IOException {
    //int consecutivePasses = 0; have in model.
    int turnCount = 0;

    while (!model.isGameOver()) {
      turnCount++;
      Player currentPlayer = model.getCurrentPlayer();
      System.out.println("Current player moved: " + currentPlayer);
      System.out.println("Current turn count: " + turnCount);
      boolean cardPlaced = tryPlace(model, currentPlayer);
      if (!cardPlaced) {
        //if there's no more spots we pass
        System.out.println("Player pass: " + currentPlayer);
        model.pass();
      }
      view.render();
    }
    showResults(model, view);
  }

  //i think we have this somewhere
  // let me grab coffee rq
  // ok
  private static boolean canPlace(Board board, SanguinePlayingCard card,
                                  int row, int col, Player player) {
    Cell cell = board.getCell(row, col);

    return cell.hasOwner() && cell.isOwnedBy(player)
        && !cell.hasCard() && cell.getPawnCount() >= card.getCost();

  }

  private static boolean tryPlace(SanguineStandard model, Player player) {
    Board board = model.getBoard();
    List<SanguinePlayingCard> hand = model.getCurrentHand();

    System.out.println("Current hand: " + hand);

    for (SanguinePlayingCard card : hand) {
      try {
        for (int row = 0; row < board.getRowNum(); row++) {
          for (int col = 0; col < board.getColNum(); col++) {
            if (canPlace(board, card, row, col, player)) {
              System.out.println(player + " Placed" + card + " at: " + row + " " + col);
              model.placeCard(card, row, col, player);
              System.out.println(player + "placed: " + card.getName() + " at: " + row + " " + col);
              return true;
            }
          }
        }
      } catch (Exception e) {
        continue;
      }

    }
    return false;
  }

  private static void showResults(SanguineStandard model, SanguineView view) {

    Board board = model.getBoard();
    System.out.println("Game over: ");
    int redScore = board.getTotalScore(Player.RED);
    int blueScore = board.getTotalScore(Player.BLUE);

    System.out.println("Red Score: " + redScore);
    System.out.println("Blue Score: " + blueScore);

    if (redScore > blueScore) {
      System.out.println("\nWinner: RED");
    } else if (blueScore > redScore) {
      System.out.println("\nWinner: BLUE");
    } else {
      System.out.println("\nWinner: TIE");
    }
    //I think we can use total score - yes ok fire so this is good - yes i believe
    // ok wait so one more quesiton do we only get results when game is over?
    // yes but we show row score throughot game by view so each time move m,ade
    //board updfates
  }


}
