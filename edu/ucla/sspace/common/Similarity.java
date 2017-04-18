package edu.ucla.sspace.common;

import edu.ucla.sspace.similarity.AverageCommonFeatureRank;
import edu.ucla.sspace.similarity.CosineSimilarity;
import edu.ucla.sspace.similarity.EuclideanSimilarity;
import edu.ucla.sspace.similarity.JaccardIndex;
import edu.ucla.sspace.similarity.KLDivergence;
import edu.ucla.sspace.similarity.KendallsTau;
import edu.ucla.sspace.similarity.LinSimilarity;
import edu.ucla.sspace.similarity.PearsonCorrelation;
import edu.ucla.sspace.similarity.SimilarityFunction;
import edu.ucla.sspace.similarity.SpearmanRankCorrelation;
import edu.ucla.sspace.similarity.TanimotoCoefficient;
import edu.ucla.sspace.util.DoubleEntry;
import edu.ucla.sspace.util.IntegerEntry;
import edu.ucla.sspace.vector.CompactSparseIntegerVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.IntegerVector;
import edu.ucla.sspace.vector.SparseIntegerVector;
import edu.ucla.sspace.vector.SparseVector;
import edu.ucla.sspace.vector.Vector;
import edu.ucla.sspace.vector.VectorMath;
import edu.ucla.sspace.vector.Vectors;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;












































public class Similarity
{
  private Similarity() {}
  
  public static enum SimType
  {
    COSINE, 
    



    PEARSON_CORRELATION, 
    





    EUCLIDEAN, 
    
    SPEARMAN_RANK_CORRELATION, 
    
    JACCARD_INDEX, 
    


    LIN, 
    KL_DIVERGENCE, 
    AVERAGE_COMMON_FEATURE_RANK, 
    
    KENDALLS_TAU, 
    




    TANIMOTO_COEFFICIENT;
  }
  
















