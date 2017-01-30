package hangman;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.lang.String;

import sun.reflect.generics.tree.Tree;

/**
 * Created by tyler on 1/24/2017.
 */

public class EvilHangmanGame implements IEvilHangmanGame {
    private Set<String> dictSubset;
    private int lengthOfWords;
    private Set<String> alreadyGuessed;
    EvilHangmanGame(){
        dictSubset = new TreeSet<String>();
        alreadyGuessed = new TreeSet<>();
    }

    /**
     * Starts a new game of evil hangman using words from <code>dictionary</code>
     * with length <code>wordLength</code>.
     *	<p>
     *	This method should set up everything required to play the game,
     *	but should not actually play the game. (ie. There should not be
     *	a loop to prompt for input from the user.)
     *
     * @param dictionary Dictionary of words to use for the game
     * @param wordLength Number of characters in the word to guess
     */
    public void startGame(File dictionary, int wordLength) {//added a parameter to this function
        // what if word length is 1000? Where do i get my guesses from?
        Scanner scan;
        lengthOfWords = wordLength;
        if (wordLength < 2) {
            System.out.println("Must have a word length greater or equal to 2");
            return;
        }
        try {
            scan = new Scanner(dictionary).useDelimiter("[^A-Za-z]"); //ignores all none letters
            while (scan.hasNext()) {
                String input = scan.next();
                if (input.length() == wordLength) {
                    dictSubset.add(input.toLowerCase());
                }
            }
            scan.close();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return;
        }

    }

    /**
     * Make a guess in the current game.
     *
     * @param guess The character being guessed
     *
     * @return The set of strings that satisfy all the guesses made so far
     * in the game, including the guess made in this call. The game could claim
     * that any of these words had been the secret word for the whole game.
     *
     * @throws GuessAlreadyMadeException If the character <code>guess</code>
     * has already been guessed in this game.
     */
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException{
        String strGuess = "";
        strGuess += guess;
        if(alreadyGuessed.contains(strGuess)){ throw new GuessAlreadyMadeException();}
        return partitionSet(strGuess);

    }
    /**
     * Partitions our set to find the new set for our evil algorithm
     *@Param String guess: This is our guess char in string form
     *@return returns the set which contains our words with the guess char at a given index(or not at all)
     * */
    private Set<String> partitionSet(String guess){
        HashMap<String,Set<String>> subSets = new HashMap<>();
       // ArrayList<String> patterns = new ArrayList<>();
        for(String str : dictSubset){
           // if(!containsAlreadyGuessed(str,alreadyGuessed)) {
                String pattern = makePattern(str, guess.charAt(0));
                if (subSets.containsKey(pattern)) {
                    subSets.get(pattern).add(str);
                }
                else {
                    Set<String> sets = new TreeSet<>();
                    sets.add(str);
                    subSets.put(pattern, sets);
                    //patterns.add(pattern);
                }
           // }
        }
        alreadyGuessed.add(guess);
        return chooseSet(subSets,guess);
    }
    /**
     * Checks to see if a given string contains a char which has already been guessed
     *
     * */
  /*  private boolean containsAlreadyGuessed(String input, Set<String> alreadyGuessed){
        for(String str : alreadyGuessed){
            if(input.contains(str)){
                return true;
            }
        }
        return false;
    }*/

    /**
     *This adds words to a given set according to the how many instances of a given letter there are
     *
     * */
    private String makePattern(String word, char guess){
        char[] cArr = word.toCharArray();
        String pattern = "";
        for(char c : cArr){
            if(c == guess){pattern += guess;}
            else{pattern += "-";}
        }
        return pattern;
    }

