package sanguine.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import sanguine.model.Board;
import sanguine.model.Cell;
import sanguine.model.InfluenceGrid;
import sanguine.model.Player;
import sanguine.model.SanguinePlayingCard;
import sanguine.model.SanguinedModel;


//we were pressed on time so sorry TA, it's very messy and might is poor design
//we are trying to fix.
//very sorry for the rushed
//we need mouselistener class

/**
 * our panel handles draw and feature implementation.
 */
public class SanguinePanel extends JPanel implements InPanelView {
  private final SanguinedModel<SanguinePlayingCard> model;
  private SanguinedFeatures features;
  private Player playerHand;
  private JLabel scoreLabel;
  private JLabel turnLabel;
  //player fore view
  private Player playerViewType;
  JButton passButton = new JButton("Pass");

  private JPanel createButtonPanel() {

    int startX = getX();
    int startY = getY();
    passButton = new JButton("Pass");
    passButton.setFont(new Font("Arial", Font.BOLD, 16));
    passButton.setBackground(Color.CYAN);
    JPanel buttonPanel = new JPanel();
    passButton.addActionListener(e -> {
      if (features != null) {
        features.pass();
        updateTurnLabel();
      }
    });
    return buttonPanel;
  }

  private void updateTurnLabel() {
    if (model.getCurrentPlayer() != null) {
      String currentPlayer = model.getCurrentPlayer().toString();
      turnLabel.setText("Current Turn: " + model.getHandSize());
    }
  }

