package spell;
import  java.util.*;
/**
 * Created by tyler on 1/17/2017.
 */

public class Trie implements ITrie {
    private int wordCount;//counts the number of words in the trie
    private int nodeCount;
    private TreeSet<String> words;
    private INode root;

    public Trie(){
        root = new INode();
        nodeCount = 1;//including the root
        wordCount = 0;
        words = new TreeSet<String>();
    }
    /**
     * Adds the specified word to the trie (if necessary) and increments the word's frequency count
     *
     * @param currNode The starting node
     * @param wordPos Where in the word we are at
     * @param word A string in which our traversal through it is tracked by int wordPos
     *
     */
    private void nodeTraversal(INode currNode,String word, int wordPos){
        int arrPos = word.charAt(wordPos) - AlphabetEnum.A.getValue();
        if(currNode.alphabet[arrPos] == null){//if this spot isnt filled, fill it!
            INode n = new INode();
            ++nodeCount;
            currNode.alphabet[arrPos] = n;
            if(wordPos == word.length() - 1) {//if we are at the end
                wordCount++; currNode.alphabet[arrPos].incrementFreq(); return;
            }
            //not at the end so keep going
            nodeTraversal(currNode.alphabet[arrPos], word, ++wordPos);

        }
        else if(currNode.alphabet[arrPos] != null && wordPos == word.length()-1){//if this is not null and it is the end of the word
            wordCount++;
            currNode.alphabet[arrPos].incrementFreq();
            return;
        }
        else{//this spot has already been filled! But we are not at the end of the word
            nodeTraversal(currNode.alphabet[arrPos],word,++wordPos);
        }
    }

    /**
     * Adds the specified word to the trie (if necessary) and increments the word's frequency count
     *
     * @param word The word being added to the trie
     */
    public void add(String word){
        words.add(word);
        //System.out.println(word);
        INode node = (INode)find(word);//can we find this word?
        if(node != null){
            node.incrementFreq();
        }
        else{//if this word does not exist
            int wordPos = 0;
            nodeTraversal(root, word, wordPos);
        }

    }
    /**
     * Helper method for the find(String word) function
     *
     * @param word The word being searched for
     * @param currNode The current node
     * @param wordPos where in the word we are at
     * @return A reference to the trie node that represents the word,
     * 			or null if the word is not in the trie
     */
    private ITrie.INode findHelper(INode currNode,String word, int wordPos){
        int index = word.charAt(wordPos) - AlphabetEnum.A.getValue();
        if(currNode.alphabet[index] == null){return null;}
        else if (currNode.alphabet[index] != null) {//if this spot isnt filled
            //if we are at the end of the word and this is actually a word
            if (wordPos == word.length() - 1 && currNode.alphabet[index].getValue() > 0) {
                return currNode.alphabet[index];//we found it!
            }
            else if(wordPos == word.length() - 1 && currNode.alphabet[index].getValue() == 0) {
                return null;//The Word does not exist
            }
        }
        return findHelper(currNode.alphabet[index], word, ++wordPos);

    }
    /**
     * Searches the trie for the specified word
     *
     * @param word The word being searched for
     *
     * @return A reference to the trie node that represents the word,
     * 			or null if the word is not in the trie
     */
    public ITrie.INode find(String word){
        if(word.length() == 0){return null;}
        word = word.toLowerCase();//lower the case
        int wordPos = 0;
        int index = word.charAt(wordPos) - AlphabetEnum.A.getValue();
        if(root.alphabet[index] == null){return null;}
        return findHelper(root,word,wordPos);
    }

    /**
     * Returns the number of unique words in the trie
     *
     * @return The number of unique words in the trie
     */
    public int getWordCount(){
        return this.wordCount;
    }

    /**
     * Returns the number of nodes in the trie
     *
     * @return The number of nodes in the trie
     */
    public int getNodeCount(){
        return this.nodeCount;
    }

    /**
     * The toString specification is as follows:
     * For each word, in alphabetical order:
     * <word>\n
     */
    @Override
    public String toString(){
        if(root.alphabet == null){return "";}
        int num = 0;
        Iterator itr = words.iterator();
        StringBuilder strB = new StringBuilder();
        while(itr.hasNext()){
            strB.append(itr.next() + "\n");
        }
        return strB.toString();
    }

    @Override
    public int hashCode(){
        return this.nodeCount * this.wordCount + (root.hashCode() * words.hashCode());
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null){return false;}
        else if(obj == this){return true;}
        else if(this.getClass() != obj.getClass()){return false;}
        Trie other = (Trie)obj;
        if(wordCount != other.wordCount){return false;}
        else if(nodeCount != other.nodeCount){return false;}
        boolean isTrue = true;
        return equalTraversal(root,other.root,isTrue);
    }
    /**
     * This function traverses two Tries and sees if they are exactly equal
     * @Param myNode The "comparer" object
     * @Param ONode The "comparee" object
     * @return if they are exactly the same
     * */
    boolean equalTraversal(INode myNode,INode ONode,boolean isTrue){
        for(int i = 0; i < 26; i++){//go through all the elements
            if((myNode.alphabet[i] == null && ONode.alphabet[i] == null)){
               continue;
            }
            else if((myNode.alphabet[i] != null && ONode.alphabet[i] != null)){
                 if((myNode.alphabet[i].getValue() != ONode.alphabet[i].getValue())){//if they dont have the same value
                    return false;
                 }
             }
             else{
                return false;
            }
            isTrue = equalTraversal(myNode.alphabet[i],ONode.alphabet[i],isTrue);
        }
        return isTrue;
    }

    /**
     * Your trie node class should implement the ITrie.INode interface
     */
    public class INode implements ITrie.INode{
        private int freqCount;
        public INode[] alphabet;
        INode(){
            this.alphabet = new INode[26];
            freqCount = 0;
        }
        INode(int freqCount){
            this.alphabet = new INode[26];
            this.freqCount = freqCount;
        }
        /**
         * Returns the frequency count for the word represented by the node
         *
         * @return The frequency count for the word represented by the node
         */
        public int getValue(){
           return  this.freqCount;
        }
        public void incrementFreq(){
            this.freqCount++;
        }

    }

}
