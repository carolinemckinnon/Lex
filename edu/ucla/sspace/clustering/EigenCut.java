package edu.ucla.sspace.clustering;

import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.vector.DoubleVector;

public abstract interface EigenCut
{
  public abstract double rhoSum();
  
  public abstract DoubleVector computeRhoSum(Matrix paramMatrix);
  
  public abstract void computeCut(Matrix paramMatrix);
  
  public abstract int[] getLeftReordering();
  
  public abstract Matrix getLeftCut();
  
  public abstract Matrix getRightCut();
  
  public abstract int[] getRightReordering();
  
  public abstract double getKMeansObjective();
  
  public abstract double getKMeansObjective(double paramDouble1, double paramDouble2, int paramInt1, int[] paramArrayOfInt1, int paramInt2, int[] paramArrayOfInt2);
  
  public abstract double getSplitObjective(double paramDouble1, double paramDouble2, int paramInt1, int[] paramArrayOfInt1, int paramInt2, int[] paramArrayOfInt2);
  
  public abstract double getMergedObjective(double paramDouble1, double paramDouble2);
}
