package edu.ucla.sspace.clustering.seeding;

import edu.ucla.sspace.common.Statistics;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import java.util.BitSet;































public class RandomSeed
  implements KMeansSeed
{
  public RandomSeed() {}
  
  public DoubleVector[] chooseSeeds(int numCentroids, Matrix dataPoints)
  {
    DoubleVector[] centers = new DoubleVector[numCentroids];
    


    if (numCentroids >= dataPoints.rows()) {
      for (int i = 0; i < dataPoints.rows(); i++) {
        centers[i] = dataPoints.getRowVector(i);
      }
      


      for (int i = dataPoints.rows(); i < numCentroids; i++) {
        centers[i] = new DenseVector(dataPoints.columns());
      }
      return centers;
    }
    

    BitSet selectedCentroids = Statistics.randomDistribution(
      numCentroids, dataPoints.rows());
    

    int c = 0; for (int i = selectedCentroids.nextSetBit(0); i >= 0; 
        i = selectedCentroids.nextSetBit(i + 1)) {
      centers[c] = dataPoints.getRowVector(i);c++;
    }
    return centers;
  }
}
