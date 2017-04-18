package edu.ucla.sspace.clustering;

import edu.ucla.sspace.matrix.Matrices;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.SparseMatrix;
import edu.ucla.sspace.util.Generator;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.ScaledDoubleVector;
import edu.ucla.sspace.vector.ScaledSparseDoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;





































































public class SpectralClustering
{
  private static final Logger LOGGER = Logger.getLogger(SpectralClustering.class.getName());
  





  private final double alpha;
  





  private final double beta;
  





  private final Generator<EigenCut> cutterGenerator;
  





  public SpectralClustering(double alpha, Generator<EigenCut> cutterGenerator)
  {
    this.alpha = alpha;
    beta = (1.0D - alpha);
    this.cutterGenerator = cutterGenerator;
  }
  






  public Assignments cluster(Matrix matrix)
  {
    ClusterResult r = fullCluster(scaleMatrix(matrix), 0);
    verbose("Created " + numClusters + " clusters");
    
    Assignment[] assignments = new HardAssignment[assignments.length];
    for (int i = 0; i < assignments.length; i++) {
      assignments[i] = new HardAssignment(assignments[i]);
    }
    return new Assignments(numClusters, assignments, matrix);
  }
  








  public Assignments cluster(Matrix matrix, int maxClusters, boolean useKMeans)
  {
    LimitedResult[] results = limitedCluster(
      scaleMatrix(matrix), maxClusters, useKMeans);
    


    LimitedResult r = results.length == 1 ? 
      results[0] : 
      results[(maxClusters - 1)];
    


    for (int i = maxClusters - 2; (r == null) && (i >= 0); i--) {
      r = results[i];
    }
    
    verbose("Created " + numClusters + " clusters");
    Assignment[] assignments = new HardAssignment[assignments.length];
    for (int i = 0; i < assignments.length; i++) {
      assignments[i] = new HardAssignment(assignments[i]);
    }
    return new Assignments(numClusters, assignments, matrix);
  }
  








  private Matrix scaleMatrix(Matrix matrix)
  {
    if ((matrix instanceof SparseMatrix)) {
      List<SparseDoubleVector> scaledVectors = 
        new ArrayList(matrix.rows());
      SparseMatrix sm = (SparseMatrix)matrix;
      for (int r = 0; r < matrix.rows(); r++) {
        SparseDoubleVector v = sm.getRowVector(r);
        scaledVectors.add(new ScaledSparseDoubleVector(
          v, 1.0D / v.magnitude()));
      }
      return Matrices.asSparseMatrix(scaledVectors);
    }
    List<DoubleVector> scaledVectors = 
      new ArrayList(matrix.rows());
    for (int r = 0; r < matrix.rows(); r++) {
      DoubleVector v = matrix.getRowVector(r);
      scaledVectors.add(new ScaledDoubleVector(v, 1.0D / v.magnitude()));
    }
    return Matrices.asMatrix(scaledVectors);
  }
  






  private ClusterResult fullCluster(Matrix matrix, int depth)
  {
    verbose("Clustering at depth " + depth);
    



    if (matrix.rows() <= 1) {
      return new ClusterResult(new int[matrix.rows()], 1);
    }
    

    EigenCut eigenCutter = (EigenCut)cutterGenerator.generate();
    eigenCutter.computeCut(matrix);
    
    Matrix leftMatrix = eigenCutter.getLeftCut();
    Matrix rightMatrix = eigenCutter.getRightCut();
    
    verbose(String.format("Splitting into two matricies %d-%d", new Object[] {
      Integer.valueOf(leftMatrix.rows()), Integer.valueOf(rightMatrix.rows()) }));
    


    if ((leftMatrix.rows() == matrix.rows()) || 
      (rightMatrix.rows() == matrix.rows())) {
      return new ClusterResult(new int[matrix.rows()], 1);
    }
    




    ClusterResult[] results = new ClusterResult[2];
    results[0] = fullCluster(leftMatrix, depth + 1);
    results[1] = fullCluster(rightMatrix, depth + 1);
    
    ClusterResult leftResult = results[0];
    ClusterResult rightResult = results[1];
    
    verbose("Merging at depth " + depth);
    


    double splitObjective = eigenCutter.getSplitObjective(
      alpha, beta, 
      numClusters, assignments, 
      numClusters, assignments);
    

    double mergedObjective = eigenCutter.getMergedObjective(alpha, beta);
    


    int[] assignments = new int[matrix.rows()];
    int numClusters = 1;
    if (mergedObjective < splitObjective) {
      verbose("Selecting to combine sub trees at depth " + depth);
      Arrays.fill(assignments, 0);
    } else {
      verbose("Selecting to maintain sub trees at depth " + depth);
      



      numClusters = numClusters + numClusters;
      
      int[] leftReordering = eigenCutter.getLeftReordering();
      int[] rightReordering = eigenCutter.getRightReordering();
      
      for (int index = 0; index < leftReordering.length; index++)
        assignments[leftReordering[index]] = 
          assignments[index];
      for (int index = 0; index < rightReordering.length; index++)
        assignments[rightReordering[index]] = 
          (assignments[index] + numClusters);
    }
    return new ClusterResult(assignments, numClusters);
  }
  




