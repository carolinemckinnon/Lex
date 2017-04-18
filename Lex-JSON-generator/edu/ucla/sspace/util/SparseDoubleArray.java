package edu.ucla.sspace.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;







































































public class SparseDoubleArray
  implements SparseNumericArray<Double>, Serializable, Iterable<DoubleEntry>
{
  private static final long serialVersionUID = 1L;
  private final int maxLength;
  private int[] indices;
  private double[] values;
  
  public SparseDoubleArray()
  {
    this(Integer.MAX_VALUE);
  }
  


  public SparseDoubleArray(int length)
  {
    if (length < 0)
      throw new IllegalArgumentException("length must be non-negative");
    maxLength = length;
    
    indices = new int[0];
    values = new double[0];
  }
  






  public SparseDoubleArray(double[] array)
  {
    maxLength = array.length;
    

    int nonZero = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i] != 0.0D) {
        nonZero++;
      }
    }
    indices = new int[nonZero];
    values = new double[nonZero];
    int index = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i] != 0.0D) {
        indices[index] = i;
        values[(index++)] = array[i];
      }
    }
  }
  












  public SparseDoubleArray(int[] indices, double[] values, int length)
  {
    if (indices.length != values.length)
      throw new IllegalArgumentException(
        "different number of indices and values");
    maxLength = length;
    this.indices = indices;
    this.values = values;
    
    for (int i = 0; i < this.indices.length - 1; i++) {
      if (this.indices[i] >= this.indices[(i + 1)]) {
        throw new IllegalArgumentException(
          "Indices must be sorted and unique.  Given " + 
          this.indices[i] + " and " + this.indices[(i + 1)]);
      }
    }
  }
  





  public double addPrimitive(int index, double delta)
  {
    if ((index < 0) || (index >= maxLength)) {
      throw new ArrayIndexOutOfBoundsException(
        "invalid index: " + index);
    }
    
    if (delta == 0.0D) {
      return get(index).doubleValue();
    }
    int pos = Arrays.binarySearch(indices, index);
    


    if (pos < 0) {
      int newPos = 0 - (pos + 1);
      int[] newIndices = Arrays.copyOf(indices, indices.length + 1);
      double[] newValues = Arrays.copyOf(values, values.length + 1);
      

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
    
    double newValue = values[pos] + delta;
    


    if (newValue == 0.0D) {
      int newLength = indices.length - 1;
      int[] newIndices = new int[newLength];
      double[] newValues = new double[newLength];
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
  



  public Double add(int index, Double delta)
  {
    return Double.valueOf(addPrimitive(index, delta.doubleValue()));
  }
  


  public int cardinality()
  {
    return indices.length;
  }
  








  public double dividePrimitive(int index, double value)
  {
    if ((index < 0) || (index >= maxLength)) {
      throw new ArrayIndexOutOfBoundsException(
        "invalid index: " + index);
    }
    int pos = Arrays.binarySearch(indices, index);
    



    if (pos < 0) {
      return 0.0D;
    }
    double newValue = values[pos] / value;
    


    if (newValue == 0.0D) {
      int newLength = indices.length - 1;
      int[] newIndices = new int[newLength];
      double[] newValues = new double[newLength];
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
  



  public Double divide(int index, Double value)
  {
    return Double.valueOf(dividePrimitive(index, value.doubleValue()));
  }
  


  public Double get(int index)
  {
    return Double.valueOf(getPrimitive(index));
  }
  




  public int[] getElementIndices()
  {
    return indices;
  }
  










  public double getPrimitive(int index)
  {
    if ((index < 0) || (index >= maxLength)) {
      throw new ArrayIndexOutOfBoundsException(
        "invalid index: " + index);
    }
    int pos = Arrays.binarySearch(indices, index);
    double value = pos >= 0 ? values[pos] : 0.0D;
    return value;
  }
  


  public Iterator<DoubleEntry> iterator()
  {
    return new SparseDoubleArrayIterator();
  }
  


  public int length()
  {
    return maxLength;
  }
  








  public double multiplyPrimitive(int index, double value)
  {
    if ((index < 0) || (index >= maxLength)) {
      throw new ArrayIndexOutOfBoundsException(
        "invalid index: " + index);
    }
    int pos = Arrays.binarySearch(indices, index);
    



    if (pos < 0) {
      return 0.0D;
    }
    double newValue = values[pos] * value;
    


    if (newValue == 0.0D) {
      int newLength = indices.length - 1;
      int[] newIndices = new int[newLength];
      double[] newValues = new double[newLength];
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
  



  public Double multiply(int index, Double value)
  {
    return Double.valueOf(multiplyPrimitive(index, value.doubleValue()));
  }
  


  public void set(int index, Double value)
  {
    setPrimitive(index, value.doubleValue());
  }
  



  public void setPrimitive(int index, double value)
  {
    int pos = Arrays.binarySearch(indices, index);
    
    if (value != 0.0D)
    {
      if (pos < 0) {
        int newPos = 0 - (pos + 1);
        int[] newIndices = Arrays.copyOf(indices, indices.length + 1);
        double[] newValues = Arrays.copyOf(values, values.length + 1);
        

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
    else if ((value == 0.0D) && (pos >= 0)) {
      int newLength = indices.length - 1;
      int[] newIndices = new int[newLength];
      double[] newValues = new double[newLength];
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
        array[i] = Double.valueOf(values[j]);
        j++;
      }
      else {
        array[i] = Double.valueOf(0.0D);
      } }
    return array;
  }
  




  public double[] toPrimitiveArray(double[] array)
  {
    int i = 0; for (int j = 0; i < array.length; i++) {
      int index = -1;
      if ((j < indices.length) && ((index = indices[j]) == i)) {
        array[i] = values[j];
        j++;
      } else {
        array[i] = 0.0D;
      } }
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
  

  private class SparseDoubleArrayIterator
    implements Iterator<DoubleEntry>
  {
    int curIndex;
    
    DoubleEntryImpl e;
    

    public SparseDoubleArrayIterator()
    {
      curIndex = 0;
      e = new DoubleEntryImpl(-1, -1);
    }
    
    public boolean hasNext() {
      return indices.length > curIndex;
    }
    
    public DoubleEntry next() {
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
    

    private class DoubleEntryImpl
      implements DoubleEntry
    {
      public int index;
      
      public double val;
      
      public DoubleEntryImpl(int index, int val)
      {
        this.index = index;
        this.val = val;
      }
      


      public int index()
      {
        return index;
      }
      


      public double value()
      {
        return val;
      }
      
      public String toString() {
        return "[" + index + "->" + val + "]";
      }
    }
  }
}
