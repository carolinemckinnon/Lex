package edu.ucla.sspace.wordsi.psd;

import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.wordsi.ContextExtractor;
import edu.ucla.sspace.wordsi.ContextGenerator;
import edu.ucla.sspace.wordsi.Wordsi;
import java.io.BufferedReader;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;



































































public class PseudoWordContextExtractor
  implements ContextExtractor
{
  private static final String EMPTY = "";
  private final Map<String, String> pseudoWordMap;
  private final ContextGenerator generator;
  private final int windowSize;
  
  public PseudoWordContextExtractor(ContextGenerator generator, int windowSize, Map<String, String> pseudoWordMap)
  {
    this.pseudoWordMap = pseudoWordMap;
    this.generator = generator;
    this.windowSize = windowSize;
  }
  


  public int getVectorLength()
  {
    return generator.getVectorLength();
  }
  


  public void processDocument(BufferedReader document, Wordsi wordsi)
  {
    Queue<String> prevWords = new ArrayDeque();
    Queue<String> nextWords = new ArrayDeque();
    Queue<String> nextRealWord = new ArrayDeque();
    
    Iterator<String> it = IteratorFactory.tokenizeOrdered(document);
    


    for (int i = 0; (i < windowSize) && (it.hasNext()); i++) {
      addNextToken((String)it.next(), nextWords, nextRealWord);
    }
    

    String focusWord = null;
    String replacementWord = null;
    while (!nextWords.isEmpty()) {
      focusWord = (String)nextWords.remove();
      replacementWord = (String)nextRealWord.remove();
      

      if (it.hasNext()) {
        addNextToken((String)it.next(), nextWords, nextRealWord);
      }
      
      if (!replacementWord.equals("")) {
        SparseDoubleVector contextVector = generator.generateContext(
          prevWords, nextWords);
        wordsi.handleContextVector(
          focusWord, replacementWord, contextVector);
      }
      

      prevWords.offer(focusWord);
      if (prevWords.size() > windowSize) {
        prevWords.remove();
      }
    }
  }
  






  private void addNextToken(String token, Queue<String> nextWords, Queue<String> nextRealWords)
  {
    String replacement = (String)pseudoWordMap.get(token);
    if (replacement == null) {
      nextWords.offer(token);
      nextRealWords.offer("");
    } else {
      nextWords.offer(replacement);
      nextRealWords.offer(token);
    }
  }
}
