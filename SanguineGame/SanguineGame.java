package sanguine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import sanguine.controller.FileReader;
import sanguine.model.Board;
import sanguine.model.Player;
import sanguine.model.SanguinePlayingCard;
import sanguine.model.SanguineStandard;
import sanguine.view.InFeatures;
import sanguine.view.InFrame;
import sanguine.view.SanguineFrame;
import sanguine.view.SanguinedFeatures;




/**
 * SanguineGame main class gui.
 */
public class SanguineGame {

  /**
   * main method.
   *
   * @param args args
   * @throws FileNotFoundException file
   */
  public static void main(String[] args) throws FileNotFoundException {

    try {
      SanguineStandard model = new SanguineStandard();
      //bug in panel..
      int numRows = 5;
      int numCol = 7;
      Board board = new Board(numRows, numCol);

      String deckPath;
      if (args.length > 0) {
        deckPath = args[0];
      } else {
        deckPath = "src//main//java//sanguine//deck3config";
      }
      File deckFile = new File(deckPath);
      System.out.println("Deck path: " + deckPath);
      FileReader fr = new FileReader(deckFile);
      List<SanguinePlayingCard> playingCards = fr.readConfig();

      SanguineGame game = new SanguineGame();
      model.startGame(board, playingCards, playingCards, Player.RED, Player.BLUE);
      InFrame frame = new SanguineFrame(model, model.getCurrentPlayer());

      InFeatures features = new SanguinedFeatures(model, frame, Player.RED);
      frame.setVisible(true);
      frame.addFeatures(features);
    } catch (FileNotFoundException e) {
      System.out.println("File not found" + e.getMessage());
      e.printStackTrace();
      System.exit(0);
    } catch (Exception e) {
      System.err.println("Something went wrong" + e.getMessage());
      e.printStackTrace();
      System.exit(0);
    }
  }
}
