package edu.ucla.sspace.clustering.criterion;

import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.DenseDynamicMagnitudeVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.VectorMath;
import java.util.ArrayList;
import java.util.List;





























































































public abstract class HybridBaseFunction
  implements CriterionFunction
{
  protected List<DoubleVector> matrix;
  protected int[] assignments;
  protected DoubleVector[] centroids;
  protected int[] clusterSizes;
  protected double[] e1Costs;
  private double e1Cost;
  protected double[] i1Costs;
  private double i1Cost;
  private double totalCost;
  protected DoubleVector completeCentroid;
  protected double[] simToComplete;
  private BaseFunction i1Func;
  private BaseFunction e1Func;
  
  public HybridBaseFunction() {}
  
  public void setup(Matrix m, int[] initialAssignments, int numClusters)
  {
    assignments = initialAssignments;
    matrix = new ArrayList(m.rows());
    for (int i = 0; i < m.rows(); i++) {
      matrix.add(m.getRowVector(i));
    }
    
    centroids = new DoubleVector[numClusters];
    clusterSizes = new int[numClusters];
    simToComplete = new double[numClusters];
    e1Costs = new double[numClusters];
    i1Costs = new double[numClusters];
    

    for (int c = 0; c < numClusters; c++) {
      centroids[c] = new DenseDynamicMagnitudeVector(m.columns());
    }
    int assignment;
    for (int i = 0; i < m.rows(); i++) {
      assignment = initialAssignments[i];
      VectorMath.add(centroids[assignment], (DoubleVector)matrix.get(i));
      clusterSizes[assignment] += 1;
    }
    

    completeCentroid = new DenseDynamicMagnitudeVector(m.columns());
    for (DoubleVector v : matrix) {
      VectorMath.add(completeCentroid, v);
    }
    
    for (int c = 0; c < centroids.length; c++) {
      simToComplete[c] = Similarity.cosineSimilarity(
        centroids[c], completeCentroid);
    }
    
    i1Func = getInternalFunction();
    e1Func = getExternalFunction();
    
    SparseDoubleVector empty = new CompactSparseVector(m.columns());
    
    for (int c = 0; c < numClusters; c++) {
      if (clusterSizes[c] != 0)
      {
        i1Costs[c] = i1Func.getOldCentroidScore(
          empty, c, clusterSizes[c]);
        i1Cost += i1Costs[c];
        

        e1Costs[c] = e1Func.getOldCentroidScore(
          empty, c, clusterSizes[c]);
        e1Cost += e1Costs[c];
      }
    }
    

    totalCost = (i1Cost / e1Cost);
  }
  


  public boolean update(int currentVectorIndex)
  {
    int currentClusterIndex = assignments[currentVectorIndex];
    

    double bestE1Delta = 0.0D;
    double bestI1Delta = 0.0D;
    

    double bestTotal = totalCost;
    int bestDeltaIndex = -1;
    

    DoubleVector vector = (DoubleVector)matrix.get(currentVectorIndex);
    


    double baseE1Delta = 0.0D;
    double baseI1Delta = 0.0D;
    


    if (clusterSizes[currentClusterIndex] > 1) {
      baseE1Delta = e1Func.getOldCentroidScore(
        vector, currentClusterIndex, 
        clusterSizes[currentClusterIndex] - 1);
      baseE1Delta -= e1Costs[currentClusterIndex];
      
      baseI1Delta = i1Func.getOldCentroidScore(
        vector, currentClusterIndex, 
        clusterSizes[currentClusterIndex] - 1);
      baseI1Delta -= i1Costs[currentClusterIndex];
    }
    


    for (int i = 0; i < centroids.length; i++)
    {
      if (currentClusterIndex != i)
      {





        double newE1Delta = e1Func.getNewCentroidScore(i, vector);
        newE1Delta -= e1Costs[i];
        
        double newI1Delta = i1Func.getNewCentroidScore(i, vector);
        newI1Delta -= i1Costs[i];
        


        double newI1Score = i1Cost + newI1Delta + baseI1Delta;
        double newE1Score = e1Cost + newE1Delta + baseE1Delta;
        double newScore = newI1Score / newE1Score;
        
        if (newScore > bestTotal) {
          bestTotal = newScore;
          bestE1Delta = newE1Delta;
          bestI1Delta = newI1Delta;
          bestDeltaIndex = i;
        }
      }
    }
    
    if (bestDeltaIndex >= 0)
    {
      e1Costs[currentClusterIndex] += baseE1Delta;
      i1Costs[currentClusterIndex] += baseI1Delta;
      
      e1Costs[bestDeltaIndex] += bestE1Delta;
      i1Costs[bestDeltaIndex] += bestI1Delta;
      
      e1Func.updateScores(bestDeltaIndex, currentClusterIndex, vector);
      i1Func.updateScores(bestDeltaIndex, currentClusterIndex, vector);
      
      e1Cost += baseE1Delta + bestE1Delta;
      i1Cost += baseI1Delta + bestI1Delta;
      totalCost = (i1Cost / e1Cost);
      

      clusterSizes[currentClusterIndex] -= 1;
      clusterSizes[bestDeltaIndex] += 1;
      

      centroids[currentClusterIndex] = BaseFunction.subtract(
        centroids[currentClusterIndex], vector);
      centroids[bestDeltaIndex] = VectorMath.add(
        centroids[bestDeltaIndex], vector);
      

      assignments[currentVectorIndex] = bestDeltaIndex;
      return true;
    }
    

    return false;
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
    return totalCost;
  }
  
  protected abstract BaseFunction getInternalFunction();
  
  protected abstract BaseFunction getExternalFunction();
}
