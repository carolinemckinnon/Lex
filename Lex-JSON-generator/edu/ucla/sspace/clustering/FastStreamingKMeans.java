package edu.ucla.sspace.clustering;

import edu.ucla.sspace.clustering.seeding.GeneralizedOrssSeed;
import edu.ucla.sspace.matrix.Matrices;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.similarity.SimilarityFunction;
import edu.ucla.sspace.util.LoggerUtil;
import edu.ucla.sspace.util.ReflectionUtil;
import edu.ucla.sspace.util.primitive.IntIterator;
import edu.ucla.sspace.util.primitive.IntSet;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.IntegerVector;
import edu.ucla.sspace.vector.Vector;
import edu.ucla.sspace.vector.VectorMath;
import edu.ucla.sspace.vector.Vectors;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

























































public class FastStreamingKMeans
{
  private static final Logger LOGGER = Logger.getLogger(FastStreamingKMeans.class.getName());
  






  private static final String PROPERTY_PREFIX = "edu.ucla.sspace.cluster.FastStreamingKMeans";
  






  public static final String BETA_PROPERTY = "edu.ucla.sspace.cluster.FastStreamingKMeans.beta";
  





  public static final String KAPPA_PROPERTY = "edu.ucla.sspace.cluster.FastStreamingKMeans.kappa";
  





  public static final String SIMILARITY_FUNCTION_PROPERTY = "edu.ucla.sspace.cluster.FastStreamingKMeans.similarityFunction";
  





  public static final double DEFAULT_BETA = 74.0D;
  





  private static final int MAX_BATCH_KMEANS_ITERS = 1000;
  





  public static final SimilarityFunction DEFAULT_SIMILARITY_FUNCTION = new SimilarityFunction() { public void setParams(double... arguments) {}
    
    public boolean isSymmetric() { return true; }
    
    public double sim(IntegerVector v1, IntegerVector v2) { return sim(Vectors.asDouble(v1), Vectors.asDouble(v2)); }
    

    public double sim(Vector v1, Vector v2) { return sim(Vectors.asDouble(v1), Vectors.asDouble(v2)); }
    
    public double sim(DoubleVector v1, DoubleVector v2) {
      if (v1.length() != v2.length())
        throw new IllegalArgumentException(
          "vector lengths are different");
      int length = v1.length();
      double sum = 0.0D;
      for (int i = 0; i < length; i++) {
        double d1 = v1.get(i);
        double d2 = v2.get(i);
        sum += (d1 - d2) * (d1 - d2);
      }
      return sum > 0.0D ? 1.0D / sum : 1.0D;
    }
  };
  

  public FastStreamingKMeans() {}
  

  public Assignments cluster(Matrix matrix, Properties props)
  {
    throw new UnsupportedOperationException(
      "Must specify the number of clusters");
  }
  










  public Assignments cluster(Matrix matrix, int numClusters, Properties props)
  {
    if ((matrix == null) || (props == null))
      throw new NullPointerException();
    if (numClusters < 1)
      throw new IllegalArgumentException(
        "The number of clusters must be positive");
    if (numClusters > matrix.rows()) {
      throw new IllegalArgumentException(
        "The number of clusters exceeds the number of data points");
    }
    int kappa = (int)(numClusters * (1.0D + Math.log(matrix.rows())));
    String kappaProp = props.getProperty("edu.ucla.sspace.cluster.FastStreamingKMeans.kappa");
    if (kappaProp != null) {
      try {
        int k = Integer.parseInt(kappaProp);
        if ((k < numClusters) || 
          (k > numClusters * (1.0D + Math.log(matrix.rows())))) {
          throw new IllegalArgumentException(
            "kappa must be at least the number of clusters, k, and at most k * log(matrix.rows())");
        }
        kappa = k;
      } catch (NumberFormatException nfe) {
        throw new IllegalArgumentException("Invalid kappa", nfe);
      }
    }
    
    double beta = 74.0D;
    String betaProp = props.getProperty("edu.ucla.sspace.cluster.FastStreamingKMeans.beta");
    if (betaProp != null) {
      try {
        double b = Double.parseDouble(betaProp);
        if (b <= 0.0D)
          throw new IllegalArgumentException(
            "beta must be positive");
        beta = b;
      } catch (NumberFormatException nfe) {
        throw new IllegalArgumentException("Invalid beta", nfe);
      }
    }
    
    SimilarityFunction simFunc = DEFAULT_SIMILARITY_FUNCTION;
    String simFuncProp = props.getProperty("edu.ucla.sspace.cluster.FastStreamingKMeans.similarityFunction");
    if (simFuncProp != null) {
      try {
        SimilarityFunction sf = 
          (SimilarityFunction)ReflectionUtil.getObjectInstance(simFuncProp);
        simFunc = sf;
      } catch (Error e) {
        throw new IllegalArgumentException(
          "Invalid similarity function class", e);
      }
    }
    
    return cluster(matrix, numClusters, kappa, beta, simFunc);
  }
  

























