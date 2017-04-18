package edu.ucla.sspace.util;

import gnu.trove.TDecorators;
import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;











































public class ObjectCounter<T>
  implements Counter<T>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final TObjectIntMap<T> counts;
  private int sum;
  
  public ObjectCounter()
  {
    counts = new TObjectIntHashMap();
    sum = 0;
  }
  


  public ObjectCounter(int capacity)
  {
    counts = new TObjectIntHashMap(capacity);
    sum = 0;
  }
  



  public ObjectCounter(Collection<? extends T> items)
  {
    this(items.size());
    for (T item : items) {
      count(item);
    }
  }
  


  public void add(Counter<? extends T> counter)
  {
    for (Map.Entry<? extends T, Integer> e : counter) {
      T t = e.getKey();
      Integer cur = Integer.valueOf(counts.get(t));
      counts.put(t, cur == null ? ((Integer)e.getValue()).intValue() : cur.intValue() + ((Integer)e.getValue()).intValue());
    }
  }
  


  public int count(T obj)
  {
    int count = counts.get(obj);
    count++;
    counts.put(obj, count);
    sum += 1;
    return count;
  }
  






  public int count(T obj, int count)
  {
    if (count < 1)
      throw new IllegalArgumentException("Count must be positive: " + count);
    int oldCount = counts.get(obj);
    int newCount = count + oldCount;
    counts.put(obj, newCount);
    sum += count;
    return newCount;
  }
  


  public void countAll(Collection<? extends T> c)
  {
    for (T t : c)
      count(t);
  }
  
  public boolean equals(Object o) {
    if ((o instanceof Counter)) {
      Counter<?> c = (Counter)o;
      if ((counts.size() != c.size()) || (sum != c.sum()))
        return false;
      for (Map.Entry<?, Integer> e : c) {
        int i = counts.get(e.getKey());
        if (i != ((Integer)e.getValue()).intValue())
          return false;
      }
      return true;
    }
    return false;
  }
  



  public int getCount(T obj)
  {
    return counts.get(obj);
  }
  




  public double getFrequency(T obj)
  {
    double count = getCount(obj);
    return sum == 0 ? 0.0D : count / sum;
  }
  
  public int hashCode() {
    return counts.hashCode();
  }
  




  public Set<T> items()
  {
    return Collections.unmodifiableSet(counts.keySet());
  }
  



  public Iterator<Map.Entry<T, Integer>> iterator()
  {
    return 
      Collections.unmodifiableSet(TDecorators.wrap(counts).entrySet()).iterator();
  }
  




  public T max()
  {
    TObjectIntIterator<T> iter = counts.iterator();
    int maxCount = -1;
    T max = null;
    while (iter.hasNext()) {
      iter.advance();
      int count = iter.value();
      if (count > maxCount) {
        max = iter.key();
        maxCount = count;
      }
    }
    return max;
  }
  




  public T min()
  {
    TObjectIntIterator<T> iter = counts.iterator();
    int minCount = Integer.MAX_VALUE;
    T min = null;
    while (iter.hasNext()) {
      iter.advance();
      int count = iter.value();
      if (count < minCount) {
        min = iter.key();
        minCount = count;
      }
    }
    return min;
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
  
  public String toString() {
    return counts.toString();
  }
}
