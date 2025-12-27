package sanguine.strategy;

import java.util.Optional;
import sanguine.Sanguine;
import sanguine.model.Player;
import sanguine.model.SanguineStandard;


/**
 * public strategy class for different game methods. takes
 * moves class and uses optional to check values AND
 * also check null errors, found in geeks-for-geeks doc.
 * This is ideal as the model has to check if there
 * is any value of moves for the model so
 * Returns optional with non-null val
 */
public interface StratInterface {
  /**
   * our strategy options.
   * takes in model and player to keep track of both.
   * Flexible for all strategies
   */
  Optional<Moves> move(SanguineStandard sanguine, Player player);


}
