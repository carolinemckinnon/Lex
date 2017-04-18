package edu.ucla.sspace.text;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;






































public class WordReplacementIterator
  implements Iterator<String>
{
  private final Iterator<String> baseIterator;
  private final Map<String, String> replacementMap;
  private String next;
  
  public WordReplacementIterator(Iterator<String> base, Map<String, String> map)
  {
    baseIterator = base;
    replacementMap = map;
    next = null;
    advance();
  }
  


  public void advance()
  {
    String s = null;
    if (baseIterator.hasNext()) {
      next = ((String)baseIterator.next());
    } else {
      next = null;
    }
  }
  

  public boolean hasNext()
  {
    return next != null;
  }
  






  public String next()
  {
    if (next == null)
      throw new NoSuchElementException();
    String replacement = (String)replacementMap.get(next);
    replacement = replacement == null ? next : replacement;
    advance();
    return replacement;
  }
  


  public void remove()
  {
    throw new UnsupportedOperationException("remove is not supported");
  }
}
