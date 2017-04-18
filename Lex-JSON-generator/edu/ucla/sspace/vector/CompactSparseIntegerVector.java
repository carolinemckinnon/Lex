package edu.ucla.sspace.vector;

import edu.ucla.sspace.util.IntegerEntry;
import edu.ucla.sspace.util.SparseIntArray;
import java.io.Serializable;
import java.util.Iterator;











































public class CompactSparseIntegerVector
  extends AbstractIntegerVector
  implements SparseIntegerVector, Serializable, Iterable<IntegerEntry>
{
  private static final long serialVersionUID = 1L;
  private final SparseIntArray intArray;
  private double magnitude;
  
  public CompactSparseIntegerVector(int length)
  {
    intArray = new SparseIntArray(length);
    magnitude = 0.0D;
  }
  






  public CompactSparseIntegerVector(IntegerVector v)
  {
    intArray = new SparseIntArray(v.length());
    if ((v instanceof SparseVector)) {
      SparseVector sv = (SparseVector)v;
      for (int i : sv.getNonZeroIndices()) {
        intArray.set(i, Integer.valueOf(v.get(i)));
      }
    } else {
      for (int i = 0; i < v.length(); i++)
        intArray.set(i, Integer.valueOf(v.get(i)));
    }
    magnitude = -1.0D;
  }
  






  public CompactSparseIntegerVector(int[] values)
  {
    intArray = new SparseIntArray(values);
    magnitude = -1.0D;
  }
  


  public int add(int index, int delta)
  {
    magnitude = -1.0D;
    return intArray.addPrimitive(index, delta);
  }
  


  public int get(int index)
  {
    return intArray.getPrimitive(index);
  }
  


  public int[] getNonZeroIndices()
  {
    return intArray.getElementIndices();
  }
  



  public Iterator<IntegerEntry> iterator()
  {
    return intArray.iterator();
  }
  


  public int length()
  {
    return intArray.length();
  }
  



  public double magnitude()
  {
    if (magnitude < 0.0D) {
      double m = 0.0D;
      for (int nz : getNonZeroIndices()) {
        int i = intArray.get(nz).intValue();
        m += i * i;
      }
      magnitude = Math.sqrt(m);
    }
    return magnitude;
  }
  


  public void set(int index, int value)
  {
    intArray.set(index, Integer.valueOf(value));
    magnitude = -1.0D;
  }
}
