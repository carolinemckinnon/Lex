package edu.ucla.sspace.vector;

import java.io.Serializable;
import java.util.Arrays;























































public class TernaryVector
  implements SparseVector<Integer>, IntegerVector, Serializable
{
  private static final long serialVersionUID = 1L;
  protected int[] positiveDimensions;
  protected int[] negativeDimensions;
  private int length;
  
  public TernaryVector(int length, int[] positiveIndices, int[] negativeIndices)
  {
    this.length = length;
    positiveDimensions = positiveIndices;
    negativeDimensions = negativeIndices;
    
    Arrays.sort(positiveDimensions);
    Arrays.sort(negativeDimensions);
  }
  


  public int add(int index, int delta)
  {
    throw new UnsupportedOperationException(
      "TernaryVector instances cannot be modified");
  }
  


  public int get(int index)
  {
    if ((index < 0) || (index > length))
      throw new IndexOutOfBoundsException(
        "index not within vector: " + index);
    if (Arrays.binarySearch(positiveDimensions, index) >= 0)
      return 1;
    if (Arrays.binarySearch(negativeDimensions, index) >= 0)
      return -1;
    return 0;
  }
  



  public int[] getNonZeroIndices()
  {
    int[] nz = new int[negativeDimensions.length + 
      positiveDimensions.length];
    System.arraycopy(negativeDimensions, 0, nz, 
      0, negativeDimensions.length);
    System.arraycopy(positiveDimensions, 0, nz, 
      negativeDimensions.length, positiveDimensions.length);
    return nz;
  }
  


  public Integer getValue(int index)
  {
    return Integer.valueOf(get(index));
  }
  


  public int length()
  {
    return length;
  }
  






  public int[] negativeDimensions()
  {
    return negativeDimensions;
  }
  






  public int[] positiveDimensions()
  {
    return positiveDimensions;
  }
  


  public double magnitude()
  {
    return Math.sqrt(positiveDimensions.length + negativeDimensions.length);
  }
  


  public void set(int index, int value)
  {
    throw new UnsupportedOperationException(
      "TernaryVector instances cannot be modified");
  }
  


  public void set(int index, Number value)
  {
    throw new UnsupportedOperationException(
      "TernaryVector instances cannot be modified");
  }
  


  public int[] toArray()
  {
    int[] array = new int[length];
    for (int p : positiveDimensions)
      array[p] = 1;
    for (int n : negativeDimensions)
      array[n] = -1;
    return array;
  }
}
