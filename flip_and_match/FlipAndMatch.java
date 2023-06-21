package flip_and_match;

import utility.Font;
import utility.Screen;

//Predefined Classes
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;

public class FlipAndMatch {
  /*****************************
   Attributes for the Game Class
  *****************************/
    private static final String gameTitle = "Flip and Match";
    //=== this is flexible, you can change 20 to any even number from 2 to 52========
    private static final int totalCards = 20; // 20 = (4 rows * 5 columns) 
    //totalCards must be an even integer because cards must come in paris.
    //totalCards must be an integer from 2 to 52 because there are only 26 alphabets.
    //===============================================================================
    private String[] cardDisplay = new String[totalCards];
    private char[] cards = new char[totalCards];
    private boolean gameover;
    private long startTime;
    private long elapsedTimeMillis;
    private static final int asciiChar_A = 65; // (char)65 == 'A'
    private static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);
  
  /**********************************
   Driver Program & Start Game Method
  **********************************/
    public static void main(String[] args) {
      new FlipAndMatch().startGame();
    }

    public void startGame() {
      boolean continuePlay = false;

      do {
        //Set up a new match (generate new board and shuffle cards)
        generateNewCardDisplay();
        shuffleCards();
        gameover = false;

        //Display logo
        Screen.clear();
        displayLogo();

        //Display game rules
        displayGameRule();
        System.out.print("Let's Go!!! Press " + 
                       Font.getStr(Font.ANSI_GREEN, ">>ENTER<<") + 
                       " to start the game \\>w</ ! " + 
                       Font.getStr(Font.BLACK_BOLD_BRIGHT, " >> (press enter)"));
        scanner.nextLine();

        //Display initial game board
        Screen.clear();
        displayLogo();
        displayDivider();
        displayGameBoard();

        //Start the timer
        startTime = System.currentTimeMillis();

        //Start the game
        while(!gameover){
          flipAndCheck();
        }
        //End the game and display result
        displayGameResult();

        //Ask if the user wants to play again
        boolean validOption = false;
        do {
          try {
            System.out.print("Do you want to play this game again? (Y/N): ");
            String input = (scanner.nextLine()).toUpperCase();
            if (input.equals("YES") || input.equals("Y")){
              continuePlay = true;
              validOption = true;
            } else if (input.equals("NO") || input.equals("N")){
              continuePlay = false;
              validOption = true;
            } else {
              throw new InvalidChoiceException();
            }
          } catch (InvalidChoiceException invalidChoiceException){
            System.out.println(InvalidChoiceException.getErrorMsg());
          }
        } while(!validOption);
      } while (continuePlay);
    }
  
  /******************
   Game Logic Methods 
  ******************/
    private void generateNewCardDisplay() {
      // cardDisplay[0] = "| 01 |"
      // cardDisplay[1] = "| 02 |"
      // .
      // .
      // .
      // cardDisplay[20] = "| 20 |"
      for (int i = 0; i < totalCards; i++){
        cardDisplay [i] = String.format("| %02d |", i + 1);
      }
    }
  
    private void shuffleCards() {
      //20 cards : (A - J) x 2
      ArrayList<Character> letters = new ArrayList<Character>();
      for (int i = 0; i < totalCards/2; i++) {
        char letter = (char) (i + asciiChar_A); //A,B,C,D...
        for (int j = 0; j < 2; j++) {
          letters.add(letter); //{'A','A','B','B','C','C',...}
        }
      }

      //Randomly assign letters into cards
      int index;
      for(int i = 0; i < totalCards; i++) {
        index = random.nextInt(letters.size());
        //{'A','A','B','B','C','C',...} 
        //--randomise--> {'C','D','A','J','C','B',...}
        cards[i] = letters.get(index);
        letters.remove(index);
      }
    }

    private void flipAndCheck() {
      int flipCardNo = 0;
      int flipCardIndex1 = -1;
      int flipCardIndex2 = -1;
      int index;
      //FLIP (Ask for 2 Card Numbers to flip)
        String[] InputMsg = {"Enter 1st Card Number to Flip : ","Enter 2nd Card Number to Flip : "};
        for(int i = 0; i < 2; i++) {
          boolean validOption = false;
          do { 
            try {
              //Prompt for Card Number to flip
              String input = JOptionPane.showInputDialog(null, InputMsg[i], gameTitle, JOptionPane.QUESTION_MESSAGE);
              if (input == null){ //Handle Close Button / Cancel Button
                gameover = confirmExitGame();
                if(gameover == true){
                  //Stop the timer (Calculate Elapsed Time - exit at the middle of the game)
                  elapsedTimeMillis = System.currentTimeMillis() - startTime;
                  //If the user confirms exit after flipping one card,
                  //then reset the flipped card to hidden.
                  if (i == 1){
                    cardDisplay[flipCardIndex1] = String.format("| %02d |", flipCardIndex1 + 1);
                  }
                  return;
                }
                continue;
              } 
              //Convert input to an integer
              flipCardNo = Integer.parseInt(input);
              index = flipCardNo - 1;
              //Handle Different Input
              if (flipCardNo < 1 || flipCardNo > totalCards) { //Handle out of Range CardNo
                throw new InvalidCardNoException(InvalidCardNoException.ErrorType.OUT_OF_RANGE);
              } else if (cardIsFlip(index)) { //selected CardNo has been flipped
                  throw new InvalidCardNoException(InvalidCardNoException.ErrorType.CARD_IS_FLIPPED);
              }  else { //Valid Input
                if(i == 0) { //if it's the first card, save the index
                  flipCardIndex1 = index;
                } else { //else, save the second card' index
                  flipCardIndex2 = index;
                }
                  //Flip the card and show to user
                  //| 01 | --> |  A |
                  cardDisplay[index] = String.format("| %2c |", cards[index]);
                  Screen.clear();
                  displayLogo();
                  displayDivider();
                  displayGameBoard();
                  validOption = true;
              } 
            } catch (InvalidCardNoException invalidCardNoException)  { 
              String msg = invalidCardNoException.getErrorMsg();
              JOptionPane.showMessageDialog(null, msg, 
                                            gameTitle, JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException numberFormatException) {
              String msg = "Please enter an integer between 1 - " + totalCards + " only";
              JOptionPane.showMessageDialog(null, msg, 
                                            gameTitle, JOptionPane.INFORMATION_MESSAGE);
            } 
          } while(!validOption);
        }
      //CHECK MATCH (After two cards is flipped)
        //If these two cards matched
        if (cards[flipCardIndex1] == cards[flipCardIndex2]) { 
          gameover = checkGameComplete();
          if (gameover){
            //Stop the timer (Calculate Elapsed Time - finish the game)
            elapsedTimeMillis = System.currentTimeMillis() - startTime;
          }
          String SuccessMsg = "BINGOO!!! >v< \nBoth Cards Matched!!!";
          JOptionPane.showMessageDialog(null, SuccessMsg, gameTitle, JOptionPane.INFORMATION_MESSAGE); 
          return;
        } else { //Else, these two cards are not matched
          cardDisplay[flipCardIndex1] = String.format("| %02d |", flipCardIndex1 + 1);
          cardDisplay[flipCardIndex2] = String.format("| %02d |", flipCardIndex2 + 1);
          String SuccessMsg = "Opps... QAQ \nBoth Cards Don't Match...";
          JOptionPane.showMessageDialog(null, SuccessMsg, gameTitle, JOptionPane.INFORMATION_MESSAGE);
          Screen.clear();
          displayLogo();
          displayDivider();
          displayGameBoard();
          return;
        }
    }

    private boolean confirmExitGame() {
      int option = JOptionPane.showConfirmDialog(null, "End the Game Now? ;-;", 
                                                 "End Confirmation", JOptionPane.YES_NO_OPTION);
      switch(option) { //User confirmed exit game
        case JOptionPane.YES_OPTION:
          return true;
        default: //Other selection
          return false;
      }
    }

    private boolean cardIsFlip(int cardDisplayIndex) {
      //If a card is flipped, it will not show it's cardNo as its display
      //For example, if the first card is 'A', 
      //its cardDisplay is |  A | and not | 01 |
      if (cardDisplay[cardDisplayIndex].equals(String.format(
          "| %02d |", cardDisplayIndex + 1))){
        return false;
      } else {
        return true;
      }
    }

    private boolean checkGameComplete() {
        for (int i = 0; i < totalCards; i++){
          //If one or more cards are not flip, 
          //the game is not completed yet.
          if (!cardIsFlip(i)){
            return false;
          }
        }
        return true;
      }

  /*****************************
   Drawing Methods (for Output Displaying)
  *****************************/
    private void displayGameRule() {
      String gameRule = 
      "===========================================================================\n" +
      "|                                GAME RULES                               |\n"+
      "===========================================================================\n" +
      "|  In this game, there are 10 pairs of same letter hidden down the cards. |\n"+
      "|  1. For each round, enter the card number (1-"+totalCards+") to flip open 2 cards.  |\n"+
      "|  2. If the two cards                                                    |\n"+
      "|         Are same: they will stay revealed.                              |\n"+
      "|         Not same: they will turn back hidden.                           |\n" +
      "|  3. Reveal all the cards to win!                                        |\n" + 
      "===========================================================================\n" + 
      "|                       ***Good Luck and Enjoy***                         |\n"+
      "===========================================================================\n"; 
      System.out.println(gameRule);
    }

    private void displayLogo() {
      Font.print(Font.ANSI_CYAN,
          "_______                           _ _ _               \n"+
          "(  /  //o                     /  ( / ) )     _/_    / \n"+
          " -/--//,   ,_     __,  __  __/    / / / __,  /  _, /_ ");
      Font.print(Font.ANSI_BLUE,
          "_/  (/_(__/|_)   (_/(_/ /_(_/    / / (_(_/(_(__(__/ / \n"+
          "          /|                                            \n"
          +"         (/                                             ");
    }

    private void displayGameBoard() {
      String border = String.format("%12s+----------------------------+\n","");
      String borderColoured = Font.getStr(Font.ANSI_CYAN, border);
      System.out.print(borderColoured);
      for (int i = 0; i < (int)Math.ceil(totalCards/5.0); i++){
        System.out.printf("%12s","");
        for (int j = 0; j < 5; j++){
          int index = j + (i*5);
          if (cardDisplay.length <= index){
            break;
          }
          System.out.printf((cardIsFlip(index))
            ? Font.getStr(Font.CYAN_BOLD_BRIGHT, cardDisplay[index])
            : Font.getStr(Font.ANSI_RED, cardDisplay[index]));
        }
        System.out.print("\n" + borderColoured);
      }
      System.out.print("=======================================================");
    }

    private void displayGameResult() {
      //Convert Elapsed Time to Minutes and Seconds
        int minutes = (int)((elapsedTimeMillis / 1000) / 60);
        int seconds = (int)((elapsedTimeMillis / 1000) % 60);
      //String for the output
        String successMsg = Font.getStr(
          Font.GREEN_BOLD_BRIGHT, 
          "Game Over!          You are the True Master of Flip! :D");
        String failMsg = Font.getStr(
          Font.CYAN_BOLD_BRIGHT, 
          "Game Over!          Opps...You don't like this game? :(");
        String time = String.format("Flipping Time : %d minute(s) %d second(s)",  minutes, seconds);
      //Display Result output
        Screen.clear();
        displayLogo();
        displayDivider();
        System.out.printf("%s\n%s\n%-19s%s%19s\n",
                          checkGameComplete() ? successMsg : failMsg, 
                          "=======================================================", 
                          "++++++++++++", Font.getStr(Font.CYAN_BOLD_BRIGHT,"Your Flip Result:"), 
                          "+++++++++++++" );
        displayGameBoard();
        System.out.printf("\n%55s\n%s", time, 
                          "=======================================================\n\n");
      }
      
    private void displayDivider() {
      for (int i = 0; i < 55; i++) {
        System.out.print("=");
      }
      System.out.println();
    }

  /**************************
   Exception Handling classes
  **************************/
    private static class InvalidChoiceException extends Exception {
      private final static String errorMsg = Font.getStr(Font.ANSI_RED, 
                                            "<<< Invalid input. Please enter Y / N only >>>");

      public static String getErrorMsg() {
        return errorMsg;
      }
    }

    private static class InvalidCardNoException extends Exception {
      private final String errorMsg;

      public enum ErrorType {
        CARD_IS_FLIPPED, //Error of which the card is already revealed
        OUT_OF_RANGE, //Error of which input of integers other than 1 to totalCards (20)
      }

      InvalidCardNoException(ErrorType errorType) {
        //Determine the error message based on errorType
        if (errorType == ErrorType.CARD_IS_FLIPPED) {
          this.errorMsg = "This card has already been revealed!";
        } else {
          this.errorMsg = "Please enter an integer between 1 - " + totalCards + " only";
        }
      }

      public String getErrorMsg() {
        return errorMsg;
      }
    }
}

//
//                       _oo0oo_
//                      o8888888o
//                      88" . "88
//                      (| -_- |)
//                      0\  =  /0
//                    ___/`---'\___
//                  .' \\|     |// '.
//                 / \\|||  :  |||// \
//                / _||||| -:- |||||- \
//               |   | \\\  -  /// |   |
//               | \_|  ''\---/''  |_/ |
//               \  .-\__  '-'  ___/-. /
//             ___'. .'  /--.--\  `. .'___
//          ."" '<  `.___\_<|>_/___.' >' "".
//         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//         \  \ `_.   \_ __\ /__ _/   .-` /  /
//     =====`-.____`.___ \_____/___.-`___.-'=====
//                       `=---='
//
//
//     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//               BO BI BO BI BUG GO AWAY
//               Title  : Flip And Match
//               Author : Wickey Chai