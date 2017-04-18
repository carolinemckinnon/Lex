package edu.ucla.sspace.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract interface Indexer<T>
  extends Iterable<Map.Entry<T, Integer>>
{
  public abstract int index(T paramT);
  
  public abstract boolean indexAll(Collection<T> paramCollection);
  
  public abstract void clear();
  
  public abstract boolean contains(T paramT);
  
  public abstract Set<T> items();
  
  public abstract int find(T paramT);
  
  public abstract int highestIndex();
  
  public abstract Iterator<Map.Entry<T, Integer>> iterator();
  
  public abstract T lookup(int paramInt);
  
  public abstract Map<Integer, T> mapping();
  
  public abstract int size();
}
