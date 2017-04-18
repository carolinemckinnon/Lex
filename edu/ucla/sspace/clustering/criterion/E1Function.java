package edu.ucla.sspace.clustering.criterion;

import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.vector.DenseDynamicMagnitudeVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.VectorMath;
import java.util.List;




































































public class E1Function
  extends BaseFunction
{
  private DoubleVector completeCentroid;
  private double[] simToComplete;
  
  public E1Function() {}
  
  E1Function(List<DoubleVector> matrix, DoubleVector[] centroids, double[] costs, int[] assignments, int[] clusterSizes, DoubleVector completeCentroid, double[] simToComplete)
  {
    super(matrix, centroids, costs, assignments, clusterSizes);
    this.completeCentroid = completeCentroid;
    this.simToComplete = simToComplete;
  }
  


  protected void subSetup(Matrix m)
  {
    completeCentroid = new DenseDynamicMagnitudeVector(m.rows());
    for (DoubleVector v : matrix) {
      VectorMath.add(completeCentroid, v);
    }
    simToComplete = new double[centroids.length];
    for (int c = 0; c < centroids.length; c++) {
      simToComplete[c] = VectorMath.dotProduct(
        centroids[c], completeCentroid);
    }
  }
  



  protected double getOldCentroidScore(DoubleVector vector, int oldCentroidIndex, int altClusterSize)
  {
    double newScore = simToComplete[oldCentroidIndex];
    newScore -= VectorMath.dotProduct(completeCentroid, vector);
    newScore /= subtractedMagnitude(centroids[oldCentroidIndex], vector);
    newScore *= altClusterSize;
    return newScore;
  }
  



  protected double getNewCentroidScore(int newCentroidIndex, DoubleVector dataPoint)
  {
    double newScore = VectorMath.dotProduct(completeCentroid, dataPoint);
    newScore += simToComplete[newCentroidIndex];
    newScore /= modifiedMagnitude(centroids[newCentroidIndex], dataPoint);
    newScore *= (clusterSizes[newCentroidIndex] + 1);
    return newScore;
  }
  


  public boolean isMaximize()
  {
    return false;
  }
  




  protected void updateScores(int newCentroidIndex, int oldCentroidIndex, DoubleVector vector)
  {
    simToComplete[newCentroidIndex] += VectorMath.dotProduct(
      completeCentroid, vector);
    simToComplete[oldCentroidIndex] -= VectorMath.dotProduct(
      completeCentroid, vector);
  }
}
