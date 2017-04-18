package edu.ucla.sspace.vector;

import edu.ucla.sspace.util.DoubleEntry;
import java.io.Serializable;
import java.util.Arrays;















































public class DenseDynamicMagnitudeVector
  implements DoubleVector, Serializable
{
  private static final long serialVersionUID = 1L;
  private double[] vector;
  private double magnitude;
  
  public DenseDynamicMagnitudeVector(int vectorLength)
  {
    vector = new double[vectorLength];
    magnitude = 0.0D;
  }
  






  public DenseDynamicMagnitudeVector(double[] vector)
  {
    this.vector = Arrays.copyOf(vector, vector.length);
    magnitude = 0.0D;
    for (double d : vector) {
      magnitude += d * d;
    }
  }
  





  public DenseDynamicMagnitudeVector(DoubleVector v)
  {
    vector = new double[v.length()];
    magnitude = v.magnitude();
    magnitude *= magnitude;
    if ((v instanceof Iterable)) {
      for (DoubleEntry e : (Iterable)v) {
        vector[e.index()] = e.value();
      }
    } else if ((v instanceof SparseDoubleVector)) {
      for (int i : ((SparseDoubleVector)v).getNonZeroIndices()) {
        vector[i] = v.get(i);
      }
    } else {
      for (int i = 0; i < v.length(); i++) {
        vector[i] = v.get(i);
      }
    }
  }
  

  public double add(int index, double delta)
  {
    magnitude -= vector[index] * vector[index];
    vector[index] += delta;
    magnitude += vector[index] * vector[index];
    return vector[index];
  }
  


  public void set(int index, double value)
  {
    magnitude -= vector[index] * vector[index];
    vector[index] = value;
    magnitude += vector[index] * vector[index];
  }
  


  public void set(int index, Number value)
  {
    set(index, value.doubleValue());
  }
  


  public double get(int index)
  {
    return vector[index];
  }
  


  public Double getValue(int index)
  {
    return Double.valueOf(get(index));
  }
  


  public double magnitude()
  {
    return Math.sqrt(magnitude);
  }
  


  public double[] toArray()
  {
    return Arrays.copyOf(vector, vector.length);
  }
  


  public int length()
  {
    return vector.length;
  }
}
