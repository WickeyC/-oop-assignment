package five_dice;

import javax.swing.JOptionPane;
import java.util.Random;
import java.util.Arrays ;

public class FiveDice {
    
    public static void main() {
        int option;
        boolean pass = true;
        Player user = new Player();
        Player com = new Player();
        String gameBrief = "In this game, you will be playing with the computer. Each of the player will be given 5 dices and throws randomly."
                + "\nThen, we will compare the combination. The one with higher combination will win the game.\n\nThe combination are (from highest to lowest):"
                + "\n5 in rows : aaaaa\n4 in rows : aaaab\n3 in rows with a pair : aaabb\n3 in rows : aaabc\n2 pairs in rows : aabbc\n1 pair in rows: aabcd\n\n"
                + "If both players get the same combination, the one with higher number will win the game, else it would be tie.\n"
                + "After 10 rounds, the one with the higher score win.\nEnter 1 to continue.";
        do {
            //Print game rules
            String startGame = JOptionPane.showInputDialog(null, gameBrief, "Welcome to FiveDice Game", JOptionPane.QUESTION_MESSAGE);
            try {
                int acceptStartGame = Integer.parseInt(startGame);
                if (acceptStartGame != 1) {
                    JOptionPane.showMessageDialog(null, "Please enter 1 to start the game.", "Welcome to FiveDice Game", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    pass = false;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Please only enter 1 to start the game.", "Welcome to FiveDice Game", JOptionPane.INFORMATION_MESSAGE);
            }
        } while (pass); //Only end loop if the user enter valid input

        
        do {
            //Start the game with resetting the variable
            int count = 1;
            user.overscore = 0;
            com.overscore = 0;
            do {
                user.priority = 0;
                com.priority = 0;
                //Random number generate and assign
                Random ranNum = new Random();
                for (int i = 0; i < 5; i++) {
                    user.diceRow[i] = 1 + ranNum.nextInt(6);
                    com.diceRow[i] = 1 + ranNum.nextInt(6);
                }
                
                //Testing Purpose
                /*user.diceRow[0] = 1; user.diceRow[1] = 1; user.diceRow[2] = 2; user.diceRow[3] = 2; user.diceRow[4] = 4;
                com.diceRow[0] = 1;  com.diceRow[1] = 1;  com.diceRow[2] = 2;  com.diceRow[3] = 2;  com.diceRow[4] = 5;*/
                
                //Sort the number
                Arrays.sort(user.diceRow);
                Arrays.sort(com.diceRow);                
                //Determine the type of the rows
                rowType(user);
                rowType(com);
                //bigger priority means win
                if (user.priority > com.priority) {
                    user.overscore += 1;
                } else if (com.priority > user.priority) {
                    com.overscore += 1;
                } else {
                    //if same priority, compare the number in the rows
                    sizeCompare(user, com);
                }
                
                String displayTitle = "FiveDice Game -- Round " + count;
                String displayMessage = "User Dice: " + Arrays.toString(user.diceRow) + " --- " + user.wintype() + "\nCom Dice: " + Arrays.toString(com.diceRow) + " --- " + com.wintype()
                        + "\nUser: " + user.overscore + " point" + "\nCom : " + com.overscore + " point";
                //Display the dice number, row type and score
                JOptionPane.showMessageDialog(null, displayMessage, displayTitle, JOptionPane.INFORMATION_MESSAGE);
                count++;

            } while (count <= 10); // loop until the 10 rounds have run
            String resultmessage;
            String resultscore = "User: " + user.overscore + " point" + "\nCom : " + com.overscore + " point\n\n";
            if (user.overscore > com.overscore) {
                resultmessage = "Yeah! You won!\n\nPat Riley--\"A champion needs a motivation above and beyond winning.\"";
            } else if (com.overscore > user.overscore) {
                resultmessage = "Sorry. You lost.\n\nMorgan Wootten--\"You learn more from losing than winning. You learn how to keep going.\"";
            } else {
                resultmessage = "A draw. Try harder next time.\n\nLionel Messi--\"There are more important things in life than winning or losing a game.\"";
            }
            String result = resultscore + resultmessage;
            //print the message either the user win, the computer win or tie
            JOptionPane.showMessageDialog(null, result, "FiveDice Game Result", JOptionPane.INFORMATION_MESSAGE);
            //Prompt and ask the user if he/she wants to play again
            option = JOptionPane.showConfirmDialog(null, "Play again?", "FiveDice Game", JOptionPane.YES_NO_CANCEL_OPTION);

        } while (option == JOptionPane.YES_OPTION);// loop back if the user wants to play again
        pass = true;
        do {
            //Prompt user to give rating for this game
            String rating = JOptionPane.showInputDialog(null, "Give us a rating from 1 - 10", "FiveDice Game", JOptionPane.QUESTION_MESSAGE);
            try {
                int acceptRating = Integer.parseInt(rating);
                if (acceptRating > 10) {
                    JOptionPane.showMessageDialog(null, "Your ratings is too high for us, Please consider to choose between 1 - 10.", "FiveDice Game", JOptionPane.INFORMATION_MESSAGE);
                } else if (acceptRating < 1) {
                    JOptionPane.showMessageDialog(null, "Please don't be mad. Choose between 1 - 10.", "FiveDice Game", JOptionPane.INFORMATION_MESSAGE);
                } else if (acceptRating >= 1 && acceptRating <= 10) {
                    pass = false;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Please only enter input between 1 - 10.", "FiveDice Game", JOptionPane.INFORMATION_MESSAGE);
                pass = true;
            }
        } while (pass); //Only end loop if the user enter valid input

        JOptionPane.showMessageDialog(null, "Thank you for playing", "FiveDice Game", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void rowType(Player userCom) {

        boolean temp;
        //five in row, priority = 6(highest priority)
        if (userCom.diceRow[0] == userCom.diceRow[1] && userCom.diceRow[1] == userCom.diceRow[2] && userCom.diceRow[2] == userCom.diceRow[3] && userCom.diceRow[3] == userCom.diceRow[4]) {
            userCom.storerownum = userCom.diceRow[0];
            userCom.priority = 6;
        }

        //four in row, priority = 5
        if (userCom.diceRow[0] == userCom.diceRow[1] && userCom.diceRow[1] == userCom.diceRow[2] && userCom.diceRow[2] == userCom.diceRow[3] && userCom.diceRow[3] != userCom.diceRow[4]) { //xxxxy
            userCom.storerownum = userCom.diceRow[0];
            userCom.priority = 5;
            userCom.notRowNumber[0] = userCom.diceRow[4];
        } else if (userCom.diceRow[0] != userCom.diceRow[1] && userCom.diceRow[1] == userCom.diceRow[2] && userCom.diceRow[2] == userCom.diceRow[3] && userCom.diceRow[3] == userCom.diceRow[4]) { //yxxxx
            userCom.storerownum = userCom.diceRow[1];
            userCom.priority = 5;
            userCom.notRowNumber[0] = userCom.diceRow[0];
        }

        //three and pair in row, priority = 4
        if (userCom.diceRow[0] == userCom.diceRow[1] && userCom.diceRow[1] != userCom.diceRow[2] && userCom.diceRow[2] == userCom.diceRow[3] && userCom.diceRow[3] == userCom.diceRow[4]) { //xxyyy
            userCom.storerownum = userCom.diceRow[2];
            userCom.storesecondrownum = userCom.diceRow[0];
            userCom.priority = 4;
        } else if (userCom.diceRow[0] == userCom.diceRow[1] && userCom.diceRow[1] == userCom.diceRow[2] && userCom.diceRow[2] != userCom.diceRow[3] && userCom.diceRow[3] == userCom.diceRow[4]) { //yyyxx
            userCom.storerownum = userCom.diceRow[0];
            userCom.storesecondrownum = userCom.diceRow[3];
            userCom.priority = 4;
        }

        //three in row, priority = 3
        if (userCom.diceRow[0] == userCom.diceRow[1] && userCom.diceRow[1] == userCom.diceRow[2] && userCom.diceRow[2] != userCom.diceRow[3] && userCom.diceRow[3] != userCom.diceRow[4]) {//xxxyz
            userCom.storerownum = userCom.diceRow[0];
            userCom.priority = 3;
            userCom.notRowNumber[0] = userCom.diceRow[3];
            userCom.notRowNumber[1] = userCom.diceRow[4];
        } else if (userCom.diceRow[0] != userCom.diceRow[1] && userCom.diceRow[1] == userCom.diceRow[2] && userCom.diceRow[2] == userCom.diceRow[3] && userCom.diceRow[3] != userCom.diceRow[4]) {//xyyyz
            userCom.storerownum = userCom.diceRow[1];
            userCom.priority = 3;
            userCom.notRowNumber[0] = userCom.diceRow[0];
            userCom.notRowNumber[1] = userCom.diceRow[4];
        } else if (userCom.diceRow[0] != userCom.diceRow[1] && userCom.diceRow[1] != userCom.diceRow[2] && userCom.diceRow[2] == userCom.diceRow[3] && userCom.diceRow[3] == userCom.diceRow[4]) {//xyzzz
            userCom.storerownum = userCom.diceRow[2];
            userCom.priority = 3;
            userCom.notRowNumber[0] = userCom.diceRow[0];
            userCom.notRowNumber[1] = userCom.diceRow[1];
        }

        //two in row, priority = 2 if two pair, priority = 1 if one pair
        if (userCom.priority == 0) {
            for (int i = 0; i < 4; i++) {
                temp = (userCom.diceRow[i] == userCom.diceRow[i + 1]);
                if (temp) {
                    if (userCom.priority == 0) {
                        //one pair
                        userCom.storerownum = userCom.diceRow[i];
                        userCom.priority = 1;
                    } else {
                        //two pair
                        userCom.storesecondrownum = userCom.diceRow[i];
                        userCom.priority = 2;
                    }
                }
            }
            //store last digit
            if (userCom.priority == 2) {
                for (int i = 0; i < 5; i++) {
                    int tempHighestNum = userCom.diceRow[i];
                    if (tempHighestNum != userCom.storerownum && tempHighestNum != userCom.storesecondrownum) {
                        userCom.notRowNumber[0] = tempHighestNum;
                    }
                }
            }
            
            if (userCom.priority == 1) {
                int x = 0;
                for (int i = 0; i < 5; i++) {
                    if (userCom.diceRow[i] != userCom.storerownum) {
                        userCom.notRowNumber[x] = userCom.diceRow[i];
                        x++;
                    }
                }
            }
        }

        //No type
        if (userCom.priority == 0) {
            System.arraycopy(userCom.diceRow, 0, userCom.notRowNumber, 0, 5);
        }
    }

    public static void sizeCompare(Player user, Player com) {

        switch (user.priority) {
            case 6:
                if (user.storerownum > com.storerownum) {
                    user.overscore += 1;
                } else if (com.storerownum > user.storerownum) {
                    com.overscore += 1;
                }
                break;
            case 5:
                if (user.storerownum > com.storerownum) {
                    user.overscore += 1;
                } else if (com.storerownum > user.storerownum) {
                    com.overscore += 1;
                } else {
                    if (user.notRowNumber[0] > com.notRowNumber[0]) {
                        user.overscore += 1;
                    } else if (com.notRowNumber[0] > user.notRowNumber[0]) {
                        com.overscore += 1;
                    }
                }
                break;
            case 4:
                if (user.storerownum > com.storerownum) {
                    user.overscore += 1;
                } else if (com.storerownum > user.storerownum) {
                    com.overscore += 1;
                } else {
                    if (user.storesecondrownum > com.storesecondrownum) {
                        user.overscore += 1;
                    } else if (com.storesecondrownum > user.storesecondrownum) {
                        com.overscore += 1;
                    }
                }
                break;
            case 3:
                if (user.storerownum > com.storerownum) {
                    user.overscore += 1;
                } else if (com.storerownum > user.storerownum) {
                    com.overscore += 1;
                } else {
                    if (user.notRowNumber[1] > com.notRowNumber[1]) {
                        user.overscore += 1;
                    } else if (com.notRowNumber[1] > user.notRowNumber[1]) {
                        com.overscore += 1;
                    } else {
                        if (user.notRowNumber[0] > com.notRowNumber[0]) {
                            user.overscore += 1;
                        } else if (com.notRowNumber[0] > user.notRowNumber[0]) {
                            com.overscore += 1;
                        }
                    }
                }
                break;
            case 2:
                int isUserWin = 0,
                 isComWin = 0;
                if (user.storesecondrownum > com.storesecondrownum) {
                    isUserWin += (user.storesecondrownum - com.storesecondrownum);
                } else if (com.storesecondrownum > user.storesecondrownum) {
                    isComWin += (com.storesecondrownum - user.storesecondrownum);
                }
                if (user.storerownum > com.storerownum) {
                    isUserWin += (user.storerownum - com.storerownum);
                } else if (com.storerownum > user.storerownum) {
                    isComWin += (com.storerownum - user.storerownum);
                }
                if (isUserWin > isComWin) {
                    user.overscore += 1;
                } else if (isComWin > isUserWin) {
                    com.overscore += 1;
                } else {
                    if (user.notRowNumber[0] > com.notRowNumber[0]) {
                        user.overscore += 1;
                    } else if (com.notRowNumber[0] > user.notRowNumber[0]) {
                        com.overscore += 1;
                    }
                }
                break;
            case 1:
                if (user.storerownum > com.storerownum) {
                    user.overscore += 1;
                } else if (com.storerownum > user.storerownum) {
                    com.overscore += 1;
                } else {
                    if (user.notRowNumber[2] > com.notRowNumber[2]) {
                        user.overscore += 1;
                    } else if (com.notRowNumber[2] > user.notRowNumber[2]) {
                        com.overscore += 1;
                    } else {
                        if (user.notRowNumber[1] > com.notRowNumber[1]) {
                            user.overscore += 1;
                        } else if (com.notRowNumber[1] > user.notRowNumber[1]) {
                            com.overscore += 1;
                        } else {
                            if (user.notRowNumber[0] > com.notRowNumber[0]) {
                                user.overscore += 1;
                            } else if (com.notRowNumber[0] > user.notRowNumber[0]) {
                                com.overscore += 1;
                            }
                        }
                    }
                }
                break;
            case 0:
                boolean notEnd = true;
                int i = 0;
                do{
                    if (user.notRowNumber[i] > com.notRowNumber[i]){
                        user.overscore += 1;
                        notEnd = false;
                    } else if (com.notRowNumber[i] > user.notRowNumber[i]){
                        com.overscore += 1;
                        notEnd = false;
                    } else if (i == 4){
                        notEnd = false;
                    }
                    i++;
                }while(notEnd);
                break;
        }
    }
}



