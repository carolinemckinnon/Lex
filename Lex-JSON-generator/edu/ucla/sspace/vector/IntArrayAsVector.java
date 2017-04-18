package edu.ucla.sspace.vector;

import java.io.Serializable;
import java.util.Arrays;






































class IntArrayAsVector
  implements IntegerVector, Serializable
{
  private static final long serialVersionUID = 1L;
  private final int[] array;
  
  public IntArrayAsVector(int[] array)
  {
    assert (array != null) : "wrapped array cannot be null";
    this.array = array;
  }
  


  public int add(int index, int delta)
  {
    return array[index] += delta;
  }
  


  public void set(int index, int value)
  {
    array[index] = value;
  }
  


  public void set(int index, Number value)
  {
    array[index] = value.intValue();
  }
  


  public int get(int index)
  {
    return array[index];
  }
  


  public Integer getValue(int index)
  {
    return Integer.valueOf(array[index]);
  }
  


  public double magnitude()
  {
    double m = 0.0D;
    for (int i : array)
      m += i * i;
    return Math.sqrt(m);
  }
  


  public int[] toArray()
  {
    return Arrays.copyOf(array, array.length);
  }
  


  public int length()
  {
    return array.length;
  }
}