  private LimitedResult[] limitedCluster(Matrix matrix, int maxClusters)
  {
    return limitedCluster(matrix, maxClusters, false);
  }
  







  private LimitedResult[] limitedCluster(Matrix matrix, int maxClusters, boolean useKMeans)
  {
    verbose("Clustering for " + maxClusters + " clusters.");
    



    EigenCut eigenCutter = (EigenCut)cutterGenerator.generate();
    



    if ((matrix.rows() <= 1) || (maxClusters <= 1)) {
      eigenCutter.computeRhoSum(matrix);
      LimitedResult result;
      LimitedResult result; if (useKMeans) {
        result = new KMeansLimitedResult(new int[matrix.rows()], 1, 
          eigenCutter.getKMeansObjective());

      }
      else
      {
        result = new SpectralLimitedResult(new int[matrix.rows()], 1, 
          eigenCutter.getMergedObjective(alpha, beta), 
          eigenCutter.rhoSum() - matrix.rows() / 2.0D, 
          matrix.rows() * (matrix.rows() - 1) / 2); }
      return new LimitedResult[] { result };
    }
    


    eigenCutter.computeCut(matrix);
    
    Matrix leftMatrix = eigenCutter.getLeftCut();
    Matrix rightMatrix = eigenCutter.getRightCut();
    


    if ((leftMatrix.rows() == matrix.rows()) || 
      (rightMatrix.rows() == matrix.rows())) {
      eigenCutter.computeRhoSum(matrix);
      LimitedResult result;
      LimitedResult result; if (useKMeans) {
        result = new KMeansLimitedResult(new int[matrix.rows()], 1, 
          eigenCutter.getKMeansObjective());

      }
      else
      {
        result = new SpectralLimitedResult(new int[matrix.rows()], 1, 
          eigenCutter.getMergedObjective(alpha, beta), 
          eigenCutter.rhoSum() - matrix.rows() / 2.0D, 
          matrix.rows() * (matrix.rows() - 1) / 2); }
      return new LimitedResult[] { result };
    }
    
    verbose(String.format("Splitting into two matricies %d-%d", new Object[] {
      Integer.valueOf(leftMatrix.rows()), Integer.valueOf(rightMatrix.rows()) }));
    




    LimitedResult[][] subResults = new LimitedResult[2][];
    subResults[0] = limitedCluster(leftMatrix, maxClusters - 1, useKMeans);
    subResults[1] = limitedCluster(rightMatrix, maxClusters - 1, useKMeans);
    
    LimitedResult[] leftResults = subResults[0];
    LimitedResult[] rightResults = subResults[1];
    
    verbose("Merging at for: " + maxClusters + " clusters");
    


    int[] leftReordering = eigenCutter.getLeftReordering();
    int[] rightReordering = eigenCutter.getRightReordering();
    
    LimitedResult[] results = new LimitedResult[maxClusters];
    if (useKMeans) {
      results[0] = new KMeansLimitedResult(new int[matrix.rows()], 1, 
        eigenCutter.getKMeansObjective());

    }
    else
    {
      results[0] = new SpectralLimitedResult(new int[matrix.rows()], 1, 
        eigenCutter.getMergedObjective(alpha, beta), 
        eigenCutter.rhoSum() - matrix.rows() / 2.0D, 
        matrix.rows() * (matrix.rows() - 1) / 2);
    }
    



    for (int i = 0; i < leftResults.length; i++) {
      LimitedResult leftResult = leftResults[i];
      


      if (leftResult != null)
      {

        for (int j = 0; j < rightResults.length; j++) {
          LimitedResult rightResult = rightResults[j];
          


          if (rightResult != null)
          {



            int numClusters = 
              numClusters + numClusters - 1;
            if (numClusters < results.length)
            {
              LimitedResult newResult;
              
              LimitedResult newResult;
              
              if (useKMeans) {
                newResult = leftResult.combine(leftResult, rightResult, 
                  leftReordering, rightReordering, 0.0D);
              } else {
                newResult = leftResult.combine(
                  leftResult, rightResult, 
                  leftReordering, rightReordering, 
                  eigenCutter.rhoSum());
              }
              



              if ((results[numClusters] == null) || 
                (results[numClusters].compareTo(newResult) < 0.0D))
                results[numClusters] = newResult;
            }
          }
        } } }
    return results;
  }
  




