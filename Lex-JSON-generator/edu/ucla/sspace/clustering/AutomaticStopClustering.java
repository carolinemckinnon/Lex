package edu.ucla.sspace.clustering;

import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.MatrixIO;
import edu.ucla.sspace.matrix.MatrixIO.Format;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;



























































public class AutomaticStopClustering
  implements Clustering
{
  private static final Logger LOGGER = Logger.getLogger(AutomaticStopClustering.class.getName());
  




  public static final String PROPERTY_PREFIX = "edu.ucla.sspace.clustering.AutomaticStopClustering";
  



  public static final String NUM_CLUSTERS_START = "edu.ucla.sspace.clustering.AutomaticStopClustering.numClusterStart";
  



  public static final String NUM_CLUSTERS_END = "edu.ucla.sspace.clustering.AutomaticStopClustering.numClusterEnd";
  



  public static final String CLUSTERING_METHOD = "edu.ucla.sspace.clustering.AutomaticStopClustering.clusteringMethod";
  



  public static final String PK1_THRESHOLD = "edu.ucla.sspace.clustering.AutomaticStopClustering.pk1Threshold";
  



  private static final String DEFAULT_NUM_CLUSTERS_START = "1";
  



  private static final String DEFAULT_NUM_CLUSTERS_END = "10";
  



  private static final String DEFAULT_CLUSTERING_METHOD = "PK3";
  



  private static final String DEFAULT_PK1_THRESHOLD = "-.70";
  



  public AutomaticStopClustering() {}
  




  public static enum Measure
  {
    PK1, 
    







    PK2, 
    







    PK3;
  }
  










  private static final Random random = new Random();
  



  private static final ClutoClustering.Method METHOD = ClutoClustering.Method.KMEANS;
  
  private static final ClutoClustering.Criterion CRITERION = ClutoClustering.Criterion.H2;
  








  public Assignments cluster(Matrix matrix, Properties props)
  {
    int endSize = Integer.parseInt(props.getProperty(
      "edu.ucla.sspace.clustering.AutomaticStopClustering.numClusterEnd", "10"));
    return cluster(matrix, endSize, props);
  }
  










  public Assignments cluster(Matrix m, int numClusters, Properties props)
  {
    int startSize = Integer.parseInt(props.getProperty(
      "edu.ucla.sspace.clustering.AutomaticStopClustering.numClusterStart", "1"));
    
    int numIterations = numClusters - startSize;
    
    Measure measure = Measure.valueOf(props.getProperty(
      "edu.ucla.sspace.clustering.AutomaticStopClustering.clusteringMethod", "PK3"));
    
    double pk1Threshold = Double.parseDouble(props.getProperty(
      "edu.ucla.sspace.clustering.AutomaticStopClustering.pk1Threshold", "-.70"));
    

    File matrixFile = null;
    try {
      matrixFile = File.createTempFile("cluto-input", ".matrix");
      MatrixIO.writeMatrix(m, matrixFile, MatrixIO.Format.CLUTO_DENSE);
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
    
    double[] objectiveWeights = new double[numIterations];
    File[] outFiles = new File[numIterations];
    
    String result = null;
    for (int i = 0; i < numIterations; i++) {
      LOGGER.fine("Clustering with " + (startSize + i) + " clusters");
      
      try
      {
        outFiles[i] = 
          File.createTempFile("autostop-clustering-out", ".matrix");
        result = ClutoWrapper.cluster(null, 
          matrixFile, 
          METHOD.getClutoName(), 
          CRITERION.getClutoName(), 
          outFiles[i], 
          i + startSize);
        
        objectiveWeights[i] = extractScore(result);
      } catch (IOException ioe) {
        throw new IOError(ioe);
      }
    }
    

    int bestK = -1;
    switch (measure) {
    case PK1: 
      bestK = computePk1Measure(objectiveWeights, pk1Threshold);
      break;
    case PK2: 
      bestK = computePk2Measure(objectiveWeights);
      break;
    case PK3: 
      bestK = computePk3Measure(objectiveWeights);
    }
    
    

    Assignment[] assignments = new HardAssignment[m.rows()];
    try {
      ClutoWrapper.extractAssignments(outFiles[bestK], assignments);
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
    


    matrixFile.delete();
    for (File outFile : outFiles) {
      outFile.delete();
    }
    return new Assignments(bestK, assignments, m);
  }
  



  private int computePk1Measure(double[] objectiveScores, double pk1Threshold)
  {
    LOGGER.fine("Computing the PK1 measure");
    

    double average = 0.0D;
    for (int k = 0; k < objectiveScores.length; k++)
      average += objectiveScores[k];
    average /= objectiveScores.length;
    

    double stdev = 0.0D;
    for (int k = 0; k < objectiveScores.length; k++)
      stdev += Math.pow(objectiveScores[k], 2.0D);
    stdev /= objectiveScores.length;
    stdev = Math.sqrt(stdev);
    

    for (int k = 0; k < objectiveScores.length; k++) {
      objectiveScores[k] = ((objectiveScores[k] - average) / stdev);
      if (objectiveScores[k] > pk1Threshold) {
        return k;
      }
    }
    return 0;
  }
  


  private int computePk2Measure(double[] objectiveScores)
  {
    LOGGER.fine("Computing the PK2 measure");
    

    double average = 0.0D;
    for (int k = objectiveScores.length - 1; k > 0; k--) {
      objectiveScores[k] /= objectiveScores[(k - 1)];
      average += objectiveScores[k];
    }
    average /= (objectiveScores.length - 1);
    

    double stdev = 0.0D;
    for (int k = 1; k < objectiveScores.length; k++)
      stdev += Math.pow(objectiveScores[k] - average, 2.0D);
    stdev /= (objectiveScores.length - 2);
    stdev = Math.sqrt(stdev);
    


    double referencePoint = 1.0D + stdev;
    int bestIndex = 0;
    double bestScore = Double.MAX_VALUE;
    for (int k = 1; k < objectiveScores.length; k++) {
      if ((objectiveScores[k] < bestScore) && 
        (objectiveScores[k] >= referencePoint)) {
        bestIndex = k;
        bestScore = objectiveScores[k];
      }
    }
    
    return bestIndex;
  }
  


  private int computePk3Measure(double[] objectiveScores)
  {
    LOGGER.fine("Computing the PK3 measure");
    

    double average = 0.0D;
    double[] weightedScores = new double[objectiveScores.length - 2];
    for (int k = 1; k < objectiveScores.length - 1; k++) {
      weightedScores[(k - 1)] = 
        (2.0D * objectiveScores[k] / (objectiveScores[(k - 1)] + objectiveScores[(k + 1)]));
      average += weightedScores[(k - 1)];
    }
    average /= (objectiveScores.length - 2);
    

    double stdev = 0.0D;
    for (int k = 0; k < weightedScores.length; k++)
      stdev += Math.pow(weightedScores[k] - average, 2.0D);
    stdev /= (objectiveScores.length - 2);
    stdev = Math.sqrt(stdev);
    


    double referencePoint = 1.0D + stdev;
    int bestIndex = 0;
    double bestScore = Double.MAX_VALUE;
    for (int k = 0; k < weightedScores.length; k++) {
      if ((weightedScores[k] < bestScore) && 
        (weightedScores[k] >= referencePoint)) {
        bestIndex = k;
        bestScore = weightedScores[k];
      }
    }
    
    return bestIndex + 1;
  }
  



  private double extractScore(String clutoOutput)
    throws IOException
  {
    double score = 0.0D;
    BufferedReader reader = 
      new BufferedReader(new StringReader(clutoOutput));
    String line = null;
    while ((line = reader.readLine()) != null) {
      if (line.contains("[I2=")) {
        String[] split = line.split("=");
        int endOfIndex = split[1].indexOf("]");
        return Double.parseDouble(split[1].substring(0, endOfIndex));
      }
    }
    return 0.0D;
  }
}
