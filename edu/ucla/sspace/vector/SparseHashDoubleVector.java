package edu.ucla.sspace.vector;

import gnu.trove.TDoubleCollection;
import gnu.trove.iterator.TDoubleIterator;
import gnu.trove.map.hash.TIntDoubleHashMap;
import java.io.Serializable;
import java.util.Arrays;













































public class SparseHashDoubleVector
  extends AbstractDoubleVector
  implements SparseDoubleVector, Serializable
{
  private static final long serialVersionUID = 1L;
  private final TIntDoubleHashMap vector;
  private int maxLength;
  private double magnitude;
  private int[] nonZeroIndices;
  
  public SparseHashDoubleVector()
  {
    this(Integer.MAX_VALUE);
  }
  




  public SparseHashDoubleVector(int length)
  {
    maxLength = length;
    vector = new TIntDoubleHashMap();
  }
  






  public SparseHashDoubleVector(double[] values)
  {
    maxLength = values.length;
    vector = new TIntDoubleHashMap();
    nonZeroIndices = null;
    magnitude = -1.0D;
    for (int i = 0; i < values.length; i++) {
      if (values[i] != 0.0D) {
        vector.put(i, values[i]);
      }
    }
  }
  




  public SparseHashDoubleVector(DoubleVector values)
  {
    maxLength = values.length();
    nonZeroIndices = null;
    magnitude = -1.0D;
    if ((values instanceof SparseHashDoubleVector)) {
      SparseHashDoubleVector v = (SparseHashDoubleVector)values;
      vector = new TIntDoubleHashMap(vector);
    }
    else if ((values instanceof SparseVector)) {
      int[] nonZeros = ((SparseVector)values).getNonZeroIndices();
      vector = new TIntDoubleHashMap(nonZeros.length);
      for (int index : nonZeros)
        vector.put(index, values.get(index));
    } else {
      vector = new TIntDoubleHashMap();
      for (int index = 0; index < values.length(); index++) {
        double value = values.get(index);
        if (value != 0.0D) {
          vector.put(index, value);
        }
      }
    }
  }
  

  public double add(int index, double delta)
  {
    double val = vector.get(index) + delta;
    if (val == 0.0D) {
      vector.remove(index);
    } else
      set(index, val);
    nonZeroIndices = null;
    magnitude = -1.0D;
    return val;
  }
  


  public double get(int index)
  {
    return vector.get(index);
  }
  


  public Double getValue(int index)
  {
    return Double.valueOf(get(index));
  }
  











  public void set(int index, Number value)
  {
    set(index, value.doubleValue());
  }
  


  public void set(int index, double value)
  {
    double old = vector.get(index);
    if (value == 0.0D) {
      vector.remove(index);
    } else
      vector.put(index, value);
    magnitude = -1.0D;
  }
  


  public double[] toArray()
  {
    double[] array = new double[length()];
    for (int i : vector.keys())
      array[i] = vector.get(i);
    return array;
  }
  


  public int length()
  {
    return maxLength;
  }
  


  public double magnitude()
  {
    if (magnitude < 0.0D) {
      magnitude = 0.0D;
      TDoubleIterator iter = vector.valueCollection().iterator();
      while (iter.hasNext()) {
        double d = iter.next();
        magnitude += d * d;
      }
      magnitude = Math.sqrt(magnitude);
    }
    return magnitude;
  }
  


  public int[] getNonZeroIndices()
  {
    if (nonZeroIndices == null) {
      nonZeroIndices = vector.keys();
      Arrays.sort(nonZeroIndices);
    }
    return nonZeroIndices;
  }
  


  public SparseDoubleVector instanceCopy()
  {
    return new SparseHashDoubleVector(maxLength);
  }
}
