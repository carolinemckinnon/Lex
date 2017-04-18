package edu.ucla.sspace.vector;

import edu.ucla.sspace.util.IntegerEntry;













































class SparseIntegerVectorView
  extends IntegerVectorView
  implements SparseIntegerVector
{
  private static final long serialVersionUID = 1L;
  private final SparseVector sparseVector;
  
  public <T extends IntegerVector,  extends SparseVector<Integer>> SparseIntegerVectorView(T v)
  {
    this(v, 0, v.length(), false);
  }
  








  public <T extends IntegerVector,  extends SparseVector<Integer>> SparseIntegerVectorView(T v, boolean isImmutable)
  {
    this(v, 0, v.length(), isImmutable);
  }
  










  public <T extends IntegerVector,  extends SparseVector<Integer>> SparseIntegerVectorView(T v, int offset, int length)
  {
    this(v, offset, length, false);
  }
  













  public <T extends IntegerVector,  extends SparseVector<Integer>> SparseIntegerVectorView(T v, int offset, int length, boolean isImmutable)
  {
    super(v, offset, length, isImmutable);
    sparseVector = ((SparseVector)v);
  }
  


  public int[] getNonZeroIndices()
  {
    if ((vectorOffset == 0) && (vectorLength == sparseVector.length())) {
      return sparseVector.getNonZeroIndices();
    }
    

    int inRange = 0;
    int[] indices = sparseVector.getNonZeroIndices();
    for (int nz : indices) {
      if ((nz >= vectorOffset) && (nz < vectorOffset + vectorLength))
        inRange++;
    }
    int[] arr = new int[inRange];
    int idx = 0;
    for (int nz : indices) {
      if ((nz >= vectorOffset) && (nz < vectorOffset + vectorLength))
        arr[(idx++)] = nz;
    }
    return arr;
  }
  





  public double magnitude()
  {
    if (magnitude < 0.0D) {
      double m = 0.0D;
      int idx;
      int i;
      if ((sparseVector instanceof Iterable)) {
        for (IntegerEntry e : (Iterable)sparseVector) {
          idx = e.index();
          if ((idx >= vectorOffset) && 
            (idx < vectorOffset + vectorLength)) {
            i = e.value();
            m += i * i;
          }
          
        }
      } else {
        for (int nz : sparseVector.getNonZeroIndices()) {
          if ((nz >= vectorOffset) && 
            (nz < vectorOffset + vectorLength)) {
            int j = intVector.get(nz);
            m += j * j;
          }
        }
      }
      magnitude = Math.sqrt(m);
    }
    return magnitude;
  }
}
