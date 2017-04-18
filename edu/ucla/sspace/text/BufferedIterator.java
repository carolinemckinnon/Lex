package edu.ucla.sspace.text;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;











































public class BufferedIterator
  implements Iterator<String>
{
  private final Iterator<String> tokenizer;
  private final List<String> buffer;
  
  public BufferedIterator(String str)
  {
    this(new BufferedReader(new StringReader(str)));
  }
  



  public BufferedIterator(BufferedReader br)
  {
    this(new WordIterator(br));
  }
  




  public BufferedIterator(Iterator<String> tokens)
  {
    tokenizer = tokens;
    buffer = new LinkedList();
  }
  








  private boolean advance(int tokens)
  {
    while ((buffer.size() < tokens) && (tokenizer.hasNext()))
      buffer.add((String)tokenizer.next());
    return buffer.size() >= tokens;
  }
  


  public boolean hasNext()
  {
    return (buffer.size() > 0) || (advance(1));
  }
  


  public String next()
  {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    return (String)buffer.remove(0);
  }
  










  public List<String> peek(int tokens)
  {
    advance(tokens);
    return new ArrayList(
      buffer.subList(0, Math.min(tokens, buffer.size())));
  }
  


  public void remove()
  {
    throw new UnsupportedOperationException("remove is not supported");
  }
}
