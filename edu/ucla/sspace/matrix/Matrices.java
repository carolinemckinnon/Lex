package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import java.util.List;
import java.util.logging.Logger;





































public class Matrices
{
  private static final Logger LOGGER = Logger.getLogger(Matrices.class.getName());
  



  private static final int BYTES_PER_DOUBLE = 8;
  



  private static final double SPARSE_DENSITY = 1.0E-5D;
  




  private Matrices() {}
  



  public static <T extends DoubleVector> Matrix asMatrix(List<T> vectors)
  {
    return new ListMatrix(vectors);
  }
  





  public static <T extends SparseDoubleVector> SparseMatrix asSparseMatrix(List<T> vectors)
  {
    return new SparseListMatrix(vectors);
  }
  





  public static <T extends SparseDoubleVector> SparseMatrix asSparseMatrix(List<T> vectors, int columns)
  {
    return new SparseListMatrix(vectors, columns);
  }
  











  public static Matrix create(int rows, int cols, boolean isDense)
  {
    long size = isDense ? 
      rows * cols * 8L : 
      (rows * cols * 8.0E-5D);
    
    Runtime r = Runtime.getRuntime();
    
    long available = r.freeMemory();
    

    if (size < available) {
      if (isDense) {
        if (size > 2147483647L) {
          LOGGER.finer("too big for ArrayMatrix; creating new OnDiskMatrix");
          
          return new OnDiskMatrix(rows, cols);
        }
        LOGGER.finer("creating new (in memory) ArrayMatrix");
        return new ArrayMatrix(rows, cols);
      }
      
      LOGGER.finer("can fit sparse in memory; creating new SparseMatrix");
      
      return new YaleSparseMatrix(rows, cols);
    }
    

    LOGGER.finer("cannot fit in memory; creating new OnDiskMatrix");
    return new OnDiskMatrix(rows, cols);
  }
  












  public static Matrix copy(Matrix matrix)
  {
    Matrix copiedMatrix = null;
    if ((matrix instanceof SparseMatrix)) {
      copiedMatrix = create(
        matrix.rows(), matrix.columns(), Matrix.Type.SPARSE_IN_MEMORY);
    } else
      copiedMatrix = create(
        matrix.rows(), matrix.columns(), Matrix.Type.DENSE_IN_MEMORY);
    return copyTo(matrix, copiedMatrix);
  }
  













  public static Matrix copyTo(Matrix matrix, Matrix output)
  {
    if ((matrix.rows() != output.rows()) || 
      (matrix.columns() != output.columns())) {
      throw new IllegalArgumentException(
        "Matrix dimensions must match when copying.");
    }
    if ((matrix instanceof SparseMatrix)) {
      SparseMatrix smatrix = (SparseMatrix)matrix;
      


      for (int row = 0; row < matrix.rows(); row++) {
        SparseDoubleVector rowVec = smatrix.getRowVector(row);
        for (int col : rowVec.getNonZeroIndices())
          output.set(row, col, rowVec.get(col));
      }
    } else {
      for (int row = 0; row < matrix.rows(); row++)
        for (int col = 0; col < matrix.columns(); col++)
          output.set(row, col, matrix.get(row, col));
    }
    return output;
  }
  







  public static Matrix create(int rows, int cols, Matrix.Type matrixType)
  {
    switch (matrixType) {
    case DENSE_IN_MEMORY: 
      return new YaleSparseMatrix(rows, cols);
    case DENSE_ON_DISK: 
      return new ArrayMatrix(rows, cols);
    case SPARSE_ON_DISK: 
      return new DiagonalMatrix(rows);
    

    case DIAGONAL_IN_MEMORY: 
      return new OnDiskMatrix(rows, cols);
    case SPARSE_IN_MEMORY: 
      return new OnDiskMatrix(rows, cols);
    }
    throw new IllegalArgumentException(
      "Unknown matrix type: " + matrixType);
  }
  






  public static MatrixBuilder getMatrixBuilderForSVD()
  {
    return getMatrixBuilderForSVD(false);
  }
  











  @Deprecated
  public static MatrixBuilder getMatrixBuilderForSVD(boolean transpose)
  {
    SVD.Algorithm fastest = SVD.getFastestAvailableAlgorithm();
    



    if (fastest == null) {
      LOGGER.warning("no SVD support detected.  Returning default matrix builder instead");
      
      return new MatlabSparseMatrixBuilder(transpose);
    }
    
    switch (fastest) {
    case ANY: 
      return new SvdlibcSparseBinaryMatrixBuilder(transpose);
    }
    
    



    return new MatlabSparseMatrixBuilder(transpose);
  }
  








