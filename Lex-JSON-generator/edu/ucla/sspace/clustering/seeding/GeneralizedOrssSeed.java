package edu.ucla.sspace.clustering.seeding;

import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.similarity.SimilarityFunction;
import edu.ucla.sspace.util.primitive.IntIterator;
import edu.ucla.sspace.util.primitive.IntPair;
import edu.ucla.sspace.util.primitive.IntSet;
import edu.ucla.sspace.util.primitive.TroveIntSet;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.ScaledDoubleVector;
import edu.ucla.sspace.vector.VectorMath;
import java.util.Arrays;


































































public class GeneralizedOrssSeed
  implements KMeansSeed
{
  private final SimilarityFunction simFunc;
  
  public GeneralizedOrssSeed(SimilarityFunction simFunc)
  {
    this.simFunc = simFunc;
  }
  











  public DoubleVector[] chooseSeeds(int k, Matrix dataPoints)
  {
    int[] weights = new int[dataPoints.rows()];
    Arrays.fill(weights, 1);
    return chooseSeeds(dataPoints, k, weights);
  }
  













  public DoubleVector[] chooseSeeds(Matrix dataPoints, int k, int[] weights)
  {
    IntSet selected = new TroveIntSet();
    int rows = dataPoints.rows();
    

    if (rows <= k) {
      DoubleVector[] arr = new DoubleVector[rows];
      for (int i = 0; i < rows; i++)
        arr[i] = dataPoints.getRowVector(i);
      return arr;
    }
    




    double[] probabilities = new double[rows];
    




    double[] inverseSimilarities = new double[rows];
    







    IntPair firstTwoCenters = 
      pickFirstTwo(dataPoints, simFunc, weights, inverseSimilarities);
    selected.add(x);
    selected.add(y);
    





    for (int i = 2; i < k; i++)
    {


      determineProbabilities(inverseSimilarities, weights, 
        probabilities, selected);
      


      int point = selectWithProb(probabilities);
      



      boolean added = selected.add(point);
      assert (added) : "Added duplicate row to the set of selected points";
      updateNearestCenter(inverseSimilarities, dataPoints, 
        point, simFunc);
    }
    
    IntIterator iter = selected.iterator();
    DoubleVector[] centroids = new DoubleVector[k];
    for (int i = 0; iter.hasNext(); i++)
      centroids[i] = dataPoints.getRowVector(iter.nextInt());
    return centroids;
  }
  


  private static IntPair pickFirstTwo(Matrix dataPoints, SimilarityFunction simFunc, int[] weights, double[] inverseSimilarities)
  {
    double OPT_1 = 0.0D;
    DoubleVector centerOfMass = new DenseVector(dataPoints.columns());
    double sum = 0.0D;
    int rows = dataPoints.rows();
    int cols = dataPoints.columns();
    double[] probs = new double[rows];
    int totalWeight = 0;
    
    for (int i = 0; i < rows; i++) {
      DoubleVector v = dataPoints.getRowVector(i);
      int weight = weights[i];
      
      VectorMath.add(centerOfMass, new ScaledDoubleVector(v, weight));
      totalWeight += weight;
    }
    

    for (int j = 0; j < cols; j++) {
      centerOfMass.set(j, centerOfMass.get(j) / totalWeight);
    }
    
    for (int i = 0; i < rows; i++) {
      double sim = simFunc.sim(centerOfMass, dataPoints.getRowVector(i));
      sim = invertSim(sim);
      inverseSimilarities[i] = sim;
      OPT_1 += sim * weights[i];
    }
    

    for (int i = 0; i < rows; i++) {
      probs[i] = 
        ((OPT_1 + totalWeight * inverseSimilarities[i]) / (2 * totalWeight * OPT_1));
      sum += probs[i];
    }
    


    for (int i = 0; i < rows; i++) {
      probs[i] /= sum;
    }
    

    int c1 = selectWithProb(probs);
    DoubleVector y = dataPoints.getRowVector(c1);
    


    double invSimFromCtrToC1 = invertSim(simFunc.sim(y, centerOfMass));
    


    sum = 0.0D;
    probs[c1] = 0.0D;
    for (int i = 0; i < rows; i++)
    {

      if (i != c1)
      {

        double sim = invertSim(simFunc.sim(dataPoints.getRowVector(i), y)) * 
          weights[i];
        inverseSimilarities[i] = sim;
        probs[i] = (sim / (OPT_1 + totalWeight * invSimFromCtrToC1));
        sum += probs[i];
      }
    }
    
    for (int i = 0; i < rows; i++) {
      probs[i] /= sum;
    }
    
    int c2 = selectWithProb(probs);
    DoubleVector z = dataPoints.getRowVector(c2);
    
    inverseSimilarities[c1] = 0.0D;
    inverseSimilarities[c2] = 0.0D;
    



    for (int i = 0; i < rows; i++) {
      DoubleVector v = dataPoints.getRowVector(i);
      double sim1 = simFunc.sim(v, y);
      sim1 = invertSim(sim1);
      double sim2 = simFunc.sim(v, z);
      sim2 = invertSim(sim2);
      inverseSimilarities[i] = Math.min(sim1, sim2);
    }
    return new IntPair(c1, c2);
  }
  


  static double invertSim(double sim)
  {
    assert (sim >= 0.0D) : "negative similarity invalidates distance conversion";
    return sim == 0.0D ? 0.0D : 1.0D / sim;
  }
  



  private static int selectWithProb(double[] probs)
  {
    double cutoff = Math.random();
    double curProbSum = 0.0D;
    
    for (int i = 0; i < probs.length; i++) {
      curProbSum += probs[i];
      

      assert (curProbSum <= 1.0001D) : 
        ("sum of probabilities > 1: " + curProbSum);
      if (curProbSum >= cutoff)
        return i;
    }
    throw new IllegalStateException("Probablilities do not sum to 1");
  }
  






  private static void updateNearestCenter(double[] inverseSimilarities, Matrix dataPoints, int newlyChosen, SimilarityFunction simFunc)
  {
    DoubleVector chosenVec = dataPoints.getRowVector(newlyChosen);
    for (int i = 0; i < inverseSimilarities.length; i++) {
      double sim = simFunc.sim(dataPoints.getRowVector(i), chosenVec);
      sim = invertSim(sim);
      inverseSimilarities[i] = Math.min(inverseSimilarities[i], sim);
    }
  }
  

  private static void determineProbabilities(double[] inverseSimilarities, int[] weights, double[] probs, IntSet selected)
  {
    double sum = 0.0D;
    



    for (int i = 0; i < probs.length; i++)
    {

      if (selected.contains(i)) {
        probs[i] = 0.0D;

      }
      else
      {
        inverseSimilarities[i] *= weights[i];
        sum += probs[i];
      }
    }
    

    for (int i = 0; i < probs.length; i++) {
      probs[i] /= sum;
    }
  }
}
