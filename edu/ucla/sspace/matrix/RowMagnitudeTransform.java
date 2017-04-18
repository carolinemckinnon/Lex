package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DoubleVector;
import java.io.File;
































public class RowMagnitudeTransform
  extends BaseTransform
{
  public RowMagnitudeTransform() {}
  
  protected GlobalTransform getTransform(Matrix matrix)
  {
    return new RowMagnitudeGlobalTransform(matrix);
  }
  



  protected GlobalTransform getTransform(File inputMatrixFile, MatrixIO.Format format)
  {
    return new RowMagnitudeGlobalTransform(inputMatrixFile, format);
  }
  


  public String toString()
  {
    return "TF-IDF";
  }
  



  public class RowMagnitudeGlobalTransform
    implements GlobalTransform
  {
    private double[] rowMagnitudes;
    


    public RowMagnitudeGlobalTransform(Matrix matrix)
    {
      rowMagnitudes = new double[matrix.rows()];
      for (int r = 0; r < matrix.rows(); r++) {
        rowMagnitudes[r] = matrix.getRowVector(r).magnitude();
      }
    }
    








    public RowMagnitudeGlobalTransform(File inputMatrixFile, MatrixIO.Format format) {}
    







    public double transform(int row, int column, double value)
    {
      return value / rowMagnitudes[row];
    }
    




    public double transform(int row, DoubleVector column)
    {
      double value = column.get(row);
      return value / rowMagnitudes[row];
    }
  }
}
