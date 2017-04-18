package edu.ucla.sspace.clustering;

import edu.ucla.sspace.clustering.criterion.CriterionFunction;
import edu.ucla.sspace.matrix.ClutoSparseMatrixBuilder;
import edu.ucla.sspace.matrix.Matrices;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.MatrixBuilder;
import edu.ucla.sspace.matrix.RowMagnitudeTransform;
import edu.ucla.sspace.matrix.SparseMatrix;
import edu.ucla.sspace.matrix.TfIdfDocStripedTransform;
import edu.ucla.sspace.matrix.Transform;
import edu.ucla.sspace.util.ReflectionUtil;
import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;





































































public class GapStatistic
  implements Clustering
{
  private static final Logger LOGGER = Logger.getLogger(GapStatistic.class.getName());
  



  public static final String PROPERTY_PREFIX = "edu.ucla.sspace.clustering.GapStatistic";
  



  public static final String NUM_CLUSTERS_START = "edu.ucla.sspace.clustering.GapStatistic.numClusterStart";
  



  public static final String NUM_REFERENCE_DATA_SETS = "edu.ucla.sspace.clustering.GapStatistic.numReferenceDataSets";
  



  public static final String METHOD_PROPERTY = "edu.ucla.sspace.clustering.GapStatistic.method";
  



  private static final String DEFAULT_NUM_CLUSTERS_START = "1";
  



  private static final String DEFAULT_NUM_CLUSTERS_END = "10";
  



  private static final String DEFAULT_NUM_REFERENCE_DATA_SETS = "5";
  



  private static final String DEFAULT_METHOD = "edu.ucla.sspace.clustering.criterion.H2Function";
  



  private static final Random random = new Random();
  
  public GapStatistic() {}
  
  public Assignments cluster(Matrix matrix, Properties props)
  {
    return cluster(matrix, Integer.MAX_VALUE, props);
  }
  









  public Assignments cluster(Matrix m, int maxClusters, Properties props)
  {
    int startSize = Integer.parseInt(props.getProperty(
      "edu.ucla.sspace.clustering.GapStatistic.numClusterStart", "1"));
    int numGaps = Integer.parseInt(props.getProperty(
      "edu.ucla.sspace.clustering.GapStatistic.numReferenceDataSets", "5"));
    int numIterations = maxClusters - startSize;
    String criterion = props.getProperty("edu.ucla.sspace.clustering.GapStatistic.method", "edu.ucla.sspace.clustering.criterion.H2Function");
    
    verbose("Transforming the original data set");
    Transform tfidf = new TfIdfDocStripedTransform();
    Transform rowMag = new RowMagnitudeTransform();
    m = rowMag.transform(tfidf.transform(m));
    
    verbose("Generating the reference data set");
    
    ReferenceDataGenerator generator = new ReferenceDataGenerator(m);
    Matrix[] gapMatrices = new Matrix[numGaps];
    for (int i = 0; i < numGaps; i++) {
      gapMatrices[i] = rowMag.transform(tfidf.transform(
        generator.generateTestData()));
    }
    double[] gapResults = new double[numIterations];
    double[] gapStds = new double[numIterations];
    Assignments[] gapAssignments = new Assignments[numIterations];
    
    Assignments bestAssignments = null;
    double bestGap = Double.NEGATIVE_INFINITY;
    int bestK = 0;
    
    for (int i = 0; i < numIterations; i++) {
      clusterIteration(i, startSize, criterion, m, gapMatrices, 
        gapResults, gapStds, gapAssignments);
      if (bestGap >= gapResults[i] - gapStds[i]) {
        break;
      }
      

      bestGap = gapResults[i];
      bestAssignments = gapAssignments[i];
      bestK = i + startSize;
    }
    
    return bestAssignments;
  }
  



  private void clusterIteration(int i, int startSize, String methodName, Matrix matrix, Matrix[] gapMatrices, double[] gapResults, double[] gapStds, Assignments[] gapAssignments)
  {
    int k = i + startSize;
    double numGaps = gapMatrices.length;
    CriterionFunction function = 
      (CriterionFunction)ReflectionUtil.getObjectInstance(methodName);
    verbose("Clustering reference data for %d clusters\n", new Object[] { Integer.valueOf(k) });
    


    double referenceScore = 0.0D;
    double[] referenceScores = new double[gapMatrices.length];
    for (int j = 0; j < gapMatrices.length; j++) {
      verbose("Clustering reference data %d \n", new Object[] { Integer.valueOf(j) });
      Assignments result = DirectClustering.cluster(
        gapMatrices[j], k, 1, function);
      referenceScores[j] = Math.log(function.score());
      referenceScore += referenceScores[j];
    }
    referenceScore /= numGaps;
    

    double referenceStdev = 0.0D;
    for (double score : referenceScores)
      referenceStdev += Math.pow(score - referenceScore, 2.0D);
    referenceStdev /= numGaps;
    referenceStdev = Math.sqrt(referenceStdev) * Math.sqrt(1.0D + 1.0D / numGaps);
    
    verbose("Clustering original data for %d clusters\n", new Object[] { Integer.valueOf(k) });
    

    Assignments result = DirectClustering.cluster(
      matrix, k, 1, function);
    



    double gap = Math.log(function.score());
    verbose("Completed iteration with referenceScore: %f, gap:%f\n", new Object[] {
      Double.valueOf(referenceScore), Double.valueOf(gap) });
    gap = referenceScore - gap;
    
    System.out.printf("k: %d gap: %f std: %f\n", new Object[] { Integer.valueOf(i), Double.valueOf(gap), Double.valueOf(referenceStdev) });
    gapResults[i] = gap;
    gapStds[i] = referenceStdev;
    gapAssignments[i] = result;
  }
  





  private class ReferenceDataGenerator
  {
    private final double[] minValues;
    



    private final double[] maxValues;
    



    private final double averageNumValuesPerRow;
    



    private final double stdevNumValuesPerRow;
    



    private final int rows;
    



    private Set<Integer> nonZeroFeatures;
    




    public ReferenceDataGenerator(Matrix m)
    {
      rows = m.rows();
      minValues = new double[m.columns()];
      maxValues = new double[m.columns()];
      nonZeroFeatures = new HashSet();
      int[] numNonZeros = new int[m.rows()];
      double averageNumNonZeros = 0.0D;
      
      if ((m instanceof SparseMatrix)) {
        SparseMatrix sm = (SparseMatrix)m;
        for (int r = 0; r < m.rows(); r++) {
          SparseDoubleVector v = sm.getRowVector(r);
          nonZeros = v.getNonZeroIndices();
          numNonZeros[r] += nonZeros.length;
          averageNumNonZeros += nonZeros.length;
          for (column : nonZeros) {
            nonZeroFeatures.add(Integer.valueOf(column));
            double value = v.get(column);
            
            if (value < minValues[column])
              minValues[column] = value;
            if (value > maxValues[column])
              maxValues[column] = value;
          }
        }
      } else {
        for (int r = 0; r < m.rows(); r++) {
          for (int c = 0; c < m.columns(); c++) {
            double value = m.get(r, c);
            
            if (value < minValues[c])
              minValues[c] = value;
            if (value > maxValues[c]) {
              maxValues[c] = value;
            }
            
            if (value != 0.0D) {
              numNonZeros[r] += 1;
              averageNumNonZeros += 1.0D;
              nonZeroFeatures.add(Integer.valueOf(c));
            }
          }
        }
      }
      

      averageNumValuesPerRow = (averageNumNonZeros / m.rows());
      


      double stdev = 0.0D;
      int[] arrayOfInt1; int column = (arrayOfInt1 = numNonZeros).length; for (int[] nonZeros = 0; nonZeros < column; nonZeros++) { int nonZeroCount = arrayOfInt1[nonZeros];
        stdev += Math.pow(averageNumValuesPerRow - nonZeroCount, 2.0D);
      }
      
      stdevNumValuesPerRow = Math.sqrt(stdev / m.rows());
    }
    





    public Matrix generateTestData()
    {
      verbose("Generating a new reference set");
      
      List<SparseDoubleVector> vectors = 
        new ArrayList();
      
      MatrixBuilder builder = new ClutoSparseMatrixBuilder();
      for (int i = 0; i < rows; i++) {
        int cols = minValues.length;
        



        SparseDoubleVector column = new CompactSparseVector(cols);
        int numNonZeros = 
          (int)(GapStatistic.random.nextGaussian() * stdevNumValuesPerRow + 
          averageNumValuesPerRow);
        if (numNonZeros == 0) {
          numNonZeros++;
        }
        for (int j = 0; j < numNonZeros; j++)
        {
          int col = getNonZeroColumn();
          double value = GapStatistic.random.nextDouble() * (
            maxValues[col] - minValues[col]) + minValues[col];
          column.set(col, value);
        }
        vectors.add(column);
      }
      

      return Matrices.asSparseMatrix(vectors);
    }
    

    private int getNonZeroColumn()
    {
      int col;
      
      do
      {
        col = GapStatistic.random.nextInt(minValues.length);
      } while (!nonZeroFeatures.contains(Integer.valueOf(col)));
      return col;
    }
  }
  

  protected void verbose(String msg)
  {
    LOGGER.fine(msg);
  }
  
  protected void verbose(String format, Object... args) {
    LOGGER.fine(String.format(format, args));
  }
}
