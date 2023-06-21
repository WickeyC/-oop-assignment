package scissor_rock_paper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;



import utility.Font;
import utility.Screen;

public class ScissorRockPaper {
  private static final Scanner input = new Scanner(System.in);
  public static void main() {
    Screen.clear();
    
    System.out.println("-----------------------------------------------------------------------------------------------");
    Font.print(Font.ANSI_PURPLE,"           _____     _                    _____         _      _____                 ");
    Font.print(Font.ANSI_PURPLE,"          |   __|___|_|___ ___ ___ ___   | __  |___ ___| |_   |  _  |___ ___ ___ ___ ");
    Font.print(Font.ANSI_PURPLE,"          |__   |  _| |_ -|_ -| . |  _|  |    -| . |  _| '_|  |   __| .'| . | -_|  _|");
    Font.print(Font.ANSI_YELLOW,"          |_____|___|_|___|___|___|_|    |__|__|___|___|_,_|  |__|  |__,|  _|___|_|  ");
    Font.print(Font.ANSI_YELLOW,"                                                                        |_|          ");
    System.out.println("-----------------------------------------------------------------------------------------------");
    System.out.println("                        In this game, you will have 10 round to compete.");
    System.out.println("     The system will auto generate a random number and you will compete with the system.");

    // used for validating do while loop
    boolean validSelection = true;

do {
  try {
    int[] result = startgame(); //Start the game and save the result
    showresult(result);
    validSelection = true;
  } catch (Exception e) {
    Screen.clear(); //IF the user input character rather than integer, the error will be lead here
    System.out.println("-----------------------------------------------------------------------------------------------");
    Font.print(Font.ANSI_PURPLE,"           _____     _                    _____         _      _____                 ");
    Font.print(Font.ANSI_PURPLE,"          |   __|___|_|___ ___ ___ ___   | __  |___ ___| |_   |  _  |___ ___ ___ ___ ");
    Font.print(Font.ANSI_PURPLE,"          |__   |  _| |_ -|_ -| . |  _|  |    -| . |  _| '_|  |   __| .'| . | -_|  _|");
    Font.print(Font.ANSI_YELLOW,"          |_____|___|_|___|___|___|_|    |__|__|___|___|_,_|  |__|  |__,|  _|___|_|  ");
    Font.print(Font.ANSI_YELLOW,"                                                                        |_|          ");
    System.out.println("-----------------------------------------------------------------------------------------------");
    Font.print(Font.ANSI_RED, "                    <<< Invalid choice,characters are not allowed. >>>");
    Font.print(Font.ANSI_RED, "                         <<< The game will now restart. >>>");
    input.next();
    validSelection = false;
  }
} while (!validSelection);
do{
  try{
    restartgame();
    validSelection = true;
  }catch (Exception e){
    Screen.clear();
    System.out.println("-----------------------------------------------------------------------------------------------");
    Font.print(Font.ANSI_PURPLE,"           _____     _                    _____         _      _____                 ");
    Font.print(Font.ANSI_PURPLE,"          |   __|___|_|___ ___ ___ ___   | __  |___ ___| |_   |  _  |___ ___ ___ ___ ");
    Font.print(Font.ANSI_PURPLE,"          |__   |  _| |_ -|_ -| . |  _|  |    -| . |  _| '_|  |   __| .'| . | -_|  _|");
    Font.print(Font.ANSI_YELLOW,"          |_____|___|_|___|___|___|_|    |__|__|___|___|_,_|  |__|  |__,|  _|___|_|  ");
    Font.print(Font.ANSI_YELLOW,"                                                                        |_|          ");
    System.out.println("-----------------------------------------------------------------------------------------------");
    Font.print(Font.ANSI_RED, "                    <<< Invalid choice, only 1 or 2 are not allowed. >>>");
    Font.print(Font.ANSI_RED, "                         <<< Please pick the right choice. >>>");
    input.next();
    validSelection = false;
  }
} while (!validSelection);

  }

