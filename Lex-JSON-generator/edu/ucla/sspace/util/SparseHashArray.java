package edu.ucla.sspace.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

























































public class SparseHashArray<T>
  implements SparseArray<T>, Iterable<ObjectEntry<T>>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final int maxLength;
  private Map<Integer, T> indexToValue;
  private int[] indices;
  
  public SparseHashArray()
  {
    this(Integer.MAX_VALUE);
  }
  


  public SparseHashArray(int length)
  {
    if (length < 0)
      throw new IllegalArgumentException("length must be non-negative");
    maxLength = length;
    indices = null;
    indexToValue = new HashMap();
  }
  




  public SparseHashArray(T[] array)
  {
    maxLength = array.length;
    indices = null;
    
    int nonZero = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i] != null) {
        indexToValue.put(Integer.valueOf(i), array[i]);
      }
    }
  }
  

  public int cardinality()
  {
    return indexToValue.size();
  }
  


  public T get(int index)
  {
    return indexToValue.get(Integer.valueOf(index));
  }
  




  public int[] getElementIndices()
  {
    if (indices != null)
      return indices;
    Integer[] objIndices = (Integer[])indexToValue.keySet().toArray(new Integer[0]);
    indices = new int[objIndices.length];
    for (int i = 0; i < objIndices.length; i++) {
      indices[i] = objIndices[i].intValue();
    }
    Arrays.sort(indices);
    return indices;
  }
  




  public Iterator<ObjectEntry<T>> iterator()
  {
    return new SparseHashArrayIterator();
  }
  


  public int length()
  {
    return maxLength;
  }
  



  public void set(int index, T value)
  {
    if (value != null)
    {

      if (indexToValue.put(Integer.valueOf(index), value) == null) {
        indices = null;
      }
      
    }
    else if (indexToValue.remove(Integer.valueOf(index)) != null)
    {

      indices = null;
    }
  }
  



  public <E> E[] toArray(E[] array)
  {
    for (int i = 0; i < array.length; i++) {
      T j = indexToValue.get(Integer.valueOf(i));
      if (j != null)
        array[i] = j;
    }
    return array;
  }
  

  private class SparseHashArrayIterator
    implements Iterator<ObjectEntry<T>>
  {
    Iterator<Map.Entry<Integer, T>> arrayIter;
    

    public SparseHashArrayIterator()
    {
      arrayIter = indexToValue.entrySet().iterator();
    }
    
    public boolean hasNext() {
      return arrayIter.hasNext();
    }
    
    public ObjectEntry<T> next() {
      final Map.Entry<Integer, T> e = (Map.Entry)arrayIter.next();
      
      new ObjectEntry() {
        public int index() { return ((Integer)e.getKey()).intValue(); }
        public T value() { return e.getValue(); }
      };
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
}
