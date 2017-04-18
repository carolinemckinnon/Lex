package edu.ucla.sspace.svs;

import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.VectorMath;
import java.io.Serializable;

































public class PointWiseCombinor
  implements VectorCombinor, Serializable
{
  private static final long serialVersionUID = 1L;
  
  public PointWiseCombinor() {}
  
  public SparseDoubleVector combine(SparseDoubleVector v1, SparseDoubleVector v2)
  {
    return VectorMath.multiplyUnmodified(v1, v2);
  }
  



  public SparseDoubleVector combineUnmodified(SparseDoubleVector v1, SparseDoubleVector v2)
  {
    return VectorMath.multiplyUnmodified(v1, v2);
  }
}
