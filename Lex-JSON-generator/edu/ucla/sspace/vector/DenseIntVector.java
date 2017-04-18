package edu.ucla.sspace.vector;

import java.io.Serializable;
import java.util.Arrays;




































public class DenseIntVector
  extends AbstractIntegerVector
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private final int[] vector;
  
  public DenseIntVector(int length)
  {
    vector = new int[length];
  }
  






  public DenseIntVector(IntegerVector v)
  {
    vector = new int[v.length()];
    for (int i = 0; i < v.length(); i++) {
      vector[i] = v.get(i);
    }
  }
  





  public DenseIntVector(int[] values)
  {
    vector = Arrays.copyOf(values, values.length);
  }
  


  public int add(int index, int delta)
  {
    vector[index] += delta;
    return vector[index];
  }
  


  public int get(int index)
  {
    return vector[index];
  }
  


  public int length()
  {
    return vector.length;
  }
  


  public double magnitude()
  {
    double m = 0.0D;
    int[] arrayOfInt; int j = (arrayOfInt = vector).length; for (int i = 0; i < j; i++) { double i = arrayOfInt[i];
      m += i * i; }
    return Math.sqrt(m);
  }
  


  public void set(int index, int value)
  {
    vector[index] = value;
  }
}
