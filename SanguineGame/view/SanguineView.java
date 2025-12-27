package sanguine.view;

import java.io.IOException;
import sanguine.model.Board;
import sanguine.model.Cell;
import sanguine.model.Player;
import sanguine.model.SanguinedModel;


//possibly put gui here? or
//make new class for gui
/**
 * this class handles our view for the model,
 * showing in the system the board and all moves after
 * each step done automatically.
 */
public class SanguineView implements TextView {

  private final SanguinedModel<?> model;
  private final Appendable appendable;


  /**
   * our sanguine-view constructor that takes in
   * generic (our sanguineplayingcard) model.
   *
   * @param model our model with playing cards
   */
  public SanguineView(SanguinedModel<?> model) {
    this.model = model;
    this.appendable = System.out;
  }

  /**
   * returns our board to string.
   *
   * @return sb.toString() which is our stringbuilder for our board
   */
  public String toString() {
    Board board = model.getBoard();
    StringBuilder sb = new StringBuilder();

    for (int row = 0; row < board.getRowNum(); row++) {

      int redRowScore = calculateRowScore(board, row, Player.RED);
      int blueRowScore = calculateRowScore(board, row, Player.BLUE);

      sb.append(redRowScore).append(" ");

      for (int col = 0; col < board.getColNum(); col++) {
        Cell cell = board.getCell(row, col);
        sb.append(renderCell(cell));
      }

      sb.append(" ").append(blueRowScore);

      if (row < board.getRowNum() - 1) {
        sb.append("\n");
      }


    }
    return sb.toString();

  }


  /**
   * renders every single cell on the board and
   * their characters, _ for empty, R for red, B for blue.
   *
   * @param cell our  cell at a position.
   * @return character on a board
   */
  private String renderCell(Cell cell) {
    if (cell.hasCard()) {
      return cell.isOwnedBy(Player.RED) ? "R" : "B";
    } else if (cell.hasOwner()) {
      //char owner = cell.isOwnedBy(Player.RED) ? 'R' : 'B';
      return String.valueOf(cell.getPawnCount());
    } else {
      return "_";
    }
  }

  /**
   * returns the score of a single row.
   *
   * @param board our board object
   * @param row our row of a cell to get our score
   * @param player our player of that row
   * @return score of the entire row
   */
  private int calculateRowScore(Board board, int row, Player player) {
    int score = 0;
    for (int col = 0; col < board.getColNum(); col++) {
      Cell cell = board.getCell(row, col);
      if (cell.hasCard() && cell.isOwnedBy(player)) {
        score += cell.getCard().getValue();
      }
    }
    return score;
  }


  @Override
  public void render() throws IOException {
    appendable.append(toString()).append("\n");
  }
}
