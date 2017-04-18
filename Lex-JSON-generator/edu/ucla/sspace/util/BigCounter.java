package edu.ucla.sspace.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public abstract interface BigCounter<T>
  extends Iterable<Map.Entry<T, Long>>
{
  public abstract void add(BigCounter<? extends T> paramBigCounter);
  
  public abstract void add(Counter<? extends T> paramCounter);
  
  public abstract long count(T paramT);
  
  public abstract long count(T paramT, long paramLong);
  
  public abstract void countAll(Collection<? extends T> paramCollection);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract long getCount(T paramT);
  
  public abstract double getFrequency(T paramT);
  
  public abstract int hashCode();
  
  public abstract Set<T> items();
  
  public abstract Iterator<Map.Entry<T, Long>> iterator();
  
  public abstract T max();
  
  public abstract T min();
  
  public abstract void reset();
  
  public abstract int size();
  
  public abstract long sum();
}
