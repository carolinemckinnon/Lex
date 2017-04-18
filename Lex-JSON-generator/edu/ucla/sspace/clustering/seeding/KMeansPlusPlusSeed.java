package edu.ucla.sspace.clustering.seeding;

import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.vector.DoubleVector;













































public class KMeansPlusPlusSeed
  implements KMeansSeed
{
  private static final double EPSILON = 0.001D;
  
  public KMeansPlusPlusSeed() {}
  
  public DoubleVector[] chooseSeeds(int numCentroids, Matrix dataPoints)
  {
    int[] centroids = new int[numCentroids];
    
    DoubleVector[] centers = new DoubleVector[numCentroids];
    int centroidIndex = (int)Math.round(
      Math.random() * (dataPoints.rows() - 1));
    centers[0] = dataPoints.getRowVector(centroidIndex);
    

    double[] distances = new double[dataPoints.rows()];
    computeDistances(distances, false, dataPoints, centers[0]);
    





    for (int i = 1; i < numCentroids; i++) {
      double sum = distanceSum(distances);
      double probability = Math.random();
      centroidIndex = chooseWithProbability(distances, sum, probability);
      centers[i] = dataPoints.getRowVector(centroidIndex);
      computeDistances(distances, true, dataPoints, centers[i]);
    }
    
    return centers;
  }
  


  private static double distanceSum(double[] distances)
  {
    double sum = 0.0D;
    double[] arrayOfDouble = distances;int j = distances.length; for (int i = 0; i < j; i++) { double distance = arrayOfDouble[i];
      sum += Math.pow(distance, 2.0D); }
    return sum;
  }
  















  private static void computeDistances(double[] distances, boolean selectMin, Matrix dataPoints, DoubleVector centroid)
  {
    for (int i = 0; i < distances.length; i++) {
      double distance = Similarity.euclideanDistance(
        centroid, dataPoints.getRowVector(i));
      if ((!selectMin) || ((selectMin) && (distance < distances[i]))) {
        distances[i] = distance;
      }
    }
  }
  




  private static int chooseWithProbability(double[] distances, double sum, double probability)
  {
    for (int j = 0; j < distances.length; j++) {
      double probOfDistance = Math.pow(distances[j], 2.0D) / sum;
      probability -= probOfDistance;
      if (probability <= 0.001D) {
        return j;
      }
    }
    return distances.length - 1;
  }
}
