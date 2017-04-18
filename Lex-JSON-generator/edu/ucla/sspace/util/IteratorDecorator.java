package edu.ucla.sspace.util;

import java.io.Serializable;
import java.util.Iterator;














































public class IteratorDecorator<T>
  implements Iterator<T>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final Iterator<T> iter;
  protected T cur;
  
  public IteratorDecorator(Iterator<T> iter)
  {
    if (iter == null)
      throw new NullPointerException();
    this.iter = iter;
  }
  


  public boolean hasNext()
  {
    return iter.hasNext();
  }
  


  public T next()
  {
    cur = iter.next();
    return cur;
  }
  


  public void remove()
  {
    cur = null;
    iter.remove();
  }
}
