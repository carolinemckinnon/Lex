package edu.ucla.sspace.util.primitive;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import java.io.Serializable;
import java.util.Collection;
import java.util.NoSuchElementException;































public class TroveIntSet
  extends AbstractIntSet
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private final TIntSet set;
  
  public TroveIntSet()
  {
    this(4);
  }
  


  public TroveIntSet(int capacity)
  {
    set = new TIntHashSet(capacity);
  }
  



  public TroveIntSet(Collection<? extends Integer> c)
  {
    this(c.size());
    addAll(c);
  }
  



  public TroveIntSet(IntSet c)
  {
    this(c.size());
    addAll(c);
  }
  



  private TroveIntSet(TIntSet set)
  {
    this.set = set;
  }
  
  public boolean add(Integer i) {
    return add(i.intValue());
  }
  
  public boolean add(int i) {
    return set.add(i);
  }
  
  public boolean contains(int i) {
    return set.contains(i);
  }
  
  public boolean isEmpty() {
    return set.isEmpty();
  }
  
  public IntIterator iterator() {
    return new TroveIterator();
  }
  
  public boolean remove(int i) {
    return set.remove(i);
  }
  
  public int size() {
    return set.size();
  }
  
  public static IntSet wrap(TIntSet set) {
    return new TroveIntSet(set);
  }
  
  private class TroveIterator implements IntIterator
  {
    private final TIntIterator iter;
    private boolean removed;
    
    public TroveIterator()
    {
      iter = set.iterator();
      removed = true;
    }
    
    public boolean hasNext() {
      return iter.hasNext();
    }
    
    public Integer next() {
      if (!iter.hasNext())
        throw new NoSuchElementException();
      removed = false;
      return Integer.valueOf(iter.next());
    }
    
    public int nextInt() {
      if (!iter.hasNext())
        throw new NoSuchElementException();
      removed = false;
      return iter.next();
    }
    
    public void remove() {
      if (removed)
        throw new IllegalStateException();
      removed = true;
      iter.remove();
    }
  }
}
