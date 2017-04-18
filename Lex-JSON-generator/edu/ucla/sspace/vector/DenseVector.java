package edu.ucla.sspace.vector;

import edu.ucla.sspace.util.DoubleEntry;
import java.io.Serializable;
import java.util.Arrays;












































public class DenseVector
  extends AbstractDoubleVector
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private double[] vector;
  private double magnitude;
  
  public DenseVector(int vectorLength)
  {
    vector = new double[vectorLength];
    magnitude = 0.0D;
  }
  






  public DenseVector(double[] vector)
  {
    this.vector = Arrays.copyOf(vector, vector.length);
    magnitude = -1.0D;
  }
  






  public DenseVector(DoubleVector v)
  {
    vector = new double[v.length()];
    magnitude = v.magnitude();
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
    magnitude = -1.0D;
    vector[index] += delta;
    return vector[index];
  }
  


  public double get(int index)
  {
    return vector[index];
  }
  


  public double magnitude()
  {
    if (magnitude < 0.0D) {
      double m = 0.0D;
      for (double d : vector)
        m += d * d;
      magnitude = Math.sqrt(m);
    }
    return magnitude;
  }
  


  public void set(int index, double value)
  {
    magnitude = -1.0D;
    vector[index] = value;
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
