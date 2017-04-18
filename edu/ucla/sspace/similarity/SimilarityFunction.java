package edu.ucla.sspace.similarity;

import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.IntegerVector;
import edu.ucla.sspace.vector.Vector;

public abstract interface SimilarityFunction
{
  public abstract void setParams(double... paramVarArgs);
  
  public abstract boolean isSymmetric();
  
  public abstract double sim(DoubleVector paramDoubleVector1, DoubleVector paramDoubleVector2);
  
  public abstract double sim(IntegerVector paramIntegerVector1, IntegerVector paramIntegerVector2);
  
  public abstract double sim(Vector paramVector1, Vector paramVector2);
}
