package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DoubleVector;
import java.io.File;












































public class TfIdfDocStripedTransform
  extends BaseTransform
{
  public TfIdfDocStripedTransform() {}
  
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
    return "TF-IDF-DOC-STRIPED";
  }
  




  public class TfIdfGlobalTransform
    implements GlobalTransform
  {
    private double[] docTermCount;
    


    private double[] termDocCount;
    


    private int totalDocCount;
    



    public TfIdfGlobalTransform(Matrix matrix)
    {
      TransformStatistics.MatrixStatistics stats = 
        TransformStatistics.extractStatistics(matrix, false, true);
      docTermCount = rowSums;
      termDocCount = columnSums;
      totalDocCount = docTermCount.length;
    }
    



    public TfIdfGlobalTransform(File inputMatrixFile, MatrixIO.Format format)
    {
      TransformStatistics.MatrixStatistics stats = TransformStatistics.extractStatistics(
        inputMatrixFile, format, false, true);
      docTermCount = rowSums;
      termDocCount = columnSums;
      totalDocCount = docTermCount.length;
    }
    










    public double transform(int row, int column, double value)
    {
      double tf = value / docTermCount[row];
      double idf = 
        Math.log(totalDocCount / (1.0D + termDocCount[column]));
      return tf * idf;
    }
    


    public double transform(int row, DoubleVector column)
    {
      throw new UnsupportedOperationException(
        "Cannot compute weighting given a column an a doc-striped matrix.");
    }
  }
}
