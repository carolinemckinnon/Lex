package edu.ucla.sspace.hal;

import edu.ucla.sspace.vector.SparseDoubleVector;
import java.io.Serializable;





























class ConcatenatedSparseDoubleVector
  implements SparseDoubleVector, Serializable
{
  private static final long serialVersionUID = 1L;
  private final SparseDoubleVector v1;
  private final SparseDoubleVector v2;
  
  public ConcatenatedSparseDoubleVector(SparseDoubleVector v1, SparseDoubleVector v2)
  {
    this.v1 = v1;
    this.v2 = v2;
  }
  


  public double add(int index, double value)
  {
    return index < v1.length() ? 
      v1.add(index, index) : 
      v2.add(index - v1.length(), value);
  }
  


  public double get(int index)
  {
    return index < v1.length() ? 
      v1.get(index) : 
      v2.get(index - v1.length());
  }
  


  public int[] getNonZeroIndices()
  {
    int[] v1nz = v1.getNonZeroIndices();
    int[] v2nz = v2.getNonZeroIndices();
    int[] nz = new int[v1nz.length + v2nz.length];
    System.arraycopy(v1nz, 0, nz, 0, v1nz.length);
    
    int i = 0; for (int j = v1nz.length; i < v2nz.length; j++) {
      v2nz[i] += v1.length();i++; }
    return nz;
  }
  


  public Double getValue(int index)
  {
    return index < v1.length() ? 
      v1.getValue(index) : 
      v2.getValue(index - v1.length());
  }
  


  public int length()
  {
    return v1.length() + v2.length();
  }
  


  public double magnitude()
  {
    double m = 0.0D;
    for (int nz : v1.getNonZeroIndices()) {
      double d = v1.get(nz);
      m += d * d;
    }
    for (int nz : v2.getNonZeroIndices()) {
      double d = v2.get(nz);
      m += d * d;
    }
    return Math.sqrt(m);
  }
  


  public void set(int index, Number value)
  {
    if (index < v1.length()) {
      v1.set(index, index);
    } else {
      v2.set(index - v1.length(), value);
    }
  }
  

  public void set(int index, double value)
  {
    if (index < v1.length()) {
      v1.set(index, index);
    } else {
      v2.set(index - v1.length(), value);
    }
  }
  

  public double[] toArray()
  {
    double[] array = new double[length()];
    for (int nz : v1.getNonZeroIndices())
      array[nz] = v1.get(nz);
    for (int nz : v2.getNonZeroIndices())
      array[(nz + v1.length())] = v2.get(nz);
    return array;
  }
  


  public SparseDoubleVector instanceCopy()
  {
    throw new UnsupportedOperationException(
      "Cannot return a new instance of the decorated Double vectors");
  }
}
