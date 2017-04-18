package edu.ucla.sspace.vector;

import edu.ucla.sspace.util.DoubleEntry;
import edu.ucla.sspace.util.SparseDoubleArray;
import java.io.Serializable;
import java.util.Iterator;














































public class CompactSparseVector
  extends AbstractDoubleVector
  implements SparseDoubleVector, Serializable, Iterable<DoubleEntry>
{
  private static final long serialVersionUID = 1L;
  private SparseDoubleArray vector;
  private double magnitude;
  
  public CompactSparseVector()
  {
    vector = new SparseDoubleArray();
    magnitude = 0.0D;
  }
  





  public CompactSparseVector(int length)
  {
    vector = new SparseDoubleArray(length);
    magnitude = 0.0D;
  }
  





  public CompactSparseVector(double[] array)
  {
    vector = new SparseDoubleArray(array);
    magnitude = -1.0D;
  }
  





  public CompactSparseVector(SparseDoubleVector v)
  {
    int length = v.length();
    int[] nz = v.getNonZeroIndices();
    double[] values = new double[nz.length];
    for (int i = 0; i < nz.length; i++)
      values[i] = v.get(nz[i]);
    vector = new SparseDoubleArray(nz, values, length);
    magnitude = -1.0D;
  }
  













  public CompactSparseVector(int[] nonZeroIndices, double[] values, int length)
  {
    vector = new SparseDoubleArray(nonZeroIndices, values, length);
    magnitude = -1.0D;
  }
  


  public double add(int index, double delta)
  {
    magnitude = -1.0D;
    return vector.addPrimitive(index, delta);
  }
  



  public Iterator<DoubleEntry> iterator()
  {
    return vector.iterator();
  }
  



  public double get(int index)
  {
    return vector.getPrimitive(index);
  }
  


  public int[] getNonZeroIndices()
  {
    return vector.getElementIndices();
  }
  



  public int length()
  {
    return vector.length();
  }
  



  public double magnitude()
  {
    if (magnitude < 0.0D) {
      double m = 0.0D;
      for (DoubleEntry e : this)
        m += e.value() * e.value();
      magnitude = Math.sqrt(m);
    }
    return magnitude;
  }
  


  public void set(double[] values)
  {
    vector = new SparseDoubleArray(values);
    magnitude = -1.0D;
  }
  


  public void set(int index, double value)
  {
    vector.setPrimitive(index, value);
    magnitude = -1.0D;
  }
  


  public double[] toArray()
  {
    double[] array = new double[vector.length()];
    return vector.toPrimitiveArray(array);
  }
  


  public SparseDoubleVector instanceCopy()
  {
    return new CompactSparseVector(length());
  }
}
