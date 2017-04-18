package edu.ucla.sspace.clustering.seeding;

import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.vector.DoubleVector;

public abstract interface KMeansSeed
{
  public abstract DoubleVector[] chooseSeeds(int paramInt, Matrix paramMatrix);
}
