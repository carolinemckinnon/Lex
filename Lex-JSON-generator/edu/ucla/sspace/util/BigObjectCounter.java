package edu.ucla.sspace.util;

import gnu.trove.TDecorators;
import gnu.trove.iterator.TObjectLongIterator;
import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.hash.TObjectLongHashMap;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;











































public class BigObjectCounter<T>
  implements BigCounter<T>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final TObjectLongMap<T> counts;
  private long sum;
  
  public BigObjectCounter()
  {
    counts = new TObjectLongHashMap();
    sum = 0L;
  }
  



  public BigObjectCounter(Collection<? extends T> items)
  {
    this();
    for (T item : items) {
      count(item);
    }
  }
  


  public void add(Counter<? extends T> counter)
  {
    for (Map.Entry<? extends T, Integer> e : counter) {
      T t = e.getKey();
      Long cur = Long.valueOf(counts.get(t));
      counts.put(t, cur == null ? ((Integer)e.getValue()).intValue() : cur.longValue() + ((Integer)e.getValue()).intValue());
    }
  }
  



  public void add(BigCounter<? extends T> counter)
  {
    for (Map.Entry<? extends T, Long> e : counter) {
      T t = e.getKey();
      Long cur = Long.valueOf(counts.get(t));
      counts.put(t, cur == null ? ((Long)e.getValue()).longValue() : cur.longValue() + ((Long)e.getValue()).longValue());
    }
  }
  


  public long count(T obj)
  {
    long count = counts.get(obj);
    count += 1L;
    counts.put(obj, count);
    sum += 1L;
    return count;
  }
  






  public long count(T obj, long count)
  {
    if (count < 1L)
      throw new IllegalArgumentException("Count must be positive: " + count);
    long oldCount = counts.get(obj);
    long newCount = count + oldCount;
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
      BigCounter<?> c = (BigCounter)o;
      if ((counts.size() != c.size()) || (sum != c.sum()))
        return false;
      for (Map.Entry<?, Long> e : c) {
        long i = counts.get(e.getKey());
        if (i != ((Long)e.getValue()).longValue())
          return false;
      }
      return true;
    }
    return false;
  }
  



  public long getCount(T obj)
  {
    return counts.get(obj);
  }
  




  public double getFrequency(T obj)
  {
    double count = getCount(obj);
    return sum == 0L ? 0.0D : count / sum;
  }
  
  public int hashCode() {
    return counts.hashCode();
  }
  




  public Set<T> items()
  {
    return Collections.unmodifiableSet(counts.keySet());
  }
  



  public Iterator<Map.Entry<T, Long>> iterator()
  {
    return 
      Collections.unmodifiableSet(TDecorators.wrap(counts).entrySet()).iterator();
  }
  




  public T max()
  {
    TObjectLongIterator<T> iter = counts.iterator();
    long maxCount = -1L;
    T max = null;
    while (iter.hasNext()) {
      iter.advance();
      long count = iter.value();
      if (count > maxCount) {
        max = iter.key();
        maxCount = count;
      }
    }
    return max;
  }
  




  public T min()
  {
    TObjectLongIterator<T> iter = counts.iterator();
    long minCount = Long.MAX_VALUE;
    T min = null;
    while (iter.hasNext()) {
      iter.advance();
      long count = iter.value();
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
    sum = 0L;
  }
  


  public int size()
  {
    return counts.size();
  }
  


  public long sum()
  {
    return sum;
  }
  
  public String toString() {
    return counts.toString();
  }
}
