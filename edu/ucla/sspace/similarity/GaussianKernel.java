package edu.ucla.sspace.similarity;

import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.IntegerVector;
import edu.ucla.sspace.vector.Vector;








































public class GaussianKernel
  extends AbstractSymmetricSimilarityFunction
{
  private double gaussianKernelParam;
  
  public GaussianKernel() {}
  
  public void setParams(double... params)
  {
    gaussianKernelParam = params[0];
  }
  


  public double sim(DoubleVector v1, DoubleVector v2)
  {
    double dist = Similarity.euclideanDistance(v1, v2);
    return Math.pow(2.718281828459045D, -(dist / gaussianKernelParam));
  }
  


  public double sim(IntegerVector v1, IntegerVector v2)
  {
    double dist = Similarity.euclideanDistance(v1, v2);
    return Math.pow(2.718281828459045D, -(dist / gaussianKernelParam));
  }
  


  public double sim(Vector v1, Vector v2)
  {
    double dist = Similarity.euclideanDistance(v1, v2);
    return Math.pow(2.718281828459045D, -(dist / gaussianKernelParam));
  }
}
