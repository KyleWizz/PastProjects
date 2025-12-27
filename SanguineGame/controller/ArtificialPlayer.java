package sanguine.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import sanguine.Sanguine;
import sanguine.model.Player;
import sanguine.model.SanguineStandard;
import sanguine.strategy.Moves;
import sanguine.strategy.StratInterface;

/**
 * This class is so our main can have
 * one human player and one AI player.
 * Checks all moves for specified strar.
 */
public class ArtificialPlayer implements PlayerNotifier {

  public Player player;
  List<PlayerMoveListener> listeners;
  private SanguineStandard model;
  private StratInterface strat;

  /**
   * our public constructor for AI.
   * takes in model, player, strat and stores.
   *
   * @param player player color
   * @param model model sanguine standard
   * @param strat strategy given
   */
  public ArtificialPlayer(Player player,
                          SanguineStandard model, StratInterface strat) {
    this.listeners = new ArrayList<>();
    this.player = player;
    this.model = model;
    this.strat = strat;
  }

  @Override
  public void notifyPlayerTurn() {
    Optional<Moves> moves = strat.move(model, player);

    if (moves.isPresent()) {
      Moves move = moves.get();
      System.out.println("AI MOVE: " + move.toString() + "Card "
          + move.getCardIndex() + "Row: " + move.getRow()
          + "| Column " + move.getCol());
      for (PlayerMoveListener listener : listeners) {
        listener.onCardSelected(move.getCardIndex());
      }
      for (PlayerMoveListener listener : listeners) {
        listener.onCellSelected(move.getRow(), move.getCol());
      }
      for (PlayerMoveListener listener : listeners) {
        listener.onConfirmMove();
      }
    } else {
      System.out.println("No moves detected in AI!");
      for (PlayerMoveListener listener : listeners) {
        System.out.println("pass");
        listener.onPass();
      }
    }
  }

  @Override
  public void addPlayerMoveListener(PlayerMoveListener listener) {
    if (listeners == null) {
      throw new IllegalStateException("listeners is null");
    }
    listeners.add(listener);
  }
}
