package edu.ucla.sspace.util;

import java.util.Iterator;

































public class SynchronizedIterator<T>
  implements Iterator<T>
{
  private final Iterator<T> iter;
  
  public SynchronizedIterator(Iterator<T> iterator)
  {
    iter = iterator;
  }
  


  public synchronized boolean hasNext()
  {
    return iter.hasNext();
  }
  


  public T next()
  {
    return iter.next();
  }
  


  public void remove()
  {
    iter.remove();
  }
}
