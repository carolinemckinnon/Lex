package edu.ucla.sspace.wordsi.semeval;

import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.wordsi.ContextExtractor;
import edu.ucla.sspace.wordsi.ContextGenerator;
import edu.ucla.sspace.wordsi.Wordsi;
import java.io.BufferedReader;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;






























































public class SemEvalContextExtractor
  implements ContextExtractor
{
  private static final String DEFAULT_SEPARATOR = "||||";
  private final ContextGenerator generator;
  private final int windowSize;
  private final String separator;
  
  public SemEvalContextExtractor(ContextGenerator generator, int windowSize)
  {
    this(generator, windowSize, "||||");
  }
  









  public SemEvalContextExtractor(ContextGenerator generator, int windowSize, String separator)
  {
    this.generator = generator;
    this.windowSize = windowSize;
    this.separator = separator;
  }
  


  public int getVectorLength()
  {
    return generator.getVectorLength();
  }
  


  public void processDocument(BufferedReader document, Wordsi wordsi)
  {
    Queue<String> prevWords = new ArrayDeque();
    Queue<String> nextWords = new ArrayDeque();
    
    Iterator<String> it = IteratorFactory.tokenizeOrdered(document);
    

    if (!it.hasNext()) {
      return;
    }
    String instanceId = (String)it.next();
    


    for (int i = 0; it.hasNext(); i++) {
      String term = (String)it.next();
      if (term.equals(separator))
        break;
      prevWords.offer(term.intern());
    }
    

    while (prevWords.size() > windowSize) {
      prevWords.remove();
    }
    

    if (!it.hasNext()) {
      return;
    }
    String focusWord = ((String)it.next()).intern();
    

    while ((it.hasNext()) && (nextWords.size() < windowSize)) {
      nextWords.offer(((String)it.next()).intern());
    }
    
    SparseDoubleVector contextVector = generator.generateContext(
      prevWords, nextWords);
    wordsi.handleContextVector(focusWord, instanceId, contextVector);
  }
}
