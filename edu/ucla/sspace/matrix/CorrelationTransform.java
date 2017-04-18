package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseVector;
import java.io.File;










































public class CorrelationTransform
  extends BaseTransform
{
  public CorrelationTransform() {}
  
  protected GlobalTransform getTransform(File inputMatrixFile, MatrixIO.Format format)
  {
    return new CorrelationGlobalTransform(inputMatrixFile, format);
  }
  


  protected GlobalTransform getTransform(Matrix matrix)
  {
    return new CorrelationGlobalTransform(matrix);
  }
  


  public String toString()
  {
    return "Correlation";
  }
  




  public class CorrelationGlobalTransform
    implements GlobalTransform
  {
    private double[] rowSums;
    


    private double[] colSums;
    


    private double totalSum;
    



    public CorrelationGlobalTransform(Matrix matrix)
    {
      TransformStatistics.MatrixStatistics stats = 
        TransformStatistics.extractStatistics(matrix);
      rowSums = rowSums;
      colSums = columnSums;
      totalSum = matrixSum;
    }
    




    public CorrelationGlobalTransform(File inputMatrixFile, MatrixIO.Format format)
    {
      TransformStatistics.MatrixStatistics stats = 
        TransformStatistics.extractStatistics(inputMatrixFile, format);
      rowSums = rowSums;
      colSums = columnSums;
      totalSum = matrixSum;
    }
    












    public double transform(int row, int column, double value)
    {
      if (value == 0.0D) {
        return 0.0D;
      }
      double newValue = 
        (totalSum * value - rowSums[row] * colSums[column]) / 
        Math.sqrt(rowSums[row] * (totalSum - rowSums[row]) * 
        colSums[column] * (totalSum - colSums[column]));
      return newValue > 0.0D ? Math.sqrt(newValue) : 0.0D;
    }
    












    public double transform(int row, DoubleVector column)
    {
      double value = column.get(row);
      if (value == 0.0D) {
        return 0.0D;
      }
      
      double colSum = 0.0D;
      if ((column instanceof SparseVector)) {
        SparseVector sv = (SparseVector)column;
        for (int nz : sv.getNonZeroIndices()) {
          colSum += column.get(nz);
        }
      } else {
        int length = column.length();
        for (int i = 0; i < length; i++) {
          colSum += column.get(i);
        }
      }
      double newValue = 
        (totalSum * value - rowSums[row] * colSum) / 
        Math.sqrt(rowSums[row] * (totalSum - rowSums[row]) * 
        colSum * (totalSum - colSum));
      return newValue > 0.0D ? Math.sqrt(newValue) : 0.0D;
    }
  }
}
