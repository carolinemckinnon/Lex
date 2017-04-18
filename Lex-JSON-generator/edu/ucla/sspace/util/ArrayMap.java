package edu.ucla.sspace.util;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;















































public class ArrayMap<T>
  extends AbstractMap<Integer, T>
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private final T[] array;
  private int size;
  
  public ArrayMap(T[] array, int sizeOfMap)
  {
    this.array = Arrays.copyOf(array, sizeOfMap);
    size = -1;
  }
  
  public ArrayMap(T[] array) {
    if (array == null) {
      throw new NullPointerException();
    }
    this.array = array;
    size = -1;
  }
  
  public Set<Map.Entry<Integer, T>> entrySet() {
    return new EntrySet(null);
  }
  
  public boolean containsKey(Object key) {
    if ((key instanceof Integer)) {
      Integer i = (Integer)key;
      return (i.intValue() >= 0) && (i.intValue() < array.length) && (array[i.intValue()] != null);
    }
    return false;
  }
  
  public boolean containsValue(Object key) {
    for (T t : array)
      if ((t != null) && (t.equals(key)))
        return true;
    return false;
  }
  
  public T get(Object key) {
    if ((key instanceof Integer)) {
      Integer i = (Integer)key;
      return (i.intValue() < 0) || (i.intValue() >= array.length) ? 
        null : 
        array[i.intValue()];
    }
    return null;
  }
  


  public int maxKey()
  {
    return array.length - 1;
  }
  




  public T put(Integer key, T value)
  {
    if ((key.intValue() < 0) || (key.intValue() >= array.length))
      throw new IllegalArgumentException(
        "key goes beyond bounds of the array backing this Map:" + key);
    if (value == null)
      throw new IllegalArgumentException("null values are not supported");
    T t = array[key.intValue()];
    array[key.intValue()] = value;
    

    if ((size >= 0) && (t == null))
      size += 1;
    return t;
  }
  
  public T remove(Object key) {
    if ((key instanceof Integer)) {
      Integer i = (Integer)key;
      if ((i.intValue() < 0) || (i.intValue() >= array.length))
        return null;
      T t = array[i.intValue()];
      array[i.intValue()] = null;
      if ((size >= 0) && (t != null))
        size -= 1;
      return t;
    }
    return null;
  }
  
  public int size() {
    if (size == -1) {
      size = 0;
      for (T t : array) {
        if (t != null)
          size += 1;
      }
      System.out.printf("array: %s, size: %d%n", new Object[] { Arrays.toString(array), Integer.valueOf(size) });
    }
    return size;
  }
  
  private class EntrySet extends AbstractSet<Map.Entry<Integer, T>> {
    private EntrySet() {}
    
    public boolean add(Map.Entry<Integer, T> e) { throw new UnsupportedOperationException(); }
    
    public boolean contains(Object o)
    {
      if ((o instanceof Map.Entry)) {
        Map.Entry<?, ?> e = (Map.Entry)o;
        T t = get(e.getKey());
        return (t != null) && (t.equals(e.getValue()));
      }
      return false;
    }
    
    public Iterator<Map.Entry<Integer, T>> iterator() {
      return new EntryIterator();
    }
    
    public boolean remove(Object o) {
      throw new UnsupportedOperationException();
    }
    
    public int size() {
      return size;
    }
    
    private class EntryIterator implements Iterator<Map.Entry<Integer, T>>
    {
      private int i;
      private Map.Entry<Integer, T> next;
      
      public EntryIterator()
      {
        i = 0;
        advance();
      }
      
      public void advance() {
        next = null;
        while (i < array.length) {
          T t = array[i];
          i += 1;
          if (t != null) {
            next = new AbstractMap.SimpleImmutableEntry(Integer.valueOf(i), t);
            break;
          }
        }
      }
      
      public boolean hasNext() {
        return next != null;
      }
      
      public Map.Entry<Integer, T> next() {
        if (!hasNext())
          throw new IllegalStateException();
        Map.Entry<Integer, T> n = next;
        advance();
        return n;
      }
      
      public void remove() {
        throw new UnsupportedOperationException();
      }
    }
  }
}
