package edu.ucla.sspace.clustering.seeding;

import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.ScaledDoubleVector;
import edu.ucla.sspace.vector.VectorMath;
























































public class OrssSeed
  implements KMeansSeed
{
  private static final double EPSILON = 0.001D;
  
  public OrssSeed() {}
  
  public DoubleVector[] chooseSeeds(int numCentroids, Matrix dataPoints)
  {
    int numDataPoints = dataPoints.rows();
    DoubleVector[] centroids = new DoubleVector[numCentroids];
    



    DoubleVector singleCentroid = new DenseVector(dataPoints.columns());
    for (int i = 0; i < numDataPoints; i++)
      VectorMath.add(singleCentroid, dataPoints.getRowVector(i));
    singleCentroid = new ScaledDoubleVector(
      singleCentroid, 1.0D / numDataPoints);
    

    double optimalSingleCost = 0.0D;
    double[] distances = new double[numDataPoints];
    for (int i = 0; i < numDataPoints; i++) {
      distances[i] = Similarity.euclideanDistance(
        singleCentroid, dataPoints.getRowVector(i));
      optimalSingleCost += distances[i];
    }
    



    double probability = Math.random();
    double optimalDistance = 0.0D;
    for (int i = 0; i < numDataPoints; i++)
    {

      double distance = distances[i];
      
      double cost = optimalSingleCost + numDataPoints * distance * distance;
      cost /= 2 * numDataPoints * optimalSingleCost;
      



      probability -= cost;
      if (probability <= 0.001D) {
        centroids[0] = dataPoints.getRowVector(i);
        optimalDistance = distance;
        break;
      }
    }
    

    probability = Math.random();
    



    double normalFactor = 
      optimalSingleCost + numDataPoints * Math.pow(optimalDistance, 2.0D);
    



    double[] minDistances = new double[numDataPoints];
    boolean selected = false;
    for (int i = 0; i < numDataPoints; i++)
    {

      double distance = Similarity.euclideanDistance(
        centroids[0], dataPoints.getRowVector(i));
      minDistances[i] = distance;
      


      double cost = Math.pow(distance, 2.0D) / normalFactor;
      probability -= cost;
      if ((probability <= 0.001D) && (!selected)) {
        centroids[1] = dataPoints.getRowVector(i);
        selected = true;
      }
    }
    








    for (int c = 2; c < numCentroids; c++) {
      selected = false;
      probability = Math.random();
      for (int i = 0; i < numDataPoints; i++)
      {


        double distance = Similarity.euclideanDistance(
          centroids[(c - 1)], dataPoints.getRowVector(i));
        if (distance < minDistances[c]) {
          minDistances[i] = distance;
        }
        

        double cost = Math.pow(minDistances[i], 2.0D);
        probability -= cost;
        if ((probability <= 0.001D) && (!selected)) {
          centroids[c] = dataPoints.getRowVector(i);
          selected = true;
        }
      }
    }
    
    return centroids;
  }
}
