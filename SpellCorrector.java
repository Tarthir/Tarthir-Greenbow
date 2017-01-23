package spell;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.*;


/**
 * Created by tyler on 1/17/2017.
 */

public class SpellCorrector implements ISpellCorrector {
    private Trie trie;
    private Trie trie2;

    public SpellCorrector(){   }
    /**
     * Tells this <code>ISpellCorrector</code> to use the given file as its dictionary
     * for generating suggestions.
     * @param dictionaryFileName File containing the words to be used
     * @throws IOException If the file cannot be read
     */
    public void useDictionary(String dictionaryFileName) throws IOException{
        File file = new File(dictionaryFileName);
        //File file2 = new File("words3.txt");
        trie = new Trie();
        //trie2 = new Trie();
        try {
            Scanner scan = new Scanner(file);

            while(scan.hasNext()){
                trie.add(scan.nextLine());
            }
        }catch(FileNotFoundException e){System.out.println(e.getMessage().toString());}

       // try {
          //  Scanner scan2 = new Scanner(file2);

           // while(scan2.hasNext()){
           //     trie2.add(scan2.nextLine());
          //  }
        //}catch(FileNotFoundException e){System.out.println(e.getMessage().toString());}
       // boolean tr = trie.equals(trie2);
       // boolean tre;

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
            String suggestedWord = "";
            suggestedWord = findSimilarWord(inputWord);
            if(suggestedWord == ""){throw new NoSimilarWordFoundException();}
            return suggestedWord;
        }
    }
    /** This function goes through a set of words up to twice to try and see if we can find a similiar word to inputWord
     * @Param trie Our Trie tree
     * @Param inputWord The word we are correcting
     * */
    private String findSimilarWord(String inputWord){
        String suggestedWord = "";
        TreeSet<String> set = new TreeSet<String>();
        set = deletionDistance(inputWord,set);
        set = transpositionDistance(inputWord,set);
        set = alterationDistance(inputWord,set);
        set = insertionDistance(inputWord,set);
        int count = 1;
        suggestedWord =  pickSuggestedWord(set,inputWord,count);
        if(suggestedWord == "") {//Go through the edit -1 words
            TreeSet<String> set2 = new TreeSet<String>();
            for (String word : set) {
                set2 = deletionDistance(word, set2);
                set2 = transpositionDistance(word, set2);
                set2 = alterationDistance(word, set2);
                set2 = insertionDistance(word, set2);
            }
            ++count;
            suggestedWord = pickSuggestedWord(set2,inputWord,count);//lets hope we find one
        }
        return suggestedWord;
    }

    String pickSuggestedWord(TreeSet<String> set,String inputWord, int count){
        if(set.isEmpty()){
            return "";
        }
        int maxFreq = 0;
        int inputLength = inputWord.length();
        String suggestedWord = "";
        for(String str : set){
            ITrie.INode node = trie.find(str);//
            if (node != null) {//is the node in our trie?
                int freqCount = node.getValue();//grab its frequency
                int currNodeLength = str.length();
                //check its freq against our currMaxfreq and if this word is within the right edit distance
                if (freqCount > maxFreq && (inputLength <= currNodeLength + count && inputLength >= currNodeLength - count)) {
                    maxFreq = node.getValue();
                    suggestedWord = str;
                }
            }
        }
        return suggestedWord;
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
            if(i+1 < inputWord.length()) {
                str = swap(str, i, i + 1);
                set.add(str);
            }
        }
        //if(set.contains("yea")){
        //    System.out.println("Got Yea");
       // }
        return set;
    }
    private TreeSet<String> alterationDistance(String inputWord, TreeSet<String> set){
        char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
        for(int i = 0; i < inputWord.length(); i++){//for every letter in the word
            String tmp = inputWord;
            for(int j = 0; j < alphabet.length; j++){//and for every letter in the alpabet
                String newWord = tmp;
                char[] cArr = newWord.toCharArray();
                if(cArr[i] != alphabet[j]) {//if the char value is the smae it would just be the same word
                    cArr[i] = alphabet[j];//replace with a differnt letter
                    String word = to_String(cArr);
                    set.add(word);
                }
            }
        }
        return set;
    }
    private TreeSet<String> insertionDistance(String inputWord, TreeSet<String> set){
        String[] alphabet = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
        for(int i = 0; i < inputWord.length(); i++){
            for(int j = 0; j < alphabet.length; j++){
                set.add(alphabet[j] + inputWord);
                if(i + 1 < inputWord.length()) {//letter added to middle of word
                    set.add(inputWord.substring(i, i + 1) + alphabet[j] + inputWord.substring(i + 1));
                }
                set.add(inputWord + alphabet[j]);
            }
        }
        return set;
    }

   private String swap(String input, int one, int two){
       if(!(two >= input.length())){
           char[] cArr = input.toCharArray();
           char tmp = cArr[one];
           cArr[one] = cArr[two];
           cArr[two] = tmp;
           return to_String(cArr);
       }
       return "Swap function Malfunctioned";
    }

    private String to_String(char[] cArr){
        String str= "";
        for(char c : cArr){
            str += c;
        }
        return str;
    }
}