  public Assignments cluster(Matrix matrix, int numClusters, int kappa, double beta, SimilarityFunction simFunc)
  {
    int rows = matrix.rows();
    int cols = matrix.columns();
    

    double f = 1.0D / (numClusters * (1.0D + Math.log(rows)));
    

    List<CandidateCluster> facilities = 
      new ArrayList(kappa);
    
    for (int r = 0; r < rows;)
    {
      for (; (facilities.size() <= kappa) && (r < rows); r++) {
        DoubleVector x = matrix.getRowVector(r);
        
        CandidateCluster closest = null;
        

        double delta = Double.MAX_VALUE;
        for (CandidateCluster y : facilities) {
          double similarity = 
            simFunc.sim(x, y.centerOfMass());
          double invSim = invertSim(similarity);
          if (invSim < delta) {
            delta = invSim;
            closest = y;
          }
        }
        





        if ((closest == null) || (Math.random() < delta / f)) {
          CandidateCluster fac = new CandidateCluster();
          fac.add(r, x);
          facilities.add(fac);
        }
        else
        {
          closest.add(r, x);
        }
      }
      


      if (r < rows)
      {



        while (facilities.size() > kappa) {
          f *= beta;
          
          int curNumFacilities = facilities.size();
          List<CandidateCluster> consolidated = 
            new ArrayList(kappa);
          consolidated.add((CandidateCluster)facilities.get(0));
          for (int j = 1; j < curNumFacilities; j++) {
            CandidateCluster x = (CandidateCluster)facilities.get(j);
            int pointsAssigned = x.size();
            



            double delta = Double.MAX_VALUE;
            CandidateCluster closest = null;
            for (CandidateCluster y : consolidated) {
              double similarity = 
                simFunc.sim(x.centerOfMass(), y.centerOfMass());
              double invSim = invertSim(similarity);
              if (invSim < delta) {
                delta = invSim;
                closest = y;
              }
            }
            




            if (Math.random() < pointsAssigned * delta / f) {
              consolidated.add(x);

            }
            else
            {
              assert (closest != null) : "no closest facility";
              closest.merge(x);
            }
          }
          LoggerUtil.verbose(LOGGER, "Consolidated %d facilities down to %d", new Object[] {
            Integer.valueOf(facilities.size()), Integer.valueOf(consolidated.size()) });
          facilities = consolidated;
        }
      }
      else
      {
        int clusterId;
        

        if (facilities.size() <= numClusters) {
          LoggerUtil.verbose(LOGGER, "Had fewer facilities, %d, than the requested number of clusters %d", new Object[] {
          
            Integer.valueOf(facilities.size()), Integer.valueOf(numClusters) });
          





          Assignment[] assignments = new Assignment[rows];
          int numFacilities = facilities.size();
          for (int j = 0; j < numFacilities; j++) {
            CandidateCluster fac = (CandidateCluster)facilities.get(j);
            LoggerUtil.veryVerbose(LOGGER, "Facility %d had a center of mass at %s", new Object[] {
              Integer.valueOf(j), fac.centerOfMass() });
            
            clusterId = j;
            IntIterator iter = fac.indices().iterator();
            while (iter.hasNext()) {
              int row = iter.nextInt();
              assignments[row] = 
                new HardAssignment(clusterId);
            }
          }
          return new Assignments(numClusters, assignments, matrix);
        }
        
        LoggerUtil.verbose(LOGGER, "Had more than %d facilities, consolidating to %d", new Object[] {
          Integer.valueOf(facilities.size()), 
          Integer.valueOf(numClusters) });
        
        List<DoubleVector> facilityCentroids = 
          new ArrayList(facilities.size());
        int[] weights = new int[facilities.size()];
        int i = 0;
        for (CandidateCluster fac : facilities) {
          facilityCentroids.add(fac.centerOfMass());
          weights[(i++)] = fac.size();
        }
        
        Matrix m = Matrices.asMatrix(facilityCentroids);
        




        GeneralizedOrssSeed orss = new GeneralizedOrssSeed(simFunc);
        DoubleVector[] centroids = orss.chooseSeeds(numClusters, m);
        assert (nonNullCentroids(centroids)) : 
          "ORSS seed returned too few centroids";
        



        int[] facilityAssignments = new int[facilities.size()];
        



        int numChanged = 0;
        int kmeansIters = 0;
        do {
          numChanged = 0;
          
          DoubleVector[] updatedCentroids = 
            new DoubleVector[numClusters];
          for (i = 0; i < updatedCentroids.length; i++)
            updatedCentroids[i] = new DenseVector(cols);
          int[] updatedCentroidSizes = new int[numClusters];
          
          double similaritySum = 0.0D;
          

          i = 0;
          for (CandidateCluster fac : facilities) {
            int mostSim = -1;
            double highestSim = -1.0D;
            for (int j = 0; j < centroids.length; j++)
            {


              double sim = simFunc.sim(centroids[j], 
                fac.centerOfMass());
              if (sim > highestSim) {
                highestSim = sim;
                mostSim = j;
              }
            }
            



            VectorMath.add(updatedCentroids[mostSim], 
              fac.sum());
            updatedCentroidSizes[mostSim] += fac.size();
            int curAssignment = facilityAssignments[i];
            facilityAssignments[i] = mostSim;
            similaritySum += highestSim;
            if (curAssignment != mostSim) {
              LoggerUtil.veryVerbose(LOGGER, "Facility %d changed its centroid from %d to %d", new Object[] {
              
                Integer.valueOf(i), Integer.valueOf(curAssignment), Integer.valueOf(mostSim) });
              numChanged++;
            }
            i++;
          }
          




          for (int j = 0; j < updatedCentroids.length; j++) {
            DoubleVector v = updatedCentroids[j];
            int size = updatedCentroidSizes[j];
            for (int k = 0; k < cols; k++) {
              v.set(k, v.get(k) / size);
            }
            centroids[j] = v;
          }
          
          LoggerUtil.veryVerbose(LOGGER, "%d centroids swapped their facility", new Object[] {
            Integer.valueOf(numChanged) });
          if (numChanged <= 0) break;
          kmeansIters++;
        } while (
        




























































          kmeansIters < 1000);
        


        Assignment[] assignments = new Assignment[rows];
        for (int j = 0; j < facilityAssignments.length; j++) {
          CandidateCluster fac = (CandidateCluster)facilities.get(j);
          LoggerUtil.veryVerbose(LOGGER, "Facility %d had a center of mass at %s", new Object[] {
            Integer.valueOf(j), fac.centerOfMass() });
          
          int clusterId = facilityAssignments[j];
          IntIterator iter = fac.indices().iterator();
          while (iter.hasNext()) {
            int row = iter.nextInt();
            assignments[row] = 
              new HardAssignment(clusterId);
          }
        }
        return new Assignments(numClusters, assignments, matrix);
      }
    }
    
    throw new AssertionError(
      "Processed all data points without making assignment");
  }
  
  static boolean nonNullCentroids(DoubleVector[] centroids) {
    for (int i = 0; i < centroids.length; i++)
      if (centroids[i] == null)
        return false;
    return true;
  }
  


  static double invertSim(double sim)
  {
    assert (sim >= 0.0D) : "negative similarity invalidates distance conversion";
    return sim == 0.0D ? 0.0D : 1.0D / sim;
  }
}
