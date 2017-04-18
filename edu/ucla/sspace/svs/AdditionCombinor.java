package edu.ucla.sspace.svs;

import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.VectorMath;
import java.io.Serializable;
































public class AdditionCombinor
  implements VectorCombinor, Serializable
{
  private static final long serialVersionUID = 1L;
  
  public AdditionCombinor() {}
  
  public SparseDoubleVector combine(SparseDoubleVector v1, SparseDoubleVector v2)
  {
    return (SparseDoubleVector)VectorMath.add(v1, v2);
  }
  



  public SparseDoubleVector combineUnmodified(SparseDoubleVector v1, SparseDoubleVector v2)
  {
    return (SparseDoubleVector)VectorMath.addUnmodified(v1, v2);
  }
}
