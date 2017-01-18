import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.*;

import sun.reflect.generics.tree.Tree;

/**
 * Created by tyler on 1/17/2017.
 */

public class SpellCorrector implements ISpellCorrector {
    public Trie trie;

    SpellCorrector(){
        trie = new Trie();
    }

    /**
     * Tells this <code>ISpellCorrector</code> to use the given file as its dictionary
     * for generating suggestions.
     * @param dictionaryFileName File containing the words to be used
     * @throws IOException If the file cannot be read
     */
    public void useDictionary(String dictionaryFileName) throws IOException{
        File file = new File(dictionaryFileName);
        try {
            Scanner scan = new Scanner(file);

            while(scan.hasNext()){
                trie.add(scan.nextLine());
            }
        }catch(FileNotFoundException e){System.out.println(e.getMessage().toString());}
    }

    /**
     * Suggest a word from the dictionary that most closely matches
     * <code>inputWord</code>
     * @param inputWord
     * @return The suggestion
     * @throws NoSimilarWordFoundException If no similar word is in the dictionary
     */
    public String suggestSimilarWord(String inputWord) throws NoSimilarWordFoundException{
        ITrie.INode node = trie.find(inputWord);
        if(node != null){//if we have found the node
            return inputWord;//just return th string they gave us because we know we found it
        }
        else{//we have not found the node
            return findSimilarWord((ITrie.INode)trie,inputWord);
        }
    }
    /**
     * @Param trie Our Trie tree
     * @Param inputWord The word we are correcting
     * */
    private String findSimilarWord(ITrie.INode trie,String inputWord){
        String suggestedWord = "";
        TreeSet<String> set = new TreeSet<String>();
        for(int i = 0; i < 2; i++) {
            set = deletionDistance(inputWord,set);
            if (set.size() == 0) {
                set = transpositionDistance(inputWord,set);
            }
            if (set.size() == 0) {
                set = alterationDistance(inputWord,set);
            }
            if (set.size() == 0) {
                set = insertionDistance(inputWord,set);
            }
            if (set.size() > 0) {
               return pickSuggestedWord();
            }
        }
        throw new NoSimilarWordFoundException();
    }

    private TreeSet<String> deletionDistance(String inputWord, TreeSet<String> set){
        for(int i = 0; i < inputWord.length(); i++){
            StringBuilder strB = new StringBuilder();
            strB.append(inputWord);
            set.add(strB.deleteCharAt(i).toString());
        }
        return set;
    }
    private TreeSet<String> transpositionDistance(String inputWord, TreeSet<String> set){
        for(int i = 0; i < inputWord.length(); i++){
                String str = inputWord;
                str = swap(str,i,i+1);
                set.add(str);
        }
        return set;
    }
    private TreeSet<String> alterationDistance(String inputWord, TreeSet<String> set){
        for(int i = 0; i < inputWord.length(); i++){

        }
    }
    private TreeSet<String> insertionDistance(String inputWord, TreeSet<String> set){
        for(int i = 0; i < inputWord.length(); i++){

        }
    }

   private String swap(String input, int one, int two){
       if(!(two >= input.length())){
           char[] cArr = input.toCharArray();
           char tmp = cArr[one];
           cArr[one] = cArr[two];
           cArr[two] = tmp;
           return cArr.toString();
       }
    }

}
