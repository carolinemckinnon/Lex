package edu.ucla.sspace.vector;

import java.io.Serializable;
import java.util.Arrays;






































class DoubleArrayAsVector
  implements DoubleVector, Serializable
{
  private static final long serialVersionUID = 1L;
  private final double[] array;
  
  public DoubleArrayAsVector(double[] array)
  {
    assert (array != null) : "wrapped array cannot be null";
    this.array = array;
  }
  


  public double add(int index, double delta)
  {
    return array[index] += delta;
  }
  


  public void set(int index, double value)
  {
    array[index] = value;
  }
  


  public void set(int index, Number value)
  {
    array[index] = value.doubleValue();
  }
  


  public double get(int index)
  {
    return array[index];
  }
  


  public Double getValue(int index)
  {
    return Double.valueOf(array[index]);
  }
  


  public double magnitude()
  {
    double m = 0.0D;
    for (double d : array)
      m += d * d;
    return Math.sqrt(m);
  }
  


  public double[] toArray()
  {
    return Arrays.copyOf(array, array.length);
  }
  


  public int length()
  {
    return array.length;
  }
}
