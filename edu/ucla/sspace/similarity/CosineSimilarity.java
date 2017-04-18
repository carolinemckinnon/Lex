package edu.ucla.sspace.similarity;

import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.IntegerVector;
import edu.ucla.sspace.vector.Vector;


































public class CosineSimilarity
  extends AbstractSymmetricSimilarityFunction
{
  public CosineSimilarity() {}
  
  public double sim(DoubleVector v1, DoubleVector v2)
  {
    return Similarity.cosineSimilarity(v1, v2);
  }
  


  public double sim(IntegerVector v1, IntegerVector v2)
  {
    return Similarity.cosineSimilarity(v1, v2);
  }
  


  public double sim(Vector v1, Vector v2)
  {
    return Similarity.cosineSimilarity(v1, v2);
  }
}
