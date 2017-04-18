package edu.ucla.sspace.util;

import gnu.trove.iterator.TIntIntIterator;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import java.io.Serializable;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

































































public class IndexedCounter<T>
  implements Counter<T>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final Indexer<T> objectIndices;
  private final TIntIntMap indexToCount;
  private final boolean allowNewIndices;
  private int sum;
  
  public IndexedCounter(Indexer<T> objectIndices)
  {
    this(objectIndices, true);
  }
  








  public IndexedCounter(Indexer<T> objectIndices, boolean allowNewIndices)
  {
    this.objectIndices = objectIndices;
    this.allowNewIndices = allowNewIndices;
    indexToCount = new TIntIntHashMap();
    sum = 0;
  }
  



  public void add(Counter<? extends T> counter)
  {
    for (Map.Entry<? extends T, Integer> e : counter) {
      count(e.getKey(), ((Integer)e.getValue()).intValue());
    }
  }
  

  public int count(T obj)
  {
    int objIndex = allowNewIndices ? 
      objectIndices.index(obj) : 
      objectIndices.find(obj);
    if (objIndex < 0)
      return 0;
    int curCount = indexToCount.get(objIndex);
    indexToCount.put(objIndex, curCount + 1);
    sum += 1;
    return curCount + 1;
  }
  






  public int count(T obj, int count)
  {
    if (count < 1)
      throw new IllegalArgumentException("Count must be positive: " + count);
    int objIndex = allowNewIndices ? 
      objectIndices.index(obj) : 
      objectIndices.find(obj);
    if (objIndex < 0)
      return 0;
    int curCount = indexToCount.get(objIndex);
    indexToCount.put(objIndex, curCount + count);
    sum += count;
    return curCount + count;
  }
  


  public void countAll(Collection<? extends T> c)
  {
    for (T t : c) {
      count(t);
    }
  }
  
  public boolean equals(Object o) {
    throw new Error();
  }
  



  public int getCount(T obj)
  {
    int objIndex = allowNewIndices ? 
      objectIndices.index(obj) : 
      objectIndices.find(obj);
    return objIndex < 0 ? 0 : indexToCount.get(objIndex);
  }
  




  public double getFrequency(T obj)
  {
    double count = getCount(obj);
    return sum == 0 ? 0.0D : count / sum;
  }
  
  public int hashCode() {
    throw new Error();
  }
  




  public Set<T> items()
  {
    return new ItemSet(null);
  }
  



  public Iterator<Map.Entry<T, Integer>> iterator()
  {
    return new EntryIter();
  }
  




  public T max()
  {
    if (sum == 0)
      return null;
    int maxCount = -1;
    int maxIndex = -1;
    for (TIntIntIterator it = indexToCount.iterator(); it.hasNext();) {
      it.advance();
      if (it.value() > maxCount) {
        maxIndex = it.key();
        maxCount = it.value();
      }
    }
    return objectIndices.lookup(maxIndex);
  }
  




  public T min()
  {
    if (sum == 0)
      return null;
    int minCount = Integer.MAX_VALUE;
    int minIndex = -1;
    for (TIntIntIterator it = indexToCount.iterator(); it.hasNext();) {
      it.advance();
      if (it.value() < minCount) {
        minIndex = it.key();
        minCount = it.value();
      }
    }
    return objectIndices.lookup(minIndex);
  }
  


  public void reset()
  {
    indexToCount.clear();
    sum = 0;
  }
  


  public int size()
  {
    return indexToCount.size();
  }
  


  public int sum()
  {
    return sum;
  }
  
  public String toString() {
    return indexToCount.toString();
  }
  
  private class ItemSet extends AbstractSet<T> {
    private ItemSet() {}
    
    public boolean contains(Object o) {
      T t = o;
      int idx = objectIndices.find(t);
      return indexToCount.get(idx) > 0;
    }
    
    public boolean isEmpty() {
      return IndexedCounter.this.size() == 0;
    }
    
    public Iterator<T> iterator() {
      return new IndexedCounter.ItemIter(IndexedCounter.this);
    }
    
    public int size() {
      return IndexedCounter.this.size();
    }
  }
  
  private class ItemIter implements Iterator<T>
  {
    private final Iterator<Map.Entry<T, Integer>> itemToIndex;
    private T next;
    
    public ItemIter()
    {
      itemToIndex = objectIndices.iterator();
      next = null;
      advance();
    }
    
    private void advance() {
      while ((next == null) && (itemToIndex.hasNext())) {
        Map.Entry<T, Integer> e = (Map.Entry)itemToIndex.next();
        int count = indexToCount.get(((Integer)e.getValue()).intValue());
        if (count > 0)
          next = e.getKey();
      }
    }
    
    public boolean hasNext() {
      return next != null;
    }
    
    public T next() {
      if (next == null)
        throw new NoSuchElementException();
      T t = next;
      advance();
      return t;
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
  
  private class EntryIter implements Iterator<Map.Entry<T, Integer>>
  {
    private final Iterator<Map.Entry<T, Integer>> itemToIndex;
    private Map.Entry<T, Integer> next;
    
    public EntryIter() {
      itemToIndex = objectIndices.iterator();
      next = null;
      advance();
    }
    
    private void advance() {
      next = null;
      while ((next == null) && (itemToIndex.hasNext())) {
        Map.Entry<T, Integer> e = (Map.Entry)itemToIndex.next();
        int count = indexToCount.get(((Integer)e.getValue()).intValue());
        if (count > 0)
          next = new AbstractMap.SimpleImmutableEntry(
            e.getKey(), Integer.valueOf(count));
      }
    }
    
    public boolean hasNext() {
      return next != null;
    }
    
    public Map.Entry<T, Integer> next() {
      if (next == null)
        throw new NoSuchElementException();
      Map.Entry<T, Integer> e = next;
      advance();
      return e;
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
}
