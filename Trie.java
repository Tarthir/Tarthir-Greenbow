import  java.util.*;
/**
 * Created by tyler on 1/17/2017.
 */

public class Trie implements ITrie {
    private int wordCount;//counts the number of words in the trie
    private int nodeCount;
    private TreeSet<String> words;
    private INode root;

    Trie(){
        root = new INode();
        nodeCount = 0;
        wordCount = 0;
        words = new TreeSet<String>();
    }
    /**
     * Adds the specified word to the trie (if necessary) and increments the word's frequency count
     *
     * @param currNode The starting node
     * @param arrPos The position in the INode array we are going to create a node in
     * @param wordPos Where in the word we are at
     * @param word A string in which our traversal through it is tracked by int wordPos
     *
     */
    private void nodeTraversal(INode currNode,String word, int wordPos, int arrPos){

        if(wordPos < word.length()) {//if we are in-bounds
            arrPos = word.charAt(wordPos) - AlphabetEnum.A.getValue();
            if (currNode.alphabet[arrPos] == null) {//if this spot isnt filled, fill it!
                //ceate the new node and add it in
                INode n = new INode();
                currNode.alphabet[arrPos] = n;
                if(wordPos == word.length() - 1) {wordCount++; currNode.alphabet[arrPos].incrementFreq(); return;}//we are done
                nodeTraversal(currNode.alphabet[arrPos], word, ++wordPos, arrPos);
            }
            else {
                nodeTraversal(currNode.alphabet[arrPos], word, ++wordPos, arrPos);
            }
        }
    }

    /**
     * Adds the specified word to the trie (if necessary) and increments the word's frequency count
     *
     * @param word The word being added to the trie
     */
    public void add(String word){
        words.add(word +"\n");
        //System.out.println(word);
        INode node = (INode)find(word);//can we find this word?
        if(node != null){
            node.incrementFreq();
            wordCount++;
        }
        else{//if this word does not exist
            int wordPos = 0;
            int index = word.charAt(wordPos) - AlphabetEnum.A.getValue();
            if(root.alphabet[index] == null){//if this spot isnt filled, fill it!
                INode n = new INode();
                root.alphabet[index] = n;
                nodeTraversal(root.alphabet[index],word,++wordPos,index);
            }
            else{//this spot has already been filled! Lets see what hasnt been
                nodeTraversal(root.alphabet[index],word,++wordPos,index);
            }
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
        if(wordPos < word.length()) {
            int index = word.charAt(wordPos) - AlphabetEnum.A.getValue();
            if (currNode.alphabet[index] != null) {//if this spot isnt filled
                //if we are at the end of the word and this is actually a word
                if (wordPos == word.length() - 1 && currNode.alphabet[index].getValue() > 0) {
                    return currNode;//we found it!
                }
                findHelper(currNode.alphabet[index], word, ++wordPos);
            }
        }
        return null;
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
        word = word.toLowerCase();//lower the case
        int wordPos = 0;
        int index = word.charAt(wordPos) - AlphabetEnum.A.getValue();
        if(root.alphabet[index] == null){//if this spot isnt filled, fill it!
           return null;
        }
        else{//this spot has already been filled! Lets see if the rest is there
           return findHelper(root.alphabet[index],word,++wordPos);
        }
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

        return equalTraversal(root,other.root);
    }
    /**
     * This function traverses two Tries and sees if they are exactly equal
     * @Param myNode The "comparer" object
     * @Param ONode The "comparee" object
     * @return if they are exactly the same
     * */
    boolean equalTraversal(INode myNode,INode ONode){
        for(int i = 0; i < myNode.alphabet.length; i++){
            if((myNode.alphabet[i] == null && ONode.alphabet[i] == null)){
                equalTraversal(myNode.alphabet[i],ONode.alphabet[i]);
                return true;
            }
            else if((myNode.alphabet[i] != null && ONode.alphabet[i] != null)){
                if((myNode.alphabet[i].getValue() == ONode.alphabet[i].getValue())){
                    equalTraversal(myNode.alphabet[i],ONode.alphabet[i]);
                    return true;
                }
            }
            else{
                return false;
            }
        }
        return false;
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