  @Deprecated
  public static Method getMethod(SimType similarityType)
  {
    String methodName = null;
    switch (similarityType) {
    case AVERAGE_COMMON_FEATURE_RANK: 
      methodName = "cosineSimilarity";
      break;
    case COSINE: 
      methodName = "correlation";
      break;
    case EUCLIDEAN: 
      methodName = "euclideanSimilarity";
      break;
    case JACCARD_INDEX: 
      methodName = "spearmanRankCorrelationCoefficient";
      break;
    case KENDALLS_TAU: 
      methodName = "jaccardIndex";
      break;
    case PEARSON_CORRELATION: 
      methodName = "averageCommonFeatureRank";
      break;
    case KL_DIVERGENCE: 
      methodName = "linSimilarity";
      break;
    case LIN: 
      methodName = "klDivergence";
      break;
    case SPEARMAN_RANK_CORRELATION: 
      methodName = "kendallsTau";
      break;
    case TANIMOTO_COEFFICIENT: 
      methodName = "tanimotoCoefficient";
      break;
    
    default: 
      if (!$assertionsDisabled) throw new AssertionError(similarityType);
      break; }
    Method m = null;
    try {
      m = Similarity.class.getMethod(methodName, 
        new Class[] { [D.class, [D.class });
    }
    catch (NoSuchMethodException nsme) {
      throw new Error(nsme);
    }
    
    return m;
  }
  
  public static SimilarityFunction getSimilarityFunction(SimType similarityType)
  {
    switch (similarityType) {
    case AVERAGE_COMMON_FEATURE_RANK: 
      return new CosineSimilarity();
    case COSINE: 
      return new PearsonCorrelation();
    case EUCLIDEAN: 
      return new EuclideanSimilarity();
    case JACCARD_INDEX: 
      return new SpearmanRankCorrelation();
    case KENDALLS_TAU: 
      return new JaccardIndex();
    case PEARSON_CORRELATION: 
      return new AverageCommonFeatureRank();
    case KL_DIVERGENCE: 
      return new LinSimilarity();
    case LIN: 
      return new KLDivergence();
    case SPEARMAN_RANK_CORRELATION: 
      return new KendallsTau();
    case TANIMOTO_COEFFICIENT: 
      return new TanimotoCoefficient();
    }
    throw new IllegalArgumentException("Unhandled SimType: " + 
      similarityType);
  }
  











  public static double getSimilarity(SimType similarityType, double[] a, double[] b)
  {
    switch (similarityType) {
    case AVERAGE_COMMON_FEATURE_RANK: 
      return cosineSimilarity(a, b);
    case COSINE: 
      return correlation(a, b);
    case EUCLIDEAN: 
      return euclideanSimilarity(a, b);
    case JACCARD_INDEX: 
      return spearmanRankCorrelationCoefficient(a, b);
    case KENDALLS_TAU: 
      return jaccardIndex(a, b);
    case PEARSON_CORRELATION: 
      return averageCommonFeatureRank(a, b);
    case KL_DIVERGENCE: 
      return linSimilarity(a, b);
    case LIN: 
      return klDivergence(a, b);
    case SPEARMAN_RANK_CORRELATION: 
      return kendallsTau(a, b);
    case TANIMOTO_COEFFICIENT: 
      return tanimotoCoefficient(a, b);
    }
    throw new IllegalArgumentException("Unhandled SimType: " + 
      similarityType);
  }
  











  public static <T extends Vector> double getSimilarity(SimType similarityType, T a, T b)
  {
    switch (similarityType) {
    case AVERAGE_COMMON_FEATURE_RANK: 
      return cosineSimilarity(a, b);
    case COSINE: 
      return correlation(a, b);
    case EUCLIDEAN: 
      return euclideanSimilarity(a, b);
    case JACCARD_INDEX: 
      return spearmanRankCorrelationCoefficient(a, b);
    case KENDALLS_TAU: 
      return jaccardIndex(a, b);
    case PEARSON_CORRELATION: 
      return averageCommonFeatureRank(a, b);
    case KL_DIVERGENCE: 
      return linSimilarity(a, b);
    case LIN: 
      return klDivergence(a, b);
    case SPEARMAN_RANK_CORRELATION: 
      return kendallsTau(a, b);
    case TANIMOTO_COEFFICIENT: 
      return tanimotoCoefficient(a, b);
    }
    return 0.0D;
  }
  



  private static void check(double[] a, double[] b)
  {
    if (a.length != b.length) {
      throw new IllegalArgumentException(
        "input array lengths do not match");
    }
  }
  



  private static void check(int[] a, int[] b)
  {
    if (a.length != b.length) {
      throw new IllegalArgumentException(
        "input array lengths do not match");
    }
  }
  



  private static void check(Vector a, Vector b)
  {
    if (a.length() != b.length()) {
      throw new IllegalArgumentException(
        "input vector lengths do not match");
    }
  }
  




  public static double cosineSimilarity(double[] a, double[] b)
  {
    check(a, b);
    double dotProduct = 0.0D;
    double aMagnitude = 0.0D;
    double bMagnitude = 0.0D;
    for (int i = 0; i < b.length; i++) {
      double aValue = a[i];
      double bValue = b[i];
      aMagnitude += aValue * aValue;
      bMagnitude += bValue * bValue;
      dotProduct += aValue * bValue;
    }
    aMagnitude = Math.sqrt(aMagnitude);
    bMagnitude = Math.sqrt(bMagnitude);
    return (aMagnitude == 0.0D) || (bMagnitude == 0.0D) ? 
      0.0D : 
      dotProduct / (aMagnitude * bMagnitude);
  }
  





  public static double cosineSimilarity(int[] a, int[] b)
  {
    check(a, b);
    
    long dotProduct = 0L;
    long aMagnitude = 0L;
    long bMagnitude = 0L;
    for (int i = 0; i < b.length; i++) {
      int aValue = a[i];
      int bValue = b[i];
      aMagnitude += aValue * aValue;
      bMagnitude += bValue * bValue;
      dotProduct += aValue * bValue;
    }
    
    double aMagnitudeSqRt = Math.sqrt(aMagnitude);
    double bMagnitudeSqRt = Math.sqrt(bMagnitude);
    return (aMagnitudeSqRt == 0.0D) || (bMagnitudeSqRt == 0.0D) ? 
      0.0D : 
      dotProduct / (aMagnitudeSqRt * bMagnitudeSqRt);
  }
  



  public static double cosineSimilarity(DoubleVector a, DoubleVector b)
  {
    double dotProduct = 0.0D;
    double aMagnitude = a.magnitude();
    double bMagnitude = b.magnitude();
    

    double bValue;
    
    if (((a instanceof Iterable)) && ((b instanceof Iterable)))
    {


      boolean useA = 
        (a.length() < b.length()) || (
        ((a instanceof SparseVector)) && ((b instanceof SparseVector)) && (
        ((SparseVector)a).getNonZeroIndices().length < 
        ((SparseVector)b).getNonZeroIndices().length));
      




      if (useA) {
        DoubleVector t = a;
        a = b;
        b = t;
      }
      
      for (DoubleEntry e : (Iterable)b) {
        int index = e.index();
        double aValue = a.get(index);
        bValue = e.value();
        dotProduct += aValue * bValue;
      }
    } else {
      int[] nzA;
      int[] nzB;
      int nz;
      if (((a instanceof SparseVector)) && ((b instanceof SparseVector))) {
        SparseVector svA = (SparseVector)a;
        SparseVector svB = (SparseVector)b;
        nzA = svA.getNonZeroIndices();
        nzB = svB.getNonZeroIndices();
        




        if ((a.length() < b.length()) || 
          (nzA.length < nzB.length)) {
          DoubleVector t = a;
          a = b;
          b = t;
        }
        int[] arrayOfInt3;
        bValue = (arrayOfInt3 = nzB).length; for (double d1 = 0; d1 < bValue; d1++) { nz = arrayOfInt3[d1];
          double aValue = a.get(nz);
          double bValue = b.get(nz);
          dotProduct += aValue * bValue;

        }
        


      }
      else if ((b instanceof SparseVector)) {
        SparseVector svB = (SparseVector)b;
        nzB = (nz = svB.getNonZeroIndices()).length; for (int[] arrayOfInt1 = 0; arrayOfInt1 < nzB; arrayOfInt1++) { int nz = nz[arrayOfInt1];
          dotProduct += b.get(nz) * a.get(nz);

        }
        

      }
      else if ((a instanceof SparseVector)) {
        SparseVector svA = (SparseVector)a;
        nzB = (nz = svA.getNonZeroIndices()).length; for (int[] arrayOfInt2 = 0; arrayOfInt2 < nzB; arrayOfInt2++) { int nz = nz[arrayOfInt2];
          dotProduct += b.get(nz) * a.get(nz);

        }
        

      }
      else
      {

        if (a.length() < b.length()) {
          DoubleVector t = a;
          a = b;
          b = t;
        }
        
        for (int i = 0; i < b.length(); i++) {
          double aValue = a.get(i);
          double bValue = b.get(i);
          dotProduct += aValue * bValue;
        }
      } }
    return (aMagnitude == 0.0D) || (bMagnitude == 0.0D) ? 
      0.0D : dotProduct / (aMagnitude * bMagnitude);
  }
  






  public static double cosineSimilarity(IntegerVector a, IntegerVector b)
  {
    check(a, b);
    
    int dotProduct = 0;
    double aMagnitude = a.magnitude();
    double bMagnitude = b.magnitude();
    

    int bValue;
    
    if (((a instanceof Iterable)) && ((b instanceof Iterable)))
    {


      boolean useA = 
        ((a instanceof SparseVector)) && ((b instanceof SparseVector)) && (
        ((SparseVector)a).getNonZeroIndices().length < 
        ((SparseVector)b).getNonZeroIndices().length);
      




      if (useA) {
        IntegerVector t = a;
        a = b;
        b = t;
      }
      
      for (IntegerEntry e : (Iterable)b) {
        int index = e.index();
        int aValue = a.get(index);
        bValue = e.value();
        dotProduct += aValue * bValue;

      }
      

    }
    else if (((a instanceof SparseVector)) && ((b instanceof SparseVector))) {
      SparseVector svA = (SparseVector)a;
      SparseVector svB = (SparseVector)b;
      int[] nzA = svA.getNonZeroIndices();
      int[] nzB = svB.getNonZeroIndices();
      



      if (nzA.length < nzB.length) {
        for (int nz : nzA) {
          int aValue = a.get(nz);
          int bValue = b.get(nz);
          dotProduct += aValue * bValue;
        }
        
      } else {
        for (int nz : nzB) {
          int aValue = a.get(nz);
          int bValue = b.get(nz);
          dotProduct += aValue * bValue;
        }
        
      }
    }
    else
    {
      for (int i = 0; i < b.length(); i++) {
        int aValue = a.get(i);
        int bValue = b.get(i);
        dotProduct += aValue * bValue;
      }
    }
    return (aMagnitude == 0.0D) || (bMagnitude == 0.0D) ? 
      0.0D : dotProduct / (aMagnitude * bMagnitude);
  }
  





  public static double cosineSimilarity(Vector a, Vector b)
  {
    return 
      ((a instanceof IntegerVector)) && ((b instanceof IntegerVector)) ? 
      cosineSimilarity((IntegerVector)a, (IntegerVector)b) : 
      cosineSimilarity(Vectors.asDouble(a), Vectors.asDouble(b));
  }
  






  public static double correlation(double[] arr1, double[] arr2)
  {
    check(arr1, arr2);
    

    double xSum = 0.0D;
    double ySum = 0.0D;
    for (int i = 0; i < arr1.length; i++) {
      xSum += arr1[i];
      ySum += arr2[i];
    }
    
    double xMean = xSum / arr1.length;
    double yMean = ySum / arr1.length;
    
    double numerator = 0.0D;double xSqSum = 0.0D;double ySqSum = 0.0D;
    for (int i = 0; i < arr1.length; i++) {
      double x = arr1[i] - xMean;
      double y = arr2[i] - yMean;
      numerator += x * y;
      xSqSum += x * x;
      ySqSum += y * y;
    }
    if ((xSqSum == 0.0D) || (ySqSum == 0.0D)) {
      return 0.0D;
    }
    return numerator / Math.sqrt(xSqSum * ySqSum);
  }
  






  public static double correlation(int[] arr1, int[] arr2)
  {
    check(arr1, arr2);
    

    long xSum = 0L;
    long ySum = 0L;
    for (int i = 0; i < arr1.length; i++) {
      xSum += arr1[i];
      ySum += arr2[i];
    }
    
    double xMean = xSum / arr1.length;
    double yMean = ySum / arr1.length;
    
    double numerator = 0.0D;double xSqSum = 0.0D;double ySqSum = 0.0D;
    for (int i = 0; i < arr1.length; i++) {
      double x = arr1[i] - xMean;
      double y = arr2[i] - yMean;
      numerator += x * y;
      xSqSum += x * x;
      ySqSum += y * y;
    }
    return numerator / Math.sqrt(xSqSum * ySqSum);
  }
  






  public static double correlation(DoubleVector arr1, DoubleVector arr2)
  {
    check(arr1, arr2);
    
    check(arr1, arr2);
    

    double xSum = 0.0D;
    double ySum = 0.0D;
    for (int i = 0; i < arr1.length(); i++) {
      xSum += arr1.get(i);
      ySum += arr2.get(i);
    }
    
    double xMean = xSum / arr1.length();
    double yMean = ySum / arr1.length();
    
    double numerator = 0.0D;double xSqSum = 0.0D;double ySqSum = 0.0D;
    for (int i = 0; i < arr1.length(); i++) {
      double x = arr1.get(i) - xMean;
      double y = arr2.get(i) - yMean;
      numerator += x * y;
      xSqSum += x * x;
      ySqSum += y * y;
    }
    return numerator / Math.sqrt(xSqSum * ySqSum);
  }
  






  public static double correlation(IntegerVector arr1, DoubleVector arr2)
  {
    check(arr1, arr2);
    

    double xSum = 0.0D;
    double ySum = 0.0D;
    for (int i = 0; i < arr1.length(); i++) {
      xSum += arr1.get(i);
      ySum += arr2.get(i);
    }
    
    double xMean = xSum / arr1.length();
    double yMean = ySum / arr1.length();
    
    double numerator = 0.0D;double xSqSum = 0.0D;double ySqSum = 0.0D;
    for (int i = 0; i < arr1.length(); i++) {
      double x = arr1.get(i) - xMean;
      double y = arr2.get(i) - yMean;
      numerator += x * y;
      xSqSum += x * x;
      ySqSum += y * y;
    }
    return numerator / Math.sqrt(xSqSum * ySqSum);
  }
  






  public static double correlation(Vector a, Vector b)
  {
    return correlation(Vectors.asDouble(a), Vectors.asDouble(b));
  }
  





  public static double euclideanDistance(double[] a, double[] b)
  {
    check(a, b);
    double sum = 0.0D;
    for (int i = 0; i < a.length; i++)
      sum += Math.pow(a[i] - b[i], 2.0D);
    return Math.sqrt(sum);
  }
  





  public static double euclideanDistance(int[] a, int[] b)
  {
    check(a, b);
    
    long sum = 0L;
    for (int i = 0; i < a.length; i++)
      sum = (sum + Math.pow(a[i] - b[i], 2.0D));
    return Math.sqrt(sum);
  }
  





  public static double euclideanDistance(DoubleVector a, DoubleVector b)
  {
    check(a, b);
    int[] nzIndices;
    int nz; if (((a instanceof SparseVector)) && ((b instanceof SparseVector))) {
      SparseVector svA = (SparseVector)a;
      SparseVector svB = (SparseVector)b;
      
      int[] aNonZero = svA.getNonZeroIndices();
      int[] bNonZero = svB.getNonZeroIndices();
      TIntSet union = new TIntHashSet(aNonZero);
      union.addAll(bNonZero);
      
      double sum = 0.0D;
      nzIndices = union.toArray();
      for (nz : nzIndices) {
        double x = a.get(nz);
        double y = b.get(nz);
        double diff = x - y;
        sum += diff * diff;
      }
      return Math.sqrt(sum);
    }
    if ((b instanceof SparseVector))
    {


      SparseVector sb = (SparseVector)b;
      int[] bNonZero = sb.getNonZeroIndices();
      double sum = 0.0D;
      



      double aMagnitude = Math.pow(a.magnitude(), 2.0D);
      
      int[] arrayOfInt1;
      
      nz = (arrayOfInt1 = bNonZero).length; for (nzIndices = 0; nzIndices < nz; nzIndices++) { int index = arrayOfInt1[nzIndices];
        double value = a.get(index);
        
        aMagnitude -= Math.pow(value, 2.0D);
        sum += Math.pow(value - b.get(index), 2.0D);
      }
      




      sum += aMagnitude;
      
      return sum < 0.0D ? 0.0D : Math.sqrt(sum);
    }
    
    double sum = 0.0D;
    for (int i = 0; i < a.length(); i++)
      sum += Math.pow(a.get(i) - b.get(i), 2.0D);
    return Math.sqrt(sum);
  }
  





  public static double euclideanDistance(IntegerVector a, IntegerVector b)
  {
    check(a, b);
    
    if (((a instanceof SparseVector)) && ((b instanceof SparseVector))) {
      SparseVector svA = (SparseVector)a;
      SparseVector svB = (SparseVector)b;
      
      int[] aNonZero = svA.getNonZeroIndices();
      int[] bNonZero = svB.getNonZeroIndices();
      HashSet<Integer> sparseIndicesA = new HashSet(
        aNonZero.length);
      double sum = 0.0D;
      for (int nonZero : aNonZero) {
        sum += Math.pow(a.get(nonZero) - b.get(nonZero), 2.0D);
        sparseIndicesA.add(Integer.valueOf(nonZero));
      }
      
      for (int nonZero : bNonZero)
        if (!sparseIndicesA.contains(bNonZero))
          sum += Math.pow(b.get(nonZero), 2.0D);
      return sum;
    }
    
    double sum = 0.0D;
    for (int i = 0; i < a.length(); i++)
      sum += Math.pow(a.get(i) - b.get(i), 2.0D);
    return Math.sqrt(sum);
  }
  





  public static double euclideanDistance(Vector a, Vector b)
  {
    return euclideanDistance(Vectors.asDouble(a), Vectors.asDouble(b));
  }
  





  public static double euclideanSimilarity(int[] a, int[] b)
  {
    return 1.0D / (1.0D + euclideanDistance(a, b));
  }
  





  public static double euclideanSimilarity(double[] a, double[] b)
  {
    return 1.0D / (1.0D + euclideanDistance(a, b));
  }
  





  public static double euclideanSimilarity(Vector a, Vector b)
  {
    return 1.0D / (1.0D + euclideanDistance(a, b));
  }
  



  public static double jaccardIndex(Set<?> a, Set<?> b)
  {
    int intersection = 0;
    for (Object o : a) {
      if (b.contains(o)) {
        intersection++;
      }
    }
    double union = a.size() + b.size() - intersection;
    return intersection / union;
  }
  




  public static double jaccardIndex(double[] a, double[] b)
  {
    Set<Double> intersection = new HashSet();
    Set<Double> union = new HashSet();
    double[] arrayOfDouble1 = a;int j = a.length; for (int i = 0; i < j; i++) { double d = arrayOfDouble1[i];
      intersection.add(Double.valueOf(d));
      union.add(Double.valueOf(d));
    }
    Set<Double> tmp = new HashSet();
    for (double d : b) {
      tmp.add(Double.valueOf(d));
      union.add(Double.valueOf(d));
    }
    
    intersection.retainAll(tmp);
    return intersection.size() / union.size();
  }
  










  public static double jaccardIndex(int[] a, int[] b)
  {
    BitSet c = new BitSet();
    BitSet d = new BitSet();
    BitSet union = new BitSet();
    int[] arrayOfInt = a;int j = a.length; for (int i = 0; i < j; i++) { int i = arrayOfInt[i];
      c.set(i);
      union.set(i);
    }
    for (int i : b) {
      d.set(i);
      union.set(i);
    }
    

    c.and(d);
    return c.cardinality() / union.cardinality();
  }
  




  public static double jaccardIndex(DoubleVector a, DoubleVector b)
  {
    Set<Double> intersection = new HashSet();
    Set<Double> union = new HashSet();
    for (int i = 0; i < a.length(); i++) {
      double d = a.get(i);
      intersection.add(Double.valueOf(d));
      union.add(Double.valueOf(d));
    }
    Set<Double> tmp = new HashSet();
    for (int i = 0; i < b.length(); i++) {
      double d = b.get(i);
      tmp.add(Double.valueOf(d));
      union.add(Double.valueOf(d));
    }
    
    intersection.retainAll(tmp);
    return intersection.size() / union.size();
  }
  




  public static double jaccardIndex(IntegerVector a, IntegerVector b)
  {
    Set<Integer> intersection = new HashSet();
    Set<Integer> union = new HashSet();
    for (int i = 0; i < a.length(); i++) {
      int d = a.get(i);
      intersection.add(Integer.valueOf(d));
      union.add(Integer.valueOf(d));
    }
    Set<Integer> tmp = new HashSet();
    for (int i = 0; i < b.length(); i++) {
      int d = b.get(i);
      tmp.add(Integer.valueOf(d));
      union.add(Integer.valueOf(d));
    }
    
    intersection.retainAll(tmp);
    return intersection.size() / union.size();
  }
  




  public static double jaccardIndex(Vector a, Vector b)
  {
    return jaccardIndex(Vectors.asDouble(a), Vectors.asDouble(b));
  }
  











  public static double spearmanRankCorrelationCoefficient(double[] a, double[] b)
  {
    check(a, b);
    int N = a.length;
    int NcubedMinusN = N * N * N - N;
    





    double[] rankedA = rank(a);
    double[] rankedB = rank(b);
    
    double sumDiffs = 0.0D;
    for (int i = 0; i < rankedA.length - 1; i++) {
      double diff = rankedA[i] - rankedB[i];
      sumDiffs += diff * diff;
    }
    
    double aCorrectionFactor = rankedA[(rankedA.length - 1)];
    double bCorrectionFactor = rankedB[(rankedB.length - 1)];
    
    double tiesSum = aCorrectionFactor + bCorrectionFactor;
    

    return (NcubedMinusN - 6.0D * sumDiffs - tiesSum / 2.0D) / 
      Math.sqrt(NcubedMinusN * NcubedMinusN - 
      tiesSum * NcubedMinusN + 
      aCorrectionFactor * bCorrectionFactor);
  }
  










  public static double spearmanRankCorrelationCoefficient(int[] a, int[] b)
  {
    check(a, b);
    
    int N = a.length;
    int NcubedMinusN = N * N * N - N;
    





    double[] rankedA = rank(a);
    double[] rankedB = rank(b);
    
    double sumDiffs = 0.0D;
    for (int i = 0; i < rankedA.length - 1; i++) {
      double diff = rankedA[i] - rankedB[i];
      sumDiffs += diff * diff;
    }
    
    double aCorrectionFactor = rankedA[(rankedA.length - 1)];
    double bCorrectionFactor = rankedB[(rankedB.length - 1)];
    
    double tiesSum = aCorrectionFactor + bCorrectionFactor;
    

    return (NcubedMinusN - 6.0D * sumDiffs - tiesSum / 2.0D) / 
      Math.sqrt(NcubedMinusN * NcubedMinusN - 
      tiesSum * NcubedMinusN + 
      aCorrectionFactor * bCorrectionFactor);
  }
  





  static double[] rank(int[] vals)
  {
    IntRank[] ranked = new IntRank[vals.length];
    for (int i = 0; i < vals.length; i++)
      ranked[i] = new IntRank(vals[i], i);
    Arrays.sort(ranked);
    
    double[] ranks = new double[vals.length + 1];
    int correctionFactor = 0;
    
    for (int i = 0; i < ranked.length; i++)
    {
      int ties = 0;
      for (int j = i + 1; (j < ranked.length) && 
            (val == val); ties++) { j++;
      }
      if (ties == 0) {
        ranks[index] = (i + 1);
      }
      else
      {
        double rank = i + 1 + ties / 2.0D;
        for (int j = i; j < i + ties + 1; j++) {
          ranks[index] = rank;
        }
        i += ties;
        



        ties++;
        correctionFactor += ties * ties * ties - ties;
      }
    }
    ranks[(ranks.length - 1)] = correctionFactor;
    return ranks;
  }
  
  static class IntRank implements Comparable<IntRank>
  {
    int index;
    int val;
    
    public IntRank(int val, int index)
    {
      this.val = val;
      this.index = index;
    }
    
    public int compareTo(IntRank r) { return val - val; }
    
    public boolean equals(Object o) {
      if ((o instanceof IntRank)) {
        IntRank r = (IntRank)o;
        return (val == val) && (index == index);
      }
      return false;
    }
    
    public int hashCode() { return val; }
    
    public String toString() {
      return "(v:" + val + ")[" + index + "]";
    }
  }
  






  static double[] rank(double[] vals)
  {
    DoubleRank[] ranked = new DoubleRank[vals.length];
    for (int i = 0; i < vals.length; i++)
      ranked[i] = new DoubleRank(vals[i], i);
    Arrays.sort(ranked);
    
    double[] ranks = new double[vals.length + 1];
    int correctionFactor = 0;
    
    for (int i = 0; i < ranked.length; i++)
    {
      int ties = 0;
      for (int j = i + 1; (j < ranked.length) && 
            (val == val); ties++) { j++;
      }
      if (ties == 0) {
        ranks[index] = (i + 1);
      }
      else
      {
        double rank = i + 1 + ties / 2.0D;
        for (int j = i; j < i + ties + 1; j++) {
          ranks[index] = rank;
        }
        i += ties;
        



        ties++;
        correctionFactor += ties * ties * ties - ties;
      }
    }
    ranks[(ranks.length - 1)] = correctionFactor;
    return ranks;
  }
  
  static class DoubleRank implements Comparable<DoubleRank>
  {
    int index;
    double val;
    
    public DoubleRank(double val, int index)
    {
      this.val = val;
      this.index = index;
    }
    
    public int compareTo(DoubleRank r) { return Double.compare(val, val); }
    
    public boolean equals(Object o) {
      if ((o instanceof DoubleRank)) {
        DoubleRank r = (DoubleRank)o;
        return (val == val) && (index == index);
      }
      return false;
    }
    
    public int hashCode() { return index; }
    
    public String toString() {
      return "(v:" + val + ")[" + index + "]";
    }
  }
  














  public static double spearmanRankCorrelationCoefficient(DoubleVector a, DoubleVector b)
  {
    return spearmanRankCorrelationCoefficient(a.toArray(), b.toArray());
  }
  














  public static double spearmanRankCorrelationCoefficient(IntegerVector a, IntegerVector b)
  {
    return spearmanRankCorrelationCoefficient(a.toArray(), b.toArray());
  }
  












  public static double spearmanRankCorrelationCoefficient(Vector a, Vector b)
  {
    return spearmanRankCorrelationCoefficient(Vectors.asDouble(a), 
      Vectors.asDouble(b));
  }
  





































  public static double averageCommonFeatureRank(double[] a, double[] b)
  {
    check(a, b);
    int size = a.length;
    



    int n = 20;
    

    Object a_index = new 1Pair[size];
    for (int i = 0; i < size; i++) {
      a_index[i = new Object()
      {
        public int i;
        public double v;
        
        public void set(int index, double value)
        {
          i = index;
          v = value;
        }
      };
    }
    































    Object b_index = new 1Pair[size];
    for (int i = 0; i < size; i++) {
      b_index[i = new Object()
      {
        public int i;
        public double v;
        
        public void set(int index, double value)
        {
          i = index;
          v = value;
        }
      };
    }
    




































    Arrays.sort(a_index, new Comparator()
    {
      public int compare(Similarity.1Pair o1, Similarity.1Pair o2)
      {
        if (v < v) return 1;
        if (v > v) { return -1;
        }
        if (i < i) return 1;
        if (i > i) return -1;
        return 0;
      }
      
      public boolean equals(Similarity.1Pair o1, Similarity.1Pair o2)
      {
        return compare(o1, o2) == 0;










      }
      











    });
    Arrays.sort(b_index, new Comparator()
    {
      public int compare(Similarity.1Pair o1, Similarity.1Pair o2)
      {
        if (v < v) return 1;
        if (v > v) { return -1;
        }
        if (i < i) return 1;
        if (i > i) return -1;
        return 0;
      }
      
      public boolean equals(Similarity.1Pair o1, Similarity.1Pair o2)
      {
        return compare(o1, o2) == 0;













      }
      













    });
    Object a_rank = new 1Pair[size];
    int last_i = 1;
    for (int i = 0; i < size; i++) {
      Object x = a_index[i];
      
      if ((i > 0) && (v == 1v)) {
        a_rank[i = new Object()
        {
          public int i;
          public double v;
          
          public void set(int index, double value)
          {
            i = index;
            v = value;












          }
          













        };












      }
      else
      {












        a_rank[i = new Object()
        {
          public int i;
          public double v;
          
          public void set(int index, double value)
          {
            i = index;
            v = value;


























          }
          



























        };
        last_i = i + 1;
      }
    }
    
    last_i = 1;
    Object b_rank = new 1Pair[size];
    for (int i = 0; i < size; i++) {
      Object x = b_index[i];
      
      if ((i > 0) && (v == 1v)) {
        b_rank[i = new Object()
        {
          public int i;
          public double v;
          
          public void set(int index, double value)
          {
            i = index;
            v = value;















          }
          
















        };
















      }
      else
      {















        b_rank[i = new Object()
        {
          public int i;
          public double v;
          
          public void set(int index, double value)
          {
            i = index;
            v = value;

































          }
          

































        };
        last_i = i + 1;
      }
    }
    



    int[] nTop = new int[n];
    boolean[] seenbefore = new boolean[size];
    Arrays.fill(seenbefore, false);
    int a_i = 0;
    int b_i = 0;
    int i = 0; for (goto 502; i < n; i++) {
      do {
        if (a_i >= size) break; } while (seenbefore[i] != 0);
      
      while ((b_i < size) && (seenbefore[i] != 0)) {
        b_i++;
      }
      

      if ((a_i < size) && 
        (1 == new Comparator()
      {
        public int compare(Similarity.1Pair o1, Similarity.1Pair o2)
        {
          if (v < v) return 1;
          if (v > v) { return -1;
          }
          if (i < i) return 1;
          if (i > i) return -1;
          return 0;
        }
        
        public boolean equals(Similarity.1Pair o1, Similarity.1Pair o2)
        {
          return compare(o1, o2) == 0;



































        }
        




































      }.compare(a_index[a_i], b_index[b_i])))
      {
        nTop[i] = i;
        seenbefore[nTop[i]] = true;
        a_i++;
      }
      else {
        nTop[i] = i;
        seenbefore[nTop[i]] = true;
        b_i++;
      }
    }
    


    double sum = 0.0D;
    for (int i = 0; i < n; i++) {
      sum += 0.5D * (i + i);
    }
    
    return n / sum;
  }
  








  public static double averageCommonFeatureRank(Vector a, Vector b)
  {
    return averageCommonFeatureRank(Vectors.asDouble(a).toArray(), 
      Vectors.asDouble(b).toArray());
  }
  








  public static double averageCommonFeatureRank(int[] a, int[] b)
  {
    return averageCommonFeatureRank(Vectors.asVector(a), 
      Vectors.asVector(b));
  }
  
















  public static double linSimilarity(DoubleVector a, DoubleVector b)
  {
    check(a, b);
    

    double aInformation = 0.0D;
    
    double bInformation = 0.0D;
    
    double combinedInformation = 0.0D;
    

    if (((a instanceof SparseVector)) && 
      ((b instanceof SparseVector)))
    {
      SparseVector sa = (SparseVector)a;
      int[] aNonZeros = sa.getNonZeroIndices();
      
      SparseVector sb = (SparseVector)b;
      int[] bNonZeros = sb.getNonZeroIndices();
      



      if (bNonZeros.length < aNonZeros.length) {
        SparseVector temp = sa;
        tempNonZeros = aNonZeros;
        
        sa = sb;
        aNonZeros = bNonZeros;
        
        sb = temp;
        bNonZeros = tempNonZeros;
      }
      
      int[] arrayOfInt3;
      
      int[] arrayOfInt1 = (arrayOfInt3 = aNonZeros).length; for (int[] tempNonZeros = 0; tempNonZeros < arrayOfInt1; tempNonZeros++) { int index = arrayOfInt3[tempNonZeros];
        double aValue = a.get(index);
        double bValue = b.get(index);
        aInformation += aValue;
        combinedInformation += aValue + bValue;
      }
      


      int[] arrayOfInt2 = (arrayOfInt3 = bNonZeros).length; for (tempNonZeros = 0; tempNonZeros < arrayOfInt2; tempNonZeros++) { int index = arrayOfInt3[tempNonZeros];
        bInformation += b.get(index);
      }
      
    }
    else
    {
      for (int i = 0; i < a.length(); i++) {
        double aValue = a.get(i);
        aInformation += aValue;
        
        double bValue = b.get(i);
        bInformation += bValue;
        
        if ((aValue != 0.0D) && (bValue != 0.0D))
          combinedInformation += aValue + bInformation;
      }
    }
    return combinedInformation / (aInformation + bInformation);
  }
  















  public static double linSimilarity(IntegerVector a, IntegerVector b)
  {
    check(a, b);
    

    double aInformation = 0.0D;
    
    double bInformation = 0.0D;
    
    double combinedInformation = 0.0D;
    

    if (((a instanceof SparseVector)) && 
      ((b instanceof SparseVector)))
    {
      SparseVector sa = (SparseVector)a;
      int[] aNonZeros = sa.getNonZeroIndices();
      
      SparseVector sb = (SparseVector)b;
      int[] bNonZeros = sb.getNonZeroIndices();
      



      if (bNonZeros.length < aNonZeros.length) {
        SparseVector temp = sa;
        tempNonZeros = aNonZeros;
        
        sa = sb;
        aNonZeros = bNonZeros;
        
        sb = temp;
        bNonZeros = tempNonZeros;
      }
      
      int[] arrayOfInt3;
      
      int[] arrayOfInt1 = (arrayOfInt3 = aNonZeros).length; for (int[] tempNonZeros = 0; tempNonZeros < arrayOfInt1; tempNonZeros++) { int index = arrayOfInt3[tempNonZeros];
        double aValue = a.get(index);
        double bValue = b.get(index);
        aInformation += aValue;
        combinedInformation += aValue + bValue;
      }
      


      int[] arrayOfInt2 = (arrayOfInt3 = bNonZeros).length; for (tempNonZeros = 0; tempNonZeros < arrayOfInt2; tempNonZeros++) { int index = arrayOfInt3[tempNonZeros];
        bInformation += b.get(index);
      }
      
    }
    else
    {
      for (int i = 0; i < a.length(); i++) {
        double aValue = a.get(i);
        aInformation += aValue;
        
        double bValue = b.get(i);
        bInformation += bValue;
        
        if ((aValue != 0.0D) && (bValue != 0.0D))
          combinedInformation += aValue + bInformation;
      }
    }
    return combinedInformation / (aInformation + bInformation);
  }
  















  public static double linSimilarity(Vector a, Vector b)
  {
    check(a, b);
    

    double aInformation = 0.0D;
    
    double bInformation = 0.0D;
    
    double combinedInformation = 0.0D;
    

    if (((a instanceof SparseVector)) && 
      ((b instanceof SparseVector)))
    {
      SparseVector sa = (SparseVector)a;
      int[] aNonZeros = sa.getNonZeroIndices();
      
      SparseVector sb = (SparseVector)b;
      int[] bNonZeros = sb.getNonZeroIndices();
      



      if (bNonZeros.length < aNonZeros.length) {
        SparseVector temp = sa;
        tempNonZeros = aNonZeros;
        sa = sb;
        aNonZeros = bNonZeros;
        sb = temp;
        bNonZeros = tempNonZeros;
      }
      
      int[] arrayOfInt3;
      
      int[] arrayOfInt1 = (arrayOfInt3 = aNonZeros).length; for (int[] tempNonZeros = 0; tempNonZeros < arrayOfInt1; tempNonZeros++) { int index = arrayOfInt3[tempNonZeros];
        double aValue = a.getValue(index).doubleValue();
        double bValue = b.getValue(index).doubleValue();
        aInformation += aValue;
        combinedInformation += aValue + bValue;
      }
      


      int[] arrayOfInt2 = (arrayOfInt3 = bNonZeros).length; for (tempNonZeros = 0; tempNonZeros < arrayOfInt2; tempNonZeros++) { int index = arrayOfInt3[tempNonZeros];
        bInformation += b.getValue(index).doubleValue();
      }
      
    }
    else
    {
      for (int i = 0; i < a.length(); i++) {
        double aValue = a.getValue(i).doubleValue();
        aInformation += aValue;
        
        double bValue = b.getValue(i).doubleValue();
        bInformation += bValue;
        
        if ((aValue != 0.0D) && (bValue != 0.0D))
          combinedInformation += aValue + bInformation;
      }
    }
    return combinedInformation / (aInformation + bInformation);
  }
  















  public static double linSimilarity(double[] a, double[] b)
  {
    check(a, b);
    

    double aInformation = 0.0D;
    
    double bInformation = 0.0D;
    
    double combinedInformation = 0.0D;
    


    for (int i = 0; i < a.length; i++) {
      aInformation += a[i];
      bInformation += b[i];
      if ((a[i] != 0.0D) && (b[i] != 0.0D))
        combinedInformation += a[i] + b[i];
    }
    return combinedInformation / (aInformation + bInformation);
  }
  















  public static double linSimilarity(int[] a, int[] b)
  {
    check(a, b);
    

    double aInformation = 0.0D;
    
    double bInformation = 0.0D;
    
    double combinedInformation = 0.0D;
    


    for (int i = 0; i < a.length; i++) {
      aInformation += a[i];
      bInformation += b[i];
      if ((a[i] != 0.0D) && (b[i] != 0.0D))
        combinedInformation += a[i] + b[i];
    }
    return combinedInformation / (aInformation + bInformation);
  }
  















  public static double klDivergence(DoubleVector a, DoubleVector b)
  {
    check(a, b);
    
    double divergence = 0.0D;
    

    if ((a instanceof SparseVector)) {
      SparseVector sa = (SparseVector)a;
      int[] aNonZeros = sa.getNonZeroIndices();
      
      for (int index : aNonZeros) {
        double aValue = a.get(index);
        double bValue = b.get(index);
        


        if (bValue != 0.0D) {
          divergence += aValue * Math.log(aValue / bValue);
        }
      }
    }
    else {
      for (int i = 0; i < a.length(); i++) {
        double aValue = a.get(i);
        double bValue = b.get(i);
        
        if (bValue == 0.0D) {
          throw new IllegalArgumentException(
            "The KL-divergence is not defined when a[i] > 0 and b[i] == 0.");
        }
        


        if (aValue != 0.0D) {
          divergence += aValue * Math.log(aValue / bValue);
        }
      }
    }
    return divergence;
  }
  















  public static double klDivergence(IntegerVector a, IntegerVector b)
  {
    check(a, b);
    
    double divergence = 0.0D;
    

    if ((a instanceof SparseVector)) {
      SparseVector sa = (SparseVector)a;
      int[] aNonZeros = sa.getNonZeroIndices();
      
      for (int index : aNonZeros) {
        double aValue = a.get(index);
        double bValue = b.get(index);
        
        if (bValue == 0.0D) {
          throw new IllegalArgumentException(
            "The KL-divergence is not defined when a[i] > 0 and b[i] == 0.");
        }
        


        if (aValue != 0.0D) {
          divergence += aValue * Math.log(aValue / bValue);
        }
      }
    }
    else {
      for (int i = 0; i < a.length(); i++) {
        double aValue = a.get(i);
        double bValue = b.get(i);
        
        if (bValue == 0.0D) {
          throw new IllegalArgumentException(
            "The KL-divergence is not defined when a[i] > 0 and b[i] == 0.");
        }
        


        if (aValue != 0.0D) {
          divergence += aValue * Math.log(aValue / bValue);
        }
      }
    }
    return divergence;
  }
  















  public static double klDivergence(Vector a, Vector b)
  {
    check(a, b);
    
    double divergence = 0.0D;
    

    if ((a instanceof SparseVector)) {
      SparseVector sa = (SparseVector)a;
      int[] aNonZeros = sa.getNonZeroIndices();
      
      for (int index : aNonZeros) {
        double aValue = a.getValue(index).doubleValue();
        double bValue = b.getValue(index).doubleValue();
        
        if (bValue == 0.0D) {
          throw new IllegalArgumentException(
            "The KL-divergence is not defined when a[i] > 0 and b[i] == 0.");
        }
        


        if (aValue != 0.0D) {
          divergence += aValue * Math.log(aValue / bValue);
        }
      }
    }
    else {
      for (int i = 0; i < a.length(); i++) {
        double aValue = a.getValue(i).doubleValue();
        double bValue = b.getValue(i).doubleValue();
        if (bValue == 0.0D) {
          throw new IllegalArgumentException(
            "The KL-divergence is not defined when a[i] > 0 and b[i] == 0.");
        }
        

        if (aValue != 0.0D) {
          divergence += aValue * Math.log(aValue / bValue);
        }
      }
    }
    return divergence;
  }
  















  public static double klDivergence(double[] a, double[] b)
  {
    check(a, b);
    
    double divergence = 0.0D;
    

    for (int i = 0; i < a.length; i++)
    {

      if (b[i] == 0.0D) {
        throw new IllegalArgumentException(
          "The KL-divergence is not defined when a[i] > 0 and b[i] == 0.");
      }
      if (a[i] != 0.0D) {
        divergence += a[i] * Math.log(a[i] / b[i]);
      }
    }
    return divergence;
  }
  















  public static double klDivergence(int[] a, int[] b)
  {
    check(a, b);
    
    double divergence = 0.0D;
    

    for (int i = 0; i < a.length; i++)
    {

      if (b[i] == 0.0D) {
        throw new IllegalArgumentException(
          "The KL-divergence is not defined when a[i] > 0 and b[i] == 0.");
      }
      if (a[i] != 0.0D) {
        divergence += a[i] * Math.log(a[i] / b[i]);
      }
    }
    return divergence;
  }
  







  public static double kendallsTau(double[] a, double[] b)
  {
    return kendallsTau(Vectors.asVector(a), Vectors.asVector(b));
  }
  







  public static double kendallsTau(int[] a, int[] b)
  {
    return kendallsTau(Vectors.asVector(a), Vectors.asVector(b));
  }
  







  public static double kendallsTau(Vector a, Vector b)
  {
    return kendallsTau(Vectors.asDouble(a), Vectors.asDouble(b));
  }
  







  public static double kendallsTau(DoubleVector a, DoubleVector b)
  {
    check(a, b);
    

    int length = a.length();
    double numerator = 0.0D;
    


    SparseIntegerVector tiesInA = new CompactSparseIntegerVector(length);
    SparseIntegerVector tiesInB = new CompactSparseIntegerVector(length);
    boolean foundTies = false;
    
    int concordant = 0;
    int discordant = 0;
    double bi;
    double bj;
    boolean atie; for (int i = 0; i < length; i++) {
      for (int j = i + 1; j < length; j++)
      {



        double ai = a.get(i);
        double aj = a.get(j);
        bi = b.get(i);
        bj = b.get(j);
        

        atie = ai == aj;
        if (ai == aj) {
          tiesInA.add(i, 1);
          foundTies = true;
        }
        if (bi == bj) {
          tiesInB.add(i, 1);
          foundTies = true;
        }
        

        if ((ai != aj) && (bi != bj)) {
          if (((ai < aj) && (bi < bj)) || ((ai > aj) && (bi > bj))) {
            concordant++;
          } else {
            discordant++;
          }
        }
      }
    }
    int n = concordant - discordant;
    double d = 0.5D * (length * (length - 1));
    
    if (foundTies)
    {






      double aSum = 0.0D;
      double d1 = (bj = tiesInA.getNonZeroIndices()).length; for (bi = 0; bi < d1; bi++) { int i = bj[bi];
        ties = tiesInA.get(i);
        aSum += ties * (ties + 1) * 0.5D;
      }
      
      double bSum = 0.0D;
      int ties = (atie = tiesInB.getNonZeroIndices()).length; for (bj = 0; bj < ties; bj++) { int i = atie[bj];
        int ties = tiesInB.get(i);
        bSum += ties * (ties + 1) * 0.5D;
      }
      
      return n / Math.sqrt((d - aSum) * (d - bSum));
    }
    
    return n / d;
  }
  







  public static double kendallsTau(IntegerVector a, IntegerVector b)
  {
    check(a, b);
    

    int length = a.length();
    double numerator = 0.0D;
    


    SparseIntegerVector tiesInA = new CompactSparseIntegerVector(length);
    SparseIntegerVector tiesInB = new CompactSparseIntegerVector(length);
    boolean foundTies = false;
    
    int concordant = 0;
    int discordant = 0;
    
    boolean atie;
    for (int i = 0; i < length; i++) {
      for (int j = i + 1; j < length; j++)
      {



        int ai = a.get(i);
        int aj = a.get(j);
        int bi = b.get(i);
        int bj = b.get(j);
        

        atie = ai == aj;
        if (ai == aj) {
          tiesInA.add(i, 1);
          foundTies = true;
        }
        if (bi == bj) {
          tiesInB.add(i, 1);
          foundTies = true;
        }
        

        if ((ai != aj) && (bi != bj)) {
          if (((ai < aj) && (bi < bj)) || ((ai > aj) && (bi > bj))) {
            concordant++;
          } else {
            discordant++;
          }
        }
      }
    }
    int n = concordant - discordant;
    double d = 0.5D * (length * (length - 1));
    
    if (foundTies)
    {






      double aSum = 0.0D;
      int[] arrayOfInt1; boolean bool1 = (arrayOfInt1 = tiesInA.getNonZeroIndices()).length; int ties; for (atie = false; atie < bool1; atie++) { int i = arrayOfInt1[atie];
        ties = tiesInA.get(i);
        aSum += ties * (ties + 1) * 0.5D;
      }
      
      double bSum = 0.0D;
      for (int i : tiesInB.getNonZeroIndices()) {
        int ties = tiesInB.get(i);
        bSum += ties * (ties + 1) * 0.5D;
      }
      
      return n / Math.sqrt((d - aSum) * (d - bSum));
    }
    
    return n / d;
  }
  









  public static double goodmanKruskalGamma(DoubleVector a, DoubleVector b)
  {
    check(a, b);
    

    int length = a.length();
    double numerator = 0.0D;
    
    int concordant = 0;
    int discordant = 0;
    

    for (int i = 0; i < length; i++) {
      for (int j = i + 1; j < length; j++)
      {



        double ai = a.get(i);
        double aj = a.get(j);
        double bi = b.get(i);
        double bj = b.get(j);
        


        if ((ai != aj) && (bi != bj)) {
          if (((ai < aj) && (bi < bj)) || ((ai > aj) && (bi > bj))) {
            concordant++;
          } else {
            discordant++;
          }
        }
      }
    }
    int cd = concordant + discordant;
    return cd == 0 ? 
      0.0D : 
      (concordant - discordant) / cd;
  }
  






  public static double tanimotoCoefficient(double[] a, double[] b)
  {
    return tanimotoCoefficient(Vectors.asVector(a), Vectors.asVector(b));
  }
  





  public static double tanimotoCoefficient(int[] a, int[] b)
  {
    return tanimotoCoefficient(Vectors.asVector(a), Vectors.asVector(b));
  }
  





  public static double tanimotoCoefficient(Vector a, Vector b)
  {
    return tanimotoCoefficient(Vectors.asDouble(a), Vectors.asDouble(b));
  }
  






  public static double tanimotoCoefficient(DoubleVector a, DoubleVector b)
  {
    check(a, b);
    








    double aMagnitude = a.magnitude();
    double bMagnitude = b.magnitude();
    
    if ((aMagnitude == 0.0D) || (bMagnitude == 0.0D)) {
      return 0.0D;
    }
    double dotProduct = VectorMath.dotProduct(a, b);
    double aMagSq = aMagnitude * aMagnitude;
    double bMagSq = bMagnitude * bMagnitude;
    
    return dotProduct / (aMagSq + bMagSq - dotProduct);
  }
  







  public static double tanimotoCoefficient(IntegerVector a, IntegerVector b)
  {
    check(a, b);
    








    double aMagnitude = a.magnitude();
    double bMagnitude = b.magnitude();
    
    if ((aMagnitude == 0.0D) || (bMagnitude == 0.0D)) {
      return 0.0D;
    }
    int dotProduct = VectorMath.dotProduct(a, b);
    double aMagSq = aMagnitude * aMagnitude;
    double bMagSq = bMagnitude * bMagnitude;
    
    return dotProduct / (aMagSq + bMagSq - dotProduct);
  }
}
