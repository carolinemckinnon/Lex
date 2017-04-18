package edu.ucla.sspace.clustering.criterion;

import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.vector.DenseDynamicMagnitudeVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.VectorMath;
import java.util.List;





































































public class G1Function
  extends BaseFunction
{
  private DoubleVector completeCentroid;
  private double[] simToComplete;
  
  public G1Function() {}
  
  G1Function(List<DoubleVector> matrix, DoubleVector[] centroids, double[] costs, int[] assignments, int[] clusterSizes, DoubleVector completeCentroid, double[] simToComplete)
  {
    super(matrix, centroids, costs, assignments, clusterSizes);
    this.completeCentroid = completeCentroid;
    this.simToComplete = simToComplete;
  }
  


  protected void subSetup(Matrix m)
  {
    completeCentroid = new DenseDynamicMagnitudeVector(m.columns());
    for (DoubleVector v : matrix) {
      VectorMath.add(completeCentroid, v);
    }
    simToComplete = new double[centroids.length];
    for (int c = 0; c < centroids.length; c++) {
      simToComplete[c] = VectorMath.dotProduct(
        completeCentroid, centroids[c]);
    }
  }
  



  protected double getOldCentroidScore(DoubleVector vector, int oldCentroidIndex, int altClusterSize)
  {
    double newScore = simToComplete[oldCentroidIndex];
    newScore -= VectorMath.dotProduct(completeCentroid, vector);
    
    newScore = newScore / subtractedMagnitudeSqrd(centroids[oldCentroidIndex], vector);
    return newScore;
  }
  
  protected double getNewCentroidScore(int newCentroidIndex, DoubleVector dataPoint)
  {
    double newScore = simToComplete[newCentroidIndex];
    newScore += VectorMath.dotProduct(completeCentroid, dataPoint);
    
    newScore = newScore / modifiedMagnitudeSqrd(centroids[newCentroidIndex], dataPoint);
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
