package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.VectorMath;
import java.io.File;










































public class LogLikelihoodTransform
  extends BaseTransform
{
  public LogLikelihoodTransform() {}
  
  protected GlobalTransform getTransform(Matrix matrix)
  {
    return new LogLikelihoodGlobalTransform(matrix);
  }
  



  protected GlobalTransform getTransform(File inputMatrixFile, MatrixIO.Format format)
  {
    return new LogLikelihoodGlobalTransform(
      inputMatrixFile, format);
  }
  


  public String toString()
  {
    return "LogLikelihood";
  }
  




  public class LogLikelihoodGlobalTransform
    implements GlobalTransform
  {
    private double[] rowCounts;
    



    private double[] colCounts;
    


    private double matrixSum;
    



    public LogLikelihoodGlobalTransform(Matrix matrix)
    {
      TransformStatistics.MatrixStatistics stats = 
        TransformStatistics.extractStatistics(matrix);
      rowCounts = rowSums;
      colCounts = columnSums;
      matrixSum = matrixSum;
    }
    





    public LogLikelihoodGlobalTransform(File inputMatrixFile, MatrixIO.Format format)
    {
      TransformStatistics.MatrixStatistics stats = 
        TransformStatistics.extractStatistics(inputMatrixFile, format);
      rowCounts = rowSums;
      colCounts = columnSums;
      matrixSum = matrixSum;
    }
    










    public double transform(int row, int col, double value)
    {
      double l = colCounts[col] - value;
      double m = rowCounts[row] - value;
      double n = matrixSum - (value + l + m);
      double likelihood = value * Math.log(value) + l * Math.log(l) + 
        m * Math.log(m) + n * Math.log(n);
      
      likelihood = likelihood - ((value + l) * Math.log(value + l) - (value + m) * Math.log(value + m));
      
      likelihood = likelihood - ((l + n) * Math.log(l + n) - (m + n) * Math.log(m + n));
      likelihood += (value + l + m + n) * Math.log(value + l + m + n);
      return 2.0D * likelihood;
    }
    









    public double transform(int row, DoubleVector column)
    {
      double value = column.get(row);
      

      double colSum = VectorMath.sum(column);
      
      double l = colSum - value;
      double m = rowCounts[row] - value;
      double n = matrixSum - (value + l + m);
      double likelihood = value * Math.log(value) + l * Math.log(l) + 
        m * Math.log(m) + n * Math.log(n);
      
      likelihood = likelihood - ((value + l) * Math.log(value + l) - (value + m) * Math.log(value + m));
      
      likelihood = likelihood - ((l + n) * Math.log(l + n) - (m + n) * Math.log(m + n));
      likelihood += (value + l + m + n) * Math.log(value + l + m + n);
      return 2.0D * likelihood;
    }
  }
}
