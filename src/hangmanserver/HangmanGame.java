package hangmanserver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jaime Sánchez Rodríguez
 */
public class HangmanGame {
    private String solutionWord;
    private String guessingWord;
    private int tries = Utils.MAX_ERRORS;
    public HangmanGame(){
        solutionWord = getRandomWord();
        guessingWord = getBlankWord(solutionWord.length());
    }
    
    
    
    private String getRandomWord(){
        String randomWord = "default";
        File wordsFile = Utils.WORDS_FILE;
        try {
            FileReader fr = new FileReader(wordsFile);
            BufferedReader br = new BufferedReader(fr);
        int randomNum = (int) (Math.random() * 50);
        for(int i=0;i<randomNum;i++){
            br.readLine(); //Skip randomNum - 1 lines
        }
        randomWord = br.readLine(); //Chosen randomWord
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HangmanGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HangmanGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return randomWord;
    }
    
    private static String getBlankWord(int length){
        String blank = "";
        for(int i = 0; i < length; i++){
            blank += "_";
        }
        return blank;
    }
    
    private void updateBlank(int pos, String letter){
        StringBuilder str = new StringBuilder(guessingWord);
        str.setCharAt(pos, letter.charAt(0));
        guessingWord = str.toString();
    }
    
    private boolean wordHasLetter(String letter){
        if(solutionWord.contains(letter)){
            String solution = solutionWord;
            while(solution.contains(letter)){
                int i = solution.indexOf(letter);
                updateBlank(i,letter);
                solution = solution.replaceFirst(letter, "*");
            }
            return true;          
        }
        return false;
    }
    
    public Result guess(String letter){
        if(guessingWord.contains(letter)){
            return Result.REPEATED;
        }
        if(wordHasLetter(letter)){
            if(solutionWord.equals(guessingWord)){//Win condition
                return Result.WIN;
            }
            return Result.RIGHT;
        }else{
            tries--;
            if((tries) == 0){
                return Result.LOSE;
            }
            return Result.WRONG;
        }
    }
    
    public void play(DataInputStream in, DataOutputStream out) throws IOException{
        out.writeUTF("Word with " + guessingWord.length() + " letters: " + guessingWord + "\nTry a letter!");
        Result result;
        do{
            result = guess(in.readUTF()); 
            switch(result){
                case WIN:
                    out.writeUTF("You won! " + guessingWord);
                    break;
                case LOSE:
                    out.writeUTF("You lost! The word was: " + solutionWord);
                    break;
                case RIGHT:
                    out.writeUTF("Right choice!" + tries + " tries remaining: " + guessingWord);
                    break;
                case REPEATED:
                    out.writeUTF("Already guessed that letter!" + tries + " tries remaining: " + guessingWord);
                    break;
                case WRONG:
                    out.writeUTF("Wrong! " + tries + " tries remaining: " + guessingWord);
                    break;
                default:
            }
        }while(result != Result.WIN && result!= Result.LOSE);
    }
    
    public enum Result{
        WIN,
        RIGHT,
        REPEATED,
        WRONG,
        LOSE
    }
}
