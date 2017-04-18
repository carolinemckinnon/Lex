package edu.ucla.sspace.similarity;

import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.IntegerVector;
import edu.ucla.sspace.vector.Vector;
import edu.ucla.sspace.vector.VectorMath;







































public class PolynomialKernel
  extends AbstractSymmetricSimilarityFunction
{
  private double degree;
  
  public PolynomialKernel() {}
  
  public void setParams(double... params)
  {
    degree = params[0];
  }
  


  public double sim(DoubleVector v1, DoubleVector v2)
  {
    double dotProduct = VectorMath.dotProduct(v1, v2);
    return Math.pow(dotProduct + 1.0D, degree);
  }
  


  public double sim(IntegerVector v1, IntegerVector v2)
  {
    double dotProduct = VectorMath.dotProduct(v1, v2);
    return Math.pow(dotProduct + 1.0D, degree);
  }
  


  public double sim(Vector v1, Vector v2)
  {
    double dotProduct = VectorMath.dotProduct(v1, v2);
    return Math.pow(dotProduct + 1.0D, degree);
  }
}
