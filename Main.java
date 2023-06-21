import java.util.Scanner;

import five_dice.FiveDice;
import flip_and_match.FlipAndMatch;
import hangman.Hangman;
import scissor_rock_paper.ScissorRockPaper;
import tictactoe.Tictactoe;
import utility.Font;
import utility.Screen;

public class Main {
  private static final Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    String gameSelection = null;
    boolean validSelection;

    do {
      displayMenu();

      do {
        try {
          System.out.print("Please enter your choice (a-f): ");
          gameSelection = scanner.nextLine().toLowerCase();
          if (!gameSelection.equals("a") && !gameSelection.equals("b") && !gameSelection.equals("c")
              && !gameSelection.equals("d")
              && !gameSelection.equals("e") && !gameSelection.equals("f")) {
            throw new InvalidGameSelectionException();
          }
          validSelection = true;
        } catch (InvalidGameSelectionException e) {
          System.out.println(e.getErrorMsg());
          validSelection = false;
        }
      } while (!validSelection);
      
      Screen.clear();
      switch (gameSelection) {
        case "a":
          // Hangman
          Hangman.main();
          break;
        case "b":
          // Scissor, rock, paper
          ScissorRockPaper.main();
          break;
        case "c":
          // Tictactoe
          Tictactoe tictactoe = new Tictactoe();
          tictactoe.startGame();
          break;
        case "d":
          // Flip and Match
          FlipAndMatch flipandmatch = new FlipAndMatch();
          flipandmatch.startGame();
          break;
        case "e":
          // Five Dice
          FiveDice.main();
          break;
        case "f":
          displayEnding();
          System.exit(0);
          break;
      }
    } while (true);
  }

  private static void displayMenu() {
    Screen.clear();
    System.out.println("-----------------------------");
    System.out.println("|         Main Menu         |");
    System.out.println("-----------------------------");
    System.out.println("| a. Hangman                |");
    System.out.println("| b. Scissor, Rock, Paper   |");
    System.out.println("| c. Tic-Tac-Toe            |");
    System.out.println("| d. Flip and Match         |");
    System.out.println("| e. Five Dice              |");
    System.out.println("-----------------------------");
    System.out.println("| f. Quit                   |");
    System.out.println("-----------------------------");
  }

  private static void displayEnding() {
    Screen.clear();
    System.out.println("||||||||||||||   ||||||||  |||||||| |||||||||||||| ");
    System.out.println("||XXXXXXXXXX||   ||XXXX||  ||XXXX|| ||XXXXXXXXXX|| ");
    System.out.println("||XX||||||XX||   ||||XX||  ||XX|||| ||XX|||||||||| ");
    System.out.println("||XX||  ||XX||     ||XXXX||XXXX||   ||XX||         ");
    System.out.println("||XX||||||XX||||   ||||XXXXXX||||   ||XX|||||||||| ");
    System.out.println("||XX||||||XX||||   ||||XXXXXX||||   ||XX|||||||||| ");
    System.out.println("||XXXXXXXXXXXX||     ||||XX||||     ||XXXXXXXXXX|| ");
    System.out.println("||XX||||||||XX||       ||XX||       ||XX|||||||||| ");
    System.out.println("||XX||    ||XX||       ||XX||       ||XX||         ");
    System.out.println("||XX||||||||XX||       ||XX||       ||XX|||||||||| ");
    System.out.println("||||||||||||||||       ||||||       |||||||||||||| ");
    System.out.println("\n                   >> Good Bye <<");
    System.out.println();
    Screen.pause(5);
  }

  // Exception for handling invalid game selection input
  private static class InvalidGameSelectionException extends Exception {
    private final String errorMsg;

    InvalidGameSelectionException() {
      this.errorMsg = Font.getStr(Font.ANSI_RED, "<<< Invalid choice, please enter a correct choice. >>>");
    }

    public String getErrorMsg() {
      return errorMsg;
    }
  }
}