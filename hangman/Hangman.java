package hangman;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utility.Screen;

import java.lang.Math;

public class Hangman {
  public static String[] wordFound;
  public static String[] wordToFind;
  public static int numMissed;
  
  //COLOURS
  public static final String RESET_COLOUR = "\u001B[0m";
  public static final String YELLOW_COLOUR = "\u001B[33m";
  public static final String RED_COLOUR = "\u001B[31m";
  public static final String GREEN_COLOUR = "\u001B[32m";
  public static final String CYAN_COLOUR = "\u001B[36m";

  private static final Scanner input = new Scanner(System.in);

  public static void main() {    
    String[] words = { "subway", "awkward", "microwave", "watermelon", "quiz", "cardholder", "hangman",
        "checkmate", "cheesecake", "seventeen", "wizard", "software", "assignment", "bicycle", "zodiac",
        "crochet", "biorhythm", "transport", "jackport", "forefinger", "beekeeper", "wisdom", "goalkeeper", "jazz",
        "vegetable", "laundry", "semicolon", "apple", "fruit", "journalist", "jawbreaker", "leadership",
        "mangosteen", "motorcycle", "tax", "vaccine", "occupation", "music", "peppermint", "phenomenon", "flower" };
    boolean validateOption = false;

    // Array that stored letters guessed by user
    ArrayList<String> letterGuessed = new ArrayList<>();

    System.out.println(YELLOW_COLOUR + "-------------------------------------------------");
    System.out.println("  _    _                                         ");
    System.out.println(" | |  | |                                        ");
    System.out.println(" | |__| | __ _ _ __   __ _ _ __ ___   __ _ _ __  ");
    System.out.println(" |  __  |/ _` | '_ \\ / _` | '_ ` _ \\ / _` | '_ \\ ");
    System.out.println(" | |  | | (_| | | | | (_| | | | | | | (_| | | | |");
    System.out.println(" |_|  |_|\\__,_|_| |_|\\__, |_| |_| |_|\\__,_|_| |_|");
    System.out.println("                     __/  |                      ");
    System.out.println("                    |____/                       ");
    System.out.println("------------------------------------------------" + RESET_COLOUR);
    System.out.println("In this game, you have 10 chances to guess a word!");
    System.out.println("      Guess one letter at each round.");
    System.out.println("                Good luck!");

    startGame(letterGuessed, words);

    do {
      try {
        int option = 0;

        System.out.println("            Do you want to play again? ");
        System.out.println("        1 -> Play Again          0 -> Exit\n");
        System.out.print("             Enter your option --> ");
        option = input.nextInt();
        do {
          do {
            if (option == 1) {
              newGame(letterGuessed, words);
              System.out.println("            Do you want to play again? ");
              System.out.println("        1 -> Play Again          0 -> Exit\n");
              System.out.print("             Enter your option --> ");
              option = input.nextInt();
            } else if (option != 0) {
              System.out.println(RED_COLOUR + "\n <<< You can only choose 1 to Play Again and 0 to exit >>> \n" + RESET_COLOUR);
              System.out.print("             Enter your option --> ");
              option = input.nextInt();
            }

          } while (option != 1 && option != 0);
        } while (option == 1);

        System.out.println("\n         Thank you for playing Hangman!");
        Screen.pause(2);
        validateOption = true;
      } catch (Exception e) {
        System.out.println(RED_COLOUR + "\n  <<< Invalid input -> Only integer is allowed >>>\n" + RESET_COLOUR);
        input.nextLine();
      }
    } while (!validateOption);

  }

  public static void newGame(ArrayList<String> letterGuessed, String[] words) {
    // Clear letters that stored in the array
    letterGuessed.clear();
    Screen.clear();

    startGame(letterGuessed, words);
  }

  public static void startGame(ArrayList<String> letterGuessed, String[] words) {
    boolean gameWon = false;
    numMissed = 0;
    int chance = 10;

    // Generate word to find
    String chosenWord = generateWord(words);
    wordToFind = chosenWord.split("");

    // Manage word that user guess it correctly and initialize it them with asterisk
    wordFound = new String[chosenWord.length()];
    for (int i = 0; i < wordFound.length; i++) {
      wordFound[i] = "*";
    }

    System.out.println("\n");
    System.out.print("              ");

    //Give hint to user of how many letter
    for (int i = 0; i < chosenWord.length(); i++) {
      System.out.print(" *");
    }

    System.out.println("\n");

    do {
      String passRound;
      // Prompt user to guess and check whether it is duplicated
      String letter = handleGuess(letterGuessed);
      // To calculate numMissed and display when user win
      passRound = progress(letter, wordFound, wordToFind);

      if (passRound == "N") {
        numMissed++;
      }

      // To determine whether the user win in this round
      gameWon = isGameWon(wordToFind, wordFound, numMissed);
      chance--;
      printHangman(chance, chosenWord);
    } while (gameWon == false && chance > 0);

    if (gameWon) {
      winMessage(numMissed);
    }
  }

