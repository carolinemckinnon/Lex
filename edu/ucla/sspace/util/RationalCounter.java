package edu.ucla.sspace.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public abstract interface RationalCounter<T>
  extends Iterable<Map.Entry<T, Double>>
{
  public abstract void add(RationalCounter<? extends T> paramRationalCounter);
  
  public abstract double count(T paramT);
  
  public abstract double count(T paramT, double paramDouble);
  
  public abstract void countAll(Collection<? extends T> paramCollection);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract double getCount(T paramT);
  
  public abstract double getFrequency(T paramT);
  
  public abstract int hashCode();
  
  public abstract Set<T> items();
  
  public abstract Iterator<Map.Entry<T, Double>> iterator();
  
  public abstract T max();
  
  public abstract T min();
  
  public abstract void reset();
  
  public abstract int size();
  
  public abstract double sum();
}
