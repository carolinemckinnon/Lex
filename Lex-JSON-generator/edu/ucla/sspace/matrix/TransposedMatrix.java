package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DoubleVector;
import java.io.Serializable;
































public class TransposedMatrix
  extends AbstractMatrix
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  final Matrix m;
  
  public TransposedMatrix(Matrix matrix)
  {
    m = matrix;
  }
  


  public int columns()
  {
    return m.rows();
  }
  


  public double get(int row, int col)
  {
    return m.get(col, row);
  }
  


  public double[] getColumn(int column)
  {
    return m.getRow(column);
  }
  


  public DoubleVector getColumnVector(int column)
  {
    return m.getRowVector(column);
  }
  


  public double[] getRow(int row)
  {
    return m.getColumn(row);
  }
  


  public DoubleVector getRowVector(int row)
  {
    return m.getColumnVector(row);
  }
  


  public int rows()
  {
    return m.columns();
  }
  


  public void set(int row, int col, double val)
  {
    m.set(col, row, val);
  }
  


  public void setColumn(int column, double[] values)
  {
    m.setRow(column, values);
  }
  


  public void setColumn(int column, DoubleVector values)
  {
    m.setRow(column, values);
  }
  


  public void setRow(int row, double[] values)
  {
    m.setColumn(row, values);
  }
  


  public void setRow(int row, DoubleVector values)
  {
    m.setColumn(row, values);
  }
  


  public double[][] toDenseArray()
  {
    double[][] arr = new double[columns()][0];
    int cols = columns();
    for (int col = 0; col < cols; col++)
      arr[col] = m.getRow(col);
    return arr;
  }
}
