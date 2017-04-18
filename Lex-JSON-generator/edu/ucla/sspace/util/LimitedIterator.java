package edu.ucla.sspace.util;

import java.util.Iterator;











































public class LimitedIterator<T>
  implements Iterator<T>
{
  private final Iterator<T> iter;
  private int itemCount;
  private final int maxItems;
  
  public LimitedIterator(Iterator<T> iter, int maxItems)
  {
    this.iter = iter;
    this.maxItems = maxItems;
    itemCount = 0;
  }
  


  public synchronized boolean hasNext()
  {
    return (itemCount < maxItems) && (iter.hasNext());
  }
  


  public synchronized T next()
  {
    itemCount += 1;
    return iter.next();
  }
  


  public void remove()
  {
    throw new UnsupportedOperationException("remove is not supported");
  }
}
