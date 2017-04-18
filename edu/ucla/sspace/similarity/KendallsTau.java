package edu.ucla.sspace.similarity;

import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.IntegerVector;
import edu.ucla.sspace.vector.Vector;































public class KendallsTau
  extends AbstractSymmetricSimilarityFunction
{
  public KendallsTau() {}
  
  public double sim(DoubleVector v1, DoubleVector v2)
  {
    return Similarity.kendallsTau(v1, v2);
  }
  


  public double sim(IntegerVector v1, IntegerVector v2)
  {
    return Similarity.kendallsTau(v1, v2);
  }
  


  public double sim(Vector v1, Vector v2)
  {
    return Similarity.kendallsTau(v1, v2);
  }
}
