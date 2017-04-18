package edu.ucla.sspace.clustering;

import edu.ucla.sspace.clustering.criterion.CriterionFunction;
import edu.ucla.sspace.clustering.criterion.I1Function;
import edu.ucla.sspace.clustering.seeding.KMeansSeed;
import edu.ucla.sspace.clustering.seeding.RandomSeed;
import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.util.ReflectionUtil;
import edu.ucla.sspace.vector.DoubleVector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;



































































































public class DirectClustering
  implements Clustering
{
  public static final String PROPERTY_PREFIX = "edu.ucla.sspace.clustering.DirectClustering";
  public static final String CRITERIA_PROPERTY = "edu.ucla.sspace.clustering.DirectClustering.criteria";
  public static final String REPEAT_PROPERTY = "edu.ucla.sspace.clustering.DirectClustering.repeat";
  public static final String SEED_PROPERTY = "edu.ucla.sspace.clustering.DirectClustering.seed";
  private static final String DEFAULT_SEED = "edu.ucla.sspace.clustering.seeding.RandomSeed";
  private static final String DEFAULT_CRITERION = "edu.ucla.sspace.clustering.criterion.I1Function";
  private static final String DEFAULT_REPEATS = "10";
  
  public DirectClustering() {}
  
  public Assignments cluster(Matrix matrix, Properties properties)
  {
    throw new UnsupportedOperationException(
      "DirectClustering requires the number of clusters to be set.");
  }
  





  public Assignments cluster(Matrix matrix, int numClusters, Properties properties)
  {
    int numRepetitions = Integer.parseInt(properties.getProperty(
      "edu.ucla.sspace.clustering.DirectClustering.repeat", "10"));
    

    KMeansSeed seedType = (KMeansSeed)ReflectionUtil.getObjectInstance(
      properties.getProperty("edu.ucla.sspace.clustering.DirectClustering.seed", "edu.ucla.sspace.clustering.seeding.RandomSeed"));
    

    CriterionFunction criterion = (CriterionFunction)ReflectionUtil.getObjectInstance(
      properties.getProperty("edu.ucla.sspace.clustering.DirectClustering.criteria", "edu.ucla.sspace.clustering.criterion.I1Function"));
    
    return cluster(matrix, numClusters, numRepetitions, 
      seedType, criterion);
  }
  






  public static Assignments cluster(Matrix matrix, int numClusters, int numRepetitions)
  {
    return cluster(matrix, numClusters, numRepetitions, 
      new RandomSeed(), new I1Function());
  }
  







  public static Assignments cluster(Matrix matrix, int numClusters, int numRepetitions, CriterionFunction criterion)
  {
    return cluster(matrix, numClusters, numRepetitions, 
      new RandomSeed(), criterion);
  }
  








  public static Assignments cluster(Matrix matrix, int numClusters, int numRepetitions, KMeansSeed seedType, CriterionFunction criterion)
  {
    int[] bestAssignment = null;
    double bestScore = criterion.isMaximize() ? 0.0D : Double.MAX_VALUE;
    for (int i = 0; i < numRepetitions; i++) {
      clusterIteration(matrix, numClusters, seedType, criterion);
      if (criterion.isMaximize()) {
        if (criterion.score() > bestScore) {
          bestScore = criterion.score();
          bestAssignment = criterion.assignments();
        }
      }
      else if (criterion.score() < bestScore) {
        bestScore = criterion.score();
        bestAssignment = criterion.assignments();
      }
    }
    


    Assignment[] assignments = new Assignment[matrix.rows()];
    for (int i = 0; i < bestAssignment.length; i++) {
      assignments[i] = new HardAssignment(bestAssignment[i]);
    }
    return new Assignments(numClusters, assignments, matrix);
  }
  





  private static void clusterIteration(Matrix matrix, int numClusters, KMeansSeed seedType, CriterionFunction criterion)
  {
    DoubleVector[] centers = seedType.chooseSeeds(numClusters, matrix);
    


    int[] initialAssignments = new int[matrix.rows()];
    

    double bestSimilarity;
    
    if (numClusters != 1) {
      int nc = 0;
      for (int i = 0; i < matrix.rows(); i++)
      {
        DoubleVector vector = matrix.getRowVector(i);
        bestSimilarity = 0.0D;
        for (int c = 0; c < numClusters; c++) {
          double similarity = Similarity.cosineSimilarity(
            centers[c], vector);
          nc++;
          if (similarity >= bestSimilarity) {
            bestSimilarity = similarity;
            initialAssignments[i] = c;
          }
        }
      }
    }
    

    criterion.setup(matrix, initialAssignments, numClusters);
    


    List<Integer> indices = new ArrayList(matrix.rows());
    for (int i = 0; i < matrix.rows(); i++) {
      indices.add(Integer.valueOf(i));
    }
    


    boolean changed = true;
    for (; changed; 
        

        bestSimilarity.hasNext())
    {
      changed = false;
      Collections.shuffle(indices);
      bestSimilarity = indices.iterator(); continue;int index = ((Integer)bestSimilarity.next()).intValue();
      changed |= criterion.update(index);
    }
  }
  
  public String toString() {
    return "DirectClustering";
  }
}