  static boolean isDense(MatrixIO.Format format)
  {
    switch (format) {
    case CLUTO_DENSE: 
    case MATLAB_SPARSE: 
    case SVDLIBC_DENSE_TEXT: 
      return true;
    case CLUTO_SPARSE: 
    case DENSE_TEXT: 
    case SVDLIBC_DENSE_BINARY: 
      return false;
    }
    
    
    if (!$assertionsDisabled) { throw new AssertionError(format);
    }
    return true;
  }
  
  private static Matrix multiplyRightDiag(Matrix m1, Matrix m2) {
    Matrix resultMatrix = create(m1.rows(), m2.columns(), true);
    for (int r = 0; r < m1.rows(); r++) {
      double[] row = m1.getRow(r);
      for (int c = 0; c < m2.columns(); c++) {
        double value = m2.get(c, c);
        resultMatrix.set(r, c, value * row[c]);
      }
    }
    return resultMatrix;
  }
  
  private static Matrix multiplyBothDiag(Matrix m1, Matrix m2) {
    Matrix resultMatrix = new DiagonalMatrix(m1.rows());
    for (int i = 0; i < m1.rows(); i++)
      resultMatrix.set(i, i, m1.get(i, i) * m2.get(i, i));
    return resultMatrix;
  }
  
  private static Matrix multiplyLeftDiag(Matrix m1, Matrix m2) {
    Matrix resultMatrix = create(m1.rows(), m2.columns(), true);
    for (int r = 0; r < m1.rows(); r++) {
      double element = m1.get(r, r);
      double[] m2Row = m2.getRow(r);
      for (int c = 0; c < m2.columns(); c++)
        resultMatrix.set(r, c, element * m2Row[c]);
    }
    return resultMatrix;
  }
  


  public static Matrix multiply(Matrix m1, Matrix m2)
  {
    if (m1.columns() != m2.rows()) {
      throw new IllegalArgumentException(
        String.format(
        "The number of columns in the first matrix (%d) do not match the number of rows in the second matrix (%d).", new Object[] {
        Integer.valueOf(m1.columns()), 
        Integer.valueOf(m2.rows()) }));
    }
    if (m1.columns() != m2.rows())
      return null;
    if ((m2 instanceof DiagonalMatrix)) {
      if ((m1 instanceof DiagonalMatrix)) {
        return multiplyBothDiag(m1, m2);
      }
      return multiplyRightDiag(m1, m2); }
    if ((m1 instanceof DiagonalMatrix)) {
      return multiplyLeftDiag(m1, m2);
    }
    

    if ((m2 instanceof DiagonalMatrix)) {
      if ((m1 instanceof DiagonalMatrix)) {
        return multiplyBothDiag(m1, m2);
      }
      return multiplyRightDiag(m1, m2); }
    if ((m1 instanceof DiagonalMatrix)) {
      return multiplyLeftDiag(m1, m2);
    }
    
    int size = m1.columns();
    Matrix resultMatrix = create(m1.rows(), m2.columns(), true);
    for (int r = 0; r < m1.rows(); r++) {
      double[] row = m1.getRow(r);
      for (int c = 0; c < m2.columns(); c++) {
        double resultValue = 0.0D;
        for (int i = 0; i < row.length; i++)
          resultValue += row[i] * m2.get(i, c);
        resultMatrix.set(r, c, resultValue);
      }
    }
    return resultMatrix;
  }
  







  public static Matrix resize(Matrix matrix, int rows, int columns)
  {
    boolean isDense = (!(matrix instanceof SparseMatrix)) && 
      (!(matrix instanceof DiagonalMatrix));
    Matrix resized = create(rows, columns, isDense);
    int r = Math.min(rows, matrix.rows());
    int c = Math.min(columns, matrix.columns());
    for (int row = 0; row < r; row++) {
      for (int col = 0; col < c; col++) {
        resized.set(row, col, matrix.get(row, col));
      }
    }
    
    return resized;
  }
  








  public static AtomicMatrix synchronizedMatrix(Matrix m)
  {
    return new SynchronizedMatrix(m);
  }
  








  public static AtomicMatrix synchronizedSparseMatrix(SparseMatrix m)
  {
    return new SynchronizedSparseMatrix(m);
  }
  





  public static Matrix transpose(Matrix matrix)
  {
    return (matrix instanceof TransposedMatrix) ? 
      m : 
      new TransposedMatrix(matrix);
  }
}
