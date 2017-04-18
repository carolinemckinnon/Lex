package edu.ucla.sspace.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


































public class ObjectIndexer<T>
  implements Indexer<T>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final BiMap<T, Integer> indices;
  
  public ObjectIndexer()
  {
    indices = new HashBiMap();
  }
  


  public ObjectIndexer(Collection<? extends T> items)
  {
    this();
    for (T item : items) {
      index(item);
    }
  }
  

  public ObjectIndexer(Indexer<? extends T> indexed)
  {
    this();
    for (Map.Entry<? extends T, Integer> e : indexed) {
      indices.put(e.getKey(), (Integer)e.getValue());
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
    Integer i = (Integer)indices.get(item);
    return i == null ? -1 : i.intValue();
  }
  


  public int highestIndex()
  {
    return indices.size() - 1;
  }
  


  public int index(T item)
  {
    Integer i = (Integer)indices.get(item);
    if (i == null) {
      synchronized (indices) {
        i = (Integer)indices.get(item);
        if (i == null) {
          i = Integer.valueOf(indices.size());
          indices.put(item, i);
        }
      }
    }
    return i.intValue();
  }
  


  public boolean indexAll(Collection<T> items)
  {
    boolean modified = false;
    for (T item : items) {
      Integer i = (Integer)indices.get(item);
      if (i == null) {
        synchronized (indices) {
          i = (Integer)indices.get(item);
          if (i == null) {
            i = Integer.valueOf(indices.size());
            indices.put(item, i);
            modified = true;
          }
        }
      }
    }
    return modified;
  }
  


  public Iterator<Map.Entry<T, Integer>> iterator()
  {
    return Collections.unmodifiableSet(indices.entrySet()).iterator();
  }
  


  public T lookup(int index)
  {
    return indices.inverse().get(Integer.valueOf(index));
  }
  


  public Map<Integer, T> mapping()
  {
    return Collections.unmodifiableMap(indices.inverse());
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
