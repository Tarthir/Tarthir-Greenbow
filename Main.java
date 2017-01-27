package hangman;

import java.io.File;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by tyler on 1/25/2017.
 */
public class Main{
    int lengthOfWords;
    public static void main (String[] args){
        Main m = new Main();
        IEvilHangmanGame game = new EvilHangmanGame();

        if(args.length > 3){System.out.println("Too many arguments, try again");return;}
        String dictionary = args[0];
        m.lengthOfWords = Integer.parseInt(args[1]);
        int numGuesses = Integer.parseInt(args[2]);
        File file = new File(dictionary);
        game.startGame(file,m.lengthOfWords);
        m.prompter(numGuesses, game);
    }
    void prompter(int numGuesses, IEvilHangmanGame game){
        TreeSet<String> alreadyGuessed = new TreeSet<>();
        Set<String> wordSet = new TreeSet<>();
        while(numGuesses != 0) {
            Scanner scan;
            System.out.println("You have " + numGuesses + " guesses left");
            System.out.println("Used Letters: ");
            for(String str : alreadyGuessed){ System.out.println(str + " ");}
            System.out.println("Word: " + printWord());
            System.out.println("Enter Guess: ");
            scan = new Scanner(System.in);
            String guessInput = scan.nextLine();
            scan.close();
            if (guessInput.length() == 1 && Character.isLetter(guessInput.charAt(0))) {
                alreadyGuessed.add(guessInput);
                    try {
                        wordSet = game.makeGuess(guessInput.charAt(0));
                        //numGuesses--;
                    }
                    catch (IEvilHangmanGame.GuessAlreadyMadeException e) {
                        System.out.println("You have already guessed " + guessInput);
                        continue;//reprompt
                    }
            }
            else{System.out.println("Invalid Input"); numGuesses--; continue;}//decrement here?
        }
    }
    //used to print out the word being guessed
    private String printWord(){
        String str = "";
        while(lengthOfWords-- > 0){
            str += "-";
        }
        return str;
    }

}