    /**
     * Chooses a set to return according to these criteria
     * 1. CHOOSE LARGEST SET
     * 2. Choose the group in which the letter does not appear at all.
     3. If each group has the guessed letter, choose the one with the
     fewest letters.
     4. If this still has not resolved the issue, choose the one with the
     rightmost guessed letter.
     5. If there is still more than one group, choose the one with the next
     rightmost letter. Repeat this step (step 4) until a group is
     chosen.
     * */
    private Set<String> chooseSet(HashMap<String,Set<String>> subSets,String guess){
        //#1: THE LARGEST SET
        int size = -1;
        Set set = subSets.entrySet();
        Iterator itr = set.iterator();
        while(itr.hasNext()){//#1
            Map.Entry sub = (Map.Entry)itr.next();
            Set<String> s = (Set<String>)sub.getValue();
            if(s.size() > size){
                size = s.size();
            }
        }
        String largestKey = getLargest(subSets, size);
        if(largestKey != ""){return dictSubset = subSets.get(largestKey);}
        //#2: the letter does not appear
        set = subSets.entrySet();
        itr = set.iterator();
        while(itr.hasNext()){
            Map.Entry sub2 = (Map.Entry)itr.next();
            String key =  sub2.getKey().toString();
            if(!key.contains(guess)){
                return dictSubset = (Set<String>) sub2.getValue();
            }
        }
        //#3: fewest letters
        set = subSets.entrySet();
        itr = set.iterator();
        String key = "";
        int counter = lengthOfWords;
        while(itr.hasNext()){//get the fewest number of char occurrences
            Map.Entry sub3 = (Map.Entry) itr.next();
            int charCount = charCounter(sub3.getKey().toString().toCharArray(), guess.charAt(0));
            if(charCount < counter){counter = charCount; key = sub3.getKey().toString();}//otherwise save the key of the set with the fewest # of letters
        }
        if(getFewestUnique(subSets,counter,guess.charAt(0))){return subSets.get(key);}//make sure there is only one set that has the fewest
        return  dictSubset = rightMostSet(subSets,guess.charAt(0));//#4
    }

    /**
     * Returns the key of the largest set in the map
     *@Param HashMap subSets: These are our partitioned sets. Their key is their pattern
     *@return returns the key of the largest set i nthe Hashmap, or an empty string if  n>1 sets are the same size
     * */
    String getLargest(HashMap<String,Set<String>> subSets, int size){
        Set set = subSets.entrySet();
        Iterator itr = set.iterator();
        String largestKey = "";
        int count = 0;
        while(itr.hasNext()){
            Map.Entry sub = (Map.Entry)itr.next();
            Set<String> s = (Set<String>)sub.getValue();
            if(size == s.size() && count == 0){//there is at least one set with the same number of elements
                count++;
                largestKey = sub.getKey().toString();
            }
            else if(size == s.size() && count > 0){
                count++;
            }
        }
        if(count > 1){return "";}//return an empty string if size == s.size() > 1 times
        return largestKey;//else
    }
    //makes sure we have only one set that has the lowest amount of 'guess' chars in it
    private boolean getFewestUnique(HashMap<String,Set<String>> subSets,int counter, char guess){
        Set set = subSets.entrySet();
        Iterator itr = set.iterator();
        int count = 0;//how many sets have the minimun number of 'guess' chars
        while(itr.hasNext()){
            Map.Entry sub3 = (Map.Entry) itr.next();
            int charCount = charCounter(sub3.getKey().toString().toCharArray(), guess);
            if(counter == charCount){count++;}//otherwise save the key of the set with the fewest # of letters
        }
        return (count == 1);
    }
    //Counts up the number of a given char in a char array
    private int charCounter(char[] cArr, char guess){
        int counter = 0;
        for(char c : cArr){
            if(c == guess){
              counter++;
            }
        }
        return counter;
    }
    //finds the set with the rightmost point of the guess char
    private Set<String> rightMostSet(HashMap<String,Set<String>> subSets,char guess) {
        Set set = subSets.entrySet();
        Iterator itr = set.iterator();
        LinkedList<String> keys = new LinkedList<>();
        int loopCount = 1, count = 0;
        String key = "";
        while(itr.hasNext()){//grab all the keys
            Map.Entry sub = (Map.Entry)itr.next();
            keys.add(sub.getKey().toString());
        }
        itr = keys.listIterator();
        while(itr.hasNext()){//find the rightmost of the keys
            String str = itr.next().toString();
            if (str.lastIndexOf(guess, str.length() - loopCount) == str.length() - loopCount){
                key = str;
                count++;
            }
            else{keys.remove(str); itr = keys.listIterator(); count = 0; continue;}//remove the String that is not rightmost and reset
            if(str.equals(keys.getLast()) && count != 1){loopCount++; itr = keys.listIterator(); count = 0; continue;}//reset
        }
        return subSets.get(key);
    }
}
