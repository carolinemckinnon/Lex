package edu.ucla.sspace.similarity;

import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.IntegerVector;
import edu.ucla.sspace.vector.Vector;


































public class OneSimilarity
  extends AbstractSymmetricSimilarityFunction
{
  public OneSimilarity() {}
  
  public double sim(DoubleVector v1, DoubleVector v2)
  {
    return 1.0D;
  }
  


  public double sim(IntegerVector v1, IntegerVector v2)
  {
    return 1.0D;
  }
  


  public double sim(Vector v1, Vector v2)
  {
    return 1.0D;
  }
}
