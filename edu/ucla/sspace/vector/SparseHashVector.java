package edu.ucla.sspace.vector;

import edu.ucla.sspace.util.SparseHashArray;
import java.io.Serializable;
















































public class SparseHashVector<T extends Number>
  extends AbstractVector<T>
  implements SparseVector<T>, Serializable
{
  private static final long serialVersionUID = 1L;
  protected SparseHashArray<Number> vector;
  protected double magnitude;
  
  public SparseHashVector(int length)
  {
    vector = new SparseHashArray(length);
    magnitude = -1.0D;
  }
  





  public SparseHashVector(T[] array)
  {
    vector = new SparseHashArray(array);
    magnitude = -1.0D;
  }
  


  public boolean equals(Object o)
  {
    if ((o instanceof DoubleVector)) {
      DoubleVector v = (DoubleVector)o;
      int len = v.length();
      if (len != length())
        return false;
      for (int i = 0; i < len; i++) {
        if (v.get(i) != ((Number)vector.get(i)).doubleValue())
          return false;
      }
      return true;
    }
    if ((o instanceof Vector)) {
      DoubleVector v = Vectors.asDouble((Vector)o);
      int len = v.length();
      if (len != length())
        return false;
      for (int i = 0; i < len; i++) {
        if (v.get(i) != ((Number)vector.get(i)).doubleValue())
          return false;
      }
      return true;
    }
    return false;
  }
  


  public int[] getNonZeroIndices()
  {
    return vector.getElementIndices();
  }
  


  public Number getValue(int index)
  {
    Number val = (Number)vector.get(index);
    return val == null ? Integer.valueOf(0) : val;
  }
  


  public int length()
  {
    return vector.length();
  }
  



  public double magnitude()
  {
    if (magnitude < 0.0D) {
      magnitude = 0.0D;
      for (int nz : getNonZeroIndices()) {
        double d = ((Number)vector.get(nz)).doubleValue();
        magnitude += d * d;
      }
      magnitude = Math.sqrt(magnitude);
    }
    return magnitude;
  }
  


  public void set(int index, Number value)
  {
    vector.set(index, value.doubleValue() == 0.0D ? null : value);
    magnitude = -1.0D;
  }
}