  // Randomly generate a word
  public static String generateWord(String[] words) {
    int randomNum = (int) (Math.random() * words.length);
    String randomWord = words[randomNum];

    return randomWord;
  }

  public static String handleGuess(ArrayList<String> letterGuessed) {
    boolean validateInput = true;
    String guessLetter = "";
    boolean handled = false;
    do {
      // Prompt user to guess a letter
      Pattern p = Pattern.compile("[a-zA-Z]");
      System.out.print("  Guess a letter -> ");
      guessLetter = input.next();
      guessLetter.toLowerCase();

      Matcher m = p.matcher(guessLetter);
      validateInput = m.matches();

      if (!validateInput) {
        System.out.println(RED_COLOUR + "\n<<< Please input a letter >>>\n" + RESET_COLOUR);
      } else if (validateInput) {
        // Check whether it is duplicated
        if (letterGuessed.contains(guessLetter)) {
          System.out.println(RED_COLOUR + "\n <<< You have already guessed this letter. Please enter another letter >>>\n" + RESET_COLOUR);
        } else {
          letterGuessed.add(guessLetter);
          handled = true;
        }
      }
    } while (!handled);

    return guessLetter;
  }

  // Display the word progress
  public static String progress(String letter, String[] wordFound, String[] wordToFind) {
    String passRound = "N";

    Screen.clear();
    // Replace the asterisk with correct letter
    for (int i = 0; i < wordToFind.length; i++) {

      if (wordToFind[i].equals(letter)) {
        wordFound[i] = wordFound[i].replace("*", letter);
        passRound = "Y";
      } else if (wordFound[i] != "*") {
        wordFound[i] = wordFound[i];
      } else {
        wordFound[i] = "*";
      }
    }

    System.out.println("\n");
    System.out.print("              ");
    
    for (int i = 0; i < wordFound.length; i++) {
      System.out.print(" " + wordFound[i]);
    }

    return passRound;
  }

  public static boolean isGameWon(String[] wordToFind, String[] wordFound, int numMissed) {
    boolean gameWon = true;

    // Check whether all the letter in the wordFound is match with the wordToFind
    for (int i = 0; i < wordToFind.length; i++) {
      if (wordFound[i].contains("*")) {
        gameWon = false;
        break;
      }
    }

    return gameWon;
  }

  public static void winMessage(int numMissed) {
    System.out.println(GREEN_COLOUR +"\n\n  ***********************************************");
        System.out.println("   _____                            _         _ ");
        System.out.println("  / ____|                          | |       | |");
        System.out.println(" | |     ___  _ __   __ _ _ __ __ _| |_ ___  | |");
        System.out.println(" | |    / _ \\| '_ \\ / _` | '__/ _` | __/ __| | |");
        System.out.println(" | |___| (_) | | | | (_| | | | (_| | |_\\__ \\ |_|");
        System.out.println("  \\_____\\___/|_| |_|\\__, |_|  \\__,_|\\__|___/ (_)");
        System.out.println("                     __/ |                      ");
        System.out.println("                    |___/                       \n");
        System.out.println("  ***********************************************" + RESET_COLOUR);
        System.out.println("            You have missed " + GREEN_COLOUR + numMissed + RESET_COLOUR + " times!\n");
  }

