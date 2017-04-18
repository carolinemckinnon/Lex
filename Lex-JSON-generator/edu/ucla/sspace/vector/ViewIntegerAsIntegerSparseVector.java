package edu.ucla.sspace.vector;

import java.util.Arrays;











































class ViewIntegerAsIntegerSparseVector
  extends IntegerVectorView
  implements SparseIntegerVector
{
  private static final long serialVersionUID = 1L;
  private final SparseIntegerVector sparseVector;
  
  public <T extends SparseIntegerVector> ViewIntegerAsIntegerSparseVector(T v)
  {
    this(v, 0, v.length(), false);
  }
  








  public <T extends SparseIntegerVector> ViewIntegerAsIntegerSparseVector(T v, boolean isImmutable)
  {
    this(v, 0, v.length(), isImmutable);
  }
  










  public <T extends SparseIntegerVector> ViewIntegerAsIntegerSparseVector(T v, int offset, int length)
  {
    this(v, offset, length, false);
  }
  













  public <T extends SparseIntegerVector> ViewIntegerAsIntegerSparseVector(T v, int offset, int length, boolean isImmutable)
  {
    super(v, offset, length, isImmutable);
    sparseVector = v;
  }
  


  public int[] getNonZeroIndices()
  {
    if (vectorOffset == 0) {
      return sparseVector.getNonZeroIndices();
    }
    

    int[] full = sparseVector.getNonZeroIndices();
    Arrays.sort(full);
    int startIndex = 0;
    int endIndex = full.length;
    for (int i = 0; i < full.length; i++) {
      if (full[i] < vectorOffset) {
        startIndex++;
      } else if (full[i] > vectorOffset + vectorLength) {
        endIndex = i - 1;
        break;
      }
    }
    if (startIndex == endIndex)
      return new int[0];
    int[] range = new int[endIndex - startIndex];
    System.arraycopy(full, startIndex, range, 0, range.length);
    return range;
  }
}
