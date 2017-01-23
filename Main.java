package spell;

import java.io.IOException;

import spell.ISpellCorrector.NoSimilarWordFoundException;





/**
 * A simple main class for running the spelling corrector. This class is not
 * used by the passoff program.
 */
public class Main {
	
	/**
	 * Give the dictionary file name as the first argument and the word to correct
	 * as the second argument.
	 */
	public static void main(String[] args){
		
		String dictionaryFileName = args[0];
		String inputWord = args[1];
		inputWord = inputWord.toLowerCase();
		
		/**
		 * Create an instance of your corrector here
		 */
		ISpellCorrector corrector = new SpellCorrector();
		try {
			corrector.useDictionary(dictionaryFileName);
		}catch(IOException e){};
		String suggestion = "";
		try{
			suggestion = corrector.suggestSimilarWord(inputWord);
		}catch(ISpellCorrector.NoSimilarWordFoundException e){}
		
		System.out.println("Suggestion is: " + suggestion);
	}

}
