package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.VectorMath;
import java.io.File;
import java.io.Serializable;











































public class TfIdfTransform
  extends BaseTransform
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  
  public TfIdfTransform() {}
  
  protected GlobalTransform getTransform(Matrix matrix)
  {
    return new TfIdfGlobalTransform(matrix);
  }
  



  protected GlobalTransform getTransform(File inputMatrixFile, MatrixIO.Format format)
  {
    return new TfIdfGlobalTransform(inputMatrixFile, format);
  }
  


  public String toString()
  {
    return "TF-IDF";
  }
  



  public class TfIdfGlobalTransform
    implements GlobalTransform, Serializable
  {
    private static final long serialVersionUID = 1L;
    


    private double[] docTermCount;
    


    private double[] termDocCount;
    


    private int totalDocCount;
    



    public TfIdfGlobalTransform(Matrix matrix)
    {
      TransformStatistics.MatrixStatistics stats = 
        TransformStatistics.extractStatistics(matrix, true, false);
      docTermCount = columnSums;
      termDocCount = rowSums;
      totalDocCount = docTermCount.length;
    }
    



    public TfIdfGlobalTransform(File inputMatrixFile, MatrixIO.Format format)
    {
      TransformStatistics.MatrixStatistics stats = TransformStatistics.extractStatistics(
        inputMatrixFile, format, true, false);
      docTermCount = columnSums;
      termDocCount = rowSums;
      totalDocCount = docTermCount.length;
    }
    










    public double transform(int row, int column, double value)
    {
      double tf = value / docTermCount[column];
      double idf = 
        Math.log(totalDocCount / (termDocCount[row] + 1.0D));
      return tf * idf;
    }
    











    public double transform(int row, DoubleVector column)
    {
      double sum = VectorMath.sum(column);
      double tf = column.get(row) / sum;
      double idf = 
        Math.log(totalDocCount / (termDocCount[row] + 1.0D));
      return tf * idf;
    }
  }
}