  private void verbose(String out)
  {
    LOGGER.info(out);
  }
  





  private abstract class LimitedResult
  {
    public int[] assignments;
    




    public int numClusters;
    




    public LimitedResult(int[] assignments, int numClusters)
    {
      this.assignments = assignments;
      this.numClusters = numClusters;
    }
    





    int[] combineAssignments(LimitedResult res1, LimitedResult res2, int[] ordering1, int[] ordering2)
    {
      int[] newAssignments = new int[
        assignments.length + assignments.length];
      
      for (int k = 0; k < ordering1.length; k++)
        newAssignments[ordering1[k]] = assignments[k];
      for (int k = 0; k < ordering2.length; k++)
        newAssignments[ordering2[k]] = 
          (assignments[k] + numClusters);
      return newAssignments;
    }
    





    abstract double score();
    





    abstract double compareTo(LimitedResult paramLimitedResult);
    





    abstract LimitedResult combine(LimitedResult paramLimitedResult1, LimitedResult paramLimitedResult2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, double paramDouble);
  }
  




  private class KMeansLimitedResult
    extends SpectralClustering.LimitedResult
  {
    public double score;
    




    public KMeansLimitedResult(int[] assignments, int numClusters, double score)
    {
      super(assignments, numClusters);
      this.score = score;
    }
    





    SpectralClustering.LimitedResult combine(SpectralClustering.LimitedResult res1, SpectralClustering.LimitedResult res2, int[] ordering1, int[] ordering2, double extra)
    {
      KMeansLimitedResult kres1 = (KMeansLimitedResult)res1;
      KMeansLimitedResult kres2 = (KMeansLimitedResult)res2;
      
      int[] newAssignments = combineAssignments(
        res1, res2, ordering1, ordering2);
      int newNumClusters = numClusters + numClusters;
      double newScore = score + score;
      return new KMeansLimitedResult(SpectralClustering.this, 
        newAssignments, newNumClusters, newScore);
    }
    


    public double score()
    {
      return score;
    }
    


    public double compareTo(SpectralClustering.LimitedResult other)
    {
      return score() - other.score();
    }
  }
  





  private class SpectralLimitedResult
    extends SpectralClustering.LimitedResult
  {
    public double totalScore;
    




    public double rawInterScore;
    



    public int interCount;
    




    public SpectralLimitedResult(int[] assignments, int numClusters, double totalScore, double rawInterScore, int interCount)
    {
      super(assignments, numClusters);
      
      this.totalScore = totalScore;
      this.rawInterScore = rawInterScore;
      this.interCount = interCount;
    }
    


    public double score()
    {
      return totalScore;
    }
    


    public double compareTo(SpectralClustering.LimitedResult other)
    {
      return other.score() - score();
    }
    





    SpectralClustering.LimitedResult combine(SpectralClustering.LimitedResult res1, SpectralClustering.LimitedResult res2, int[] ordering1, int[] ordering2, double rhoSum)
    {
      SpectralLimitedResult sres1 = (SpectralLimitedResult)res1;
      SpectralLimitedResult sres2 = (SpectralLimitedResult)res2;
      

      int[] newAssignments = combineAssignments(
        res1, res2, ordering1, ordering2);
      int newNumClusters = numClusters + numClusters;
      



      double newInterScore = rawInterScore + rawInterScore;
      int newCount = interCount + interCount;
      




      double intraClusterScore = 
        (rhoSum - newAssignments.length) / 2.0D - newInterScore;
      newInterScore = newCount - newInterScore;
      double newScore = alpha * newInterScore + beta * intraClusterScore;
      
      return new SpectralLimitedResult(SpectralClustering.this, newAssignments, newNumClusters, 
        newScore, newInterScore, newCount);
    }
  }
  

  private class ClusterResult
  {
    public int[] assignments;
    
    public int numClusters;
    

    public ClusterResult(int[] assignments, int numClusters)
    {
      this.assignments = assignments;
      this.numClusters = numClusters;
    }
  }
}
