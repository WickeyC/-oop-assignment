package five_dice;

public class Player {
    int overscore, storerownum, storesecondrownum, priority;
    int[] diceRow = new int[5];
    int[] notRowNumber = new int[5];
    
    public String wintype(){
        if(priority == 6){
            return "Five in row";
        }else if(priority == 5){
            return "Four in row";
        }else if(priority == 4){
            return "Three and pair in row";
        }else if(priority == 3){
            return "Three in row"; 
        }else if(priority == 2){
            return "Two pair";
        }else if(priority == 1){
            return "One pair";
        }else{
            return "No type";
        }
    }

}
