package sanguine.model;

import java.util.Arrays;

/**
 * This is our class for playing card.
 * Our rangeSize is the influence grid that checks whether
 * pawns are able to be placed after the card placement.
 * If able to, pawns get placed on board on true tiles from
 * influence grid.
 */
public class SanguinePlayingCard implements SanguinedCard {
  private final String name;
  private final int value;
  private final int cost;
  //5 by 5 influence of board (or custom board size)
  private final InfluenceGrid rangeSize;

  //each card has cost: how many pawns it takes to place down
  //each card has value: which is how many points it contributes to each row

  /**
   * sanguineplaying card constructor for initialization
   * of card.
   *
   * @param name of card
   * @param value of card
   * @param cost of card
   * @param rangeSize of card - our influence grid
   */
  public SanguinePlayingCard(String name, int value, int cost, InfluenceGrid rangeSize) {
    if (cost < 1 || cost > 3) {
      throw new IllegalArgumentException("Cost must be between 1 and 3");
    }
    if (name.isEmpty()) {
      throw new IllegalArgumentException("Name/Range cannot be empty");
    }
    this.value = value;
    this.name = name;
    this.cost = cost;
    this.rangeSize = rangeSize;
  }
  //we can either use pawn numbers and place those strings, or
  //we can represent them as strings ("3", "1" etc.)
  //we can figure out design imp and change here

  /**
   * Factory design pattern for a new playing card.
   *
   */
  public static SanguinePlayingCard create(String name, int value,
                                           int cost, InfluenceGrid rangeSize) {
    return new SanguinePlayingCard(name, value, cost, rangeSize);
  }

  /**
   * returns the type of card (number too).
   *
   * @return type + suit
   */
  @Override
  public String toString() {
    return "(Val:" + value + ",Cost:" + cost + " " + name + ")";
  }

  /**
   * Checks if object is a card.
   *
   * @param obj declares object
   * @return this.toString().equals(other.toString ())
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof SanguinedCard other)) {
      return false;
    }
    return this.toString().equals(other.toString());
  }

  /**
   * Gets the name of the card.
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * gets our cards place value.
   *
   * @return value
   */
  public int getValue() {
    return value;
  }

  /**
   * returns the cost of a card.
   *
   * @return cost
   */
  public int getCost() {
    return cost;
  }

  public InfluenceGrid getRangeSize() {
    return rangeSize;
    // Implement later on
  }


  /**
   * turns the int to hashcode saving position.
   *
   * @return this.toString().hashCode()
   */
  @Override
  public int hashCode() {
    return this.toString().hashCode();
  }


}