  /**
   * Sanguine Panel.
   *
   * @param model our model.
   */
  public SanguinePanel(SanguinedModel<SanguinePlayingCard> model, Player player) {
    this.model = model;
    this.playerViewType = player;
    setBackground(Color.WHITE);
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        handleMouseClick(e.getX(), e.getY());
      }
    });
  }

  private void handleMouseClick(int x, int y) {
    if (features == null || model.getBoard() == null) {
      return;
    }

    Board board = model.getBoard();
    int numRows = board.getRowNum();
    int numCols = board.getColNum();

    int handY = getHandStartY();


    if (y >= handY + 230) {
      int cardIndex = getCardIndexClick(x, numRows, numCols);
      if (cardIndex >= 0 && cardIndex < model.getHandSize()) {
        if (features.getSelectedCardIndex() == cardIndex) {
          features.setSelectedCard(-1);
          System.out.println("Card " + cardIndex + " deselected");
        } else {
          features.clickCard(cardIndex, model.getCurrentPlayer());
        }

        repaint();
      }
      return;
    }

    int[] cell = getCellClick(x, y, numRows, numCols);
    if (cell != null) {
      int row = cell[0];
      int col = cell[1];
      if (features.getSelectedCardIndex() >= 0
          &&
          features.getSelectedCardIndex() < model.getHandSize()) {
        features.setSelectedCell(row, col);
        features.confirm();
      }
      repaint();

    }
  }

  private int getHandStartY() {
    Board board = model.getBoard();
    int numRows = board.getRowNum();
    int numCols = board.getColNum();
    return DimensionClass.HAND_HEIGHT + DimensionClass.calculateCardSize(numRows, numCols);
  }

  private int getCardIndexClick(int x, int numRows, int numCols) {
    int startX = DimensionClass.calculateCardSize(numRows, numCols) / 2;
    int cardX = startX * 2 + 10 + DimensionClass.PADDING_WIDTH;
    int cardWidth = DimensionClass.calculateCardSize(numRows, numCols);

    int index = (x - cardX) / cardWidth;
    return (x >= cardX && index < model.getHandSize()) ? index : -1;
  }

  private int[] getCellClick(int x, int y, int numRows, int numCols) {
    int cellSize = DimensionClass.calculateCellSize(numRows, numCols);
    Board board = model.getBoard();
    numRows = board.getRowNum();
    numCols = board.getColNum();
    int totalBoardWidth = numCols * cellSize;
    int totalBoardHeight = numRows * cellSize;
    int startX = (getWidth() / 2) + DimensionClass.PADDING_WIDTH - (totalBoardWidth / 2);
    int startY = (getHeight() / 5) - DimensionClass.PADDING_WIDTH - (totalBoardHeight / 5);

    int col = (x - startX) / cellSize;
    int row = (y - startY) / cellSize;

    if (row >= 0 && row < numRows && col >= 0 && col < numCols) {
      return new int[]{row, col};
    }
    return null;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);


    Graphics2D g2 = (Graphics2D) g;
    drawBoard(g2);
    drawCard(g2);
    drawScore(g2);
  }

  private void highlightCard() {

  }

  private void drawCell(Graphics2D g, int row, int col, int x, int y) {
    Board board = model.getBoard();
    Cell cell = board.getCell(row, col);
    int numRows = board.getRowNum();
    int numCols = board.getColNum();
    int cellSize = DimensionClass.calculateCellSize(numRows, numCols);
    //Cell cell = board.getCell(row, col);
    //boolean highlight = false;
    if (row == getSelectedRow() && col == getSelectedCol()) {
      g.setColor(Color.ORANGE);
    } else if (cell.isOwnedBy(Player.RED)) {
      g.setColor(Color.PINK);
    } else if (cell.isOwnedBy(Player.BLUE)) {
      g.setColor(Color.CYAN);
    } else {
      g.setColor(Color.LIGHT_GRAY);
    }
    g.fillRect(x, y, cellSize, cellSize);

    g.setColor(Color.BLACK);
    g.drawRect(x, y, cellSize, cellSize);
    if (cell.hasCard()) {
      g.setFont(new Font("Times_New_Roman", Font.BOLD, 15));
      SanguinePlayingCard card = cell.getCard();
      drawText(g, String.valueOf(card.getName()), x + DimensionClass.PADDING_WIDTH, y
          + DimensionClass.PADDING_WIDTH - 30, 3, 3);
      drawText(g, "Cost: " + String.valueOf(card.getValue()),
          x + DimensionClass.PADDING_WIDTH, y + DimensionClass.PADDING_WIDTH - 10, 3, 3);
      drawText(g, "Value: " + String.valueOf(card.getValue()),
          x + DimensionClass.PADDING_WIDTH, y + DimensionClass.PADDING_WIDTH + 5, 3, 3);
      drawInfluencedCellHand(g, card, x, y - 6,
          DimensionClass.calculateCellSize(numRows, numCols) - 20);
    }
  }

  /**
   * Draw Board method with dimensions.
   *
   * @param g graphics
   */
  public void drawBoard(Graphics2D g) {
    Board board = model.getBoard();
    int numRows = board.getRowNum();
    int numCol = board.getColNum();

    int cellSize = DimensionClass.calculateCellSize(numRows, numCol);
    int totalBoardWidth = numCol * cellSize;
    int totalBoardHeight = numRows * cellSize;
    int startX = (getWidth() / 2) + DimensionClass.PADDING_WIDTH - (totalBoardWidth / 2);
    int startY = (getHeight() / 5) - DimensionClass.PADDING_WIDTH - (totalBoardHeight / 5);

    for (int row = 0; row < numRows; row++) {
      for (int col = 0; col < numCol; col++) {
        int x = startX + (col * cellSize);
        int y = startY + (row * cellSize);
        Cell cell = board.getCell(row, col);
        drawCell(g, row, col, x, y);
        drawPawn(g, board.getCell(row, col), x, y, cellSize, cellSize);
      }
    }

  }

  /**
   * our public drawPawn method for the board.
   *
   * @param g      graphics
   * @param cell   cells
   * @param x      x
   * @param y      y
   * @param width  width
   * @param height height
   */
  public void drawPawn(Graphics2D g, Cell cell, int x, int y, int width, int height) {

    if (cell.hasCard()) {
      return;
    }
    int pawnCount = cell.getPawnCount();

    if (cell.getCellOwner() == Player.RED) {
      g.setColor(Color.RED);
    } else if (cell.getCellOwner() == Player.BLUE) {
      g.setColor(Color.BLUE);
    } else {
      return;
    }
    g.setFont(new Font("Times_New_Roman", Font.BOLD, 36));
    String pawnText = String.valueOf(pawnCount);

    FontMetrics metric = g.getFontMetrics();
    int textWidth = metric.stringWidth(pawnText);
    int textHeight = metric.getHeight();

    int textX = x + (width - textWidth) / 2;
    int textY = y + (height + textHeight) / 2 - metric.getDescent();

    g.drawString(pawnText, textX, textY);

  }

  private Color checkPlayerColor(Graphics2D g) {
    return playerViewType == Player.BLUE ? Color.BLUE : Color.PINK;
  }

  private void drawScore(Graphics2D g) {
    Board board = model.getBoard();
    int numRows = board.getRowNum();
    int numCols = board.getColNum();

    int cellSize = DimensionClass.calculateCellSize(numRows, numCols);
    int totalBoardHeight = numRows * cellSize;
    int startY = (getHeight() / 5) - DimensionClass.PADDING_WIDTH - (totalBoardHeight / 5);
    for (int row = 0; row < numRows; row++) {
      //      int scoreRed = board.getRowScore(row, Player.RED);
      //      int scoreBlue = board.getRowScore(row, Player.BLUE);
      int score = board.getRowScore(row, playerViewType);
      int y = startY + (row * cellSize) + (cellSize / 2);

      g.setColor(Color.BLACK);
      String scoreText = String.valueOf(score);
      FontMetrics fm = g.getFontMetrics();
      int textWidth = fm.stringWidth(scoreText);
      int x = (getWidth() - textWidth);

      g.setColor(checkPlayerColor(g));
      g.drawString(scoreText, x, y + fm.getDescent());
    }
  }

  private void drawCard(Graphics2D g) {
    Board board = model.getBoard();
    int numRows = board.getRowNum();
    int numCol = board.getColNum();
    int startX = DimensionClass.calculateCardSize(numRows, numCol);
    int startY = HEIGHT + DimensionClass.calculateCardSize(numRows, numCol) / 2;
    g.setColor(Color.PINK); //we could just make it
    List<SanguinePlayingCard> hand = model.getHandFor(playerViewType);
    System.out.println("drawCard: playerViewType=" + playerViewType);
    int selectedCardIndex = getSelectedCard();
    for (int card = 0; card < hand.size(); card++) {
      g.setColor(checkPlayerColor(g));
      if (card == selectedCardIndex && selectedCardIndex >= 0) {
        g.setColor(Color.RED); // highlights card selected
      } else {
        g.setColor(checkPlayerColor(g));
      }
      int cardX = (int) (startX - 5 + DimensionClass.PADDING_WIDTH
          + card * DimensionClass.calculateCardSize(numRows, numCol));
      int cardY = (startY * 6) - DimensionClass.PADDING_WIDTH / 3;
      int cardWidth = startX + (200 / 10);
      int cardHeight = startY + 200 * 3;
      g.fillRect(cardX, cardY, cardWidth - 20, cardHeight);
      g.setColor(Color.BLACK);
      g.drawRect(cardX, cardY, cardWidth - 20, cardHeight);
      g.setColor(checkPlayerColor(g));
      SanguinePlayingCard playingCard = hand.get(card);
      g.setFont(new Font("Times_New_Roman", Font.BOLD, 20));
      g.setColor(Color.BLACK);
      drawText(g, playingCard.getName(), cardX * 1 + (card) + 30, cardY, 20, 20);
      g.setFont(new Font("Times_New_Roman", Font.BOLD, 15));
      drawText(g, "Value: " + String.valueOf(playingCard.getValue()),
          cardX * 1 + (card) + 30, cardY + 20, 20, 20);
      drawText(g, "Cost:" + String.valueOf(playingCard.getCost()),
          cardX * 1 + (card) + 30, cardY + 40, 20, 20);
      drawInfluencedCellHand(g, playingCard, cardX, cardY, cardWidth);
      g.setColor(checkPlayerColor(g));
    }
  }

  private void drawInfluencedCellHand(Graphics2D g, SanguinePlayingCard card, int x,
                                      int y, int width) {
    InfluenceGrid grid = card.getRangeSize();

    int cellSize = width / 7;
    if (model.getCurrentPlayer() == Player.BLUE) {
      grid = grid.mirrorInfluenceGrid();
    }
    g.setColor(checkPlayerColor(g));
    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 5; col++) {
        int cellX = x + (col * cellSize + 20);
        int cellY = y + (row * cellSize + DimensionClass.PADDING_WIDTH + 20);

        if (row == 2 && col == 2) {
          g.setColor(Color.RED);
          g.fillRect(cellX, cellY, cellSize - 1,
              cellSize - 1);
          g.setColor(Color.black);
          g.drawString("C", cellX + cellSize / 3, cellY + cellSize / 2);
        } else if (grid.isInfluenced(row, col)) {
          g.setColor(Color.darkGray);
          g.fillRect(cellX, cellY, cellSize, cellSize);
          g.setColor(Color.black);
          g.drawString("I", cellX + cellSize / 3, cellY + cellSize / 2);
        } else {
          g.setColor(checkPlayerColor(g));
          g.drawRect(cellX, cellY, cellSize - 1, cellSize - 1);
        }
        g.setColor(Color.BLACK);
        g.drawRect(cellX, cellY, cellSize - 1, cellSize - 1);
      }
    }
  }

  private void drawText(Graphics2D g, String text, int x, int y, int width, int height) {
    FontMetrics metric = g.getFontMetrics();
    int textWidth = metric.stringWidth(text);
    int textHeight = metric.getHeight();
    int textX = x + (width - textWidth) / 2;
    int textY = y + (height + textHeight) / 2 - metric.getDescent();
    g.drawString(text, textX, textY);

  }


  @Override
  public void addFeaturesListener(InFeatures features) {
    this.features = (SanguinedFeatures) features;
  }


  /**
   * gets the selecte card out of index.
   *
   * @return selected card
   */
  public int getSelectedCard() {
    if (features != null) {
      return features.getSelectedCardIndex();
    }
    return -1;
  }

  /**
   * gets selected row of board.
   *
   * @return features.getSelectedRow() or -1 if invalid
   */
  public int getSelectedRow() {
    if (features != null) {
      return features.getSelectedRow();
    }
    return -1;
  }

  /**
   * gets selected column.
   *
   * @return -1 or features
   */
  public int getSelectedCol() {
    if (features != null) {
      return features.getSelectedCol();
    }
    return -1;
  }
}
