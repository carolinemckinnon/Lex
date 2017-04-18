package edu.ucla.sspace.clustering.criterion;

import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.DenseDynamicMagnitudeVector;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.VectorMath;
import java.util.ArrayList;
import java.util.List;
































































































public abstract class BaseFunction
  implements CriterionFunction
{
  protected List<DoubleVector> matrix;
  protected int[] assignments;
  protected DoubleVector[] centroids;
  protected int[] clusterSizes;
  protected double[] costs;
  
  public BaseFunction() {}
  
  BaseFunction(List<DoubleVector> matrix, DoubleVector[] centroids, double[] costs, int[] assignments, int[] clusterSizes)
  {
    this.matrix = matrix;
    this.centroids = centroids;
    this.costs = costs;
    this.assignments = assignments;
    this.clusterSizes = clusterSizes;
  }
  



  public void setup(Matrix m, int[] initialAssignments, int numClusters)
  {
    assignments = initialAssignments;
    matrix = new ArrayList(m.rows());
    for (int i = 0; i < m.rows(); i++) {
      matrix.add(m.getRowVector(i));
    }
    
    centroids = new DoubleVector[numClusters];
    clusterSizes = new int[numClusters];
    costs = new double[numClusters];
    

    for (int c = 0; c < numClusters; c++) {
      centroids[c] = new DenseVector(m.columns());
    }
    
    for (int i = 0; i < m.rows(); i++) {
      int assignment = initialAssignments[i];
      VectorMath.add(centroids[assignment], (DoubleVector)matrix.get(i));
      clusterSizes[assignment] += 1;
    }
    

    for (int c = 0; c < numClusters; c++) {
      centroids[c] = new DenseDynamicMagnitudeVector(
        centroids[c].toArray());
    }
    subSetup(m);
    
    SparseDoubleVector empty = new CompactSparseVector(m.columns());
    for (int c = 0; c < numClusters; c++) {
      if (clusterSizes[c] != 0) {
        costs[c] = getOldCentroidScore(empty, c, clusterSizes[c]);
      }
    }
  }
  



  protected void subSetup(Matrix m) {}
  


  public boolean update(int currentVectorIndex)
  {
    int currentClusterIndex = assignments[currentVectorIndex];
    
    double bestDelta = isMaximize() ? 0.0D : Double.MAX_VALUE;
    int bestDeltaIndex = -1;
    

    DoubleVector vector = (DoubleVector)matrix.get(currentVectorIndex);
    




    double deltaBase = clusterSizes[currentClusterIndex] == 1 ? 
      0.0D : 
      getOldCentroidScore(vector, currentClusterIndex, 
      clusterSizes[currentClusterIndex] - 1);
    deltaBase -= costs[currentClusterIndex];
    


    for (int i = 0; i < centroids.length; i++)
    {
      if (currentClusterIndex != i)
      {



        double newCost = getNewCentroidScore(i, vector);
        


        double delta = newCost - costs[i] + deltaBase;
        
        if (isMaximize())
        {


          if ((delta > 0.0D) && (delta > bestDelta)) {
            bestDelta = delta;
            bestDeltaIndex = i;
          }
          
        }
        else if (delta < bestDelta) {
          bestDelta = delta;
          bestDeltaIndex = i;
        }
      }
    }
    

    if (bestDeltaIndex >= 0)
    {
      double newDelta = bestDelta - deltaBase;
      costs[currentClusterIndex] += deltaBase;
      costs[bestDeltaIndex] += newDelta;
      updateScores(bestDeltaIndex, currentClusterIndex, vector);
      

      clusterSizes[currentClusterIndex] -= 1;
      clusterSizes[bestDeltaIndex] += 1;
      

      centroids[currentClusterIndex] = subtract(
        centroids[currentClusterIndex], vector);
      centroids[bestDeltaIndex] = VectorMath.add(
        centroids[bestDeltaIndex], vector);
      

      assignments[currentVectorIndex] = bestDeltaIndex;
      return true;
    }
    

    return false;
  }
  








  protected abstract double getOldCentroidScore(DoubleVector paramDoubleVector, int paramInt1, int paramInt2);
  







  protected abstract double getNewCentroidScore(int paramInt, DoubleVector paramDoubleVector);
  







  protected void updateScores(int newCentroidIndex, int oldCentroidIndex, DoubleVector vector) {}
  







  protected static DoubleVector subtract(DoubleVector c, DoubleVector v)
  {
    DoubleVector newCentroid = new DenseDynamicMagnitudeVector(c.length());
    



    if ((v instanceof SparseDoubleVector)) {
      SparseDoubleVector sv = (SparseDoubleVector)v;
      int[] nonZeros = sv.getNonZeroIndices();
      int sparseIndex = 0;
      for (int i = 0; i < c.length(); i++) {
        double value = c.get(i);
        if ((sparseIndex < nonZeros.length) && 
          (i == nonZeros[sparseIndex])) {
          value -= sv.get(nonZeros[(sparseIndex++)]);
        }
        newCentroid.set(i, value);
      }
    } else {
      for (int i = 0; i < c.length(); i++)
        newCentroid.set(i, c.get(i) - v.get(i)); }
    return newCentroid;
  }
  


  public int[] assignments()
  {
    return assignments;
  }
  


  public DoubleVector[] centroids()
  {
    return centroids;
  }
  


  public int[] clusterSizes()
  {
    return clusterSizes;
  }
  


  public double score()
  {
    double score = 0.0D;
    for (double cost : costs)
      score += cost;
    return score;
  }
  






  protected static double modifiedMagnitudeSqrd(DoubleVector c, DoubleVector v)
  {
    if ((v instanceof SparseDoubleVector)) {
      SparseDoubleVector sv = (SparseDoubleVector)v;
      int[] nonZeros = sv.getNonZeroIndices();
      
      double magnitude = Math.pow(c.magnitude(), 2.0D);
      for (int i : nonZeros) {
        double value = c.get(i);
        magnitude -= Math.pow(value, 2.0D);
        magnitude += Math.pow(value + v.get(i), 2.0D);
      }
      return magnitude;
    }
    double magnitude = 0.0D;
    for (int i = 0; i < c.length(); i++)
      magnitude += Math.pow(c.get(i) + v.get(i), 2.0D);
    return magnitude;
  }
  





  protected static double modifiedMagnitude(DoubleVector c, DoubleVector v)
  {
    return Math.sqrt(modifiedMagnitudeSqrd(c, v));
  }
  






  protected static double subtractedMagnitudeSqrd(DoubleVector c, DoubleVector v)
  {
    if ((v instanceof SparseDoubleVector)) {
      SparseDoubleVector sv = (SparseDoubleVector)v;
      int[] nonZeros = sv.getNonZeroIndices();
      
      double magnitude = Math.pow(c.magnitude(), 2.0D);
      for (int i : nonZeros) {
        double value = c.get(i);
        magnitude -= Math.pow(value, 2.0D);
        magnitude += Math.pow(value - v.get(i), 2.0D);
      }
      return magnitude;
    }
    double magnitude = 0.0D;
    for (int i = 0; i < c.length(); i++)
      magnitude += Math.pow(c.get(i) - v.get(i), 2.0D);
    return magnitude;
  }
  





  protected static double subtractedMagnitude(DoubleVector c, DoubleVector v)
  {
    return Math.sqrt(subtractedMagnitudeSqrd(c, v));
  }
}
