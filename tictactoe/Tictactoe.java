package tictactoe;

// ******************************
// --- --- Predefined Classes Imports
// ******************************
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

// ******************************
// --- --- Utility Imports
// ******************************
import utility.Font;
import utility.Screen;
import utility.StringUtils;

public class Tictactoe {
  // ******************************
  // --- --- Class members
  // ******************************
  private static final Scanner scanner = new Scanner(System.in);
  private static final Random rand = new Random();
  private static final String USER_TURN = "X";
  private static final String COMPUTER_TURN = "O";
  private static final String INITIAL_TURN = USER_TURN;
  private static final String TIE = "TIE";
  // The 2d array for storing the 8 arrangements of 3 matching winning
  // arrangements
  private static final int[][] winTrios = {
      { 0, 1, 2 },
      { 3, 4, 5 },
      { 6, 7, 8 },
      { 0, 3, 6 },
      { 1, 4, 7 },
      { 2, 5, 8 },
      { 0, 4, 8 },
      { 2, 4, 6 },
  };
  // ******************************
  // --- --- Instance members
  // ******************************
  private String[] grid; // The array to store the 9 grid items
  private String turn; // Current turn (X or O)
  private String winner; // The winner (X or O)
  private String mode; // The mode (EASY or IMPOSSIBLE)
  private ElapsedTime elapsedTime; // Object for tracking the playing time

  // ******************************
  // --- --- Constructor
  // ******************************
  public Tictactoe() {
    reset();
  }

  private void reset() {
    turn = INITIAL_TURN;
    grid = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
    winner = null;
    mode = null;
    elapsedTime = null;
  }

  // ******************************
  // --- --- Methods
  // ******************************
  public static void main(String[] args) {
    new Tictactoe().startGame();
  }

  public void startGame() {
    boolean continuePlay = false;

    do {
      Screen.clear();

      displayLogo();
      displayLine();

      // Get user input for mode
      new Mode().getMode();
      // Start timer
      elapsedTime = new ElapsedTime();
      Screen.clear();

      // Keep playing until there's a winner or a tie
      while (this.winner == null) {
        displayLogo();
        displayGrid();

        int position = 0;
        boolean validPosition = false;

        if (turn.equals(USER_TURN)) {
          Font.print(Font.ANSI_YELLOW, "It's your (X) turn!");
          do {
            try {
              System.out.print("Enter a position to place your " + turn + " (1-9): ");
              position = scanner.nextInt();
              if (position < 1 || position > 9) {
                throw new InvalidPositionException(InvalidPositionException.ErrorType.OUT_OF_RANGE);
              } else if (!positionAvailable(position - 1)) {
                throw new InvalidPositionException(InvalidPositionException.ErrorType.SPOT_CHOSEN);
              } else {
                validPosition = true;
              }
            } catch (InvalidPositionException invalidPositionException) {
              System.out.println(invalidPositionException.getErrorMsg());
            } catch (InputMismatchException inputMismatchException) {
              Font.print(Font.ANSI_RED, "<<< Invalid position, please enter integers only. >>>");
            }
            scanner.nextLine();
          } while (!validPosition);

          grid[position - 1] = turn;
        } else {
          if (mode.equals(Mode.EASY)) {
            grid[getRandomAvailableSpot()] = turn;
          } else {
            grid[getBestMove()] = turn;
          }

          Font.print(Font.ANSI_CYAN, "It's computer (O)'s turn!");
          System.out.print("Press enter for the computer (O) to place its mark...");
          scanner.nextLine();
        }

        Screen.clear();

        String result = checkWinner();
        if (result != null) {
          // The game is ended, stop the timer
          elapsedTime.stop();
          this.winner = result;
        } else {
          nextTurn();
        }
      }

      displayLogo();
      displayGrid();
      displayEndMessage();

      boolean validOption = false;
      do {
        try {
          System.out.print("Do you want to play this game again? (Y/N): ");
          String continuePlayInput = scanner.nextLine().toUpperCase();
          if (continuePlayInput.equals("YES")
              || continuePlayInput.equals("Y")) {
            continuePlay = true;
            validOption = true;
          } else if (continuePlayInput.equals("NO")
              || continuePlayInput.equals("N")) {
            continuePlay = false;
            validOption = true;
          } else {
            throw new InvalidContinuePlayInputException();
          }
        } catch (InvalidContinuePlayInputException invalidContinuePlayInputException) {
          System.out.println(invalidContinuePlayInputException.getErrorMsg());
        }
      } while (!validOption);

      reset();
    } while (continuePlay);
  }

  private class Mode {
    private static final String EASY = "EASY";
    private static final String IMPOSSIBLE = "IMPOSSIBLE";

