package sanguine.model;

/**
 * our enumerator that creates our
 * players.
 */
public enum Player {
  RED, BLUE;


  public char getColor() {
    return switch (this) {
      case RED -> 'R';
      case BLUE -> 'B';
    };
  }

  //return Red or Blue

  /**
   * returns red or blue player.
   *
   * @return player.toString()
   */
  public String toString() {
    return switch (this) {
      case RED -> "Red";
      case BLUE -> "Blue";
    };
  }


}
