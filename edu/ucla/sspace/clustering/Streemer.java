package edu.ucla.sspace.clustering;

import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.similarity.SimilarityFunction;
import edu.ucla.sspace.util.primitive.IntIterator;
import edu.ucla.sspace.util.primitive.IntSet;
import edu.ucla.sspace.vector.DoubleVector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;






















































public class Streemer
  implements Clustering
{
  public Streemer() {}
  
  public Assignments cluster(Matrix matrix, Properties props)
  {
    throw new Error();
  }
  










  public Assignments cluster(Matrix matrix, int numClusters, Properties props)
  {
    throw new Error();
  }
  





  public Assignments cluster(Matrix matrix, int numClusters, double backgroundClusterPerc, double similarityThreshold, int minClusterSize, SimilarityFunction simFunc)
  {
    int rows = matrix.rows();
    List<CandidateCluster> candidateClusters = 
      new ArrayList();
    

    CandidateCluster first = new CandidateCluster();
    first.add(0, matrix.getRowVector(0));
    candidateClusters.add(first);
    
    CandidateCluster mostSim;
    
    for (int r = 1; r < rows; r++) {
      DoubleVector row = matrix.getRowVector(r);
      mostSim = null;
      double highestSim = -1.0D;
      for (CandidateCluster cc : candidateClusters) {
        double sim = simFunc.sim(cc.centerOfMass(), row);
        if (sim > highestSim) {
          mostSim = cc;
          highestSim = sim;
        }
      }
      
      if (highestSim < similarityThreshold) {
        CandidateCluster cc = new CandidateCluster();
        cc.add(r, row);
        candidateClusters.add(cc);
      }
      else {
        mostSim.add(r, row);
      }
    }
    



    List<CandidateCluster> finalClusters = 
      new ArrayList();
    
    for (CandidateCluster cc : candidateClusters) {
      if (cc.size() >= minClusterSize)
      {

        double maxSim = -1.0D;
        for (CandidateCluster cc2 : candidateClusters)
          if (cc != cc2)
          {
            double sim = simFunc.sim(cc.centerOfMass(), cc2.centerOfMass());
            if (sim > maxSim)
              maxSim = sim;
          }
        if (maxSim < similarityThreshold) {
          finalClusters.add(cc);
        }
        else
        {
          CandidateCluster mostCohesive = null;
          double maxCohesiveness = -1.0D;
          for (CandidateCluster cc2 : candidateClusters)
            if (cc != cc2)
            {
              double sim = simFunc.sim(cc.centerOfMass(), cc2.centerOfMass());
              if (sim >= similarityThreshold)
              {

                IntIterator iter = cc2.indices().iterator();
                double similaritySum = 0.0D;
                while (iter.hasNext()) {
                  DoubleVector v = matrix.getRowVector(((Integer)iter.next()).intValue());
                  similaritySum += simFunc.sim(cc2.centerOfMass(), v);
                }
                double avgSim = similaritySum / cc2.size();
                
                if (avgSim > maxCohesiveness) {
                  maxCohesiveness = avgSim;
                  mostCohesive = cc2;
                }
              } }
          finalClusters.add(mostCohesive);
        }
      }
    }
    







    int foundClusters = finalClusters.size();
    



    double[] similarities = new double[rows];
    int[] clusterAssignments = new int[rows];
    for (int r = 0; r < rows; r++) {
      DoubleVector v = matrix.getRowVector(r);
      double highestSim = -1.0D;
      int mostSim = -1;
      for (int j = 0; j < foundClusters; j++) {
        CandidateCluster cc = (CandidateCluster)finalClusters.get(j);
        double sim = simFunc.sim(v, cc.centerOfMass());
        if (sim > highestSim) {
          mostSim = j;
          highestSim = sim;
        }
      }
      similarities[r] = highestSim;
      clusterAssignments[r] = mostSim;
    }
    


    double[] copy = Arrays.copyOf(similarities, similarities.length);
    Arrays.sort(copy);
    double cutoffSim = copy[((int)(copy.length * backgroundClusterPerc))];
    



    int backgroundClusterId = foundClusters;
    
    Assignment[] assignments = new Assignment[rows];
    for (int i = 0; i < similarities.length; i++) {
      if (similarities[i] < cutoffSim)
        clusterAssignments[i] = backgroundClusterId;
      assignments[i] = new HardAssignment(clusterAssignments[i]);
    }
    
    return new Assignments(foundClusters + 1, assignments, matrix);
  }
}
