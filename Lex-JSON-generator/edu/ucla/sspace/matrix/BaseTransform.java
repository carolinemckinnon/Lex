package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.Vectors;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;






























public abstract class BaseTransform
  implements Transform, Serializable
{
  private static final long serialVersionUID = 1L;
  private GlobalTransform transform;
  
  public BaseTransform()
  {
    transform = null;
  }
  







  public DoubleVector transform(DoubleVector column)
  {
    DoubleVector transformed = (DoubleVector)Vectors.instanceOf(column);
    int length = column.length();
    for (int row = 0; row < length; row++) {
      double newValue = transform.transform(row, column);
      transformed.set(row, newValue);
    }
    return transformed;
  }
  



  public File transform(File inputMatrixFile, MatrixIO.Format format)
    throws IOException
  {
    File output = File.createTempFile(
      inputMatrixFile.getName() + ".matrix-transform", ".dat");
    output.deleteOnExit();
    transform(inputMatrixFile, format, output);
    return output;
  }
  


  public void transform(File inputMatrixFile, MatrixIO.Format format, File outputMatrixFile)
    throws IOException
  {
    transform = getTransform(inputMatrixFile, format);
    FileTransformer transformer = MatrixIO.fileTransformer(format);
    transformer.transform(inputMatrixFile, outputMatrixFile, transform);
  }
  


  public Matrix transform(Matrix matrix)
  {
    return transform(matrix, matrix);
  }
  




  public Matrix transform(Matrix matrix, Matrix transformed)
  {
    if ((matrix.rows() != transformed.rows()) || 
      (matrix.columns() != transformed.columns())) {
      throw new IllegalArgumentException(
        "Dimensions of the transformed matrix must match the input matrix");
    }
    
    transform = getTransform(matrix);
    
    if ((matrix instanceof SparseMatrix)) {
      SparseMatrix smatrix = (SparseMatrix)matrix;
      


      for (int row = 0; row < matrix.rows(); row++) {
        SparseDoubleVector rowVec = smatrix.getRowVector(row);
        for (int col : rowVec.getNonZeroIndices()) {
          double newValue = 
            transform.transform(row, col, rowVec.get(col));
          transformed.set(row, col, newValue);
        }
      }
    }
    else
    {
      for (int row = 0; row < matrix.rows(); row++) {
        for (int col = 0; col < matrix.columns(); col++) {
          double oldValue = matrix.get(row, col);
          if (oldValue != 0.0D) {
            double newValue = 
              transform.transform(row, col, oldValue);
            transformed.set(row, col, newValue);
          }
        }
      }
    }
    
    return transformed;
  }
  
  protected abstract GlobalTransform getTransform(Matrix paramMatrix);
  
  protected abstract GlobalTransform getTransform(File paramFile, MatrixIO.Format paramFormat);
}