    //This is where the game begin
    public static int[] startgame(){
      //intialise variable
      int[] choice = new int[10];
      int[] win_lose_draw = {0,0,0};
      boolean valid;



      int[] randomNum = randomNumgenerate(); //generate random number from another method

      //using for loop to loop the program 10 times with asking user input
      for (int i = 0 ; i <10 ; i++){
        do{
          valid = true;
          System.out.println("              =============================================================");
          System.out.println("                                    This is Round "+ (i+1) + ".");
          Font.print(Font.ANSI_CYAN,"                                      0. Scissor");
          Font.print(Font.ANSI_PURPLE,"                                      1. Rock");
          Font.print(Font.ANSI_YELLOW,"                                      2. Paper");
          System.out.print("                            Pick from 0 - 2 of your choices: ");
          choice[i] = input.nextInt(); //input ask from user
          Screen.clear();
          System.out.println("-----------------------------------------------------------------------------------------------");
          Font.print(Font.ANSI_PURPLE,"           _____     _                    _____         _      _____                 ");
          Font.print(Font.ANSI_PURPLE,"          |   __|___|_|___ ___ ___ ___   | __  |___ ___| |_   |  _  |___ ___ ___ ___ ");
          Font.print(Font.ANSI_PURPLE,"          |__   |  _| |_ -|_ -| . |  _|  |    -| . |  _| '_|  |   __| .'| . | -_|  _|");
          Font.print(Font.ANSI_YELLOW,"          |_____|___|_|___|___|___|_|    |__|__|___|___|_,_|  |__|  |__,|  _|___|_|  ");
          Font.print(Font.ANSI_YELLOW,"                                                                        |_|          ");
          System.out.println("-----------------------------------------------------------------------------------------------");
          if (choice[i] != 0 && choice[i] != 1 && choice[i] != 2){
            //If user did type number other than 0,1, or 2. There will be error and redo
            valid = false;
            Font.print(Font.ANSI_RED, "                    <<< Invalid !! Only numbers (0-2) are allowed.>>>");
          }
          else{
            if(choice[i] == randomNum[i]){
              Font.print(Font.ANSI_BLUE,"                                 Unfortunately, it's a draw.");
              win_lose_draw[2]++;
            }
            else if (choice[i] == 0 && randomNum[i] == 1){
              Font.print(Font.ANSI_RED,"                         Opps, the rock has smashed your scissor!");
              win_lose_draw[1]++;
            }
            else if (choice[i] == 0 && randomNum[i] == 2){
              Font.print(Font.ANSI_GREEN,"                 Congratulations, your scissor has cut through the paper!");
              win_lose_draw[0]++;
            }
            else if (choice[i] == 1 && randomNum[i] == 0){
              Font.print(Font.ANSI_GREEN,"          Congratulations, you have destroyed the opponent's scissor with a rock!");
              win_lose_draw[0]++;
            }
            else if (choice[i] == 1 && randomNum[i] == 2){
              Font.print(Font.ANSI_RED, "                           Oh no, Your rock has lose to the paper!");
              win_lose_draw[1]++;
            }
            else if (choice[i] == 2 && randomNum[i] == 0){
              Font.print(Font.ANSI_RED, "                Yucks, the opponent's scissor has cut through your paper!");
              win_lose_draw[1]++;
            }
            else{
              Font.print(Font.ANSI_GREEN,"                 Congratulation, you have won the rock with a paper!");
              win_lose_draw[0]++;
            }
          }
          if (i == 9){
            Screen.pause(1.5f);
          }
        }while (!valid);

      }

      // return the array of win lose draw to display the result later
      return win_lose_draw;
    }

    // this method is used for generate random number
    public static int[] randomNumgenerate(){
      int[] randomNum = new int[10];

      for (int i = 0; i< 10 ; i++){
        randomNum[i] = (int)(Math.random() * (2 - 0 + 1) + 0); 
      }

      return randomNum;
    }
    // method for showing result
    public static void showresult(int[] result){
      LocalDate date1 = LocalDate.now();
      LocalTime time1 = LocalTime.now();
      DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("hh:mm:ss");
      Screen.clear();

      System.out.println("-----------------------------------------------------------------------------------------------");
      Font.print(Font.ANSI_PURPLE,"           _____     _                    _____         _      _____                 ");
      Font.print(Font.ANSI_PURPLE,"          |   __|___|_|___ ___ ___ ___   | __  |___ ___| |_   |  _  |___ ___ ___ ___ ");
      Font.print(Font.ANSI_PURPLE,"          |__   |  _| |_ -|_ -| . |  _|  |    -| . |  _| '_|  |   __| .'| . | -_|  _|");
      Font.print(Font.ANSI_YELLOW,"          |_____|___|_|___|___|___|_|    |__|__|___|___|_,_|  |__|  |__,|  _|___|_|  ");
      Font.print(Font.ANSI_YELLOW,"                                                                        |_|          ");
      System.out.println("-----------------------------------------------------------------------------------------------");
      System.out.println(date1+ "                                                                        "+ time1.format(dtf2));
      System.out.println("                         Thank You for playing Scissor Rock Paper !!");
      System.out.println("                                   This is your result:");
      Font.print(Font.ANSI_GREEN,"                                      Win     -     "+ result[0]);
      Font.print(Font.ANSI_RED,"                                      Lose    -     "+ result[1]);
      Font.print(Font.ANSI_CYAN,"                                      Draw    -     "+ result[2]);


    }



    public static void restartgame(){
      boolean validation = true;
      int decision = 0;
      do{
        System.out.println("                  ==========================================================");
        System.out.println("                                          1. Yes");
        System.out.println("                                          2. No");
        System.out.print("                         Do you want to start another round ? (1 / 2) :");
        decision = input.nextInt();
  
        if(decision != 2 && decision != 1){
          Screen.clear();
          System.out.println("-----------------------------------------------------------------------------------------------");
          Font.print(Font.ANSI_PURPLE,"           _____     _                    _____         _      _____                 ");
          Font.print(Font.ANSI_PURPLE,"          |   __|___|_|___ ___ ___ ___   | __  |___ ___| |_   |  _  |___ ___ ___ ___ ");
          Font.print(Font.ANSI_PURPLE,"          |__   |  _| |_ -|_ -| . |  _|  |    -| . |  _| '_|  |   __| .'| . | -_|  _|");
          Font.print(Font.ANSI_YELLOW,"          |_____|___|_|___|___|___|_|    |__|__|___|___|_,_|  |__|  |__,|  _|___|_|  ");
          Font.print(Font.ANSI_YELLOW,"                                                                        |_|          ");
          System.out.println("-----------------------------------------------------------------------------------------------");
          Font.print(Font.ANSI_RED, "                    <<< Invalid !! Only numbers (1 or 2) are allowed.>>>");
          Font.print(Font.ANSI_RED, "                                 <<< Please try again..>>>");
          validation = false;
        }
        else{
          validation = true;
        }
      } while (!validation);
 
      if (decision == 1){
        main();
      }
      
    }
}

