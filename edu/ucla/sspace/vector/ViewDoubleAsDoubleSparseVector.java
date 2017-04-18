package edu.ucla.sspace.vector;

import edu.ucla.sspace.util.DoubleEntry;














































class ViewDoubleAsDoubleSparseVector
  extends DoubleVectorView
  implements SparseDoubleVector
{
  private static final long serialVersionUID = 1L;
  private final SparseVector sparseVector;
  
  public <T extends DoubleVector,  extends SparseVector<Double>> ViewDoubleAsDoubleSparseVector(T v)
  {
    this(v, 0, v.length(), false);
  }
  








  public <T extends DoubleVector,  extends SparseVector<Double>> ViewDoubleAsDoubleSparseVector(T v, boolean isImmutable)
  {
    this(v, 0, v.length(), isImmutable);
  }
  










  public <T extends DoubleVector,  extends SparseVector<Double>> ViewDoubleAsDoubleSparseVector(T v, int offset, int length)
  {
    this(v, offset, length, false);
  }
  













  public <T extends DoubleVector,  extends SparseVector<Double>> ViewDoubleAsDoubleSparseVector(T v, int offset, int length, boolean isImmutable)
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
  



  public SparseDoubleVector instanceCopy()
  {
    throw new UnsupportedOperationException(
      "Cannot return a new instance of the decorated Double vector");
  }
  




  public double magnitude()
  {
    if (magnitude < 0.0D) {
      double m = 0.0D;
      int idx;
      double d;
      if ((sparseVector instanceof Iterable)) {
        for (DoubleEntry e : (Iterable)sparseVector) {
          idx = e.index();
          if ((idx >= vectorOffset) && 
            (idx < vectorOffset + vectorLength)) {
            d = e.value();
            m += d * d;
          }
          
        }
      } else {
        for (int nz : sparseVector.getNonZeroIndices()) {
          if ((nz >= vectorOffset) && 
            (nz < vectorOffset + vectorLength)) {
            double d = doubleVector.get(nz);
            m += d * d;
          }
        }
      }
      magnitude = Math.sqrt(m);
    }
    return magnitude;
  }
}