    // Get user input of the game mode
    private void getMode() {
      System.out.println();
      System.out.println("--------------------------------------------");
      System.out.println("| 1. " + Font.getStr(Font.ANSI_GREEN, "Easy") + " Mode                             |");
      System.out.println("| 2. " + Font.getStr(Font.ANSI_RED, "Impossible") + " Mode                       |");
      System.out.println("--------------------------------------------\n");

      int modeChoice = 1;
      boolean validModeChoice = false;

      do {
        try {
          System.out.print("Choose the game mode (1/2): ");
          modeChoice = scanner.nextInt();
          if (modeChoice != 1 && modeChoice != 2) {
            throw new InvalidModeChoiceException();
          } else {
            validModeChoice = true;
          }
        } catch (InputMismatchException inputMismatchException) {
          Font.print(Font.ANSI_RED, "<<< Invalid choice, please enter integers only. >>>");
        } catch (InvalidModeChoiceException invalidModeChoiceException) {
          System.out.println(invalidModeChoiceException.getErrorMsg());
        }
        scanner.nextLine();
      } while (!validModeChoice);

      if (modeChoice == 1) {
        Tictactoe.this.mode = Mode.EASY;
      } else {
        Tictactoe.this.mode = Mode.IMPOSSIBLE;
      }
    }
  }

  // -- Adjust the current turn for the next turn
  private void nextTurn() {
    if (this.turn.equals(USER_TURN)) {
      this.turn = COMPUTER_TURN;
    } else {
      this.turn = USER_TURN;
    }
  }

  // -- Randomly get a available spot on the grid
  private int getRandomAvailableSpot() {
    int randomPosition;
    do {
      // Generate a random integer between 0 - 8
      // Repeat until an available position is obtained
      randomPosition = rand.nextInt(grid.length);
    } while (!positionAvailable(randomPosition));
    return randomPosition;
  }

  // -- Return "X" / "O" means a win
  // -- Return "TIE" means draw
  // -- Return null means game is still going on
  private String checkWinner() {
    // Check if there is any 3 matching symbols
    for (int[] winTrio : winTrios) {
      if (grid[winTrio[0]].equals(grid[winTrio[1]])
          && grid[winTrio[1]].equals(grid[winTrio[2]])) {
        // Return the winner ("X" or "O")
        return grid[winTrio[0]];
      }
    }

    // At this point, the grid has no 3 matching symbols
    // Thus, check if it's a tie or the game has not ended yet
    for (int i = 0; i < grid.length; i++) {
      if (positionAvailable(i)) {
        // There's still position available so the game hasn't ended yet
        return null;
      }
    }

    // No position is left and no 3 matching symbols, it's a tie
    return Tictactoe.TIE;
  }

  // -- Check if a given position is available or not (NOT X and NOT O)
  private boolean positionAvailable(int position) {
    return !grid[position].equals(USER_TURN) && !grid[position].equals(COMPUTER_TURN);
  }

  // -- Obtain the best move for computer in IMPOSSIBLE mode
  private int getBestMove() {
    // AI to make its turn
    int bestScore = Integer.MIN_VALUE;
    int bestMove = -1;
    for (int i = 0; i < grid.length; i++) {
      // Is the spot available?
      if (positionAvailable(i)) {
        String temp = grid[i];
        grid[i] = COMPUTER_TURN;
        // Get the score of this position
        int score = minimax(grid, 0, false);
        grid[i] = temp;
        // Check if the score is the highest
        if (score > bestScore) {
          bestScore = score;
          bestMove = i;
        }
      }
    }
    return bestMove;
  }

  // -- Hashmap to store the scores for X, O and TIE
  private static HashMap<String, Integer> scoreMap;
  static {
    scoreMap = new HashMap<>();
    scoreMap.put(Tictactoe.USER_TURN, -1);
    scoreMap.put(Tictactoe.COMPUTER_TURN, 1);
    scoreMap.put(Tictactoe.TIE, 0);
  }

  // -- Recursive minimax algorithm to calculate the move with the maximum score
  private int minimax(String[] grid, int depth, boolean isMaximizing) {
    String result = checkWinner();
    if (result != null) {
      return scoreMap.get(result);
    }

    int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
    for (int i = 0; i < grid.length; i++) {
      if (positionAvailable(i)) {
        String temp = grid[i];
        grid[i] = isMaximizing ? COMPUTER_TURN : USER_TURN;
        int score = minimax(grid, depth + 1, !isMaximizing);
        grid[i] = temp;
        // Compare between score and bestScore, and then
        // assign the larger/lower score to the bestScore
        bestScore = isMaximizing
            ? Math.max(score, bestScore)
            : Math.min(score, bestScore);
      }
    }
    return bestScore;
  }

