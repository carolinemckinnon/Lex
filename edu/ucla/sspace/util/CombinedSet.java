package edu.ucla.sspace.util;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;




























public class CombinedSet<T>
  extends AbstractSet<T>
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private final List<Set<T>> sets;
  
  public CombinedSet(Set<T>... sets)
  {
    if (sets == null)
      throw new NullPointerException("Sets cannot be null");
    this.sets = new ArrayList();
    for (Set<T> s : sets) {
      if (s == null)
        throw new NullPointerException("Cannot wrap null set");
      this.sets.add(s);
    }
  }
  
  public CombinedSet(Collection<? extends Set<T>> sets) {
    if (sets == null)
      throw new NullPointerException("Sets cannot be null");
    this.sets = new ArrayList();
    for (Set<T> s : sets) {
      if (s == null)
        throw new NullPointerException("Cannot wrap null set");
      this.sets.add(s);
    }
  }
  





  public void append(Set<T> set)
  {
    if (set == null)
      throw new NullPointerException("set cannot be null");
    sets.add(set);
  }
  



  public boolean contains(Object o)
  {
    for (Set<T> s : sets)
      if (s.contains(o))
        return true;
    return false;
  }
  


  public void clear()
  {
    for (Set<T> s : sets) {
      s.clear();
    }
  }
  

  public Iterator<T> iterator()
  {
    return new SetIterator();
  }
  


  public boolean isEmpty()
  {
    for (Set<T> s : sets)
      if (!s.isEmpty())
        return false;
    return true;
  }
  



  public boolean remove(Object o)
  {
    boolean removed = false;
    for (Set<T> s : sets)
      if (s.remove(o))
        removed = true;
    return removed;
  }
  





  public int size()
  {
    int size = 0;
    for (T t : this)
      size++;
    return size;
  }
  







  private class SetIterator
    implements Iterator<T>
  {
    int curSet = -1;
    



    Iterator<T> curIterator;
    



    private T cur;
    



    private T next;
    



    private Iterator<Set<T>> setsToProcess;
    



    public SetIterator()
    {
      setsToProcess = sets.iterator();
      advance();
    }
    



    private void advance()
    {
      next = null;
      do {
        while (((curIterator == null) || (!curIterator.hasNext())) && 
          (setsToProcess.hasNext())) {
          curIterator = ((Set)setsToProcess.next()).iterator();
          curSet += 1;
        }
        
        if ((curIterator == null) || (
          (!curIterator.hasNext()) && (!setsToProcess.hasNext()))) {
          return;
        }
        T t = curIterator.next();
        

        boolean wasReturned = false;
        for (int i = 0; i < curSet; i++) {
          if (((Set)sets.get(i)).contains(t)) {
            wasReturned = true;
            break;
          }
        }
        if (!wasReturned)
          next = t;
      } while (next == null);
    }
    


    public boolean hasNext()
    {
      return next != null;
    }
    


    public T next()
    {
      if (!hasNext())
        throw new NoSuchElementException();
      cur = next;
      advance();
      return cur;
    }
    


    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
}
