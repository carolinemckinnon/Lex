package edu.ucla.sspace.util;

import gnu.trove.TDecorators;
import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.map.hash.TObjectIntHashMap;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

















































public class HashIndexer<T>
  implements Indexer<T>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final TObjectIntHashMap<T> indices;
  private transient T[] indexLookup;
  
  public HashIndexer()
  {
    indices = new TObjectIntHashMap();
  }
  



  public HashIndexer(int initialCapacity)
  {
    indices = new TObjectIntHashMap(initialCapacity);
  }
  


  public HashIndexer(Collection<? extends T> items)
  {
    this(items.size());
    for (T item : items) {
      index(item);
    }
  }
  

  public HashIndexer(Indexer<? extends T> indexed)
  {
    this(indexed.size());
    for (Map.Entry<? extends T, Integer> e : indexed) {
      indices.put(e.getKey(), ((Integer)e.getValue()).intValue());
    }
  }
  

  public void clear()
  {
    indices.clear();
  }
  


  public boolean contains(T item)
  {
    return indices.containsKey(item);
  }
  


  public Set<T> items()
  {
    return Collections.unmodifiableSet(indices.keySet());
  }
  


  public int find(T item)
  {
    return indices.containsKey(item) ? 
      indices.get(item) : -indices.size();
  }
  


  public int highestIndex()
  {
    return indices.size() - 1;
  }
  


  public int index(T item)
  {
    if (indices.containsKey(item))
      return indices.get(item);
    synchronized (indices)
    {

      if (indices.containsKey(item))
        return indices.get(item);
      int index = indices.size();
      indices.put(item, index);
      return index;
    }
  }
  



  public boolean indexAll(Collection<T> items)
  {
    boolean changed = false;
    for (T item : items) {
      if (!indices.containsKey(item))
        synchronized (indices)
        {

          if (!indices.containsKey(item))
          {
            int index = indices.size();
            indices.put(item, index);
            changed = true;
          }
        }
    }
    return changed;
  }
  



  public Iterator<Map.Entry<T, Integer>> iterator()
  {
    return 
      Collections.unmodifiableSet(TDecorators.wrap(indices).entrySet()).iterator();
  }
  


  public T lookup(int index)
  {
    if ((index < 0) || (index >= indices.size()))
      return null;
    if ((!$assertionsDisabled) && (indices.size() <= 0)) { throw new AssertionError();
    }
    
    while ((indexLookup == null) || (indexLookup.length != indices.size())) {
      T t = indices.keySet().iterator().next();
      
      Object[] tmp = (Object[])Array.newInstance(t.getClass(), indices.size());
      TObjectIntIterator<T> iter = indices.iterator();
      while (iter.hasNext()) {
        iter.advance();
        tmp[iter.value()] = iter.key();
      }
      





      indexLookup = tmp;
    }
    return indexLookup[index];
  }
  


  public Map<Integer, T> mapping()
  {
    throw new Error();
  }
  


  public int size()
  {
    return indices.size();
  }
  


  public String toString()
  {
    return indices.toString();
  }
}
