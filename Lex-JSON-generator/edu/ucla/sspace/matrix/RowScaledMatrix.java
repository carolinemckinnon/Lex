package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.ScaledDoubleVector;
import java.io.Serializable;









































public class RowScaledMatrix
  implements Matrix, Serializable
{
  private static final long serialVersionUID = 1L;
  private final Matrix m;
  private final DoubleVector scales;
  
  public RowScaledMatrix(Matrix matrix, DoubleVector v)
  {
    m = matrix;
    scales = v;
  }
  


  public int columns()
  {
    return m.columns();
  }
  


  public double get(int row, int col)
  {
    return m.get(row, col) * scales.get(row);
  }
  


  public double[] getColumn(int column)
  {
    throw new UnsupportedOperationException("Cannot access column");
  }
  


  public DoubleVector getColumnVector(int column)
  {
    throw new UnsupportedOperationException("Cannot access column");
  }
  


  public double[] getRow(int row)
  {
    double[] values = m.getRow(row);
    for (int i = 0; i < values.length; i++)
      values[i] *= scales.get(i);
    return values;
  }
  


  public DoubleVector getRowVector(int row)
  {
    return new ScaledDoubleVector(m.getRowVector(row), scales.get(row));
  }
  


  public int rows()
  {
    return m.rows();
  }
  


  public void set(int row, int col, double val)
  {
    throw new UnsupportedOperationException("Cannot set values");
  }
  


  public void setColumn(int column, double[] values)
  {
    throw new UnsupportedOperationException("Cannot set values");
  }
  


  public void setColumn(int column, DoubleVector values)
  {
    throw new UnsupportedOperationException("Cannot set values");
  }
  


  public void setRow(int row, double[] values)
  {
    throw new UnsupportedOperationException("Cannot set values");
  }
  


  public void setRow(int row, DoubleVector values)
  {
    throw new UnsupportedOperationException("Cannot set values");
  }
  


  public double[][] toDenseArray()
  {
    return m.toDenseArray();
  }
}
