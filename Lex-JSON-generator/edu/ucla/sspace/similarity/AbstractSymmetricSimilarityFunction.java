package edu.ucla.sspace.similarity;













public abstract class AbstractSymmetricSimilarityFunction
  implements SimilarityFunction
{
  public AbstractSymmetricSimilarityFunction() {}
  












  public void setParams(double... arguments) {}
  











  public boolean isSymmetric()
  {
    return true;
  }
}
