package edu.ucla.sspace.clustering;

import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.RowMaskedMatrix;
import edu.ucla.sspace.matrix.SparseMatrix;
import edu.ucla.sspace.matrix.SparseRowMaskedMatrix;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.ScaledDoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.VectorMath;
import edu.ucla.sspace.vector.Vectors;
import java.util.Arrays;
import java.util.logging.Logger;




























































public abstract class BaseSpectralCut
  implements EigenCut
{
  private static final Logger LOGGER = Logger.getLogger(BaseSpectralCut.class.getName());
  



  protected Matrix dataMatrix;
  



  protected int numRows;
  



  protected DoubleVector rho;
  



  protected DoubleVector matrixRowSums;
  



  protected double pSum;
  



  protected int[] leftReordering;
  


  protected int[] rightReordering;
  


  protected Matrix leftSplit;
  


  protected Matrix rightSplit;
  



  public BaseSpectralCut() {}
  



  public double rhoSum()
  {
    return pSum;
  }
  


  public DoubleVector computeRhoSum(Matrix matrix)
  {
    LOGGER.info("Computing rho and rhoSum");
    
    int vectorLength = matrix.rows();
    matrixRowSums = computeMatrixRowSum(matrix);
    dataMatrix = matrix;
    

    rho = new DenseVector(vectorLength);
    pSum = 0.0D;
    for (int r = 0; r < matrix.rows(); r++) {
      double dot = VectorMath.dotProduct(
        matrixRowSums, matrix.getRowVector(r));
      pSum += dot;
      rho.set(r, dot);
    }
    return matrixRowSums;
  }
  


  public void computeCut(Matrix matrix)
  {
    dataMatrix = matrix;
    int numRows = matrix.rows();
    

    int vectorLength = matrix.rows();
    DoubleVector matrixRowSums = computeRhoSum(matrix);
    
    LOGGER.info("Computing the second eigen vector");
    


    DoubleVector v = computeSecondEigenVector(matrix, vectorLength);
    


    Index[] elementIndices = new Index[v.length()];
    for (int i = 0; i < v.length(); i++)
      elementIndices[i] = new Index(v.get(i), i);
    Arrays.sort(elementIndices);
    




    DoubleVector sortedRho = new DenseVector(matrix.rows());
    int[] reordering = new int[v.length()];
    for (int i = 0; i < v.length(); i++) {
      reordering[i] = index;
      sortedRho.set(i, rho.get(index));
    }
    
    Matrix sortedMatrix;
    
    Matrix sortedMatrix;
    
    if ((matrix instanceof SparseMatrix)) {
      sortedMatrix = new SparseRowMaskedMatrix(
        (SparseMatrix)matrix, reordering);
    } else {
      sortedMatrix = new RowMaskedMatrix(matrix, reordering);
    }
    LOGGER.info("Computing the spectral cut");
    

    int cutIndex = computeCut(
      sortedMatrix, sortedRho, pSum, matrixRowSums);
    
    leftReordering = Arrays.copyOfRange(reordering, 0, cutIndex);
    rightReordering = Arrays.copyOfRange(
      reordering, cutIndex, reordering.length);
    

    if ((matrix instanceof SparseMatrix)) {
      leftSplit = new SparseRowMaskedMatrix((SparseMatrix)matrix, 
        leftReordering);
      rightSplit = new SparseRowMaskedMatrix((SparseMatrix)matrix, 
        rightReordering);
    } else {
      leftSplit = new RowMaskedMatrix(matrix, leftReordering);
      rightSplit = new RowMaskedMatrix(matrix, rightReordering);
    }
  }
  




  protected abstract DoubleVector computeSecondEigenVector(Matrix paramMatrix, int paramInt);
  



  public Matrix getLeftCut()
  {
    return leftSplit;
  }
  


  public Matrix getRightCut()
  {
    return rightSplit;
  }
  


  public int[] getLeftReordering()
  {
    return leftReordering;
  }
  


  public int[] getRightReordering()
  {
    return rightReordering;
  }
  



  public double getKMeansObjective()
  {
    DoubleVector centroid = new ScaledDoubleVector(
      matrixRowSums, 1.0D / dataMatrix.rows());
    double score = 0.0D;
    for (int r = 0; r < dataMatrix.rows(); r++)
    {
      score = score + VectorMath.dotProduct(centroid, dataMatrix.getRowVector(r)); }
    return score;
  }
  





  public double getKMeansObjective(double alpha, double beta, int leftNumClusters, int[] leftAssignments, int rightNumClusters, int[] rightAssignments)
  {
    double score = 0.0D;
    score += kMeansObjective(leftNumClusters, leftAssignments, leftSplit);
    
    score = score + kMeansObjective(rightNumClusters, rightAssignments, rightSplit);
    return score;
  }
  






  public static double kMeansObjective(int numClusters, int[] assignments, Matrix data)
  {
    double score = 0.0D;
    DoubleVector[] centroids = new DoubleVector[numClusters];
    double[] sizes = new double[numClusters];
    for (int i = 0; i < centroids.length; i++) {
      centroids[i] = new DenseVector(data.columns());
    }
    
    for (int i = 0; i < assignments.length; i++) {
      VectorMath.add(centroids[assignments[i]], data.getRowVector(i));
      sizes[assignments[i]] += 1.0D;
    }
    


    for (int i = 0; i < centroids.length; i++) {
      if (sizes[i] != 0.0D) {
        centroids[i] = new ScaledDoubleVector(centroids[i], 1.0D / sizes[i]);
      }
    }
    for (int i = 0; i < assignments.length; i++)
    {
      score = score + VectorMath.dotProduct(centroids[assignments[i]], data.getRowVector(i)); }
    return score;
  }
  






  public double getSplitObjective(double alpha, double beta, int leftNumClusters, int[] leftAssignments, int rightNumClusters, int[] rightAssignments)
  {
    double intraClusterScore = 0.0D;
    

    int[] leftClusterCounts = new int[leftNumClusters];
    
    intraClusterScore = intraClusterScore + computeIntraClusterScore(leftAssignments, leftSplit, leftClusterCounts);
    

    int[] rightClusterCounts = new int[rightNumClusters];
    
    intraClusterScore = intraClusterScore + computeIntraClusterScore(rightAssignments, rightSplit, rightClusterCounts);
    




    double interClusterScore = (pSum - numRows) / 2.0D - intraClusterScore;
    

    int pairCount = comparisonCount(leftClusterCounts) + 
      comparisonCount(rightClusterCounts);
    

    intraClusterScore = pairCount - intraClusterScore;
    return alpha * intraClusterScore + beta * interClusterScore;
  }
  


  public double getMergedObjective(double alpha, double beta)
  {
    double intraScore = pSum - numRows / 2.0D;
    double pairCount = numRows * (numRows - 1) / 2.0D;
    return alpha * (pairCount - intraScore);
  }
  







  private static double computeIntraClusterScore(int[] assignments, Matrix m, int[] numComparisons)
  {
    DoubleVector[] centroids = new DoubleVector[numComparisons.length];
    int[] centroidSizes = new int[numComparisons.length];
    double intraClusterScore = 0.0D;
    for (int i = 0; i < assignments.length; i++) {
      int assignment = assignments[i];
      DoubleVector v = m.getRowVector(i);
      if (centroids[assignment] == null) {
        centroids[assignment] = Vectors.copyOf(v);
      } else {
        DoubleVector centroid = centroids[assignment];
        
        intraClusterScore = intraClusterScore + (centroidSizes[assignment] - VectorMath.dotProduct(v, centroid));
        VectorMath.add(centroid, v);
        numComparisons[assignment] += centroidSizes[assignment];
      }
      centroidSizes[assignment] += 1;
    }
    return intraClusterScore;
  }
  


  protected static int comparisonCount(int[] clusterSizes)
  {
    int total = 0;
    int[] arrayOfInt = clusterSizes;int j = clusterSizes.length; for (int i = 0; i < j; i++) { int count = arrayOfInt[i];
      total += count; }
    return total;
  }
  






  protected static DoubleVector orthonormalize(DoubleVector v, DoubleVector other)
  {
    double dot = VectorMath.dotProduct(v, other);
    dot -= v.get(0) * other.get(0);
    if (other.get(0) != 0.0D) {
      dot /= other.get(0);
      v.add(0, -dot);
    }
    dot = VectorMath.dotProduct(v, v);
    if ((1.0D / dot == 0.0D) || (dot == 0.0D)) {
      return v;
    }
    
    return new ScaledDoubleVector(v, 1.0D / dot);
  }
  










  protected static int computeCut(Matrix matrix, DoubleVector rho, double rhoSum, DoubleVector matrixRowSums)
  {
    DoubleVector x = new DenseVector(matrix.columns());
    DoubleVector y = matrixRowSums;
    VectorMath.subtract(y, x);
    




    VectorMath.add(x, matrix.getRowVector(0));
    double rhoX = rho.get(0);
    double rhoY = rhoSum - rho.get(0);
    

    double u = VectorMath.dotProduct(x, y);
    


    double minConductance = u / Math.min(rhoX, rhoY);
    int cutIndex = 0;
    



    for (int i = 1; i < rho.length() - 2; i++)
    {

      DoubleVector vector = matrix.getRowVector(i);
      double xv = VectorMath.dotProduct(x, vector);
      double yv = VectorMath.dotProduct(y, vector);
      u = u - xv + yv + 1.0D;
      

      VectorMath.add(x, vector);
      VectorMath.subtract(y, vector);
      

      rhoX += rho.get(i);
      rhoY -= rho.get(i);
      

      double conductance = u / Math.min(rhoX, rhoY);
      if (conductance <= minConductance) {
        minConductance = conductance;
        cutIndex = i;
      }
    }
    return cutIndex + 1;
  }
  






  protected static DoubleVector computeMatrixTransposeV(Matrix matrix, DoubleVector v)
  {
    DoubleVector newV = new DenseVector(matrix.columns());
    if ((matrix instanceof SparseMatrix)) {
      SparseMatrix smatrix = (SparseMatrix)matrix;
      for (int r = 0; r < smatrix.rows(); r++) {
        SparseDoubleVector row = smatrix.getRowVector(r);
        int[] nonZeros = row.getNonZeroIndices();
        for (int c : nonZeros)
          newV.add(c, row.get(c) * v.get(r));
      }
    } else {
      for (int r = 0; r < matrix.rows(); r++)
        for (int c = 0; c < matrix.columns(); c++)
          newV.add(c, matrix.get(r, c) * v.get(r));
    }
    return newV;
  }
  









  protected static void computeMatrixDotV(Matrix matrix, DoubleVector newV, DoubleVector v)
  {
    if ((matrix instanceof SparseMatrix)) {
      SparseMatrix smatrix = (SparseMatrix)matrix;
      for (int r = 0; r < smatrix.rows(); r++) {
        double vValue = 0.0D;
        SparseDoubleVector row = smatrix.getRowVector(r);
        int[] nonZeros = row.getNonZeroIndices();
        for (int c : nonZeros)
          vValue += row.get(c) * newV.get(c);
        v.set(r, vValue);
      }
    }
    else {
      for (int r = 0; r < matrix.rows(); r++) {
        double vValue = 0.0D;
        for (int c = 0; c < matrix.columns(); c++)
          vValue += matrix.get(r, c) * newV.get(c);
        v.set(r, vValue);
      }
    }
  }
  




  protected static <T extends Matrix> DoubleVector computeMatrixRowSum(T matrix)
  {
    DoubleVector rowSums = new DenseVector(matrix.columns());
    for (int r = 0; r < matrix.rows(); r++)
      VectorMath.add(rowSums, matrix.getRowVector(r));
    return rowSums;
  }
  

  protected static class Index
    implements Comparable
  {
    public final double weight;
    public final int index;
    
    public Index(double weight, int index)
    {
      this.weight = weight;
      this.index = index;
    }
    
    public int compareTo(Object other) {
      Index i = (Index)other;
      return (int)(weight - weight);
    }
  }
}
