package edu.ucla.sspace.clustering.criterion;

import edu.ucla.sspace.vector.DoubleVector;
import java.util.List;





















































public class I1Function
  extends BaseFunction
{
  public I1Function() {}
  
  I1Function(List<DoubleVector> matrix, DoubleVector[] centroids, double[] costs, int[] assignments, int[] clusterSizes)
  {
    super(matrix, centroids, costs, assignments, clusterSizes);
  }
  




  protected double getOldCentroidScore(DoubleVector vector, int oldCentroidIndex, int altClusterSize)
  {
    return subtractedMagnitudeSqrd(centroids[oldCentroidIndex], vector) / 
      altClusterSize;
  }
  




  protected double getNewCentroidScore(int newCentroidIndex, DoubleVector dataPoint)
  {
    return modifiedMagnitudeSqrd(centroids[newCentroidIndex], dataPoint) / (
      clusterSizes[newCentroidIndex] + 1);
  }
  


  public boolean isMaximize()
  {
    return true;
  }
}
