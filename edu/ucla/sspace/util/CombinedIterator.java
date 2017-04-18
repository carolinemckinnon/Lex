package edu.ucla.sspace.util;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;












































public class CombinedIterator<T>
  implements Iterator<T>
{
  private final Queue<Iterator<T>> iters;
  private Iterator<T> current;
  private Iterator<T> toRemoveFrom;
  
  public CombinedIterator(Iterator<T>... iterators)
  {
    this(Arrays.asList(iterators));
  }
  



  public CombinedIterator(Collection<Iterator<T>> iterators)
  {
    this(new ArrayDeque(iterators));
  }
  



  private CombinedIterator(Queue<Iterator<T>> iterators)
  {
    iters = iterators;
    advance();
  }
  



  public static <T> Iterator<T> join(Collection<Iterable<T>> iterables)
  {
    Queue<Iterator<T>> iters = 
      new ArrayDeque(iterables.size());
    for (Iterable<T> i : iterables)
      iters.add(i.iterator());
    return new CombinedIterator(iters);
  }
  



  private void advance()
  {
    if ((current == null) || (!current.hasNext())) {
      do {
        current = ((Iterator)iters.poll());
      } while ((current != null) && (!current.hasNext()));
    }
  }
  



  public synchronized boolean hasNext()
  {
    return (current != null) && (current.hasNext());
  }
  


  public synchronized T next()
  {
    if (current == null) {
      throw new NoSuchElementException();
    }
    T t = current.next();
    

    if (toRemoveFrom != current)
      toRemoveFrom = current;
    advance();
    return t;
  }
  



  public synchronized void remove()
  {
    if (toRemoveFrom == null) {
      throw new IllegalStateException();
    }
    toRemoveFrom.remove();
  }
}
