package edu.ucla.sspace.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public abstract interface Counter<T>
  extends Iterable<Map.Entry<T, Integer>>
{
  public abstract void add(Counter<? extends T> paramCounter);
  
  public abstract int count(T paramT);
  
  public abstract int count(T paramT, int paramInt);
  
  public abstract void countAll(Collection<? extends T> paramCollection);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int getCount(T paramT);
  
  public abstract double getFrequency(T paramT);
  
  public abstract int hashCode();
  
  public abstract Set<T> items();
  
  public abstract Iterator<Map.Entry<T, Integer>> iterator();
  
  public abstract T max();
  
  public abstract T min();
  
  public abstract void reset();
  
  public abstract int size();
  
  public abstract int sum();
}
