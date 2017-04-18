package edu.ucla.sspace.clustering;

import edu.ucla.sspace.matrix.Matrix;
import java.util.Properties;

public abstract interface Clustering
{
  public abstract Assignments cluster(Matrix paramMatrix, Properties paramProperties);
  
  public abstract Assignments cluster(Matrix paramMatrix, int paramInt, Properties paramProperties);
}
