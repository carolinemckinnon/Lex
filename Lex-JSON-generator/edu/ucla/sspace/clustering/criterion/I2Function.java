package edu.ucla.sspace.clustering.criterion;

import edu.ucla.sspace.vector.DoubleVector;
import java.util.List;





















































public class I2Function
  extends BaseFunction
{
  public I2Function() {}
  
  I2Function(List<DoubleVector> matrix, DoubleVector[] centroids, double[] costs, int[] assignments, int[] clusterSizes)
  {
    super(matrix, centroids, costs, assignments, clusterSizes);
  }
  




  protected double getOldCentroidScore(DoubleVector vector, int oldCentroidIndex, int altClusterSize)
  {
    return subtractedMagnitude(centroids[oldCentroidIndex], vector);
  }
  




  protected double getNewCentroidScore(int newCentroidIndex, DoubleVector dataPoint)
  {
    return modifiedMagnitude(centroids[newCentroidIndex], dataPoint);
  }
  


  public boolean isMaximize()
  {
    return true;
  }
}
