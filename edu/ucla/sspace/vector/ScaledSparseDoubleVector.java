package edu.ucla.sspace.vector;





















public class ScaledSparseDoubleVector
  extends ScaledDoubleVector
  implements SparseDoubleVector
{
  private SparseDoubleVector vector;
  




















  public ScaledSparseDoubleVector(SparseDoubleVector vector, double scale)
  {
    super(vector, scale);
    




    if ((vector instanceof ScaledSparseDoubleVector)) {
      ScaledSparseDoubleVector ssdv = (ScaledSparseDoubleVector)vector;
      this.vector = vector;
    } else {
      this.vector = vector;
    }
  }
  

  public int[] getNonZeroIndices()
  {
    return vector.getNonZeroIndices();
  }
  


  public SparseDoubleVector instanceCopy()
  {
    return vector.instanceCopy();
  }
}
