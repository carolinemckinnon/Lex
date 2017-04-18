package edu.ucla.sspace.matrix.factorization;

import edu.ucla.sspace.matrix.ArrayMatrix;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.MatrixBuilder;
import edu.ucla.sspace.matrix.MatrixFactorization;
import edu.ucla.sspace.matrix.MatrixFile;
import edu.ucla.sspace.matrix.SparseMatrix;
import edu.ucla.sspace.matrix.SvdlibcSparseBinaryMatrixBuilder;
import edu.ucla.sspace.vector.SparseDoubleVector;
import java.io.PrintStream;
import java.util.Properties;









































































public class NonNegativeMatrixFactorizationOPL
  implements MatrixFactorization
{
  public static final String PROPERTY_PREFIX = "edu.ucla.sspace.matrix.factorize.NonNegativeMatrixFactorizationOPL";
  public static final String ITERATIONS = "edu.ucla.sspace.matrix.factorize.NonNegativeMatrixFactorizationOPL.numIterations";
  public static final String DEFAULT_ITERATIONS = "100";
  private Matrix A;
  private Matrix X;
  private int numDimensions;
  private int numIterations;
  
  public NonNegativeMatrixFactorizationOPL()
  {
    this(System.getProperties());
  }
  




  public NonNegativeMatrixFactorizationOPL(Properties props)
  {
    numIterations = Integer.parseInt(props.getProperty(
      "edu.ucla.sspace.matrix.factorize.NonNegativeMatrixFactorizationOPL.numIterations", "100"));
  }
  



  public NonNegativeMatrixFactorizationOPL(int numIterations)
  {
    this.numIterations = numIterations;
  }
  


  public void factorize(MatrixFile mFile, int numDimensions)
  {
    factorize((SparseMatrix)mFile.load(), numDimensions);
  }
  


  public void factorize(SparseMatrix m, int numDimensions)
  {
    if ((numDimensions >= m.columns()) || 
      (numDimensions >= m.rows())) {
      throw new IllegalArgumentException(
        "Cannot factorize with more dimensions than there are rows or columns");
    }
    this.numDimensions = numDimensions;
    A = new ArrayMatrix(m.rows(), numDimensions);
    initialize(A);
    X = new ArrayMatrix(numDimensions, m.columns());
    initialize(X);
    
    for (int i = 0; i < numIterations; i++) {
      updateX(computeGofX(m), computeLearningRateX());
      updateA(computeGofA(m), computeLearningRateA());
    }
  }
  


  public Matrix dataClasses()
  {
    return A;
  }
  


  public Matrix classFeatures()
  {
    return X;
  }
  


  public MatrixBuilder getBuilder()
  {
    return new SvdlibcSparseBinaryMatrixBuilder();
  }
  



  private void updateX(Matrix G, double[] learningRate)
  {
    for (int k = 0; k < X.rows(); k++)
      for (int c = 0; c < X.columns(); c++)
        X.set(k, c, X.get(k, c) - learningRate[k] * G.get(k, c));
    makeNonZero(X);
  }
  



  private void updateA(Matrix G, double[] learningRate)
  {
    for (int r = 0; r < A.rows(); r++)
      for (int k = 0; k < A.columns(); k++)
        A.set(r, k, A.get(r, k) - learningRate[k] * G.get(r, k));
    makeNonZero(A);
  }
  


  public static void makeNonZero(Matrix m)
  {
    for (int r = 0; r < m.rows(); r++) {
      for (int c = 0; c < m.columns(); c++) {
        if (m.get(r, c) < 0.0D) {
          m.set(r, c, 0.0D);
        }
      }
    }
  }
  
  public static void initialize(Matrix m)
  {
    for (int r = 0; r < m.rows(); r++) {
      for (int c = 0; c < m.columns(); c++) {
        m.set(r, c, Math.random());
      }
    }
  }
  






  private Matrix computeGofX(SparseMatrix Y)
  {
    Matrix G = new ArrayMatrix(numDimensions, X.columns());
    




    for (int r = 0; r < Y.rows(); r++)
    {
      SparseDoubleVector v = Y.getRowVector(r);
      double[] vDiff = new double[v.length()];
      


      for (int c = 0; c < X.columns(); c++) {
        double sum = 0.0D;
        for (int k = 0; k < A.columns(); k++)
          sum += A.get(r, k) * X.get(k, c);
        vDiff[c] = (sum - v.get(c));
      }
      



      for (int k = 0; k < A.columns(); k++)
        for (int c = 0; c < X.columns(); c++)
          G.set(k, c, G.get(k, c) + A.get(r, k) * vDiff[c]);
    }
    return G;
  }
  








  private Matrix computeGofA(SparseMatrix Y)
  {
    Matrix G = new ArrayMatrix(A.rows(), numDimensions);
    


    for (int r = 0; r < Y.rows(); r++)
    {
      SparseDoubleVector v = Y.getRowVector(r);
      double[] vDiff = new double[v.length()];
      


      for (int c = 0; c < X.columns(); c++) {
        double sum = 0.0D;
        for (int k = 0; k < A.columns(); k++)
          sum += A.get(r, k) * X.get(k, c);
        vDiff[c] = (sum - v.get(c));
      }
      
      for (int k = 0; k < X.rows(); k++) {
        double sum = 0.0D;
        for (int c = 0; c < X.columns(); c++)
          sum += vDiff[c] * X.get(k, c);
        G.set(r, k, sum);
      }
    }
    return G;
  }
  






  private double[] computeLearningRateX()
  {
    double[] learningRateX = new double[numDimensions];
    for (int r = 0; r < A.rows(); r++)
      for (int c = 0; c < A.columns(); c++)
        learningRateX[c] += Math.pow(A.get(r, c), 2.0D);
    for (int k = 0; k < numDimensions; k++) {
      System.out.printf("%f ", new Object[] { Double.valueOf(learningRateX[k]) });
      learningRateX[k] = (2.0D / (learningRateX[k] + 1.0E-9D));
    }
    System.out.println();
    
    return learningRateX;
  }
  






  private double[] computeLearningRateA()
  {
    double[] learningRateA = new double[numDimensions];
    System.out.println("computeLearningRateA");
    for (int r = 0; r < X.rows(); r++) {
      double sum = 0.0D;
      for (int c = 0; c < X.columns(); c++) {
        System.out.printf("%f ", new Object[] { Double.valueOf(Math.pow(X.get(r, c), 2.0D)) });
        sum += Math.pow(X.get(r, c), 2.0D);
      }
      System.out.println();
      learningRateA[r] = (sum + 1.0E-9D);
    }
    
    for (int k = 0; k < numDimensions; k++)
      learningRateA[k] = (2.0D / learningRateA[k]);
    return learningRateA;
  }
}
