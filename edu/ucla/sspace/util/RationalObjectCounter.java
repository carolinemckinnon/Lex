package edu.ucla.sspace.util;

import gnu.trove.TDecorators;
import gnu.trove.iterator.TObjectDoubleIterator;
import gnu.trove.map.TObjectDoubleMap;
import gnu.trove.map.hash.TObjectDoubleHashMap;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;












































public class RationalObjectCounter<T>
  implements RationalCounter<T>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final TObjectDoubleMap<T> counts;
  private double sum;
  
  public RationalObjectCounter()
  {
    counts = new TObjectDoubleHashMap();
    sum = 0.0D;
  }
  



  public RationalObjectCounter(Collection<? extends T> items)
  {
    this();
    for (T item : items) {
      count(item);
    }
  }
  


  public void add(RationalCounter<? extends T> counter)
  {
    for (Map.Entry<? extends T, Double> e : counter) {
      T t = e.getKey();
      Double cur = Double.valueOf(counts.get(t));
      counts.put(t, cur == null ? ((Double)e.getValue()).doubleValue() : cur.doubleValue() + ((Double)e.getValue()).doubleValue());
    }
  }
  


  public double count(T obj)
  {
    double count = counts.get(obj);
    count += 1.0D;
    counts.put(obj, count);
    sum += 1.0D;
    return count;
  }
  






  public double count(T obj, double count)
  {
    if (count <= 0.0D)
      throw new IllegalArgumentException("Count must be positive: " + count);
    double oldCount = counts.get(obj);
    double newCount = count + oldCount;
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
    if ((o instanceof RationalCounter)) {
      RationalCounter<?> c = (RationalCounter)o;
      if ((counts.size() != c.size()) || (sum != c.sum()))
        return false;
      for (Map.Entry<?, Double> e : c) {
        double i = counts.get(e.getKey());
        if (i != ((Double)e.getValue()).doubleValue())
          return false;
      }
      return true;
    }
    return false;
  }
  



  public double getCount(T obj)
  {
    return counts.get(obj);
  }
  




  public double getFrequency(T obj)
  {
    double count = getCount(obj);
    return sum == 0.0D ? 0.0D : count / sum;
  }
  
  public int hashCode() {
    return counts.hashCode();
  }
  




  public Set<T> items()
  {
    return Collections.unmodifiableSet(counts.keySet());
  }
  



  public Iterator<Map.Entry<T, Double>> iterator()
  {
    return 
      Collections.unmodifiableSet(TDecorators.wrap(counts).entrySet()).iterator();
  }
  




  public T max()
  {
    TObjectDoubleIterator<T> iter = counts.iterator();
    double maxCount = -1.0D;
    T max = null;
    while (iter.hasNext()) {
      iter.advance();
      double count = iter.value();
      if (count > maxCount) {
        max = iter.key();
        maxCount = count;
      }
    }
    return max;
  }
  




  public T min()
  {
    TObjectDoubleIterator<T> iter = counts.iterator();
    double minCount = Double.MAX_VALUE;
    T min = null;
    while (iter.hasNext()) {
      iter.advance();
      double count = iter.value();
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
    sum = 0.0D;
  }
  


  public int size()
  {
    return counts.size();
  }
  


  public double sum()
  {
    return sum;
  }
  
  public String toString() {
    return counts.toString();
  }
}
