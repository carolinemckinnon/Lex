package edu.ucla.sspace.text;

import java.io.BufferedReader;
import java.util.Iterator;
import java.util.NoSuchElementException;










































public class FilteredIterator
  implements Iterator<String>
{
  private final Iterator<String> tokenizer;
  private final TokenFilter filter;
  private String next;
  
  public FilteredIterator(String str, TokenFilter filter)
  {
    this(new WordIterator(str), filter);
  }
  


  public FilteredIterator(BufferedReader reader, TokenFilter filter)
  {
    this(new WordIterator(reader), filter);
  }
  



  public FilteredIterator(Iterator<String> tokens, TokenFilter filter)
  {
    tokenizer = tokens;
    this.filter = filter;
    next = null;
    advance();
  }
  


  private void advance()
  {
    String s = null;
    while (tokenizer.hasNext()) {
      String nextToken = (String)tokenizer.next();
      
      if (filter.accept(nextToken)) {
        s = nextToken;
        break;
      }
    }
    next = s;
  }
  



  public boolean hasNext()
  {
    return next != null;
  }
  


  public String next()
  {
    if (next == null) {
      throw new NoSuchElementException();
    }
    String s = next;
    advance();
    return s;
  }
  


  public void remove()
  {
    throw new UnsupportedOperationException("remove is not supported");
  }
}
