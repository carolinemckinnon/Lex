package edu.ucla.sspace.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;







































































public class SparseIntArray
  implements SparseNumericArray<Integer>, Serializable, Iterable<IntegerEntry>
{
  private static final long serialVersionUID = 1L;
  private final int maxLength;
  private int[] indices;
  private int[] values;
  
  public SparseIntArray()
  {
    this(Integer.MAX_VALUE);
  }
  


  public SparseIntArray(int length)
  {
    if (length < 0)
      throw new IllegalArgumentException("length must be non-negative");
    maxLength = length;
    
    indices = new int[0];
    values = new int[0];
  }
  




  public SparseIntArray(int[] array)
  {
    maxLength = array.length;
    

    int nonZero = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i] != 0) {
        nonZero++;
      }
    }
    indices = new int[nonZero];
    values = new int[nonZero];
    int index = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i] != 0) {
        indices[index] = i;
        values[(index++)] = array[i];
      }
    }
  }
  












  public SparseIntArray(int[] indices, int[] values, int length)
  {
    if (indices.length != values.length)
      throw new IllegalArgumentException(
        "different number of indices and values");
    maxLength = length;
    this.indices = indices;
    this.values = values;
    
    for (int i = 0; i < this.indices.length - 1; i++) {
      if (this.indices[i] <= this.indices[(i + 1)]) {
        throw new IllegalArgumentException(
          "Indices must be sorted and unique");
      }
    }
  }
  





  public int addPrimitive(int index, int delta)
  {
    if ((index < 0) || (index >= maxLength)) {
      throw new ArrayIndexOutOfBoundsException(
        "invalid index: " + index);
    }
    
    if (delta == 0) {
      return get(index).intValue();
    }
    int pos = Arrays.binarySearch(indices, index);
    


    if (pos < 0) {
      int newPos = 0 - (pos + 1);
      int[] newIndices = Arrays.copyOf(indices, indices.length + 1);
      int[] newValues = Arrays.copyOf(values, values.length + 1);
      

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
    
    int newValue = values[pos] + delta;
    


    if (newValue == 0) {
      int newLength = indices.length - 1;
      int[] newIndices = new int[newLength];
      int[] newValues = new int[newLength];
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
  



  public Integer add(int index, Integer delta)
  {
    return Integer.valueOf(addPrimitive(index, delta.intValue()));
  }
  


  public int cardinality()
  {
    return indices.length;
  }
  








  public int dividePrimitive(int index, int value)
  {
    if ((index < 0) || (index >= maxLength)) {
      throw new ArrayIndexOutOfBoundsException(
        "invalid index: " + index);
    }
    int pos = Arrays.binarySearch(indices, index);
    



    if (pos < 0) {
      return 0;
    }
    int newValue = values[pos] / value;
    


    if (newValue == 0) {
      int newLength = indices.length - 1;
      int[] newIndices = new int[newLength];
      int[] newValues = new int[newLength];
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
  



  public Integer divide(int index, Integer value)
  {
    return Integer.valueOf(dividePrimitive(index, value.intValue()));
  }
  


  public Integer get(int index)
  {
    return Integer.valueOf(getPrimitive(index));
  }
  




  public int[] getElementIndices()
  {
    return indices;
  }
  










  public int getPrimitive(int index)
  {
    if ((index < 0) || (index >= maxLength)) {
      throw new ArrayIndexOutOfBoundsException("invalid index: " + 
        index);
    }
    
    int pos = Arrays.binarySearch(indices, index);
    int value = pos >= 0 ? values[pos] : 0;
    return value;
  }
  


  public Iterator<IntegerEntry> iterator()
  {
    return new SparseIntArrayIterator();
  }
  


  public int length()
  {
    return maxLength;
  }
  








  public int multiplyPrimitive(int index, int value)
  {
    if ((index < 0) || (index >= maxLength)) {
      throw new ArrayIndexOutOfBoundsException(
        "invalid index: " + index);
    }
    int pos = Arrays.binarySearch(indices, index);
    



    if (pos < 0) {
      return 0;
    }
    int newValue = values[pos] * value;
    


    if (newValue == 0) {
      int newLength = indices.length - 1;
      int[] newIndices = new int[newLength];
      int[] newValues = new int[newLength];
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
  



  public Integer multiply(int index, Integer value)
  {
    return Integer.valueOf(multiplyPrimitive(index, value.intValue()));
  }
  


  public void set(int index, Integer value)
  {
    setPrimitive(index, value.intValue());
  }
  




  public void setPrimitive(int index, int value)
  {
    int pos = Arrays.binarySearch(indices, index);
    
    if (value != 0)
    {
      if (pos < 0) {
        int newPos = 0 - (pos + 1);
        int[] newIndices = new int[indices.length + 1];
        int[] newValues = new int[values.length + 1];
        


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
    else if ((value == 0) && (pos >= 0)) {
      int newLength = indices.length - 1;
      int[] newIndices = new int[newLength];
      int[] newValues = new int[newLength];
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
        array[i] = Integer.valueOf(values[j]);
        j++;
      }
      else {
        array[i] = Integer.valueOf(0);
      }
    }
    return array;
  }
  




  public int[] toPrimitiveArray(int[] array)
  {
    int i = 0; for (int j = 0; i < array.length; i++) {
      int index = -1;
      if ((j < indices.length) && ((index = indices[j]) == i)) {
        array[i] = values[j];
        j++;
      }
      else {
        array[i] = 0;
      }
    }
    return array;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder(indices.length * 6);
    sb.append('[');
    for (int i = 0; i < indices.length; i++) {
      sb.append(indices[i]).append(':').append(values[i]);
      if (i + 1 < indices.length)
        sb.append(", ");
    }
    return ']';
  }
  

  private class SparseIntArrayIterator
    implements Iterator<IntegerEntry>
  {
    int curIndex;
    
    IntegerEntryImpl e;
    

    public SparseIntArrayIterator()
    {
      curIndex = 0;
      e = new IntegerEntryImpl(-1, -1);
    }
    
    public boolean hasNext() {
      return indices.length > curIndex;
    }
    
    public IntegerEntry next() {
      if (indices.length <= curIndex) {
        throw new NoSuchElementException();
      }
      
      e.index = indices[curIndex];
      e.val = values[curIndex];
      curIndex += 1;
      return e;
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
    

    private class IntegerEntryImpl
      implements IntegerEntry
    {
      public int index;
      
      public int val;
      
      public IntegerEntryImpl(int index, int val)
      {
        this.index = index;
        this.val = val;
      }
      


      public int index()
      {
        return index;
      }
      


      public int value()
      {
        return val;
      }
      
      public String toString() {
        return "[" + index + "->" + val + "]";
      }
    }
  }
}
