package edu.ucla.sspace.similarity;

import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.IntegerVector;
import edu.ucla.sspace.vector.Vector;
import edu.ucla.sspace.vector.VectorMath;

































public class DotProduct
  extends AbstractSymmetricSimilarityFunction
{
  public DotProduct() {}
  
  public double sim(DoubleVector v1, DoubleVector v2)
  {
    return VectorMath.dotProduct(v1, v2);
  }
  


  public double sim(IntegerVector v1, IntegerVector v2)
  {
    return VectorMath.dotProduct(v1, v2);
  }
  


  public double sim(Vector v1, Vector v2)
  {
    return VectorMath.dotProduct(v1, v2);
  }
}
