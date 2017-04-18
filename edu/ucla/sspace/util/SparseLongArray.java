package edu.ucla.sspace.util;

import java.io.Serializable;
import java.util.Arrays;





























































public class SparseLongArray
  implements SparseNumericArray<Long>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final int maxLength;
  private int[] indices;
  private long[] values;
  
  public SparseLongArray()
  {
    this(Integer.MAX_VALUE);
  }
  


  public SparseLongArray(int length)
  {
    if (length < 0)
      throw new IllegalArgumentException("length must be non-negative");
    maxLength = length;
    
    indices = new int[0];
    values = new long[0];
  }
  





  public SparseLongArray(long[] array)
  {
    maxLength = array.length;
    

    int nonZero = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i] != 0L) {
        nonZero++;
      }
    }
    indices = new int[nonZero];
    values = new long[nonZero];
    int index = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i] != 0L) {
        indices[index] = i;
        values[(index++)] = array[i];
      }
    }
  }
  






  public long addPrimitive(int index, long delta)
  {
    if ((index < 0) || (index >= maxLength)) {
      throw new ArrayIndexOutOfBoundsException(
        "invalid index: " + index);
    }
    
    if (delta == 0L) {
      return get(index).longValue();
    }
    int pos = Arrays.binarySearch(indices, index);
    


    if (pos < 0) {
      int newPos = 0 - (pos + 1);
      int[] newIndices = Arrays.copyOf(indices, indices.length + 1);
      long[] newValues = Arrays.copyOf(values, values.length + 1);
      

      for (int i = newPos; i < values.length; i++) {
        newValues[(i + 1)] = values[i];
        newIndices[(i + 1)] = indices[i];
      }
      

      indices = newIndices;
      values = newValues;
      pos = newPos;
      

      indices[pos] = index;
      values[pos] = delta;
      return delta;
    }
    
    long newValue = values[pos] + delta;
    


    if (newValue == 0L) {
      int newLength = indices.length - 1;
      int[] newIndices = new int[newLength];
      long[] newValues = new long[newLength];
      int i = 0; for (int j = 0; i < indices.length; i++) {
        if (i != pos) {
          newIndices[j] = indices[i];
          newValues[j] = values[i];
          j++;
        }
      }
      
      indices = newIndices;
      values = newValues;

    }
    else
    {
      values[pos] = newValue; }
    return newValue;
  }
  



  public Long add(int index, Long delta)
  {
    return Long.valueOf(addPrimitive(index, delta.longValue()));
  }
  


  public int cardinality()
  {
    return indices.length;
  }
  








  public long dividePrimitive(int index, long value)
  {
    if ((index < 0) || (index >= maxLength)) {
      throw new ArrayIndexOutOfBoundsException(
        "invalid index: " + index);
    }
    int pos = Arrays.binarySearch(indices, index);
    



    if (pos < 0) {
      return 0L;
    }
    long newValue = values[pos] / value;
    


    if (newValue == 0L) {
      int newLength = indices.length - 1;
      int[] newIndices = new int[newLength];
      long[] newValues = new long[newLength];
      int i = 0; for (int j = 0; i < indices.length; i++) {
        if (i != pos) {
          newIndices[j] = indices[i];
          newValues[j] = values[i];
          j++;
        }
      }
      
      indices = newIndices;
      values = newValues;

    }
    else
    {
      values[pos] = newValue; }
    return newValue;
  }
  



  public Long divide(int index, Long value)
  {
    return Long.valueOf(dividePrimitive(index, value.longValue()));
  }
  


  public Long get(int index)
  {
    return Long.valueOf(getPrimitive(index));
  }
  




  public int[] getElementIndices()
  {
    return indices;
  }
  










  public long getPrimitive(int index)
  {
    if ((index < 0) || (index >= maxLength)) {
      throw new ArrayIndexOutOfBoundsException("invalid index: " + 
        index);
    }
    int pos = Arrays.binarySearch(indices, index);
    
    long value = pos >= 0 ? values[pos] : 0L;
    return value;
  }
  


  public int length()
  {
    return maxLength;
  }
  








  public long multiplyPrimitive(int index, long value)
  {
    if ((index < 0) || (index >= maxLength)) {
      throw new ArrayIndexOutOfBoundsException(
        "invalid index: " + index);
    }
    int pos = Arrays.binarySearch(indices, index);
    



    if (pos < 0) {
      return 0L;
    }
    long newValue = values[pos] / value;
    


    if (newValue == 0L) {
      int newLength = indices.length - 1;
      int[] newIndices = new int[newLength];
      long[] newValues = new long[newLength];
      int i = 0; for (int j = 0; i < indices.length; i++) {
        if (i != pos) {
          newIndices[j] = indices[i];
          newValues[j] = values[i];
          j++;
        }
      }
      
      indices = newIndices;
      values = newValues;

    }
    else
    {
      values[pos] = newValue; }
    return newValue;
  }
  



  public Long multiply(int index, Long value)
  {
    return Long.valueOf(multiplyPrimitive(index, value.longValue()));
  }
  


  public void set(int index, Long value)
  {
    setPrimitive(index, value.intValue());
  }
  




  public void setPrimitive(int index, long value)
  {
    int pos = Arrays.binarySearch(indices, index);
    
    if (value != 0L)
    {
      if (pos < 0) {
        int newPos = 0 - (pos + 1);
        int[] newIndices = new int[indices.length + 1];
        long[] newValues = new long[values.length + 1];
        


        for (int i = 0; i < newPos; i++) {
          newValues[i] = values[i];
          newIndices[i] = indices[i];
        }
        

        for (int i = newPos; i < values.length; i++) {
          newValues[(i + 1)] = values[i];
          newIndices[(i + 1)] = indices[i];
        }
        

        indices = newIndices;
        values = newValues;
        pos = newPos;
        

        indices[pos] = index;
      }
      values[pos] = value;



    }
    else if ((value == 0L) && (pos >= 0)) {
      int newLength = indices.length - 1;
      int[] newIndices = new int[newLength];
      long[] newValues = new long[newLength];
      int i = 0; for (int j = 0; i < indices.length; i++) {
        if (i != pos) {
          newIndices[j] = indices[i];
          newValues[j] = values[i];
          j++;
        }
      }
      
      indices = newIndices;
      values = newValues;
    }
  }
  






  public <E> E[] toArray(E[] array)
  {
    int i = 0; for (int j = 0; i < array.length; i++) {
      int index = -1;
      if ((j < indices.length) && ((index = indices[j]) == i)) {
        array[i] = Long.valueOf(values[j]);
        j++;
      }
      else {
        array[i] = Long.valueOf(0L);
      }
    }
    return array;
  }
  




  public long[] toPrimitiveArray(long[] array)
  {
    int i = 0; for (int j = 0; i < array.length; i++) {
      int index = -1;
      if ((j < indices.length) && ((index = indices[j]) == i)) {
        array[i] = values[j];
        j++;
      }
      else {
        array[i] = 0L;
      }
    }
    return array;
  }
}
