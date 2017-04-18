package edu.ucla.sspace.wordsi;

import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.vector.SparseDoubleVector;
import java.io.BufferedReader;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;























































public class GeneralContextExtractor
  implements ContextExtractor
{
  private final ContextGenerator generator;
  private final int windowSize;
  private final boolean readHeader;
  
  public GeneralContextExtractor(ContextGenerator generator, int windowSize, boolean readHeader)
  {
    this.generator = generator;
    this.windowSize = windowSize;
    this.readHeader = readHeader;
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
    

    String header = null;
    if (readHeader) {
      header = (String)it.next();
    }
    

    for (int i = 0; (i < windowSize) && (it.hasNext()); i++) {
      nextWords.offer((String)it.next());
    }
    

    String focus = null;
    while (!nextWords.isEmpty()) {
      focus = (String)nextWords.remove();
      String secondaryKey = header == null ? focus : header;
      

      if (it.hasNext()) {
        nextWords.offer((String)it.next());
      }
      
      if (wordsi.acceptWord(focus)) {
        SparseDoubleVector contextVector = generator.generateContext(
          prevWords, nextWords);
        wordsi.handleContextVector(focus, secondaryKey, contextVector);
      }
      

      prevWords.offer(focus);
      if (prevWords.size() > windowSize) {
        prevWords.remove();
      }
    }
  }
}
