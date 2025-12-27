package sanguine.controller;


//pass cards through model
// create the cards while reading the file
// file reader that returns a deck of card
// pass in through scanner
//iterate through the scanner
//main class set up the file
//

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import sanguine.model.InfluenceGrid;
import sanguine.model.SanguinePlayingCard;


/**
 * our class to read files for deck configurations.
 * This is for our main and view.
 */
public class FileReader {

  boolean[][] pattern;
  List<SanguinePlayingCard> deck;
  File configFile;

  /**
   * our filereader constructor.
   *
   * @param configFile our deck file.
   */
  public FileReader(File configFile) {
    this.configFile = configFile;
    this.deck = new ArrayList<>();
  }

  /**
   * this is a method that reads the file when called in main.
   *
   * @return deck from file
   * @throws FileNotFoundException if file is not in the right path.
   */
  public List<SanguinePlayingCard> readConfig() throws FileNotFoundException {

    Scanner scanner = new Scanner(configFile);

    while (scanner.hasNextLine()) {
      if (!configFile.exists()) {
        throw new FileNotFoundException(configFile.getAbsolutePath());
      }
      String line = scanner.nextLine();
      if (line.isEmpty()) {
        continue;
      }
      String[] split = line.split("\\s+");
      if (split.length == 3) {
        String name = split[0];
        int value = Integer.parseInt(split[1]);
        int cost = Integer.parseInt(split[2]);
        boolean[][] pattern = readPattern(scanner, name);

        InfluenceGrid grid = new InfluenceGrid(pattern);
        SanguinePlayingCard card = new SanguinePlayingCard(name, value, cost, grid);
        deck.add(card);
      }
    }
    scanner.close();
    if (deck.isEmpty()) {
      throw new IllegalArgumentException("No cards found in file");
    }

    return deck;
  }


  private boolean[][] readPattern(Scanner scanner, String name) {
    boolean[][] pattern = new boolean[5][5];
    for (int row = 0; row < 5; row++) {
      if (!scanner.hasNextLine()) {
        scanner.close();
        throw new IllegalArgumentException("No pattern: " + name);
      }
      String patternString = scanner.nextLine();
      if (patternString.length() != 5) {
        throw new IllegalArgumentException("No pattern: " + patternString);
      }
      for (int col = 0; col < 5; col++) {
        char ch = patternString.charAt(col);
        pattern[row][col] = ch == 'I';
      }
    }
    return pattern;
  }

  private List<SanguinePlayingCard> getDeck() {
    return deck;
  }
}

