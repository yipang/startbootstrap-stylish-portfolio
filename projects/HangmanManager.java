// Yi Pang
// 2015/04/29
// Section AF
// TA: Linden Jeffrey
// 
// this class manages a game of Hangman, delays picking a word until it
// is forced to always considering a set of words that could be the answer.
// In order to fool the user into thinking it is playing fairly, 
// only consider words with same pattern
import java.util.*;

public class HangmanManager {
   private int guessLeft;
   private Set<String> possibleWords;
   private String pattern;
   private Set<Character> charGuessed;
   
   /** passed a dictionary of words, an int lenght, 
       an int max all in parameter initialize the state of game
     * pre: throw IllegalArgumentException if length < 1 or max < 0 */
   public HangmanManager(List<String> dictionary, int length, int max) {
      if (length < 1 || max < 0) {
         throw new IllegalArgumentException();
      }   
      this.guessLeft = max;
      this.possibleWords = new TreeSet<String>();
      for (int i = 0; i < dictionary.size(); i++) {
         if (dictionary.get(i).length() == length) {
            possibleWords.add(dictionary.get(i));
         }
      }
      this.pattern = "";
      for (int i = 0; i < length; i++) {
         this.pattern += "-";
      }
      this.charGuessed = new TreeSet<Character>();           
   }
   
   // return the current Set of words being considered 
   public Set<String> words() {
      return this.possibleWords;
   }
   
   // return int of how may guesses has left
   public int guessesLeft() {
      return this.guessLeft;
   }
   
   // return current set of leters that have been guessed
   public Set<Character> guesses() {
      return this.charGuessed;
   }
   
   /** return current pattern to be displayed
     * pre: throw IllegalStateException if set of words is empty */
   public String pattern() {
      if (this.possibleWords.isEmpty()) {
         throw new IllegalStateException();
      }
      return this.pattern;
   }
   
   /** record that the player made a guess in parameter, return the number 
       of occurences of given character, put this in to charGuessed,
       change the pattern, possibleWords, decrease guessLeft if it's wrong
     * pre: throw IllegalStateException if guesLeft less than 1 
            or possible answer set is empty
            throw IllegalArgumentException if guess is already guessed */
   public int record(char guess) {
      int result = 0;
      if (this.guessLeft < 1 || this.possibleWords.isEmpty()) {
         throw new IllegalStateException();
      }
      if (this.charGuessed.contains(guess)) {
         throw new IllegalArgumentException();
      }
      Map<String, Set<String>> patternAndWords = patternToWords(guess);
      String thePattern = findOutPattern(patternAndWords);
      this.pattern = thePattern;
      this.possibleWords = patternAndWords.get(thePattern);
      this.charGuessed.add(guess);
      for (int i = 0; i < this.pattern.length(); i++) {
         if (this.pattern.charAt(i) == guess) {
            result++;
         }
      }
      if (result == 0) {
         this.guessLeft--;
      }   
      return result;      
   }
   
   // return the Map of pattern and corrsponding set of words 
   // of given char guess in parameter
   private Map<String, Set<String>> patternToWords(char guess) {
      Map<String, Set<String>> result = new TreeMap<String, Set<String>>();
      for (String word : this.possibleWords) {
         String newPattern = this.pattern;
         for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == guess) {
               newPattern = newPattern.substring(0, i) 
               + guess + newPattern.substring(i + 1);
            }
         }
         if (!result.containsKey(newPattern)) {
            Set<String> currWords = new TreeSet<String>();
            result.put(newPattern, currWords);
         }
         result.get(newPattern).add(word);      
      }
      return result;            
   }
   
   // return the pattern with most corresponding set of possible words 
   // a map of string and set of string is passed in as parameter
   private String findOutPattern(Map<String, Set<String>> possible) {
      int max = 0;
      String result = this.pattern;
      for (String curPattern : possible.keySet()) {
         if (max < possible.get(curPattern).size()) {
            // find set with larger size
            result = curPattern;
            max = possible.get(curPattern).size();
         }
      }    
      return result;      
   }                       
}