  // ******************************
  // --- --- Methods for UI
  // ******************************
  // -- Display TicTacToe logo
  private void displayLogo() {
    Font.print(Font.BLUE_BOLD_BRIGHT, "    __ __|_)  __ __|      __ __|        ");
    Font.print(Font.CYAN_BOLD_BRIGHT, "       |   |  _| |  _` |  _| |  _ \\  -_)");
    Font.print(Font.BLUE_BOLD_BRIGHT, "      _|  _|\\__|_|\\__,_|\\__|_|\\___/\\___|\n");
  }

  // -- Display 3x3 board (grid)
  private void displayGrid() {
    displayLine();
    System.out.println("| Turn: " + getSymbolColor(turn)
        + "            | Mode: "
        + (this.mode.equals("EASY")
            ? Font.getStr(Font.ANSI_GREEN, this.mode + "         ")
            : Font.getStr(Font.ANSI_RED, this.mode + "   "))
        + " |");
    displayLine();
    System.out.println();

    System.out.println("            +-----------------+");
    System.out.println("            |  " + getSymbolColor(grid[0]) + "  |  " +
        getSymbolColor(grid[1]) + "  |  " + getSymbolColor(grid[2]) + "  |");
    System.out.println("            |-----|-----|-----|");
    System.out.println("            |  " + getSymbolColor(grid[3]) + "  |  " +
        getSymbolColor(grid[4]) + "  |  " + getSymbolColor(grid[5]) + "  |");
    System.out.println("            |-----|-----|-----|");
    System.out.println("            |  " + getSymbolColor(grid[6]) + "  |  "
        + getSymbolColor(grid[7]) + "  |  " + getSymbolColor(grid[8])
        + "  |");
    System.out.println("            +-----------------+\n");

    displayLine();
    System.out.println();
  }

  // -- Display results of the game
  private void displayEndMessage() {
    if (winner.equals(USER_TURN)) {
      // User (X) wins
      Font.print(Font.GREEN_BOLD_BRIGHT, StringUtils.center("# Congratulations #", 44));
      System.out.println("           >>> You (" + Font.getStr(Font.ANSI_YELLOW, winner) + ") wins! <<<");
    } else if (winner.equals(COMPUTER_TURN)) {
      // Computer (O) wins
      Font.print(Font.RED_BOLD_BRIGHT, StringUtils.center("# Good try #", 44));
      System.out.println("         >>> Computer (" + Font.getStr(Font.ANSI_CYAN, winner) + ") wins! <<<");
    } else {
      // A tie game
      Font.print(Font.ANSI_PURPLE, "         >>> It is a tie game! <<<");
    }
    System.out.println("\nPlaying Time: " + Font.getStr(Font.BLUE_BOLD_BRIGHT, elapsedTime.getMinSec()));
  }

  // -- Display horizontal line ========
  private void displayLine() {
    for (int i = 0; i < 44; i++) {
      System.out.print("=");
    }
    System.out.println();
  }

  // -- Get a colorized symbol
  private String getSymbolColor(String symbol) {
    if (symbol.equals("X")) {
      return Font.getStr(Font.ANSI_YELLOW, symbol);
    } else if (symbol.equals("O")) {
      return Font.getStr(Font.ANSI_CYAN, symbol);
    } else {
      return Font.getStr(Font.ANSI_WHITE, symbol);
    }
  }

  // ******************************
  // --- --- Exception classes
  // ******************************

  // Exception for handling invalid mode choice input
  private static class InvalidModeChoiceException extends Exception {
    private final String errorMsg;

    InvalidModeChoiceException() {
      this.errorMsg = Font.getStr(Font.ANSI_RED,
          "<<< Invalid choice, only 1 and 2 are accepted. >>>");
    }

    public String getErrorMsg() {
      return errorMsg;
    }
  }

  // Exception for handling invalid position input
  private static class InvalidPositionException extends Exception {
    private final String errorMsg;

    public enum ErrorType {
      OUT_OF_RANGE, // Error of which input of integers other than 1 to 9
      SPOT_CHOSEN // Error of which input of a placed position
    }

    InvalidPositionException(ErrorType errorType) {
      // Determine the error message based on errorType
      if (errorType == ErrorType.OUT_OF_RANGE) {
        this.errorMsg = Font.getStr(Font.ANSI_RED, "<<< Invalid position, only 1 to 9 are accepted. >>>");
      } else {
        this.errorMsg = Font.getStr(Font.ANSI_RED, "<<< Invalid position, it is chosen already. >>>");
      }
    }

    public String getErrorMsg() {
      return errorMsg;
    }
  }

  // Exception for handling invalid continue play input
  private static class InvalidContinuePlayInputException extends Exception {
    private final String errorMsg;

    InvalidContinuePlayInputException() {
      this.errorMsg = Font.getStr(Font.ANSI_RED,
          "<<< Invalid input. Please enter Y / N only >>>");
    }

    public String getErrorMsg() {
      return errorMsg;
    }
  }
}
