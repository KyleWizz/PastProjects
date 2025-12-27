package sanguine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import sanguine.controller.ArtificialPlayer;
import sanguine.controller.FileReader;
import sanguine.controller.PlayerNotifier;
import sanguine.controller.SanguineController;
import sanguine.model.Board;
import sanguine.model.Player;
import sanguine.model.SanguinePlayingCard;
import sanguine.model.SanguineStandard;
import sanguine.strategy.ControlBoard;
import sanguine.strategy.FillStrategy;
import sanguine.strategy.MaxRowScore;
import sanguine.strategy.MinMax;
import sanguine.view.InFeatures;
import sanguine.view.SanguineFrame;
import sanguine.view.SanguinedFeatures;




/**
 * SanguineGame main class gui. Take sin 6 arguments
 * for row numbers, 2 deck files, and two types
 * human or ai for players.
 */

public final class TwoViewSanguine {

  /**
   * our main method to run against either ai or human,
   * or whatever decks/player you want to use, depending
   * on args.
   *
   * @param args arguments
   * @throws FileNotFoundException exceptions
   */
  public static void main(String[] args) throws FileNotFoundException {
    int numRows;
    int numCol;
    SanguineStandard model = new SanguineStandard();

    String deckRed;
    String deckBlue;
    String player1Type; // human by default!
    String player2Type;
    if (args.length < 6) {
      throw new IllegalArgumentException("Must be 6 arguments");
    } else {
      deckRed = args[0];
      deckBlue = args[1];
      numRows = Integer.parseInt(args[2]);
      numCol = Integer.parseInt(args[3]);
      player1Type = args[4];
      player2Type = args[5];
    }

    System.out.println("Deck path: " + deckBlue);

    SanguineFrame viewPlayer1 = new SanguineFrame(model, Player.RED);
    SanguineFrame viewPlayer2 = new SanguineFrame(model, Player.BLUE);
    //
    viewPlayer1.setLocation(10, 50);
    viewPlayer2.setLocation(610, 50);
    InFeatures featuresRed = new SanguinedFeatures(model, viewPlayer1, Player.RED);
    PlayerNotifier player1 = playerCreator(model, Player.RED, player1Type);
    PlayerNotifier player2 = playerCreator(model, Player.BLUE, player2Type);
    SanguineController controller1 =
        new SanguineController(model, Player.RED, viewPlayer1, player1);
    viewPlayer1.addPlayerMoveListener(controller1);
    viewPlayer1.addFeatures(featuresRed);
    viewPlayer1.setVisible(true);

    SanguineController controller2 =
        new SanguineController(model, Player.BLUE, viewPlayer2, player2);
    viewPlayer2.addPlayerMoveListener(controller2);
    InFeatures featuresBlue = new SanguinedFeatures(model, viewPlayer2, Player.BLUE);
    viewPlayer2.addFeatures(featuresBlue);

    File deckFileRed = new File(deckRed);
    File deckFileBlue = new File(deckBlue);
    System.out.println("Deck path: " + deckRed);
    FileReader frRed = new FileReader(deckFileRed);
    FileReader frBlue = new FileReader(deckFileBlue);
    Board board = new Board(numRows, numCol);
    List<SanguinePlayingCard> playingCardsRed = frRed.readConfig();
    List<SanguinePlayingCard> playingCardsBlue = frBlue.readConfig();
    viewPlayer2.setVisible(true);
    model.startGame(board, playingCardsRed, playingCardsBlue, Player.RED, Player.BLUE);
  }

  private static PlayerNotifier playerCreator(SanguineStandard model, Player player,
                                              String type) {
    return switch (type.toLowerCase()) {
      case "human" -> null;
      case "strategy1" -> new ArtificialPlayer(player, model, new FillStrategy());
      case "strategy2" -> new ArtificialPlayer(player, model, new MaxRowScore());
      case "strategy3" -> new ArtificialPlayer(player, model, new MinMax());
      case "strategy4" -> new ArtificialPlayer(player, model, new ControlBoard());
      default -> {
        System.out.println("Unknown player type: " + type + "Default: human");
        yield null;
      }
    };
  }
}