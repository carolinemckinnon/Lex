package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DoubleVector;
import java.io.File;
import java.io.Serializable;





























public class NoTransform
  extends BaseTransform
  implements Transform, Serializable
{
  private static final long serialVersionUID = 1L;
  
  public NoTransform() {}
  
  protected GlobalTransform getTransform(File inputMatrixFile, MatrixIO.Format format)
  {
    return new NoOpTransform();
  }
  


  protected GlobalTransform getTransform(Matrix matrix)
  {
    return new NoOpTransform();
  }
  


  public String toString()
  {
    return "no";
  }
  
  static class NoOpTransform implements GlobalTransform {
    NoOpTransform() {}
    
    public double transform(int row, int column, double value) { return value; }
    
    public double transform(int row, DoubleVector column)
    {
      return column.get(row);
    }
  }
}
