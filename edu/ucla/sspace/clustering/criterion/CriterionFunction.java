package edu.ucla.sspace.clustering.criterion;

import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.vector.DoubleVector;

public abstract interface CriterionFunction
{
  public abstract void setup(Matrix paramMatrix, int[] paramArrayOfInt, int paramInt);
  
  public abstract boolean update(int paramInt);
  
  public abstract int[] assignments();
  
  public abstract DoubleVector[] centroids();
  
  public abstract int[] clusterSizes();
  
  public abstract double score();
  
  public abstract boolean isMaximize();
}
