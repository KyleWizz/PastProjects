package sanguine.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import sanguine.controller.ModelStatusListener;
import sanguine.controller.PlayerMoveListener;
import sanguine.model.Player;
import sanguine.model.SanguinePlayingCard;
import sanguine.model.SanguineStandard;
import sanguine.model.SanguinedModel;


/**
 * handles our main gui frame.
 */
public class SanguineFrame extends JFrame implements InFrame {
  private SanguinedModel<SanguinePlayingCard> model = new SanguineStandard();
  private SanguinePanel panel1;
  private InFeatures features;
  private Player player;
  public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  private JLabel scoreLabel;
  private SanguinePanel scorePanelBlue;
  private SanguinePanel scorePanelRed;
  private JButton passButton;

  private List<PlayerMoveListener> actionListeners = new ArrayList<>();

  /**
   * player listener for subscribing to player.
   *
   * @param listener listener
   */
  public void addPlayerMoveListener(PlayerMoveListener listener) {
    if (listener == null) {
      throw new NullPointerException();
    }
    actionListeners.add(listener);
  }

  /**
   * Sanguine frame.  It is a little rushed and convoluted, but the main idea
   * is that we should have multiple panels that control different things
   * on the board (hands, boards etc.) and in the multiple panels we
   * draw them, ideally. Since we were rushed with the exam, we didn't really implement that.
   *
   * @param model Model of the game.
   */
  public SanguineFrame(SanguinedModel<SanguinePlayingCard> model, Player player) {
    super("Sanguine Card Game- " + model.getCurrentPlayer());
    this.model = model;
    this.player = player;






    this.setBackground(Color.WHITE);
    //this.setBounds(0, 0, 1600, 1000);
    this.setLayout(new BorderLayout());
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    panel1 = new SanguinePanel(model, player);
    scorePanelBlue = new SanguinePanel(model, player);
    scorePanelRed = new SanguinePanel(model, player);

    //    this.add(panel1, BorderLayout.CENTER);
    this.add(panel1, BorderLayout.CENTER);
    this.add(scorePanelBlue, BorderLayout.EAST);
    this.add(scorePanelRed, BorderLayout.WEST);


    JPanel buttonPanel = createButtonPanel();
    this.add(buttonPanel, BorderLayout.SOUTH);
    this.setSize(1600, 1000);
    this.setLocationRelativeTo(null);
    //if we wanted max
    //this.setExtendedState(JFrame.MAXIMIZED_BOTH);
  }

  private JPanel createButtonPanel() {
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

    passButton = new JButton("Pass");
    passButton.setFont(new Font("Arial", Font.BOLD, 20));
    passButton.setPreferredSize(new Dimension(150, 50));
    passButton.setBackground(Color.CYAN);
    passButton.setForeground(Color.WHITE);
    passButton.addActionListener(e -> {
      if (features != null) {
        features.pass();
      }
    });
    buttonPanel.add(passButton);
    return buttonPanel;
  }

  /**
   * This updates our score, red or blue, and sets it on the board. Little bug with dupes.
   *
   * @param redScore reds score
   * @param blueScore blues score
   */
  public void updateScore(int redScore, int blueScore) {
    if (scoreLabel != null) {
      scoreLabel.setText("Red: " + redScore + " | Blue: " + blueScore);
    }
  }


  @Override
  public void refresh() {
    this.repaint();
  }

  @Override
  public void addFeatures(InFeatures features) {
    this.features = features;
    panel1.addFeaturesListener(features);
  }

  private void notifyTurnChanged(Player player) {
    for (PlayerMoveListener listener : actionListeners) {
      listener.onConfirmMove();
    }
  }

  private void notifyGameOver(Player winner, int winningScore) {
    for (PlayerMoveListener listener : actionListeners) {
      listener.onPass();
    }
  }

  private void notifySelectedCard(int cardIndex) {
    for (PlayerMoveListener listener : actionListeners) {
      listener.onCardSelected(cardIndex);
    }
  }

  private void notifySelectedCell(int row, int col) {
    for (PlayerMoveListener listener : actionListeners) {
      listener.onCellSelected(row, col);
    }
  }
}
//the score side is same as cell height and size