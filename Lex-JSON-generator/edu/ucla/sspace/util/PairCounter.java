package edu.ucla.sspace.util;

import gnu.trove.iterator.TLongIntIterator;
import gnu.trove.map.TLongIntMap;
import gnu.trove.map.hash.TLongIntHashMap;
import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

























































public class PairCounter<T>
  implements Counter<Pair<T>>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final TLongIntMap counts;
  private final Indexer<T> elementIndices;
  private int sum;
  
  public PairCounter()
  {
    counts = new TLongIntHashMap();
    elementIndices = new HashIndexer();
    sum = 0;
  }
  



  public PairCounter(Collection<? extends Pair<T>> items)
  {
    this();
    for (Pair<T> item : items) {
      count(item);
    }
  }
  


  public void add(Counter<? extends Pair<T>> counter)
  {
    for (Map.Entry<? extends Pair<T>, Integer> e : counter) {
      count((Pair)e.getKey(), ((Integer)e.getValue()).intValue());
    }
  }
  


  private long getIndex(Pair<T> p)
  {
    return getIndex(x, y);
  }
  


  private long getIndex(T x, T y)
  {
    int i = elementIndices.index(x);
    int j = elementIndices.index(y);
    long index = i << 32 | j;
    return index;
  }
  


  public int count(Pair<T> obj)
  {
    long index = getIndex(obj);
    int count = counts.get(index);
    count++;
    counts.put(index, count);
    sum += 1;
    return count;
  }
  


  public int count(T x, T y)
  {
    long index = getIndex(x, y);
    int count = counts.get(index);
    count++;
    counts.put(index, count);
    sum += 1;
    return count;
  }
  






  public int count(Pair<T> obj, int count)
  {
    if (count < 1)
      throw new IllegalArgumentException("Count must be positive: " + count);
    long index = getIndex(obj);
    int oldCount = counts.get(index);
    int newCount = count + oldCount;
    counts.put(index, newCount);
    sum += count;
    return newCount;
  }
  






  public int count(T x, T y, int count)
  {
    if (count < 1)
      throw new IllegalArgumentException("Count must be positive: " + count);
    long index = getIndex(x, y);
    int oldCount = counts.get(index);
    int newCount = count + oldCount;
    counts.put(index, newCount);
    sum += count;
    return newCount;
  }
  


  public void countAll(Collection<? extends Pair<T>> c)
  {
    for (Pair<T> t : c) {
      count(t);
    }
  }
  

  public boolean equals(Object o)
  {
    if ((o instanceof Counter)) {
      Counter<?> c = (Counter)o;
      if ((counts.size() != c.size()) || (sum != c.sum()))
        return false;
      for (Map.Entry<?, Integer> e : c) {
        Object k = e.getKey();
        if (!(k instanceof Pair)) {
          return false;
        }
        Pair<T> p = (Pair)o;
        int i = counts.get(getIndex(p));
        if (i != ((Integer)e.getValue()).intValue())
          return false;
      }
      return true;
    }
    return false;
  }
  




  public int getCount(Pair<T> obj)
  {
    return counts.get(getIndex(obj));
  }
  




  public int getCount(T x, T y)
  {
    return counts.get(getIndex(x, y));
  }
  




  public double getFrequency(Pair<T> p)
  {
    double count = getCount(p);
    return sum == 0 ? 0.0D : count / sum;
  }
  
  public int hashCode() {
    return counts.hashCode();
  }
  




  public Set<Pair<T>> items()
  {
    return new PairSet(null);
  }
  



  public Iterator<Map.Entry<Pair<T>, Integer>> iterator()
  {
    return new PairCountIterator();
  }
  




  public Pair<T> max()
  {
    int maxCount = 0;
    TLongIntIterator iter = counts.iterator();
    long maxIndex = 0L;
    
    while (!iter.hasNext()) {
      iter.advance();
      
      int count = iter.value();
      if (count > maxCount) {
        maxIndex = iter.key();
        maxCount = count;
      }
    }
    if (maxCount == 0) {
      return null;
    }
    int i = (int)(maxIndex >>> 32);
    int j = (int)(maxIndex & 0xFFFFFFF);
    return new Pair(elementIndices.lookup(i), 
      elementIndices.lookup(j));
  }
  




  public Pair<T> min()
  {
    int minCount = Integer.MAX_VALUE;
    TLongIntIterator iter = counts.iterator();
    long minIndex = 0L;
    
    while (!iter.hasNext()) {
      iter.advance();
      
      int count = iter.value();
      if (count < minCount) {
        minIndex = iter.key();
        minCount = count;
      }
    }
    if (minCount == Integer.MAX_VALUE) {
      return null;
    }
    int i = (int)(minIndex >>> 32);
    int j = (int)(minIndex & 0xFFFFFFF);
    return new Pair(elementIndices.lookup(i), 
      elementIndices.lookup(j));
  }
  



  public void reset()
  {
    counts.clear();
    sum = 0;
  }
  


  public int size()
  {
    return counts.size();
  }
  


  public int sum()
  {
    return sum;
  }
  
  public String toString()
  {
    return counts.toString();
  }
  
  private class PairSet extends AbstractSet<Pair<T>>
  {
    private PairSet() {}
    
    public boolean contains(Object o)
    {
      if ((o instanceof Pair))
      {
        Pair<T> p = (Pair)o;
        return getCount(p) > 0;
      }
      return false;
    }
    
    public Iterator<Pair<T>> iterator() {
      return new PairCounter.PairIterator(PairCounter.this);
    }
    
    public boolean isEmpty() {
      return counts.isEmpty();
    }
    
    public int size() {
      return counts.size();
    }
  }
  

  private class PairIterator
    implements Iterator<Pair<T>>
  {
    private final TLongIntIterator iter;
    

    public PairIterator()
    {
      iter = counts.iterator();
    }
    
    public boolean hasNext() {
      return iter.hasNext();
    }
    
    public Pair<T> next() {
      if (!iter.hasNext())
        throw new NoSuchElementException();
      iter.advance();
      long key = iter.key();
      int i = (int)(key >>> 32);
      int j = (int)(key & 0xFFFFFFF);
      return new Pair(elementIndices.lookup(i), 
        elementIndices.lookup(j));
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
  


  private class PairCountIterator
    implements Iterator<Map.Entry<Pair<T>, Integer>>
  {
    private final TLongIntIterator iter;
    

    public PairCountIterator()
    {
      iter = counts.iterator();
    }
    
    public boolean hasNext() {
      return iter.hasNext();
    }
    
    public Map.Entry<Pair<T>, Integer> next() {
      if (!iter.hasNext())
        throw new NoSuchElementException();
      iter.advance();
      long key = iter.key();
      int count = iter.value();
      int i = (int)(key >>> 32);
      int j = (int)(key & 0xFFFFFFF);
      Pair<T> p = new Pair(elementIndices.lookup(i), 
        elementIndices.lookup(j));
      return new AbstractMap.SimpleEntry(p, Integer.valueOf(count));
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
}
