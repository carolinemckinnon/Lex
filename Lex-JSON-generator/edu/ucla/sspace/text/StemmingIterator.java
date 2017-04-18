package edu.ucla.sspace.text;

import java.io.BufferedReader;
import java.util.Iterator;














































public class StemmingIterator
  implements Iterator<String>
{
  private final Iterator<String> tokenizer;
  private final Stemmer stemmer;
  
  public StemmingIterator(String str, Stemmer stemmer)
  {
    this(new WordIterator(str), stemmer);
  }
  


  public StemmingIterator(BufferedReader br, Stemmer stemmer)
  {
    this(new WordIterator(br), stemmer);
  }
  


  public StemmingIterator(Iterator<String> tokens, Stemmer stemmer)
  {
    tokenizer = tokens;
    this.stemmer = stemmer;
  }
  


  public boolean hasNext()
  {
    return tokenizer.hasNext();
  }
  


  public String next()
  {
    return stemmer.stem((String)tokenizer.next());
  }
  


  public void remove()
  {
    throw new UnsupportedOperationException("remove is not supported");
  }
}
