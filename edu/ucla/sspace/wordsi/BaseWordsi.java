package edu.ucla.sspace.wordsi;

import edu.ucla.sspace.common.SemanticSpace;
import java.io.BufferedReader;
import java.util.Set;


















































public abstract class BaseWordsi
  implements Wordsi, SemanticSpace
{
  private final Set<String> acceptedWords;
  private ContextExtractor extractor;
  
  public BaseWordsi(Set<String> acceptedWords, ContextExtractor extractor)
  {
    this.acceptedWords = acceptedWords;
    this.extractor = extractor;
  }
  


  public boolean acceptWord(String word)
  {
    return (acceptedWords == null) || 
      (acceptedWords.isEmpty()) || 
      (acceptedWords.contains(word));
  }
  


  public String getSpaceName()
  {
    return "Wordsi";
  }
  


  public int getVectorLength()
  {
    return extractor.getVectorLength();
  }
  


  public void processDocument(BufferedReader document)
  {
    extractor.processDocument(document, this);
  }
}
