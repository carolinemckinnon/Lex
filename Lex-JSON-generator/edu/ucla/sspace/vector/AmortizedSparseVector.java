package edu.ucla.sspace.vector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


























































public class AmortizedSparseVector
  implements SparseDoubleVector, Serializable
{
  private static final long serialVersionUID = 1L;
  private List<IndexValue> values;
  private CellComparator comp;
  private int maxLength;
  private int knownLength;
  
  public AmortizedSparseVector()
  {
    this(Integer.MAX_VALUE);
    knownLength = 0;
  }
  





  public AmortizedSparseVector(int length)
  {
    knownLength = length;
    maxLength = length;
    values = new ArrayList();
    comp = new CellComparator(null);
  }
  


  public double add(int index, double delta)
  {
    checkIndex(index);
    
    double value = get(index) + delta;
    set(index, value);
    return value;
  }
  


  public double get(int index)
  {
    checkIndex(index);
    
    IndexValue item = new IndexValue(index, 0.0D);
    int valueIndex = Collections.binarySearch(values, item, comp);
    return valueIndex >= 0 ? values.get(valueIndex)).value : 0.0D;
  }
  


  public Double getValue(int index)
  {
    return Double.valueOf(get(index));
  }
  



  public double magnitude()
  {
    double m = 0.0D;
    for (IndexValue v : values)
      m += value * value;
    return Math.sqrt(m);
  }
  





  public void set(int index, double value)
  {
    checkIndex(index);
    
    IndexValue item = new IndexValue(index, 0.0D);
    int valueIndex = Collections.binarySearch(values, item, comp);
    if ((valueIndex >= 0) && (value != 0.0D))
    {
      values.get(valueIndex)).value = value;
    } else if (value != 0.0D)
    {
      value = value;
      values.add((valueIndex + 1) * -1, item);
    } else if (valueIndex >= 0)
    {
      values.remove(valueIndex);
    }
  }
  


  public void set(int index, Number value)
  {
    set(index, value.doubleValue());
  }
  




  public void set(double[] value)
  {
    checkIndex(value.length);
    
    for (int i = 0; i < value.length; i++) {
      if (value[i] != 0.0D) {
        set(i, value[i]);
      }
    }
  }
  

  public double[] toArray()
  {
    double[] dense = new double[length()];
    for (IndexValue item : values) {
      dense[index] = value;
    }
    return dense;
  }
  


  public int[] getNonZeroIndices()
  {
    int[] indices = new int[values.size()];
    for (int i = 0; i < values.size(); i++)
      indices[i] = values.get(i)).index;
    return indices;
  }
  


  public int length()
  {
    return knownLength;
  }
  



  private void checkIndex(int length)
  {
    if ((maxLength == Integer.MAX_VALUE) && (knownLength < length)) {
      knownLength = length;
    } else if ((length < 0) || (length >= maxLength)) {
      throw new IllegalArgumentException("Length must be non negative and less than the maximum length");
    }
  }
  


  public SparseDoubleVector instanceCopy()
  {
    return new AmortizedSparseVector(length());
  }
  

  private static class IndexValue
  {
    public int index;
    
    public double value;
    

    public IndexValue(int index, double value)
    {
      this.index = index;
      this.value = value;
    }
  }
  

  private static class CellComparator
    implements Comparator<AmortizedSparseVector.IndexValue>, Serializable
  {
    private static final long serialVersionUID = 1L;
    
    private CellComparator() {}
    
    public int compare(AmortizedSparseVector.IndexValue item1, AmortizedSparseVector.IndexValue item2)
    {
      return index - index;
    }
    
    public boolean equals(Object o) {
      return this == o;
    }
  }
}
