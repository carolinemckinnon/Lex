package edu.ucla.sspace.matrix.factorization;

import edu.ucla.sspace.matrix.ArrayMatrix;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.MatrixBuilder;
import edu.ucla.sspace.matrix.MatrixFactorization;
import edu.ucla.sspace.matrix.MatrixFile;
import edu.ucla.sspace.matrix.SparseMatrix;
import edu.ucla.sspace.matrix.SvdlibcSparseBinaryMatrixBuilder;
import edu.ucla.sspace.vector.SparseDoubleVector;
import java.util.Properties;
import java.util.logging.Logger;



















































public class NonNegativeMatrixFactorizationMultiplicative
  implements MatrixFactorization
{
  private static final Logger LOG = Logger.getLogger(NonNegativeMatrixFactorizationMultiplicative.class.getName());
  




  public static final String PROPERTY_PREFIX = "edu.ucla.sspace.matrix.factorize.NonNegativeMatrixFactorizationMultiplicative";
  




  public static final String OUTER_ITERATIONS = "edu.ucla.sspace.matrix.factorize.NonNegativeMatrixFactorizationMultiplicative.outerIterations";
  




  public static final String INNER_ITERATIONS = "edu.ucla.sspace.matrix.factorize.NonNegativeMatrixFactorizationMultiplicative.innerIterations";
  




  public static final String DEFAULT_OUTER_ITERATIONS = "30";
  




  public static final String DEFAULT_INNER_ITERATIONS = "1";
  



  private Matrix W;
  



  private Matrix H;
  



  private final int innerLoop;
  



  private final int outerLoop;
  




  public NonNegativeMatrixFactorizationMultiplicative()
  {
    this(System.getProperties());
  }
  



  public NonNegativeMatrixFactorizationMultiplicative(Properties props)
  {
    outerLoop = Integer.parseInt(props.getProperty(
      "edu.ucla.sspace.matrix.factorize.NonNegativeMatrixFactorizationMultiplicative.outerIterations", "30"));
    innerLoop = Integer.parseInt(props.getProperty(
      "edu.ucla.sspace.matrix.factorize.NonNegativeMatrixFactorizationMultiplicative.innerIterations", "1"));
  }
  







  public NonNegativeMatrixFactorizationMultiplicative(int innerLoop, int outerLoop)
  {
    this.innerLoop = innerLoop;
    this.outerLoop = outerLoop;
  }
  


  public void factorize(MatrixFile mFile, int numDimensions)
  {
    factorize((SparseMatrix)mFile.load(), numDimensions);
  }
  


  public void factorize(SparseMatrix matrix, int numDimensions)
  {
    W = new ArrayMatrix(matrix.rows(), numDimensions);
    initialize(W);
    H = new ArrayMatrix(numDimensions, matrix.columns());
    initialize(H);
    






    for (int i = 0; i < outerLoop; i++) {
      LOG.info("Updating H matrix");
      double d3;
      double sum;
      double v; for (int j = 0; j < innerLoop; j++)
      {





        long start = System.currentTimeMillis();
        Matrix Hprime = new ArrayMatrix(H.rows(), H.columns());
        double s = 0.0D;
        for (int k = 0; k < numDimensions; k++)
          for (int n = 0; n < matrix.rows(); n++) {
            SparseDoubleVector v = matrix.getRowVector(n);
            int[] nonZeros = v.getNonZeroIndices();
            int[] arrayOfInt1; d3 = (arrayOfInt1 = nonZeros).length; for (double d1 = 0; d1 < d3; d1++) { int m = arrayOfInt1[d1];
              Hprime.set(k, m, Hprime.get(k, m) + 
                W.get(n, k) * v.get(m));
            }
          }
        long end = System.currentTimeMillis();
        LOG.info("Step 1: " + (end - start) + "ms");
        

        start = System.currentTimeMillis();
        Matrix WtW = new ArrayMatrix(numDimensions, numDimensions);
        for (int k = 0; k < numDimensions; k++) {
          for (int l = 0; l < numDimensions; l++) {
            double sum = 0.0D;
            for (int n = 0; n < W.rows(); n++)
              sum += W.get(n, k) * W.get(n, l);
            WtW.set(k, l, sum);
          }
        }
        end = System.currentTimeMillis();
        LOG.info("Step 2: " + (end - start) + "ms");
        












        start = System.currentTimeMillis();
        for (int k = 0; k < numDimensions; k++) {
          for (int m = 0; m < H.columns(); m++) {
            sum = 0.0D;
            for (int l = 0; l < numDimensions; l++)
              sum += WtW.get(k, l) * H.get(l, m);
            v = Hprime.get(k, m);
            double w = H.get(k, m);
            Hprime.set(k, m, w * v / sum);
          }
        }
        
        end = System.currentTimeMillis();
        LOG.info("Step 3: " + (end - start) + "ms");
        

        H = Hprime;
      }
      
      LOG.info("Updating W matrix");
      

      for (int j = 0; j < innerLoop; j++)
      {


        long start = System.currentTimeMillis();
        Matrix Wprime = new ArrayMatrix(W.rows(), W.columns());
        for (int n = 0; n < matrix.rows(); n++) {
          SparseDoubleVector v = matrix.getRowVector(n);
          int[] nonZeros = v.getNonZeroIndices();
          for (int k = 0; k < numDimensions; k++) {
            double sum = 0.0D;
            int[] arrayOfInt2; d3 = (arrayOfInt2 = nonZeros).length; for (double d2 = 0; d2 < d3; d2++) { int m = arrayOfInt2[d2];
              sum += v.get(m) * H.get(k, m); }
            Wprime.set(n, k, sum);
          }
        }
        long end = System.currentTimeMillis();
        LOG.info("Step 4: " + (end - start) + "ms");
        

        start = System.currentTimeMillis();
        Matrix HHt = new ArrayMatrix(numDimensions, numDimensions);
        for (int k = 0; k < numDimensions; k++) {
          for (int l = 0; l < numDimensions; l++) {
            double sum = 0.0D;
            for (int m = 0; m < H.columns(); m++)
              sum += H.get(k, m) * H.get(l, m);
            HHt.set(k, l, sum);
          }
        }
        end = System.currentTimeMillis();
        LOG.info("Step 5: " + (end - start) + "ms");
        












        start = System.currentTimeMillis();
        for (int n = 0; n < W.rows(); n++) {
          for (int k = 0; k < W.columns(); k++) {
            double sum = 0.0D;
            for (int l = 0; l < numDimensions; l++)
              sum += W.get(n, l) * HHt.get(l, k);
            double v = Wprime.get(n, k);
            double w = W.get(n, k);
            Wprime.set(n, k, w * v / sum);
          }
        }
        end = System.currentTimeMillis();
        LOG.info("Step 6: " + (end - start) + "ms");
        

        W = Wprime;
      }
      
      LOG.info("Finishedo processing outer loop: " + i);
    }
  }
  


  public Matrix dataClasses()
  {
    return W;
  }
  


  public Matrix classFeatures()
  {
    return H;
  }
  


  public MatrixBuilder getBuilder()
  {
    return new SvdlibcSparseBinaryMatrixBuilder();
  }
  



  public static void initialize(Matrix m)
  {
    for (int r = 0; r < m.rows(); r++) {
      for (int c = 0; c < m.columns(); c++) {
        m.set(r, c, Math.random());
      }
    }
  }
}
