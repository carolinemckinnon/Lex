package edu.ucla.sspace.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;



















































public class SparseIntHashArray
  implements SparseArray<Integer>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final int maxLength;
  private Map<Integer, Integer> indexToValue;
  
  public SparseIntHashArray()
  {
    this(Integer.MAX_VALUE);
  }
  


  public SparseIntHashArray(int length)
  {
    if (length < 0)
      throw new IllegalArgumentException("length must be non-negative");
    maxLength = length;
    
    indexToValue = new HashMap();
  }
  





  public SparseIntHashArray(int[] array)
  {
    maxLength = array.length;
    

    int nonZero = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i] != 0) {
        indexToValue.put(Integer.valueOf(i), Integer.valueOf(array[i]));
      }
    }
  }
  

  public int cardinality()
  {
    return indexToValue.size();
  }
  


  public Integer get(int index)
  {
    Integer i = (Integer)indexToValue.get(Integer.valueOf(index));
    return Integer.valueOf(i == null ? 0 : i.intValue());
  }
  




  public int[] getElementIndices()
  {
    Integer[] indices = (Integer[])indexToValue.keySet().toArray(new Integer[0]);
    int[] primitive = new int[indices.length];
    for (int i = 0; i < indices.length; i++)
      primitive[i] = indices[i].intValue();
    return primitive;
  }
  


  public int length()
  {
    return maxLength;
  }
  


  public void set(int index, Integer value)
  {
    if (value.intValue() == 0) {
      indexToValue.remove(Integer.valueOf(index));
    } else {
      indexToValue.put(Integer.valueOf(index), value);
    }
  }
  


  public <E> E[] toArray(E[] array)
  {
    for (int i = 0; i < array.length; i++) {
      Integer j = (Integer)indexToValue.get(Integer.valueOf(i));
      if (j != null)
        array[i] = j;
    }
    return array;
  }
}