  public static void printHangman(int chance, String chosenWord) {
    boolean gameWon = false;
    gameWon = isGameWon(wordToFind, wordFound, numMissed);

    if (chance == 9 && gameWon == false) {
        System.out.println(CYAN_COLOUR + "\n        Your remaining chance is " + chance + ".\n" + RESET_COLOUR);
        System.out.println("           |                 ");
        System.out.println("           |                 ");
        System.out.println("           |                 ");
        System.out.println("           |                 ");
        System.out.println("           |                 ");
        System.out.println("           |                 ");
        System.out.println("        ___|_________________\n");
    } else if (chance == 8 && gameWon == false) {
        System.out.println(CYAN_COLOUR + "\n        Your remaining chance is " + chance + ".\n" + RESET_COLOUR);
        System.out.println("           |-----------------");
        System.out.println("           |                 ");
        System.out.println("           |                 ");
        System.out.println("           |                 ");
        System.out.println("           |                 ");
        System.out.println("           |                 ");
        System.out.println("        ___|_________________\n");
    } else if (chance == 7 && gameWon == false) {
        System.out.println(CYAN_COLOUR + "\n        Your remaining chance is " + chance + ".\n" + RESET_COLOUR);
        System.out.println("           |--------T--------");
        System.out.println("           |        |        ");
        System.out.println("           |                 ");
        System.out.println("           |                 ");
        System.out.println("           |                 ");
        System.out.println("           |                 ");
        System.out.println("        ___|_________________\n");
    } else if (chance == 6 && gameWon == false) {
        System.out.println(CYAN_COLOUR + "\n        Your remaining chance is " + chance + ".\n" + RESET_COLOUR);
        System.out.println("           |--------T--------");
        System.out.println("           |        |        ");
        System.out.println("           |        @        ");
        System.out.println("           |                 ");
        System.out.println("           |                 ");
        System.out.println("           |                 ");
        System.out.println("        ___|_________________\n");
    } else if (chance == 5 && gameWon == false) {
        System.out.println(CYAN_COLOUR + "\n        Your remaining chance is " + chance + ".\n" + RESET_COLOUR);
        System.out.println("           |--------T--------");
        System.out.println("           |        |        ");
        System.out.println("           |        @        ");
        System.out.println("           |        |        ");
        System.out.println("           |                 ");
        System.out.println("           |                 ");
        System.out.println("        ___|_________________\n");
    } else if (chance == 4 && gameWon == false) {
        System.out.println(CYAN_COLOUR + "\n        Your remaining chance is " + chance + ".\n" + RESET_COLOUR);
        System.out.println("           |--------T--------");
        System.out.println("           |        |        ");
        System.out.println("           |        @        ");
        System.out.println("           |        |        ");
        System.out.println("           |        |        ");
        System.out.println("           |                 ");
        System.out.println("        ___|_________________\n");
    } else if (chance == 3 && gameWon == false) {
        System.out.println(CYAN_COLOUR + "\n        Your remaining chance is " + chance + ".\n" + RESET_COLOUR);
        System.out.println("           |--------T--------");
        System.out.println("           |        |        ");
        System.out.println("           |        @        ");
        System.out.println("           |       /|        ");
        System.out.println("           |        |        ");
        System.out.println("           |                 ");
        System.out.println("        ___|_________________\n");
    } else if (chance == 2 && gameWon == false) {
        System.out.println(CYAN_COLOUR + "\n        Your remaining chance is " + chance + ".\n" + RESET_COLOUR);
        System.out.println("           |--------T--------");
        System.out.println("           |        |        ");
        System.out.println("           |        @        ");
        System.out.println("           |       /|\\      ");
        System.out.println("           |        |        ");
        System.out.println("           |                 ");
        System.out.println("        ___|_________________\n");
    } else if (chance == 1 && gameWon == false) {
        System.out.println(CYAN_COLOUR + "\n        Your remaining chance is " + chance + ".\n" + RESET_COLOUR);
        System.out.println("           |--------T--------");
        System.out.println("           |        |        ");
        System.out.println("           |        @        ");
        System.out.println("           |       /|\\      ");
        System.out.println("           |        |        ");
        System.out.println("           |       /         ");
        System.out.println("        ___|_________________\n");
    } else if (chance == 0 && gameWon == false) {
        System.out.println(RED_COLOUR + "\n\nXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        System.out.println("   _____                         ____                   _ ");
        System.out.println("  / ____|                       / __ \\                 | |");
        System.out.println(" | |  __  __ _ _ __ ___   ___  | |  | |_   _____ _ __  | |");
        System.out.println(" | | |_ |/ _` | '_ ` _ \\ / _ \\ | |  | \\ \\ / / _ \\ '__| | |");
        System.out.println(" | |__| | (_| | | | | | |  __/ | |__| |\\ V /  __/ |    |_|");
        System.out.println("  \\_____|\\__,_|_| |_| |_|\\___|  \\____/  \\_/ \\___|_|    (_)");
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" + RESET_COLOUR);
        System.out.println("                You can do better next time!\n");
        System.out.println("                    |--------T--------");
        System.out.println("                    |        |        ");
        System.out.println("                    |        @        ");
        System.out.println("                    |       /|\\      ");
        System.out.println("                    |        |        ");
        System.out.println("                    |       / \\      ");
        System.out.println("                 ___|_________________\n");
        System.out.println("                The word was " + RED_COLOUR + chosenWord + RESET_COLOUR + ".\n");
      }
    }
}
