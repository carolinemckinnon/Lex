package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.VectorMath;
import java.io.File;






























public class PointWiseMutualInformationTransform
  extends BaseTransform
{
  public PointWiseMutualInformationTransform() {}
  
  protected GlobalTransform getTransform(Matrix matrix)
  {
    return new PointWiseMutualInformationGlobalTransform(matrix);
  }
  



  protected GlobalTransform getTransform(File inputMatrixFile, MatrixIO.Format format)
  {
    return new PointWiseMutualInformationGlobalTransform(
      inputMatrixFile, format);
  }
  


  public String toString()
  {
    return "PMI";
  }
  




  public class PointWiseMutualInformationGlobalTransform
    implements GlobalTransform
  {
    private double[] rowCounts;
    



    private double[] colCounts;
    


    private double matrixSum;
    



    public PointWiseMutualInformationGlobalTransform(Matrix matrix)
    {
      TransformStatistics.MatrixStatistics stats = 
        TransformStatistics.extractStatistics(matrix);
      rowCounts = rowSums;
      colCounts = columnSums;
      matrixSum = matrixSum;
    }
    





    public PointWiseMutualInformationGlobalTransform(File inputMatrixFile, MatrixIO.Format format)
    {
      TransformStatistics.MatrixStatistics stats = 
        TransformStatistics.extractStatistics(inputMatrixFile, format);
      rowCounts = rowSums;
      colCounts = columnSums;
      matrixSum = matrixSum;
    }
    












    public double transform(int row, int col, double value)
    {
      return Math.log(value * matrixSum / (
        rowCounts[row] * colCounts[col]));
    }
    










    public double transform(int row, DoubleVector column)
    {
      double sum = VectorMath.sum(column);
      double value = column.get(row);
      return Math.log(value * matrixSum / (
        rowCounts[row] * sum));
    }
  }
}
