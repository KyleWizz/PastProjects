package sanguine.model;


/**
 * Cell class for the cells of each of the grid.
 */
public class Cell {

  //invariant: pawnCount always 0-3
  private int pawnCount;
  private SanguinePlayingCard card;
  //invariant: cellOwner can never be null if pawncount > 0
  private Player cellOwner; // red/blue


  /**
   * Public constructor for Cell.
   */
  public Cell() {
    this.pawnCount = 0;
    this.cellOwner = null;
  }


  /**
   * Checks if the cell contains card.
   *
   * @return true if the cell has the card.
   */
  public boolean hasCard() {
    return card != null;
  }


  /**
   * Finds if it hasOwner or not.
   *
   * @return true if it has owner.
   */
  public boolean hasOwner() {
    return cellOwner != null;
    //  if is null than "_" is placed
  }

  public Player getCellOwner() {
    return cellOwner;
  }

  public int getPawnCount() {
    return pawnCount;
  }

  public SanguinePlayingCard getCard() {
    return card;
  }

  /**
   * Adds pawn to cell.
   *
   * @param owner of cell.
   */
  public void addPawn(Player owner) {
    if (this.card != null) { // do we want to continue after?
      throw new IllegalStateException("This cell has a card");
    }
    if (this.pawnCount >= 3) {
      System.out.println("This cell has maximum pawns.");
      return;
    }
    if (this.pawnCount == 0) {
      // Empty Cell - add first pawn
      this.pawnCount = 1;
      this.cellOwner = owner;
      // Has pawns but not at max and should increment if same owner
    } else if (isOwnedBy(owner)) {
      this.pawnCount++;
    }
  }


  /**
   * Converts the opponents pawn to your own color.
   *
   * @param newOwner of the pawn.
   */
  public void convertPawn(Player newOwner) {
    if (this.pawnCount == 0) {
      throw new IllegalStateException("This cell is not valid to convert!");
    }
    this.cellOwner = newOwner;
  }

  /**
   * Gets owner of cell.
   *
   * @param playerColor which color the cell is owned by.
   * @return the color the cell is owned by.
   */
  public boolean isOwnedBy(Player playerColor) {
    // red/blue
    return this.cellOwner == playerColor;
  }

  /**
   * Sets the cell to the pawn count.
   *
   * @param count the count of pawns.
   * @param owner the owner of the cell.
   */
  public void setPawns(int count, Player owner) {
    this.pawnCount = count;
    this.cellOwner = owner;
  }

  public void setCard(SanguinedCard card) {
    this.card = (SanguinePlayingCard) card;
  }


  public void setOwner(Player owner) {
    this.cellOwner = owner;
  }


}
